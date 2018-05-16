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

		btnLogin = new JButton("�α���");
		btnLogin.setFont(new Font("��������ڵ�", Font.PLAIN, 13));
		btnLogin.setBounds(160, 307, 120, 30);
		getContentPane().add(btnLogin);
		btnLogin.addActionListener(this);

		btnRegi = new JButton("ȸ������");
		btnRegi.setFont(new Font("��������ڵ�", Font.PLAIN, 13));
		btnRegi.setBounds(251, 360, 120, 30);
		getContentPane().add(btnRegi);
		btnRegi.addActionListener(this);
		
		label = new JLabel("���� ȸ���� �ƴϽŰ���?");
		label.setFont(new Font("��������ڵ�", Font.PLAIN, 13));
		label.setBounds(87, 355, 169, 41);
		getContentPane().add(label);

		JLabel lblNewLabel = new JLabel("���̵�");
		lblNewLabel.setFont(new Font("��������ڵ�", Font.PLAIN, 13));
		lblNewLabel.setBounds(73, 203, 100, 20);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("��й�ȣ");
		lblNewLabel_1.setFont(new Font("��������ڵ�", Font.PLAIN, 13));
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

		setTitle("�α���");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
		case "ȸ������":
			RegistrationUI regi = new RegistrationUI();
			break;

		case "�α���":
			
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
					JOptionPane.showMessageDialog(null, "���̵�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.", null, JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "�����ϴ� ���̵� �����ϴ�.", null, JOptionPane.INFORMATION_MESSAGE);
			}
			break;
		}
	}

	public ArrayList<MemberVO> searchMember() {
		mList = dao.searchMember(idTextField.getText());
		return mList;
	}

}
