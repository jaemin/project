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
	String ip; // ip�ּ�
	int port;// ��Ʈ��ȣ
	Socket socket;// ����
	JTextArea outputText;// ä��â
	JScrollPane scroll; // �����ڸ�ܽ�ũ��
	JScrollPane scroll2;// ä��â��ũ��
	ObjectInputStream input;
	ObjectOutputStream output;
	private static JLabel label;
	File file;
	FileReader fr;
	BufferedReader br;
	private ArrayList<String> wordList = new ArrayList<>();
	public static ArrayList<String> filterList = new ArrayList<>();

	String username; // �������̸�
	JList<String> userList; // �������̸����

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

	// ��ȭ���� ���â�� �޼��� ���
	public void output(String msg) {
		outputText.append(msg + "\n");
		// ä��â ��ũ�� �Ʒ��� ������
		scroll2.getVerticalScrollBar().setValue(scroll2.getVerticalScrollBar().getMaximum());
	}

	// ���� ����
	public void connect() {
		try {
			socket = new Socket(ip, port);

			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());

			new Thread(this).start();
			Data data = new Data(Data.FIRST_CONNECTION, username);
			output.writeObject(data);

		} catch (IOException e) {
			output("<<���� ���� ����>>");
		}
	}

	// ���� ���� ����
	public void disconnect() {
		Data data = new Data(Data.DISCONNECTION, username);
		try {
			output.writeObject(data);
			output.close();
			input.close();
			socket.close();
		} catch (Exception e) {
			output("<<<���� ����� ���� �߻�>>>");
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
					// ������ ��� ����
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
					// [0] ���� ���� �ܾ� ��ġ [1]���� ������ �� ��� [2]������ ������ �� ��� [3]�������� ����� ��ġ
					else {
						System.out.println("client new ...");
						if (socket.getLocalPort() == Integer.parseInt(data.getMessage().split("#")[2])
								&& socket.getLocalPort() == Integer.parseInt(data.getMessage().split("#")[1])) {
							output.writeObject(new Data(Data.MY_TURN, data.getName(),
									socket.getLocalPort() + "#" + data.getMessage().split("#")[0]));
						} else {
							if (socket.getLocalPort() == Integer.parseInt(data.getMessage().split("#")[1])) {
								// �ڱ� �ڽ��� ��Ʈ��ȣ�� ���������� �� ����� ������
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