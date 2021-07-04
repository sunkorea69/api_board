package com.api.board.controller;

import com.api.board.domain.Lgec_Mkt_User_Count;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.api.board.domain.Board;
import com.api.board.domain.Boards;
import com.api.board.exception.ResourceNotFoundException;
import com.api.board.service.BoardService;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/board")
@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    private int target_promotion = 517732;
    private int provide_promotion = 517733;

    /**
     * 게시글 목록 조회
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "게시글 전체 Count", notes = "게시글 전체 Count 조회합니다.")
    @RequestMapping(value = "/boardCount", method = RequestMethod.GET)
    @ResponseBody
    public List<Lgec_Mkt_User_Count> getBoardCount(@RequestParam("start") String start) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start.concat("00"));
        map.put("end", start.concat("59"));
        map.put("inflow", target_promotion);
        map.put("provide", provide_promotion);

        List<Lgec_Mkt_User_Count> hm = boardService.getBoardCount(map);

        return hm;
    }

    /**
     * 3회 이상이면 D
     *
     * @param map
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "3회이상이면 D", notes = "3회이상이면 모두 D")
    @RequestMapping(value = "/update3", method = RequestMethod.GET)
    @ResponseBody
    public int updateMkt_3(@RequestParam("start") String start, HttpServletResponse httpServletResponse) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start.concat("00"));
        map.put("end", start.concat("59"));
        map.put("inflow", target_promotion);
        map.put("provide", provide_promotion);
        //target 0 보다 크고 제한된값이 넘으면 아래부분 로직 수행
        int limit = 3;

        int a = 0;
        int b = 0;
        a = boardService.getTargetCount(map);
        b = boardService.getProvideCount(map);
        System.out.println("유입건수 "+a);
        System.out.println("지급건수 "+ b);

        int x=0;
        int y=0;
        if (a >= limit && b == 0) {
            x = boardService.updateMkt_3(map);
            System.out.println("3회이상 D처리 "+ x);

            y = boardService.updateMkt_2(map);
            System.out.println("2회 1건만 D처리 "+ y);
            httpServletResponse.sendRedirect("https://naver.com");
        }

        return x+y;
    }
    @ApiOperation(value = "게시글 목록 조회", notes = "게시글 목록을 조회합니다.")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Boards getBoardList() throws Exception {

        Boards boards = new Boards();
        boards.setBoards(boardService.getBoardList());

        return boards;
    }

    /**
     * 게시글 상세 조회
     *
     * @param board_seq
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "게시글 상세 조회", notes = "게시글를 상세 조회합니다.")
    @RequestMapping(value = "/{board_seq}", method = RequestMethod.GET)
    @ResponseBody
    public Board getBoardDetail(@PathVariable("board_seq") int board_seq) throws Exception {

        Board board = boardService.getBoardDetail(board_seq);

        if (board == null) {
            throw new ResourceNotFoundException();
        }

        return board;
    }

    /**
     * 게시글 등록
     *
     * @param board
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "게시글 등록", notes = "게시글을 등록합니다.")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Board insertBoard(@RequestBody Board board) throws Exception {

        boardService.insertBoard(board);

        int boardSeq = board.getBoard_seq();

        Board boardDetail = boardService.getBoardDetail(boardSeq);

        return boardDetail;
    }

    /**
     * 게시글 수정
     *
     * @param board
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @RequestMapping(value = "/{board_seq}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Board updateBoard(@PathVariable("board_seq") int board_seq, @RequestBody Board board) throws Exception {

        boardService.updateBoard(board);

        Board boardDetail = boardService.getBoardDetail(board_seq);

        return boardDetail;
    }

    /**
     * 게시글 삭제
     *
     * @param board_seq
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @RequestMapping(value = "/{board_seq}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Board deleteBoard(@PathVariable("board_seq") int board_seq) throws Exception {

        boardService.deleteBoard(board_seq);

        Board deleteBoard = new Board();
        deleteBoard.setBoard_seq(board_seq);

        return deleteBoard;
    }
}