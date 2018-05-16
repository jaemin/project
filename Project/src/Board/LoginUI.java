package Board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import DAO.MemberDAO;
import VO.MemberVO;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Color;

public class LoginUI extends JFrame implements ActionListener {

	private JTextField idTextField;
	private JPasswordField passwordField;
	private JButton btnLogin;
	private JButton btnRegi;
	
	private static ArrayList<MemberVO> mList;
	
	
	private MemberDAO dao = new MemberDAO();
	private JLabel logo;
	private JLabel label;
	private JLabel back;

	public LoginUI() {
		getContentPane().setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		setSize(450, 450);
		getContentPane().setLayout(null);

		idTextField = new JTextField();
		idTextField.setBounds(174, 200, 160, 25);
		getContentPane().add(idTextField);
		idTextField.setColumns(10);

		btnLogin = new JButton("로그인");
		btnLogin.setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		btnLogin.setBounds(160, 307, 120, 30);
		getContentPane().add(btnLogin);
		btnLogin.addActionListener(this);

		btnRegi = new JButton("회원가입");
		btnRegi.setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		btnRegi.setBounds(251, 360, 120, 30);
		getContentPane().add(btnRegi);
		btnRegi.addActionListener(this);
		
		label = new JLabel("아직 회원이 아니신가요?");
		label.setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		label.setBounds(87, 355, 169, 41);
		getContentPane().add(label);

		JLabel lblNewLabel = new JLabel("아이디");
		lblNewLabel.setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		lblNewLabel.setBounds(73, 203, 100, 20);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("비밀번호");
		lblNewLabel_1.setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(73, 259, 100, 20);
		getContentPane().add(lblNewLabel_1);

		passwordField = new JPasswordField();
		passwordField.setBounds(174, 256, 160, 25);
		getContentPane().add(passwordField);
		
		logo = new JLabel("");
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setIcon(new ImageIcon("logo.png"));
		logo.setBounds(0, 0, 434, 200);
		getContentPane().add(logo);
		
		
		
		back = new JLabel("");
		back.setBackground(Color.WHITE);
		back.setBounds(0, 0, 434, 411);
		getContentPane().add(back);

		setTitle("로그인");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
		case "회원가입":
			RegistrationUI regi = new RegistrationUI();
			break;

		case "로그인":
			
			if(searchMember().size()!=0) {
				char[] c = passwordField.getPassword();
				String pass = "";
				for (int i = 0; i < c.length; i++) {
					pass += c[i];
				}
				
				if(idTextField.getText().equals(mList.get(0).getId()) && pass.equals(mList.get(0).getPassword())) {
				ReadyUI ready = new ReadyUI(mList.get(0));
				this.dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "아이디와 비밀번호가 일치하지 않습니다.", null, JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "존재하는 아이디가 없습니다.", null, JOptionPane.INFORMATION_MESSAGE);
			}
			break;
		}
	}

	public ArrayList<MemberVO> searchMember() {
		mList = dao.searchMember(idTextField.getText());
		return mList;
	}

}
