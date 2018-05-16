package VO;


import java.io.Serializable;
import java.util.ArrayList;

//������ Ŭ���̾�Ʈ���� ���� ������
public class Data implements Serializable  {
	//���۵Ǵ� �������� ���� ����
    public static final int FIRST_CONNECTION = 1;	//���ο� ������
    public static final int DISCONNECTION = 2;		//���� ����
    public static final int PICTURE_MESSAGE = 3;	//�׸� ����
    public static final int CHAT_MESSAGE = 4;		//ä�� ���� ����
    public static final int NEW_QUESTION = 5;		
    public static final int ANSWER = 6;			
    public static final int REMOVE_LISTENER = 7;	
    public static final int NEXT_GAME = 8;			
    public static final int MY_TURN = 9;			
    public static final int TIMER = 10;
    public static final int WORD = 11;
    public static final int FILTER = 12;
    public static final int REDRAW = 13;
	
	int state;						//���۵Ǵ� ������ ����
	String name;					//��ȭ���� �Է��� ����� �̸�
	String message;					//�޼��� ����
	ArrayList<String> usernames;	//������ �̸� ���
	
	public Data(int state, String name) {
		this.state = state;
		this.name = name;
	}
	public Data(int state, String name, String message) {
		this.state = state;
		this.name = name;
		this.message = message;
	}
	public Data(int state, String name, String message, ArrayList<String> usernames) {
		this.state = state;
		this.name = name;
		this.message = message;
		this.usernames = usernames;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(ArrayList<String> usernames) {
		this.usernames = usernames;
	}
}