package DAO;

import java.util.ArrayList;

import VO.FilterWordVO;
import VO.GameVO;
import VO.MemberVO;
import VO.WordVO;

public interface MemberMapper {
	public void insertMember(MemberVO vo); //회원등록
	public ArrayList<MemberVO> searchMember(String id); //회원검색
	public void updateMember(MemberVO vo); // 비밀번호변경
	public ArrayList<WordVO> bringWord(); //단어리스트가져오기
	public ArrayList<FilterWordVO> bringFilterWord(); //필터링단어리스트가져오기
	
	//게임생성
	public void insertGame(GameVO vo);
	
	//게임후 업데이트
	public void updateGame(GameVO vo);
	public void updateGame2(GameVO vo);
	public void updateGameMember(MemberVO vo);
	public void updateGameMember2(MemberVO vo);
	
	//게임정보 가져오기
	public ArrayList<GameVO> bringGame(GameVO vo);
	
	//게임시퀀스 가져오기
	public int bringGameSeq();
}
