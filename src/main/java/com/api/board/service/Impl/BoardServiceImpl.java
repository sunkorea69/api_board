package com.api.board.service.Impl;

import com.api.board.domain.Board;
import com.api.board.domain.Lgec_Mkt_User_Count;
import com.api.board.mapper.BoardMapper;
import com.api.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;

	/**
	 * 게시글 count
	 *
	 * @return
	 * @throws Exception
	 */
	public List<Lgec_Mkt_User_Count> getBoardCount(Map map) throws Exception {
		return boardMapper.getBoardCount(map);
	}

	/**
	 * 3회 유입 한건 모두 D 처리
	 *
	 * @return
	 * @throws Exception
	 */

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int updateMkt_3(Map map) throws Exception {
		return boardMapper.updateMkt_3(map);
	};
	/**
	 * 2회 유입 한건 하나만 D 처리
	 *
	 * @return
	 * @throws Exception
	 */

	public int getTargetCount(Map map) throws Exception {

		return boardMapper.getTargetCount(map);
	}
	public int getProvideCount(Map map) throws Exception {
		return boardMapper.getProvideCount(map);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int updateMkt_2(Map map) throws Exception {
		return boardMapper.updateMkt_2(map);
	};

	/**
	 * 게시글 목록 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Board> getBoardList() throws Exception {
		return boardMapper.getBoardList();
	}

	/**
	 * 게시글 상세 조회
	 * 
	 * @param board_seq
	 * @return
	 * @throws Exception
	 */
	public Board getBoardDetail(int board_seq) throws Exception {
		return boardMapper.getBoardDetail(board_seq);
	};

	/**
	 * 게시글 등록
	 * 
	 * @param board
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int insertBoard(Board board) throws Exception {
		return boardMapper.insertBoard(board);
	};

	/**
	 * 게시글 수정
	 * 
	 * @param board
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int updateBoard(Board board) throws Exception {
		return boardMapper.updateBoard(board);
	};

	/**
	 * 게시글 삭제
	 * 
	 * @param board_seq
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteBoard(int board_seq) throws Exception {
		return boardMapper.deleteBoard(board_seq);
	};
}
