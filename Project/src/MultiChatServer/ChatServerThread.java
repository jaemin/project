package MultiChatServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import DAO.MemberDAO;
import VO.Data;
import VO.GameVO;
import VO.MemberVO;

public class ChatServerThread implements Runnable {
	public static HashMap<Integer, ChatServerThread> chatList = new HashMap<>(); // 접속중인 클라이언트 목록
	public static ArrayList<String> usernames = new ArrayList<String>(); // 접속자 이름 목록
	public static ArrayList<Integer> userports = new ArrayList<>();
	public static ArrayList<String> picture_record = new ArrayList<>();
	private ArrayList<String> wordList = new ArrayList<>();
	private ArrayList<String> filterList = new ArrayList<>();

	private static MemberDAO dao = new MemberDAO();
	static String questioner;

	Socket socket;
	ObjectInputStream input;
	ObjectOutputStream output;
	String username, addr;
	public static int count = -1;
	public static int beforetragger = 0, tragger = 0;
	FileReader fr;
	BufferedReader br;
	FileWriter fw;
	BufferedWriter bw;
	public static boolean flag = false;

	public ChatServerThread(Socket socket) {
		File file = new File("wordList");
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String str;
			while ((str = br.readLine()) != null) {
				wordList.add(str);
				// output.writeObject(new Data(Data.WORD, null, str));
			}
		} catch (Exception e) {
		}
		file = new File("filterWord");
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String str;
			while ((str = br.readLine()) != null) {
				filterList.add(str);
				// output.writeObject(new Data(Data.FILTER, null, str));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.socket = socket;
			// 클라이언트와의 스트림 생성
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			addr = socket.getInetAddress().getHostAddress();
		} catch (IOException e) {
			System.out.println(addr + "과의 연결 실패.");
		}
		chatList.put(socket.getPort(), this);
	}

	// 클라이언트로부터의 전송을 기다림.
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

				switch (state) {
				case Data.FIRST_CONNECTION: // 새로운 접속자
					
					message = name + "님이 접속했습니다.";
					usernames.add(name);
					userports.add(socket.getPort());

					data.setMessage(message);
					data.setUsernames(usernames);

					broadCasting(data);

					for (int i = 0; i < wordList.size(); i++)
						output.writeObject(new Data(Data.WORD, null, wordList.get(i)));
					for (int i = 0; i < filterList.size(); i++)
						output.writeObject(new Data(Data.FILTER, null, filterList.get(i)));

					if (chatList.size() >= 3) {
						if (count == -1) {
							count++;
							tragger = userports.get(count);
							output.writeObject(new Data(Data.ANSWER, data.getName(),
									ChatServer.counter + "#" + tragger + "#null#-1"));
							beforetragger = tragger;
							count++;
						}
						GameVO vocheck = new GameVO();
						vocheck.setId(name);
						vocheck.setGame_seq(String.valueOf(ChatServer.gameseq));
						if (bringGame(vocheck).size() == 0) {
							insertGame(ChatServer.gameseq, name, wordList.get(ChatServer.counter));
						}
					}
					if (picture_record.size() != 0) {
						for (int i = 0; i < picture_record.size(); i++) {
							output.writeObject(new Data(Data.REDRAW, null, picture_record.get(i)));
							Thread.sleep(1);
						}
					}
					break;

				case Data.CHAT_MESSAGE: // 채팅 내용 전송
					try {
						if (wordList.get(ChatServer.counter).equals(data.getMessage())) {
							ChatServer.counter++;
							ChatServer.gameseq++;

							if (ChatServer.counter == ChatServer.getQuestion().size())
								ChatServer.counter = 0;
							// [0] 다음 문제 단어 위치 [1]다음 문제를 낼 사람 [2]저번에 문제를 낸 사람 [3]문제내는 사람의 위치
							if (count >= chatList.size())
								count %= chatList.size();
							tragger = userports.get(count);
							broadCasting(new Data(Data.ANSWER, data.getName(), ChatServer.counter + "#"
									+ tragger + "#" + beforetragger + "#" + count));
							flag = true;
							beforetragger = tragger;
							picture_record.clear();
							System.out.println("count : " + count);
							System.out.println("next person : " + userports.get(count));
							count++;
							if (count == chatList.size())
								count = 0;
							broadCasting(new Data(Data.PICTURE_MESSAGE, data.getName(), "clear"));

							try {
								for (int i = 0; i < usernames.size(); i++) {
									GameVO vo = new GameVO();
									vo.setGame_seq(String.valueOf(ChatServer.gameseq - 1));
									vo.setId(usernames.get(i));
									String usertype = "try";

									System.out.println((ChatServer.counter % usernames.size()) - 1);
									int q = (ChatServer.counter % usernames.size()) - 1;
									if ((ChatServer.counter % usernames.size()) - 1 == -1) {
										q = usernames.size() - 1;
									}

									if (usernames.get(i).equals(data.getName())) {
										usertype = "correct";
									} else if (usernames.get(i).equals(usernames.get(q))) {
										usertype = "question";
									}

									updateGame(bringGame(vo).get(0), usertype);
									System.out.println(bringGame(vo).get(0));
									// Thread.sleep(1);
								}
							}

							catch (Exception e) {
								e.printStackTrace();
							}

						} else {
							broadCasting(data);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case Data.DISCONNECTION: // 접속 종료
					message = name + "님이 접속을 종료했습니다.";
					usernames.remove(name);
					for (int i = 0; i < userports.size(); i++) {
						if (userports.get(i) == socket.getPort()) {
							userports.remove(i);
						}
					}
					chatList.remove(socket.getPort());
					input.close();
					output.close();
					socket.close();
					data.setMessage(message);
					data.setUsernames(usernames);

					broadCasting(data);

					if (chatList.size() == 0) {
						ChatServer.counter = 0;
						count = -1;
						beforetragger = 0;
						flag = false;
					}
					if (socket.getPort() == beforetragger) {
						broadCasting(new Data(Data.PICTURE_MESSAGE, data.getName(), "clear"));
						ChatServer.counter++;
						if (ChatServer.counter == ChatServer.getQuestion().size())
							ChatServer.counter = 0;
						tragger = userports.get(count);
						broadCasting(new Data(Data.ANSWER, data.getName(),
								ChatServer.counter + "#" + tragger + "#" + beforetragger + "#" + count));
						count++;
						if (count == chatList.size())
							count = 0;
						beforetragger = tragger;
					}
					break;
				case Data.PICTURE_MESSAGE:
					picture_record.add(data.getMessage());
					broadCasting(data);
					break;
				case Data.NEW_QUESTION:
					for (int i = 0; i < usernames.size(); i++) {
						insertGame(ChatServer.gameseq, usernames.get(i), wordList.get(ChatServer.counter));
					}
					for (HashMap.Entry<Integer, ChatServerThread> thread : chatList.entrySet()) {
						if (thread.getKey() == Integer.parseInt(data.getMessage().split("#")[0])) {
							try {
								thread.getValue().output.writeObject(
										new Data(Data.NEW_QUESTION, data.getName(), data.getMessage().split("#")[1]));
								thread.getValue().output.reset();
								new TimerThread();
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						}
					}
					break;
				case Data.REMOVE_LISTENER:
					for (HashMap.Entry<Integer, ChatServerThread> thread : chatList.entrySet()) {
						if (thread.getKey() == Integer.parseInt(data.getMessage())) {
							try {
								thread.getValue().output.writeObject(data);
								thread.getValue().output.reset();
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						}
					}
					break;
				case Data.MY_TURN:
					for (HashMap.Entry<Integer, ChatServerThread> thread : chatList.entrySet()) {
						if (thread.getKey() == Integer.parseInt(data.getMessage().split("#")[0])) {
							try {
								thread.getValue().output.writeObject(
										new Data(Data.MY_TURN, data.getName(), data.getMessage().split("#")[1]));
								thread.getValue().output.reset();
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						}
					}
					break;
				case Data.TIMER:
					broadCasting(data);
					break;
				}
			} catch (Exception e) {
				return;
			}
		}
	}

	// 전체 접속자에게 전송
	public void broadCasting(Data data) {
		for (HashMap.Entry<Integer, ChatServerThread> thread : chatList.entrySet()) {
			try {
				thread.getValue().output.writeObject(data);

				thread.getValue().output.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertGame(int seq, String id, String word) {
		GameVO vo = new GameVO();
		vo.setGame_seq(String.valueOf(seq));
		vo.setId(id);
		vo.setWord(word);
		dao.insertGame(vo);
	}

	public void updateGame(GameVO vo, String usertype) {

		vo.setUsertype(usertype);
		dao.updateGame(vo);

		updateGame2(bringGame(vo).get(0));
	}

	public void updateGame2(GameVO vo2) {

		// 2018-05-12 19:03:06
		String st = vo2.getStarttime();
		int stmi = Integer.parseInt(st.substring(14, 16)); // mi
		int stss = Integer.parseInt(st.substring(17, 19)); // ss

		String et = vo2.getEndtime();
		int etmi = Integer.parseInt(et.substring(14, 16)); // mi
		int etss = Integer.parseInt(et.substring(17, 19)); //
															// ss

		int playtime = 0;

		if (stmi > etss) {
			playtime = (etmi + 60) * 60 + etss - (stmi * 60 + stss);
		} else {
			playtime = (etmi) * 60 + etss - (stmi * 60 + stss);
		}
		String pt = String.valueOf(playtime);
		vo2.setPlaytime(pt);
		dao.updateGame2(vo2);

		GameVO vo3 = bringGame(vo2).get(0);
		MemberVO vom = searchMember(vo3.getId()).get(0);
		switch (vo3.getUsertype()) {
		case "question":
			vom.setTotalplaytime(
					String.valueOf(Integer.parseInt(vom.getTotalplaytime()) + Integer.parseInt(vo3.getPlaytime())));
			break;
		case "correct":
			vom.setPlaynum(String.valueOf(Integer.parseInt(vom.getPlaynum()) + 1));
			vom.setCorrectnum(String.valueOf(Integer.parseInt(vom.getCorrectnum()) + 1));
			vom.setTotalplaytime(
					String.valueOf(Integer.parseInt(vom.getTotalplaytime()) + Integer.parseInt(vo3.getPlaytime())));
			break;
		case "try":
			vom.setPlaynum(String.valueOf(Integer.parseInt(vom.getPlaynum()) + 1));
			vom.setTotalplaytime(
					String.valueOf(Integer.parseInt(vom.getTotalplaytime()) + Integer.parseInt(vo3.getPlaytime())));
			break;
		case "default":
			vom.setPlaynum(String.valueOf(Integer.parseInt(vom.getPlaynum()) + 1));
			break;
		}

		updateGameMember(vom);
	}

	public void updateGameMember(MemberVO vom) {

		dao.updateGameMember(vom);
		updateGameMember2(searchMember(vom.getId()).get(0));
	}

	public void updateGameMember2(MemberVO vom) {
		if (Integer.parseInt(vom.getPlaynum()) != 0) {
			float f = Float.parseFloat(vom.getCorrectnum()) / Integer.parseInt(vom.getPlaynum());
			vom.setWinningRate(String.valueOf(f));
			dao.updateGameMember2(vom);
		}
	}

	public ArrayList<MemberVO> searchMember(String id) {
		ArrayList<MemberVO> mList = new ArrayList<>();
		mList = dao.searchMember(id);

		return mList;
	}

	public ArrayList<GameVO> bringGame(GameVO vo) {
		ArrayList<GameVO> gList = dao.bringGame(vo);
		return gList;
	}

	public static int bringGameSeq() {
		int result = dao.bringGameSeq();
		return result;
	}
}