import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UISignIN extends JFrame {

	private String[] account=new String[3];
	private int rememberME;
	
	private int port=9999;
	private String IPString="10.5.100.200";
	private Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DerbyDB.prepareDB();
		DerbyDB.createAccountTable();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {new UISignIN();} 
				catch (Exception e) {e.printStackTrace();}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UISignIN() {
//		setBounds(100, 100, 450, 300);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		contentPane.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);
		
		//使改窗口不能使用鼠标调整大小

		rememberME=DerbyDB.getAccount0(account);
		
		
		UISignIN signINJFrame=this;
		setResizable(false);
		setTitle("罗静怡160341218");
		Font font=new Font("宋体",Font.PLAIN,24);
		
		//设置north图标
		JLabel topImgJLabel=new JLabel();
		topImgJLabel.setIcon(new ImageIcon("img\\QQ登录页头.png"));
		Box northBox=Box.createHorizontalBox();
		northBox.add(topImgJLabel);
		getContentPane().add(northBox,BorderLayout.NORTH);
		
		
		//设置west图标
		JLabel leftImgJLabel=new JLabel();
		leftImgJLabel.setIcon(new ImageIcon("img\\QQ.jpg"));
		JPanel westJPanel=new JPanel();
		westJPanel.add(leftImgJLabel);
		getContentPane().add(westJPanel,BorderLayout.WEST);
		
		//设置center输入
		//输入控件
		JLabel IDJLabel=new JLabel("QQ号：");
		JTextField IDField=new JTextField();
		IDJLabel.setFont(font);
		IDField.setFont(font);	
		Box IDBox=Box.createHorizontalBox();
		IDBox.add(Box.createHorizontalStrut(10));
		IDBox.add(IDJLabel);
		IDBox.add(IDField);
		JLabel passwordJLabel=new JLabel("密码：");
		JPasswordField passwordField=new JPasswordField();
		passwordJLabel.setFont(font);
		passwordField.setFont(font);

		Box passwordBox=Box.createHorizontalBox();
		passwordBox.add(Box.createHorizontalStrut(10));
		passwordBox.add(passwordJLabel);
		passwordBox.add(passwordField);
		if(rememberME>0)
		{
			IDField.setText(account[0]);
			passwordField.setText(account[1]);
		}
		
		
		//记住我，自动登录（思考），找回密码
		JCheckBox rememberMeBox=new JCheckBox("记住密码",false);
		JCheckBox autoBox=new JCheckBox("自动登录",false);
		Box functionBox=Box.createHorizontalBox();
		functionBox.add(rememberMeBox);
		functionBox.add(autoBox);

		if(rememberME>0)
		{
			rememberMeBox.setSelected(true);
			if(rememberME==2)autoBox.setSelected(true);
		}
		autoBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(autoBox.isSelected()==true)
					rememberMeBox.setSelected(true);
			}
			
		});
		
		
		
		Box centerBox=Box.createVerticalBox();
		centerBox.add(Box.createVerticalStrut(25));
		centerBox.add(IDBox);
		centerBox.add(Box.createRigidArea(new Dimension(150,20)));
		centerBox.add(passwordBox);
		centerBox.add(Box.createVerticalStrut(10));
		centerBox.add(functionBox);
		getContentPane().add(centerBox,BorderLayout.CENTER);
		
		

		JButton fetchButton=new JButton("找回密码");
		fetchButton.setContentAreaFilled(false);
		fetchButton.setBorderPainted(false);
		fetchButton.setFocusPainted(false);
		fetchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		JButton showPasswordButton=new JButton("显示密码");
		showPasswordButton.setContentAreaFilled(false);
		showPasswordButton.setBorderPainted(false);
		showPasswordButton.setFocusPainted(false);
		char c=passwordField.getEchoChar();
		showPasswordButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(showPasswordButton.getText()=="显示密码")
				{
					passwordField.setEchoChar('\0');
					showPasswordButton.setText("隐藏密码");
				}
				else 
				{
					passwordField.setEchoChar(c);
					showPasswordButton.setText("显示密码");
				}
			}
		});
		Box eastBox=Box.createVerticalBox();
		eastBox.add(Box.createVerticalStrut(30));
		eastBox.add(fetchButton);
		eastBox.add(Box.createVerticalStrut(35));
		eastBox.add(showPasswordButton);
		getContentPane().add(eastBox,BorderLayout.EAST);
		
		//设置south按钮
		JPanel southJPanel=new JPanel();
		JButton signINButton=new JButton("登录");
		JButton registerButton=new JButton("注册");
		signINButton.setFont(font);
		registerButton.setFont(font);;
		southJPanel.add(signINButton);
		southJPanel.add(registerButton);		
		getContentPane().add(southJPanel,BorderLayout.SOUTH);
		signINButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int result=SignIN(IDField.getText(), String.valueOf(passwordField.getPassword()));
				if(result==0)
				{
					new friendList(IDField.getText(),socket,oos,ois);
					signINJFrame.dispose();
				}
				else if(result==1)//未输入ID
				{
					passwordField.setText("");
				}
				else if(result==2)//无此ID
				{
					IDField.setText("");
					passwordField.setText("");
				}
				else if(result==3)//密码错误
				{
					passwordField.setText("");
				}	
			}
		});
		registerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new UIRegister();
			}
		});
		
		pack();
		setVisible(true);
		setLocationRelativeTo(null);//窗体居中，参数是父窗体，null代表没有父窗体，针对桌面居中
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public int SignIN(String ID,String password) {
		if (ID.length() > 0) {
			String ensureString=DerbyDB.getPassword(ID);
			if(ensureString==null)
			{
				JOptionPane.showMessageDialog(null, "不存在此ID");
				return 3;
			}
			else if(ensureString.equals(password))
			{
				// 与服务器端建立Socket连接，如果抛出异常，则弹出对话框通知用户，并退出
				try 
				{
					socket = new Socket("localhost", port);
					// 将socket的输入流和输出流分别封装成对象输入流和对象输出流
					oos = new ObjectOutputStream(socket.getOutputStream());
					ois = new ObjectInputStream(socket.getInputStream());
				} 
				catch (UnknownHostException e1) 
				{
					JOptionPane.showMessageDialog(null, "找不到服务器主机");
					e1.printStackTrace();
					System.exit(0);
				} 
				catch (IOException e1) 
				{
					JOptionPane.showMessageDialog(null,"服务器I/O错误，服务器未启动？");
					e1.printStackTrace();
					System.exit(0);
				}
				// 向服务器发送用户上线信息，将自己的用户名和IP地址发送给服务器
				UserStateMessage userStateMessage = new UserStateMessage(ID, "", userState.onLine);
				try 
				{
					oos.writeObject(userStateMessage);
					oos.flush();
				} 
				catch (IOException e1) {e1.printStackTrace();}
				// 在“消息记录”文本框中用红色添加“XX时间登录成功”的信息
				String msgRecord = dateFormat.format(new Date())+ " 登录成功\r\n";
				return 0;
			}
			else 
			{
				JOptionPane.showMessageDialog(null,"输入密码错误！");
				return 2;
			}
		}
		else 
		{
			JOptionPane.showMessageDialog(null,	"未输入ID");
			return 1;
		}
	}
	
	
	
	
	
	
	
}
