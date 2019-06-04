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
		setTitle("ע�ᴰ��");
		Font font=new Font("����",Font.PLAIN,24);
		uiRegister=this;
		
		JLabel userNameJLabel=new JLabel("�ǳƣ�");
		JLabel passwordJLabel=new JLabel("���룺");
		JLabel ensureJLabel=new JLabel("ȷ�����룺");
		userNameJLabel.setFont(font);
		passwordJLabel.setFont(font);
		ensureJLabel.setFont(font);
		
		JTextField userNameField=new JTextField();
		JPasswordField passwordField=new JPasswordField();
		JPasswordField ensureField=new JPasswordField();
		userNameField.setFont(font);
		passwordField.setFont(font);
		ensureField.setFont(font);

	    //���������ı���ĳ���  
	    userNameField.setDocument(new PlainDocument() {        
	    	@Override  
	        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {  
	    		String text = userNameField.getText();  
	            if (text.length() + str.length() > 20) {  
	            	Toolkit.getDefaultToolkit().beep();  
	                JOptionPane.showMessageDialog(null, "���ܳ���20���ַ���!");  
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
	                JOptionPane.showMessageDialog(null, "���ܳ���20���ַ���!");  
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
	                JOptionPane.showMessageDialog(null, "���ܳ���20���ַ���!");  
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

		Button resetButton=new Button("����");
		Button submitButton=new Button("�ύ");
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
					System.out.println("ע��ǰ");
					DerbyDB.showAccountTable();
				
					id=DerbyDB.getNewID();
					JOptionPane.showMessageDialog(null,"����ID�ǣ�"+id);
					DerbyDB.insertAccountTable(id, password, userNameField.getText(), 0);
				
					System.out.println("ע���");
					DerbyDB.showAccountTable();
					JOptionPane.showMessageDialog(null, "ע��ɹ���");
					uiRegister.dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "�����������벻һ�£�");
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
		setLocationRelativeTo(null);//������У������Ǹ����壬null����û�и����壬����������
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
}