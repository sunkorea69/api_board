package com.api.board.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.api.board.domain.Board;
import com.api.board.domain.Lgec_Mkt_User_Count;

public interface BoardMapper {

	/**
	 * 게시글 전체count
	 *
	 * @return
	 * @throws Exception
	 */
	public List<Lgec_Mkt_User_Count> getBoardCount(Map map) throws Exception;
	/**
	 * 3회 유입 D
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	public int updateMkt_3(Map map) throws Exception;
	/**
	 * 2회 유입 D
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	public int updateMkt_2(Map map) throws Exception;
	public int getTargetCount(Map map) throws Exception;
	public int getProvideCount(Map map) throws Exception;
	/**
	 * 게시글 목록 조회
	 *
	 * @return
	 * @throws Exception
	 */
	public List<Board> getBoardList() throws Exception;
	
	/**
     * 게시글 상세 조회
     * 
     * @param board_seq
     * @return
     * @throws Exception
     */
    public Board getBoardDetail(int board_seq) throws Exception;
    
    /**
     * 게시글 등록 
     * 
     * @param board
     * @return
     * @throws Exception
     */
    public int insertBoard(Board board) throws Exception;  
    
    /**
     * 게시글 수정 
     * 
     * @param board
     * @return
     * @throws Exception
     */
    public int updateBoard(Board board) throws Exception;
    
    /**
     * 게시글 삭제 
     * 
     * @param board_seq
     * @return
     * @throws Exception
     */
    public int deleteBoard(int board_seq) throws Exception;
}
