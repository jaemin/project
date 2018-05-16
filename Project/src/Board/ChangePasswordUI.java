package Board;

import javax.swing.JFrame;

import DAO.MemberDAO;
import VO.MemberVO;

import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class ChangePasswordUI extends JFrame implements ActionListener {
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JPasswordField prePassword;
	private JButton button;
	private MemberVO vo;
	private MemberDAO dao = new MemberDAO();
	private JLabel logo;

	public ChangePasswordUI(MemberVO vo) {
		getContentPane().setBackground(Color.WHITE);
		this.vo = vo;
		setSize(450, 450);
		getContentPane().setLayout(null);

		passwordField = new JPasswordField();
		passwordField.setBounds(200, 250, 160, 25);
		getContentPane().add(passwordField);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(200, 300, 160, 25);
		getContentPane().add(passwordField_1);

		JLabel label = new JLabel("���� ��й�ȣ");
		label.setFont(new Font("��������ڵ�", Font.PLAIN, 13));
		label.setBounds(60, 200, 100, 25);
		getContentPane().add(label);

		JLabel label_1 = new JLabel("�� ��й�ȣ");
		label_1.setFont(new Font("��������ڵ�", Font.PLAIN, 13));
		label_1.setBounds(60, 250, 100, 25);
		getContentPane().add(label_1);

		JLabel label_2 = new JLabel("��й�ȣ Ȯ��");
		label_2.setFont(new Font("��������ڵ�", Font.PLAIN, 13));
		label_2.setBounds(60, 300, 100, 25);
		getContentPane().add(label_2);

		button = new JButton("��й�ȣ ����");
		button.setFont(new Font("��������ڵ�", Font.PLAIN, 13));
		button.setBounds(150, 355, 130, 30);
		getContentPane().add(button);
		button.addActionListener(this);

		prePassword = new JPasswordField();
		prePassword.setBounds(200, 200, 160, 25);
		getContentPane().add(prePassword);

		logo = new JLabel("");
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setIcon(new ImageIcon("logo.png"));
		logo.setBounds(0, 0, 434, 200);
		getContentPane().add(logo);

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		switch (command) {
		case "��й�ȣ ����":

			char[] pc = prePassword.getPassword();
			String ppass = "";
			for (int i = 0; i < pc.length; i++) {
				ppass += pc[i];
			}

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
			if (pass.equals("") || pass2.equals("") || ppass.equals("")) {
				JOptionPane.showMessageDialog(null, "���� ��� ä���ּ���.", null, JOptionPane.INFORMATION_MESSAGE);
			} else if (pass.contains(" ") || pass2.contains(" ") || ppass.contains(" ")) {
				JOptionPane.showMessageDialog(null, "(�����̽��� ����)", null, JOptionPane.INFORMATION_MESSAGE);
			} else {

				if (pass.equals(pass2)) {
					updateMember(vo);
					this.dispose();
					JOptionPane.showMessageDialog(null, "��й�ȣ������ �Ϸ�Ǿ����ϴ�.", null, JOptionPane.INFORMATION_MESSAGE);

				} else {
					JOptionPane.showMessageDialog(null, "��й�ȣ�� ��й�ȣ Ȯ���� ��ġ���� �ʽ��ϴ�.", null,
							JOptionPane.INFORMATION_MESSAGE);

				}

			}
			break;
		}

	}

	public void updateMember(MemberVO mem) {

		char[] c = passwordField.getPassword();
		String pass = "";
		for (int i = 0; i < c.length; i++) {
			pass += c[i];
		}
		mem.setPassword(pass);
		dao.updateMember(mem);
	}
}
