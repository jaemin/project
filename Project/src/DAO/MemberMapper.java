package DAO;

import java.util.ArrayList;

import VO.FilterWordVO;
import VO.GameVO;
import VO.MemberVO;
import VO.WordVO;

public interface MemberMapper {
	public void insertMember(MemberVO vo); //ȸ�����
	public ArrayList<MemberVO> searchMember(String id); //ȸ���˻�
	public void updateMember(MemberVO vo); // ��й�ȣ����
	public ArrayList<WordVO> bringWord(); //�ܾ��Ʈ��������
	public ArrayList<FilterWordVO> bringFilterWord(); //���͸��ܾ��Ʈ��������
	
	//���ӻ���
	public void insertGame(GameVO vo);
	
	//������ ������Ʈ
	public void updateGame(GameVO vo);
	public void updateGame2(GameVO vo);
	public void updateGameMember(MemberVO vo);
	public void updateGameMember2(MemberVO vo);
	
	//�������� ��������
	public ArrayList<GameVO> bringGame(GameVO vo);
	
	//���ӽ����� ��������
	public int bringGameSeq();
}
