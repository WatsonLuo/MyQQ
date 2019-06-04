import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class UIRegister extends JFrame {

	static private String id;
	private UIRegister uiRegister;
	public static void main(String[] args) {
		DerbyDB.prepareDB();
		DerbyDB.createAccountTable();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {new UIRegister();}
				catch (Exception e) {e.printStackTrace();}
			}
		});
	}

	public UIRegister() {
		setSize(300, 300);
		setResizable(false);
		setTitle("注册窗体");
		Font font=new Font("宋体",Font.PLAIN,24);
		uiRegister=this;
		
		JLabel userNameJLabel=new JLabel("昵称：");
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

	    //限制输入文本框的长度  
	    userNameField.setDocument(new PlainDocument() {        
	    	@Override  
	        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {  
	    		String text = userNameField.getText();  
	            if (text.length() + str.length() > 20) {  
	            	Toolkit.getDefaultToolkit().beep();  
	                JOptionPane.showMessageDialog(null, "不能超过20个字符！!");  
	                return;  
	            }  
	            super.insertString(offs, str, a);  
	       }  
	    });  
	    passwordField.setDocument(new PlainDocument() {        
	    	@Override  
	        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {  
	    		String text = String.valueOf(passwordField.getPassword());  
	            if (text.length() + str.length() > 20) {  
	            	Toolkit.getDefaultToolkit().beep();  
	                JOptionPane.showMessageDialog(null, "不能超过20个字符！!");  
	                return;  
	            }  
	            super.insertString(offs, str, a);  
	       }  
	    });  	   
	    ensureField.setDocument(new PlainDocument() {        
	    	@Override  
	        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {  
	    		String text = String.valueOf(ensureField.getPassword());  
	            if (text.length() + str.length() > 20) {  
	            	Toolkit.getDefaultToolkit().beep();  
	                JOptionPane.showMessageDialog(null, "不能超过20个字符！!");  
	                return;  
	            }  
	            super.insertString(offs, str, a);  
	       }  
	    });  
		
		
		
		
		
		
		

		Box userNameBox=Box.createHorizontalBox();
		userNameBox.add(userNameJLabel);
		userNameBox.add(Box.createHorizontalStrut(48));
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
		submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String password=String.valueOf(passwordField.getPassword());
				String ensure=String.valueOf(ensureField.getPassword());
				if(password.equals(ensure)
						&&!password.equals("")
						&&!userNameField.getText().equals(""))
				{
					System.out.println("注册前");
					DerbyDB.showAccountTable();
				
					id=DerbyDB.getNewID();
					JOptionPane.showMessageDialog(null,"您的ID是："+id);
					DerbyDB.insertAccountTable(id, password, userNameField.getText(), 0);
				
					System.out.println("注册后");
					DerbyDB.showAccountTable();
					JOptionPane.showMessageDialog(null, "注册成功！");
					uiRegister.dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "两次密码输入不一致！");
				}
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
}