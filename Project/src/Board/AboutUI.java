package Board;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;

public class AboutUI extends JFrame implements ActionListener {
	public AboutUI() {
		getContentPane().setBackground(Color.WHITE);
		setSize(450, 450);
		getContentPane().setLayout(null);

		JLabel logo = new JLabel("");
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setIcon(new ImageIcon("logo.png"));
		logo.setBounds(0, 0, 434, 200);
		getContentPane().add(logo);

		JLabel lblNewLabel = new JLabel("\uAC1C\uBC1C\uC790");
		lblNewLabel.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 13));
		lblNewLabel.setBounds(50, 250, 57, 15);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("\uC804\uD76C\uBC30");
		lblNewLabel_1.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(65, 280, 57, 15);
		getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("\uC8FC\uC7AC\uBBFC");
		lblNewLabel_2.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(65, 310, 57, 15);
		getContentPane().add(lblNewLabel_2);

		JLabel lblVersion = new JLabel("Version 1.0.0");
		lblVersion.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 13));
		lblVersion.setBounds(50, 180, 141, 40);
		getContentPane().add(lblVersion);

		JLabel lblThanksTo = new JLabel("Thanks to");
		lblThanksTo.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 13));
		lblThanksTo.setBounds(65, 340, 70, 15);
		getContentPane().add(lblThanksTo);

		JButton btnNewButton = new JButton("\uD655\uC778");
		btnNewButton.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 13));
		btnNewButton.setBounds(151, 371, 110, 30);
		btnNewButton.addActionListener(this);
		getContentPane().add(btnNewButton);
		
		setTitle("About");

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}
}
