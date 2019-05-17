import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UIRegister extends JFrame {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new UIRegister(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UIRegister(JFrame jFrame) {
		// TODO Auto-generated constructor stub
		try {jFrame.dispose();} 
		catch (Exception e) {}
		setSize(300, 300);
		setResizable(false);
		setTitle("注册窗体");
		Font font=new Font("宋体",Font.PLAIN,24);
		
		JLabel userNameJLabel=new JLabel("用户名：");
		JLabel passwordJLabel=new JLabel("密码：");
		JLabel ensureJLabel=new JLabel("确认密码：");
		userNameJLabel.setFont(font);
		passwordJLabel.setFont(font);
		ensureJLabel.setFont(font);
		
		JTextField userNameField=new JTextField();
		JPasswordField passwordField=new JPasswordField();
		JPasswordField ensureField=new JPasswordField();
		userNameField.setFont(font);
		passwordField.setFont(font);
		ensureField.setFont(font);

		Box userNameBox=Box.createHorizontalBox();
		userNameBox.add(userNameJLabel);
		userNameBox.add(Box.createHorizontalStrut(24));
		userNameBox.add(userNameField);
		Box passwordBox=Box.createHorizontalBox();
		passwordBox.add(passwordJLabel);
		passwordBox.add(Box.createHorizontalStrut(48));
		passwordBox.add(passwordField);
		Box ensureBox=Box.createHorizontalBox();
		ensureBox.add(ensureJLabel);
		ensureBox.add(ensureField);

		Button resetButton=new Button("重置");
		Button submitButton=new Button("提交");
		resetButton.setFont(font);
		submitButton.setFont(font);
		Box buttonBox=Box.createHorizontalBox();
		buttonBox.add(resetButton);
		buttonBox.add(submitButton);
		resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				userNameField.setText("");
				passwordField.setText("");
				ensureField.setText("");
			}
		});
		
		Box box=Box.createVerticalBox();
		box.add(Box.createVerticalStrut(25));
		box.add(userNameBox);
		box.add(Box.createVerticalStrut(30));
		box.add(passwordBox);
		box.add(Box.createVerticalStrut(30));
		box.add(ensureBox);
		box.add(Box.createVerticalStrut(25));
		
		getContentPane().add(box,BorderLayout.CENTER);
		getContentPane().add(buttonBox,BorderLayout.SOUTH);
		
//		pack();
		setVisible(true);
		setLocationRelativeTo(null);//窗体居中，参数是父窗体，null代表没有父窗体，针对桌面居中
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
