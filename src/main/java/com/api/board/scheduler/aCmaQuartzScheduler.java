package com.api.board.scheduler;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import com.api.board.job.acmaQuartzJob;

/*
 * 		@Component
 * 			>>> aCmaQuartzScheduler Class 를 Spring bean 으로 등록.
 */
@Component
public class aCmaQuartzScheduler {

    private SchedulerFactory schedulerFactory;
    private SchedulerFactory schedulerFactory1;
    private Scheduler scheduler;
    private Scheduler scheduler1;

    /*
     * 		@PostConstruct
     * 			>>> 해당 Class 가 인스턴스화 되자마자 자동으로 동작하게 하려는 메서드에 선언하는 어노테이션이다.
     * 			>>> aCmaQuartzScheduler Class 가 인스턴스화 되자마자 start() method 가 동작하도록 선언.
     * 			>>> 스케쥴러를 통한 배치 Job 은 사용자의 동작없이 자동으로 수행하게 하기 위한 로직이기 때문에 어딘가에서 메서드를 호출하여 사용하기 보다는
     * 				자동으로 로직이 수행되도록 구현하는 것이 좋다.
     */
    @PostConstruct
    public void start() throws SchedulerException {

        /*
         * 		quartz.SchedulerFactory 선언
         */
        schedulerFactory = new StdSchedulerFactory();
        schedulerFactory1 = new StdSchedulerFactory();


        /*
         * 		quartz.Scheduler 를 .getScheduler() 메서드를 통해 지정
         */
        scheduler = schedulerFactory.getScheduler();
        scheduler1 = schedulerFactory1.getScheduler();


        /*
         * 		스케쥴러를 .start() 해 주는 것으로 스케쥴러를 시작하겠다는 명령
         */
        scheduler.start();
        scheduler1.start();


        /*
         * 		작성한 Job을 지정
         * 			>>> identity 는 해당 Job 을 구분하는 고유 명을 지정
         * 			>>> 같은 Job 로직이더라도 서로 다른 스케쥴로 동작하게 할 경우가 있기 때문에 각각의 Job 은 고유한 identity 를 가져야 한다.
         */
        JobDetail job = JobBuilder.newJob(acmaQuartzJob.class).withIdentity("aCmaJob").build();
        JobDetail job1 = JobBuilder.newJob(acmaQuartzJob.class).withIdentity("aCmaJob1").build();


        /*
         * 		Trigger 생성
         * 			>>> Trigger 는 TriggerBuilder Class 를 사용하여 구현하며, 스케쥴러를 수행할 스케쥴 정보를 담고 있다.
         * 			>>> 이 때, Cron 문법을 사용하여 스케쥴을 지정하는 방법을 주로 사용한다.
         *
         *
         * 			# Cron Expression
         * 				>>> 총 7개의 필드가 있고 마지막 필드(년도)는 생략가능
         *
         * 				필드이름				|	허용 값
         * 				---------------------------------------------
         * 				초(Seconds)			|	0 ~ 59
         * 				분(Minutes)			|	0 ~ 59
         * 				시간(Hours)			|	0 ~ 23
         * 				일(Day-of-month)		|	1 ~ 31
         * 				달(Months)			|	1 ~ 12 or JAN ~ DEC
         * 				요일(Day-of-week)		|	1 ~ 7 or SUN-SAT
         * 				년도(Year)(생략가능)	|	1970 ~ 2099
         *
         *
         * 			# Cron Expression 특수문자
         *
         * 				Expr	|	설명
         * 				---------------------------------------------------------------------------------------------------------------
         * 				*		|	모든 수를 나타낸다.
         * 				-		|	값의 사이를 의미한다.	>>> "* 10-13 * * * *"	>>> 10, 11, 12, 13분에 동작한다.
         * 				,		|	특정 값을 지칭한다.	>>> "* 10,11,13 * * * *"	>>> 10, 11, 13분에 동작한다.
         * 				/		|	값의 증가를 표현한다.	>>> "* 0/5 * * * *"		>>> 0분부터 시작해서 5분마다 동작한다.
         * 				?		|	특별한 값이 없음을 나타낸다.	>>> (day-of-month, day-of-week 필드만 사용) 일, 요일에 하나만 설정할 때 나머지에 지정한다.
         * 				L		|	마지막 날을 나타낸다.	>>> (day-of-month, day-of-week 필드만 사용)
         * 						|						일 필드에 사용되면 이달의 마지막일을 나타낸다.
         * 						|						L-3 은 이달의 마지막날 3일 전부터 마지막날까지를 나타낸다.
         * 						|						요일 필드에 사용되면 토요일(7 or SAT)을 나타낸다.
         * 						|						6L or FRIL 은 이달의 마지막 금요일을 나타낸다.
         * 				W		|	주어진 날로부터 가장 가까운 평일(월 - 금)을 나타낸다.	>>> 15W 를 일 필드에 사용하면 이달의 15번째 날에서 가장 가까운 평일을 나타낸다.
         * 				#		|	이달의 n번째 x요일을 나타낸다.	>>> 6#3 or FRI#3 은 이달의 세번째 금요일을 나타낸다.
         *
         *
         * 			# Cron Expression 예제
         *
         * 				>>> 매 1초 마다 실행		>>> "0/1 * * * * ?"
         * 				>>> 매 5분 마다 실행		>>> "0 0/5 * ?"
         * 				>>> 20초뒤 5분마다 실행	>>> "20 0/5 * ?"
         * 				>>> 매일 오전 3시에 실행	>>> "0 0 3 * * ?"
         * 				>>> 매주 일요일 오전 3시에 실행	>>> "0 0 3 ? * SUN"
         * 				>>> 매주 월요일과 수요일 13:30, 14:30, 15:30, 16:30에 실행	>>> "0 30 13-16 ? * MON,WED"
         * 				>>> 매월 3일, 15일 오전 3시부터 오전 6시 사이에 15분 간격으로 실행	>>> "0 0/15 3-5 3,15 * ?"
         *
         */
//		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0 54 8 ? * THU")).build();
//		Trigger trigger1 = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0 55 8 ? * THU")).build();
        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")).build();
        Trigger trigger1 = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/15 * * * * ?")).build();

        /*
         * 		StartAt 과 endAt 을 사용하여 Job 스케쥴의 시작, 종료 시간 지정
         */
        //Trigger trigger = TriggerBuilder.newTrigger().startAt(startDateTime).endAt(endDateTime)
        //		.withSchedule(CronScheduleBuilder.cronSchedule("*/1 * * * *")).build();


        /*
         * 		quartz.Scheduler 에 Job 과 Trigger 연결
         */
        scheduler.scheduleJob(job, trigger);
        scheduler1.scheduleJob(job1, trigger1);
    }
}
