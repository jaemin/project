package MultiChatClient;

import java.awt.Canvas;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import javax.swing.*;
import VO.*;
import Board.Board;
import Board.CanvasMouseListener;
import MultiChatServer.*;

public class ChatClientThread implements Runnable {
	String ip; // ip주소
	int port;// 포트번호
	Socket socket;// 소켓
	JTextArea outputText;// 채팅창
	JScrollPane scroll; // 접속자명단스크롤
	JScrollPane scroll2;// 채팅창스크롤
	ObjectInputStream input;
	ObjectOutputStream output;
	private static JLabel label;
	File file;
	FileReader fr;
	BufferedReader br;
	private ArrayList<String> wordList = new ArrayList<>();
	public static ArrayList<String> filterList = new ArrayList<>();

	String username; // 접속자이름
	JList<String> userList; // 접속자이름명단

	public JScrollPane getScroll2() {
		return scroll2;
	}

	public void setScroll2(JScrollPane scroll2) {
		this.scroll2 = scroll2;
	}

	public JList<String> getUserList() {
		return userList;
	}

	public void setUserList(JList<String> userList) {
		this.userList = userList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}

	public JScrollPane getScroll() {
		return scroll;
	}

	public void setScroll(JScrollPane scroll) {
		this.scroll = scroll;
	}

	public static JLabel getJLabel() {
		return label;
	}

	public static void setJLabel(JLabel a) {
		label = a;
	}

	public JTextArea getOutputText() {
		return outputText;
	}

	public void setOutputText(JTextArea outputText) {
		this.outputText = outputText;
	}

	public ChatClientThread(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	// 대화내용 출력창에 메세지 출력
	public void output(String msg) {
		outputText.append(msg + "\n");
		// 채팅창 스크롤 아래로 내리기
		scroll2.getVerticalScrollBar().setValue(scroll2.getVerticalScrollBar().getMaximum());
	}

	// 서버 접속
	public void connect() {
		try {
			socket = new Socket(ip, port);

			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());

			new Thread(this).start();
			Data data = new Data(Data.FIRST_CONNECTION, username);
			output.writeObject(data);

		} catch (IOException e) {
			output("<<서버 접속 실패>>");
		}
	}

	// 서버 접속 종료
	public void disconnect() {
		Data data = new Data(Data.DISCONNECTION, username);
		try {
			output.writeObject(data);
			output.close();
			input.close();
			socket.close();
		} catch (Exception e) {
			output("<<<접속 종료시 오류 발생>>>");
		}
		System.exit(0);
	}

	@Override
	public void run() {
		Data data = null;
		int state = 0;
		String name = null;
		String message = null;

		while (true) {
			try {
				data = (Data) input.readObject();
				state = data.getState();
				name = data.getName();
				message = data.getMessage();

				switch (state) {
				case Data.FIRST_CONNECTION:
				case Data.DISCONNECTION:
					output(">>> " + message);
					// 참여자 목록 갱신
					ArrayList<String> usernames = data.getUsernames();
					String[] strnames = usernames.toArray(new String[usernames.size()]);
					userList.setListData(strnames);
					break;
				case Data.CHAT_MESSAGE:
					output(name + " : " + message);
					break;
				case Data.PICTURE_MESSAGE:
					if (message.equals("clear")) {
						System.out.println("=========board clear========");
						Board.canvas.setBackground(Color.BLACK);
						Board.canvas.setBackground(Color.WHITE);
					} else {
						int startX = Integer.parseInt(message.split("#")[0]);
						int startY = Integer.parseInt(message.split("#")[1]);
						int lastX = Integer.parseInt(message.split("#")[2]);
						int lastY = Integer.parseInt(message.split("#")[3]);

						int r = Integer.parseInt(message.split("#")[4]);
						int g = Integer.parseInt(message.split("#")[5]);
						int b = Integer.parseInt(message.split("#")[6]);

						if (message.split("#")[7].equals("pen"))
							CanvasMouseListener.linedraw(startX, startY, lastX, lastY, new Color(r, g, b));
						else
							CanvasMouseListener.erasedraw(startX, startY, lastX, lastY, new Color(r, g, b));
					}
					break;
				case Data.NEW_QUESTION:
					System.out.println("client : new question");
					Board.canvas.addMouseListener(Board.listener);
					Board.canvas.addMouseMotionListener(Board.listener);
					Board.send.setEnabled(false);
					Board.inputText.setEnabled(false);
					Board.question.setText(wordList.get(Integer.parseInt(data.getMessage())));

					break;
				case Data.ANSWER:
					System.out.println("before tragger : " + data.getMessage().split("#")[2]);
					if (data.getMessage().split("#")[2].equals("null")) {
						output.writeObject(new Data(Data.NEW_QUESTION, data.getName(),
								socket.getLocalPort() + "#" + data.getMessage().split("#")[0]));
					}
					// [0] 다음 문제 단어 위치 [1]다음 문제를 낼 사람 [2]저번에 문제를 낸 사람 [3]문제내는 사람의 위치
					else {
						System.out.println("client new ...");
						if (socket.getLocalPort() == Integer.parseInt(data.getMessage().split("#")[2])
								&& socket.getLocalPort() == Integer.parseInt(data.getMessage().split("#")[1])) {
							output.writeObject(new Data(Data.MY_TURN, data.getName(),
									socket.getLocalPort() + "#" + data.getMessage().split("#")[0]));
						} else {
							if (socket.getLocalPort() == Integer.parseInt(data.getMessage().split("#")[1])) {
								// 자기 자신의 포트번호와 다음문제를 낼 사람이 같으면
								output.writeObject(new Data(Data.NEW_QUESTION, data.getName(),
										socket.getLocalPort() + "#" + data.getMessage().split("#")[0]));
							}
							if (socket.getLocalPort() == Integer.parseInt(data.getMessage().split("#")[2])) {
								output.writeObject(
										new Data(Data.REMOVE_LISTENER, data.getName(), socket.getLocalPort() + ""));
							}
						}
					}
					break;
				case Data.REMOVE_LISTENER:
					Board.question.setText("");
					Board.send.setEnabled(true);
					Board.inputText.setEnabled(true);
					Board.canvas.removeMouseListener(Board.listener);
					Board.canvas.removeMouseMotionListener(Board.listener);
					break;
				case Data.MY_TURN:
					Board.question.setText(wordList.get(Integer.parseInt(data.getMessage())));
					break;
				case Data.TIMER:
					Board.timer.setText(data.getMessage());
					break;
				case Data.WORD:
					wordList.add(data.getMessage());
					break;
				case Data.FILTER:
					filterList.add(data.getMessage());
					break;
				case Data.REDRAW:
					if (message.equals("clear")) {
						System.out.println("=========board clear========");
						Board.canvas.setBackground(Color.BLACK);
						Board.canvas.setBackground(Color.WHITE);
					} else {
						int startX = Integer.parseInt(message.split("#")[0]);
						int startY = Integer.parseInt(message.split("#")[1]);
						int lastX = Integer.parseInt(message.split("#")[2]);
						int lastY = Integer.parseInt(message.split("#")[3]);

						int r = Integer.parseInt(message.split("#")[4]);
						int g = Integer.parseInt(message.split("#")[5]);
						int b = Integer.parseInt(message.split("#")[6]);

						if (message.split("#")[7].equals("pen"))
							CanvasMouseListener.linedraw(startX, startY, lastX, lastY, new Color(r, g, b));
						else
							CanvasMouseListener.erasedraw(startX, startY, lastX, lastY, new Color(r, g, b));
					}
					break;
				}

			} catch (Exception e) {
				return;
			}
		}
	}
}