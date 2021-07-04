package com.api.board.job;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.IntegerProperty;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.http.client.*;
import org.springframework.boot.configurationprocessor.json.JSONObject;

/*
 * Job Interface를 implemnets 하여 구현한다.
 */
@Slf4j
public class acmaQuartzJob implements Job {
    private static final String USER_AGENT = "Mozila/5.0";
    //    private static final String GET_URL = "https://blockchain.info/ko"
//            + "/rawblock/0000000000000bae09a7a393a8acd"
//            + "ed75aa67e46cb81f7acaa5ad94f9eacd103";
//    private static final String GET_URL = "https://m.gsshop.com/index.gs";
    private String GET_URL = "http://localhost:8080/board/boardCount";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH시 mm분 ss초");
        String currentDate = sdf1.format(date);
        String currentTime = sdf2.format(date);
        System.out.println("------------------Start----------------");

        /*
         * 		execute() method 에 로직 추가
         */
        log.info("========= acmaQuartzJob execute() method Start !!! =========");
        log.info("Start Time >>> {}", currentDate + " " + currentTime);
        sendOtherApi();
    }

    public void sendOtherApi() {

        try {

            Date today = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
            String day1 = df.format(today);
            System.out.println(day1);

            GET_URL = GET_URL.concat("?start=").concat(day1);
            System.out.println(GET_URL);
            System.out.println("=========================================");
            //http client 생성
            CloseableHttpClient httpClient = HttpClients.createDefault();

            //get 메서드와 URL 설정
            HttpGet httpGet = new HttpGet(GET_URL);

            //agent 정보 설정
            httpGet.addHeader("User-Agent", USER_AGENT);
            httpGet.addHeader("Content-type", "application/json");

            //get 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

//            System.out.println("GET Response Status");
//            System.out.println(httpResponse.getStatusLine().getStatusCode());
            String json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
//            System.out.println(httpResponse.getClass());
            System.out.println(json);

            boolean differ_status = false;
            boolean differ_target = false;

            Gson g = new Gson();
            ArrayList<Map<String, Object>> data = g.fromJson(json, ArrayList.class);
            for (Map<String, Object> mp : data) {
//                System.out.println(mp);
                for (String key : mp.keySet()) {
                    if (mp.get(key).equals("target")) {
                        if ((Double) mp.get("count") > 0) {
                            differ_target = true;
                            break;
                        }
                    }
                }

                for (String key : mp.keySet()){
                    if (mp.get(key).equals("difference")) {
                        if ((Double) mp.get("count") > 0) {
                            differ_status = true;
                            break;
                        }
                    }
                }
            }
            //target 0 보다 크면 유입으로 봄
            //difference 값이 0 이면 더이상 실행안함
            httpClient.close();

            if (differ_target && differ_status) {
                json = json.replace("}", "\n}");
                push_send("A", json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void push_send(String chatId, String fcmMessage) {
        chatId = "1656665448";
//        chatId = "887280669";
//        System.out.println(fcmMessage.substring(1,20));
//        fcmMessage = fcmMessage.substring(1,200);
        try {

            CloseableHttpClient httpClient = HttpClients.createDefault();
//         httpClient.getParams().setParameter("http.protocol", "TLSv1.2");

//            final String apiKey = "1627097682:AAHdBcBtth7Zgtje7MsQB2dYGk-n4ASUszM";
            final String apiKey = "1601503213:AAGlsQVSRF7FQu4Ve_GOsBRzjnPlKuPYWtM";
//         String chatId = "887280669";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("chat_id", chatId));
            params.add(new BasicNameValuePair("text", fcmMessage));
            UrlEncodedFormEntity strent = new UrlEncodedFormEntity(params, "UTF-8");

            HttpPost httppost = new HttpPost("https://api.telegram.org/bot" + apiKey + "/sendmessage");
//         httppost.setHeader("Content-Type", "application/json");
//         httppost.setHeader("Authorization", "key=" + Constants.FCM_SERVER_KEY);
            httppost.setEntity(strent);

            HttpResponse response = httpClient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            InputStream stm = null;
            BufferedReader bufReader = null;

            try {
                stm = resEntity.getContent();
                bufReader = new BufferedReader(new InputStreamReader(stm, "euc-kr"));

                String bufferRead = "";
                StringBuffer responseData = new StringBuffer();
                while ((bufferRead = bufReader.readLine()) != null) {
                    responseData.append(bufferRead);
                }
                System.out.println("#FCM 푸시 결과 :" + responseData.toString() + "");

                bufReader.close();
                stm.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stm != null) stm.close();
                if (bufReader != null) bufReader.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//        출처: https://eddyplusit.tistory.com/51 [EddIT]
}
