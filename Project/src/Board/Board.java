package Board;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import MultiChatClient.ChatClientThread;
import VO.*;
import MultiChatServer.*;

public class Board extends JFrame implements ItemListener {
	static Color penColor = Color.BLACK;
	static Color eraseColor = Color.WHITE;
	static String selected = "pen";
	public static Canvas canvas;
	public static CanvasMouseListener listener;
	public static JTextField inputText;
	public static JLabel question;
	JCheckBox chckbxBGM;
	public static JLabel timer;

	SoundThread r = new SoundThread();
	Thread t1 = new Thread(r);

	 static ChatClientThread thread = new ChatClientThread("127.0.0.1", 3333); //
	// ip, 포트번호 주고 객체 생성
	//static ChatClientThread thread = new ChatClientThread("203.233.196.143", 3333); // ip, 포트번호 주고 객체 생성
	public static JButton send;

	public static Color getpenColor() {
		return penColor;
	}

	public JTextField getInputText() {
		return inputText;
	}

	public void setInputText(JTextField inputText) {
		this.inputText = inputText;
	}

	public static ChatClientThread getThread() {
		return thread;
	}

	public void setThread(ChatClientThread thread) {
		this.thread = thread;
	}

	public Board(MemberVO vo) {
		getContentPane().setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		setFont(new Font("나눔고딕코딩", Font.PLAIN, 15));
		t1.start();
		setResizable(false);
		setTitle("개발괴발 괴발개발");
		setSize(1000, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("나눔고딕", Font.BOLD, 15));
		setJMenuBar(menuBar);

		JMenu file = new JMenu("File");
		file.setHorizontalAlignment(SwingConstants.CENTER);
		file.setFont(new Font("나눔고딕코딩", Font.PLAIN, 17));
		menuBar.add(file);

		JMenuItem save = new JMenuItem("Save");
		save.setFont(new Font("나눔고딕코딩", Font.PLAIN, 15));
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 이미지 저장
				/*
				 * BufferedImage image = new BufferedImage(650, 385,
				 * BufferedImage.TYPE_INT_RGB);
				 * 
				 * image.createGraphics(); CanvasMouseListener.redraw(); File file = new
				 * File("test.jpg"); try { ImageIO.write(image, "jpeg", file); } catch
				 * (IOException e1) { e1.printStackTrace(); }
				 */
			}
		});
		file.add(save);

		JMenuItem close = new JMenuItem("Close");
		close.setFont(new Font("나눔고딕코딩", Font.PLAIN, 15));
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thread.disconnect();
			}
		});
		file.add(close);

		JMenu edit = new JMenu("Edit");
		edit.setFont(new Font("나눔고딕코딩", Font.PLAIN, 17));
		edit.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(edit);

		JMenuItem pen = new JMenuItem("Pen");
		pen.setFont(new Font("나눔고딕코딩", Font.PLAIN, 15));
		pen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected = "pen"; // 커서 상태를 펜으로 바꿈
			}
		});
		edit.add(pen);

		JMenuItem eraser = new JMenuItem("Eraser");
		eraser.setFont(new Font("나눔고딕코딩", Font.PLAIN, 15));
		eraser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected = "eraser"; // 커서 상태를 지우개로 바꿈
			}
		});
		edit.add(eraser);

		JMenuItem pen_color = new JMenuItem("Pen Color");
		pen_color.setFont(new Font("나눔고딕코딩", Font.PLAIN, 15));
		pen_color.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser cc = new JColorChooser();
				Color c = cc.showDialog(null, "색 설정", penColor);
				CanvasMouseListener.redraw();
				penColor = c; // 펜의 컬러를 사용자가 지정한 색으로 바꿈
				selected = "pen";
			}
		});
		edit.add(pen_color);

		JMenuItem all_clear = new JMenuItem("All Clear");
		all_clear.setFont(new Font("나눔고딕코딩", Font.PLAIN, 15));
		all_clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.setBackground(Color.BLACK);
				canvas.setBackground(Color.WHITE); // 현재 캔버스에 그려저 있는 그림을 모두 지우고
				CanvasMouseListener.coor.clear();
				CanvasMouseListener.record.clear(); // 그림 그린 기록을 삭제함
				CanvasMouseListener.count = 0; // 카운터도 초기화함

				String msg = "clear";

				Data data = new Data(Data.PICTURE_MESSAGE, null, msg, null);
				try {
					thread.getOutput().writeObject(data);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		edit.add(all_clear);

		JMenu help = new JMenu("Help");
		help.setFont(new Font("나눔고딕코딩", Font.PLAIN, 17));
		menuBar.add(help);

		JMenuItem about = new JMenuItem("About");
		about.setFont(new Font("나눔고딕코딩", Font.PLAIN, 15));
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AboutUI();
			}
		});
		help.add(about);

		JPanel canvas_panel = new JPanel();
		// canvas_panel.setBorder(new LineBorder(new Color(169, 169, 169)));
		canvas_panel.setBounds(20, 20, 650, 385);
		getContentPane().add(canvas_panel);
		canvas_panel.setLayout(null);

		listener = new CanvasMouseListener();
		canvas = new Canvas();
		canvas.setBackground(Color.white);
		canvas.setBounds(1, 1, 648, 383);
		canvas_panel.add(canvas);

		// 채팅부분
		JPanel chat_panel = new JPanel();
		chat_panel.setBounds(20, 415, 650, 160);
		getContentPane().add(chat_panel);
		chat_panel.setOpaque(false);
		chat_panel.setLayout(null);

		// 채팅창 스크롤
		thread.setScroll2(new JScrollPane());
		thread.getScroll2().setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		thread.getScroll2().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		thread.getScroll2().setBounds(0, 0, 650, 125);
		chat_panel.add(thread.getScroll2());

		// 채팅출력창
		thread.setOutputText(new JTextArea());
		// thread.getOutputText().setBorder(new LineBorder(new Color(169, 169, 169)));
		thread.getOutputText().setLineWrap(true);
		thread.getOutputText().setEditable(false);
		thread.getOutputText().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		thread.getOutputText().setFont(new Font("나눔고딕코딩", Font.PLAIN, 12));

		thread.getScroll2().setViewportView(thread.getOutputText());

		// 채팅입력창
		inputText = new JTextField();
		inputText.setFont(new Font("나눔고딕코딩", Font.PLAIN, 12));
		inputText.setBounds(0, 130, 570, 30);
		chat_panel.add(inputText);
		inputText.setColumns(10);

		send = new JButton("Send");
		// send.setBorder(new LineBorder(new Color(169, 169, 169)));
		send.setFont(new Font("나눔고딕", Font.PLAIN, 17));
		send.setForeground(Color.DARK_GRAY);
		send.setBounds(574, 129, 76, 30);
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 채팅입력창액션
				String message = inputText.getText();

				for (int i = 0; i < ChatClientThread.filterList.size(); i++) {
					if (message.contains(ChatClientThread.filterList.get(i)))
						message = "바르고 고운말을 사용합시다.";
				}

				if (message.isEmpty())
					return;
				inputText.setText("");
				Data data = new Data(Data.CHAT_MESSAGE, thread.getUsername(), message);
				try {
					thread.getOutput().writeObject(data);
				} catch (Exception e1) {
					thread.output("<<전송 실패>>");
				}
			}
		});
		chat_panel.add(send);

		inputText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 채팅입력창액션
				String message = inputText.getText();

				File file = new File("filterWord");
				FileReader fr;
				BufferedReader br;
				String filterList = "";
				try {
					fr = new FileReader(file);
					br = new BufferedReader(fr);

					String str;
					while ((str = br.readLine()) != null) {
						filterList += str + "#";
					}
					String[] filter = filterList.split("#");

					for (int i = 0; i < filter.length; i++) {
						if (message.contains(filter[i]))
							message = "바르고 고운말을 사용합시다.";
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				if (message.isEmpty())
					return;
				inputText.setText("");
				Data data = new Data(Data.CHAT_MESSAGE, thread.getUsername(), message);
				try {
					thread.getOutput().writeObject(data);
				} catch (Exception e1) {
					thread.output("<<전송 실패>>");
				}
			}
		});

		JPanel multimedia = new JPanel();
		multimedia.setBounds(690, 20, 280, 75);
		multimedia.setVisible(true);
		getContentPane().add(multimedia);
		multimedia.setOpaque(false);
		multimedia.setLayout(null);

		// 술래에게 나타나는 문제화면
		question = new JLabel("");
		question.setFont(new Font("나눔고딕코딩", Font.PLAIN, 18));
		question.setHorizontalAlignment(SwingConstants.CENTER);
		question.setBounds(100, 24, 180, 50);
		multimedia.add(question);

		chckbxBGM = new JCheckBox("BGM ON");
		chckbxBGM.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxBGM.setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		chckbxBGM.setBackground(Color.WHITE);
		chckbxBGM.setSelected(true);
		chckbxBGM.setBounds(185, 0, 95, 27);
		chckbxBGM.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED)
					chckbxBGM.setText("BGM OFF");
				else
					chckbxBGM.setText("BGM ON");
			}
		});
		multimedia.add(chckbxBGM);

		timer = new JLabel("");
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		timer.setFont(new Font("나눔고딕코딩", Font.PLAIN, 17));
		timer.setBounds(0, 24, 100, 50);
		multimedia.add(timer);

		chckbxBGM.addItemListener(this);

		JPanel list = new JPanel();
		list.setBackground(Color.ORANGE);
		list.setBounds(690, 105, 280, 300);
		getContentPane().add(list);
		list.setOpaque(false);
		list.setLayout(null);

		// 접속자명단스크롤
		thread.setScroll(new JScrollPane());
		thread.getScroll().setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		thread.getScroll().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		thread.getScroll().setBounds(0, 0, 280, 300);
		list.add(thread.getScroll());

		// 접속자명단리스트
		thread.setUserList(new JList<String>());
		thread.getUserList().setFont(new Font("나눔고딕코딩", Font.PLAIN, 18));
		thread.getScroll().setViewportView(thread.getUserList());
		thread.getScroll().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thread.disconnect(); // 클라이언트 접속끊음

			}
		});

		setVisible(true);

		JPanel logo = new JPanel();
		logo.setBounds(690, 415, 280, 160);
		getContentPane().add(logo);
		logo.setOpaque(false);
		logo.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("logo.png"));
		lblNewLabel.setBounds(0, 0, 280, 160);
		logo.add(lblNewLabel);

		JLabel background = new JLabel("");
		background.setIcon(new ImageIcon("background.png"));
		background.setBounds(0, 0, 994, 599);
		getContentPane().add(background);

		thread.connect();// 클라이언트 접속
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			r = new SoundThread();
			t1 = new Thread(r);
			t1.start();
		} else if (e.getStateChange() == ItemEvent.DESELECTED) {
			r.stop();
		}
	}
}