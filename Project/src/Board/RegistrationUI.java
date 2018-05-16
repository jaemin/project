package Board;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import DAO.MemberDAO;

import VO.MemberVO;

import javax.swing.JPasswordField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.UIManager;

public class RegistrationUI extends JFrame implements ActionListener {
	private JTextField textField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JButton btnNewButton;

	private MemberDAO dao = new MemberDAO();
	private JLabel logo;

	public RegistrationUI() {
		getContentPane().setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		setSize(450, 450);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("\uC544\uC774\uB514");
		lblNewLabel.setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		lblNewLabel.setBounds(75, 200, 62, 18);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("\uBE44\uBC00\uBC88\uD638");
		lblNewLabel_1.setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(75, 250, 82, 18);
		getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("\uBE44\uBC00\uBC88\uD638 \uD655\uC778");
		lblNewLabel_2.setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(75, 300, 116, 18);
		getContentPane().add(lblNewLabel_2);

		textField = new JTextField();
		textField.setBounds(200, 200, 166, 24);
		getContentPane().add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(200, 250, 166, 24);
		getContentPane().add(passwordField);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(200, 300, 166, 23);
		getContentPane().add(passwordField_1);

		btnNewButton = new JButton("\uD68C\uC6D0\uAC00\uC785");
		btnNewButton.setBackground(new Color(192, 192, 192));
		btnNewButton.setFont(new Font("나눔고딕코딩", Font.PLAIN, 13));
		btnNewButton.setBounds(148, 350, 120, 30);
		getContentPane().add(btnNewButton);
		
		logo = new JLabel("");
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setIcon(new ImageIcon("logo.png"));
		logo.setBounds(0, 0, 434, 200);
		getContentPane().add(logo);
		btnNewButton.addActionListener(this);

		setTitle("회원가입");
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		switch (command) {
		case "회원등록":
			char[] c = passwordField.getPassword();
			String pass = "";
			for (int i = 0; i < c.length; i++) {
				pass += c[i];
			}
			char[] c2 = passwordField_1.getPassword();
			String pass2 = "";
			for (int i = 0; i < c2.length; i++) {
				pass2 += c2[i];
			}
			if (pass.equals("") || pass2.equals("") || textField.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "빈간을 모두 채워주세요.", null, JOptionPane.INFORMATION_MESSAGE);
			} else if (pass.contains(" ") || pass2.contains(" ") || textField.getText().contains(" ")) {
				JOptionPane.showMessageDialog(null, "(스페이스바 금지)", null, JOptionPane.INFORMATION_MESSAGE);
			}  else if(textField.getText().length()>15) {
				JOptionPane.showMessageDialog(null, "아이디는 15자 이내로 작성하여 주세요", null, JOptionPane.INFORMATION_MESSAGE);
			}else {
				if (searchMember().size() == 0) {

					if (pass.equals(pass2)) {
						insertMember();
						this.dispose();
						JOptionPane.showMessageDialog(null, "회원등록이 완료되었습니다.", null, JOptionPane.INFORMATION_MESSAGE);
					
					}
					else {
						JOptionPane.showMessageDialog(null, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", null,
								JOptionPane.INFORMATION_MESSAGE);

					}
				} else {
					JOptionPane.showMessageDialog(null, "존재하는 ID입니다.", null, JOptionPane.INFORMATION_MESSAGE);
				}
			}
			break;

		}

	}

	public void insertMember() {
		MemberVO vo = new MemberVO();
		vo.setId(textField.getText());
		char[] c = passwordField.getPassword();
		String pass = "";
		for (int i = 0; i < c.length; i++) {
			pass += c[i];
		}
		vo.setPassword(pass);
		dao.insertMember(vo);

	}

	public ArrayList<MemberVO> searchMember() {
		ArrayList<MemberVO> mList = dao.searchMember(textField.getText());

		return mList;
	}

}
