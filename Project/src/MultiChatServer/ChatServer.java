package MultiChatServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import DAO.MemberDAO;
import VO.FilterWordVO;
import VO.WordVO;

//채팅 서버 메인
public class ChatServer {
	int port = 3333; // 서버의 PORT 번호
	ServerSocket serverSocket; // 서버 소켓
	public static ArrayList<WordVO> question;
	public static ArrayList<FilterWordVO> filter;
	public static int counter = 0;	
	private static MemberDAO dao = new MemberDAO();
	public static int gameseq;

	
	
	public static void main(String[] args) {
		gameseq = ChatServerThread.bringGameSeq();/////////////////////////////////////////
		gameseq++;
		question = bringWord();
		Collections.shuffle(question);
		String wordList = "wordList";
		File file;
		FileWriter fw;
		BufferedWriter bw;
		try {
			file = new File(wordList);
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			for (int i = 0; i < question.size(); i++) {
				bw.write(question.get(i).getWord()+"\n");
				bw.flush();
			}
			bw.close();
		} catch (Exception e) {
		}
		
		filter = bringFilterWord();
		String filterWord = "filterWord";
		try {
			file = new File(filterWord);
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			for (int i = 0; i < filter.size(); i++) {
				bw.write(filter.get(i).getFilterword()+"\n");
				bw.flush();
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ChatServer chatServer = new ChatServer();
		chatServer.start();
	}

	public static ArrayList<WordVO> getQuestion() {
		return question;
	}

	public static void setQuestion(ArrayList<WordVO> question) {
		ChatServer.question = question;
	}
	
	public static String getWord(int num) {
		return question.get(num).getWord();
	}

	public void start() {
		Socket socket = null;
		ChatServerThread thread = null;

		try {
			System.out.println("서버 Start.");
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("서버 시작시 오류.");
		}

		while (true) {
			try {
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress().getHostAddress() + " : " + socket.getPort() + " 접속");

				thread = new ChatServerThread(socket);
				new Thread(thread).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<FilterWordVO> bringFilterWord() {
		ArrayList<FilterWordVO> fList = dao.bringFilterWord();
		return fList;
	}

	public static ArrayList<WordVO> bringWord() {
		ArrayList<WordVO> wList = dao.bringWord();
		return wList;
	}
}
