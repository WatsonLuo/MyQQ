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
		
		//ʹ�Ĵ��ڲ���ʹ����������С

		rememberME=DerbyDB.getAccount0(account);
		
		
		UISignIN signINJFrame=this;
		setResizable(false);
		setTitle("�޾���160341218");
		Font font=new Font("����",Font.PLAIN,24);
		
		//����northͼ��
		JLabel topImgJLabel=new JLabel();
		topImgJLabel.setIcon(new ImageIcon("img\\QQ��¼ҳͷ.png"));
		Box northBox=Box.createHorizontalBox();
		northBox.add(topImgJLabel);
		getContentPane().add(northBox,BorderLayout.NORTH);
		
		
		//����westͼ��
		JLabel leftImgJLabel=new JLabel();
		leftImgJLabel.setIcon(new ImageIcon("img\\QQ.jpg"));
		JPanel westJPanel=new JPanel();
		westJPanel.add(leftImgJLabel);
		getContentPane().add(westJPanel,BorderLayout.WEST);
		
		//����center����
		//����ؼ�
		JLabel IDJLabel=new JLabel("QQ�ţ�");
		JTextField IDField=new JTextField();
		IDJLabel.setFont(font);
		IDField.setFont(font);	
		Box IDBox=Box.createHorizontalBox();
		IDBox.add(Box.createHorizontalStrut(10));
		IDBox.add(IDJLabel);
		IDBox.add(IDField);
		JLabel passwordJLabel=new JLabel("���룺");
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
		
		
		//��ס�ң��Զ���¼��˼�������һ�����
		JCheckBox rememberMeBox=new JCheckBox("��ס����",false);
		JCheckBox autoBox=new JCheckBox("�Զ���¼",false);
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
		
		

		JButton fetchButton=new JButton("�һ�����");
		fetchButton.setContentAreaFilled(false);
		fetchButton.setBorderPainted(false);
		fetchButton.setFocusPainted(false);
		fetchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		JButton showPasswordButton=new JButton("��ʾ����");
		showPasswordButton.setContentAreaFilled(false);
		showPasswordButton.setBorderPainted(false);
		showPasswordButton.setFocusPainted(false);
		char c=passwordField.getEchoChar();
		showPasswordButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(showPasswordButton.getText()=="��ʾ����")
				{
					passwordField.setEchoChar('\0');
					showPasswordButton.setText("��������");
				}
				else 
				{
					passwordField.setEchoChar(c);
					showPasswordButton.setText("��ʾ����");
				}
			}
		});
		Box eastBox=Box.createVerticalBox();
		eastBox.add(Box.createVerticalStrut(30));
		eastBox.add(fetchButton);
		eastBox.add(Box.createVerticalStrut(35));
		eastBox.add(showPasswordButton);
		getContentPane().add(eastBox,BorderLayout.EAST);
		
		//����south��ť
		JPanel southJPanel=new JPanel();
		JButton signINButton=new JButton("��¼");
		JButton registerButton=new JButton("ע��");
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
				else if(result==1)//δ����ID
				{
					passwordField.setText("");
				}
				else if(result==2)//�޴�ID
				{
					IDField.setText("");
					passwordField.setText("");
				}
				else if(result==3)//�������
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
		setLocationRelativeTo(null);//������У������Ǹ����壬null����û�и����壬����������
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public int SignIN(String ID,String password) {
		if (ID.length() > 0) {
			String ensureString=DerbyDB.getPassword(ID);
			if(ensureString==null)
			{
				JOptionPane.showMessageDialog(null, "�����ڴ�ID");
				return 3;
			}
			else if(ensureString.equals(password))
			{
				// ��������˽���Socket���ӣ�����׳��쳣���򵯳��Ի���֪ͨ�û������˳�
				try 
				{
					socket = new Socket("localhost", port);
					// ��socket����������������ֱ��װ�ɶ����������Ͷ��������
					oos = new ObjectOutputStream(socket.getOutputStream());
					ois = new ObjectInputStream(socket.getInputStream());
				} 
				catch (UnknownHostException e1) 
				{
					JOptionPane.showMessageDialog(null, "�Ҳ�������������");
					e1.printStackTrace();
					System.exit(0);
				} 
				catch (IOException e1) 
				{
					JOptionPane.showMessageDialog(null,"������I/O���󣬷�����δ������");
					e1.printStackTrace();
					System.exit(0);
				}
				// ������������û�������Ϣ�����Լ����û�����IP��ַ���͸�������
				UserStateMessage userStateMessage = new UserStateMessage(ID, "", userState.onLine);
				try 
				{
					oos.writeObject(userStateMessage);
					oos.flush();
				} 
				catch (IOException e1) {e1.printStackTrace();}
				// �ڡ���Ϣ��¼���ı������ú�ɫ��ӡ�XXʱ���¼�ɹ�������Ϣ
				String msgRecord = dateFormat.format(new Date())+ " ��¼�ɹ�\r\n";
				return 0;
			}
			else 
			{
				JOptionPane.showMessageDialog(null,"�����������");
				return 2;
			}
		}
		else 
		{
			JOptionPane.showMessageDialog(null,	"δ����ID");
			return 1;
		}
	}
	
	
	
	
	
	
	
}
