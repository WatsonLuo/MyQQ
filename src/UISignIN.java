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

public class UISignIN extends JFrame {

	private int port=9999;
	private String IPString="10.5.100.200";
	private Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new UISignIN();
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		Box userNameBox=Box.createHorizontalBox();
		userNameBox.add(IDJLabel);
		userNameBox.add(IDField);
		JLabel passwordJLabel=new JLabel("���룺");
		JPasswordField passwordField=new JPasswordField();
		passwordJLabel.setFont(font);
		passwordField.setFont(font);

		Box passwordBox=Box.createHorizontalBox();
		passwordBox.add(passwordJLabel);
		passwordBox.add(Box.createHorizontalStrut(25));
		passwordBox.add(passwordField);
		
		
		//��ס�ң��Զ���¼��˼�������һ�����
		JCheckBox rememberMeBox=new JCheckBox("��ס����",false);
		JCheckBox autoBox=new JCheckBox("�Զ���¼",false);
		Box functionBox=Box.createHorizontalBox();
		functionBox.add(rememberMeBox);
		functionBox.add(autoBox);

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
		centerBox.add(userNameBox);
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
				if(SignIN(IDField.getText(), String.valueOf(passwordField.getPassword()))==true)
				{
					signINJFrame.dispose();
				}
				
			}
		});
		registerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new UIRegister(signINJFrame);
			}
		});
		
		pack();
		setVisible(true);
		setLocationRelativeTo(null);//������У������Ǹ����壬null����û�и����壬����������
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public boolean SignIN(String ID,String passWord) {
		if (ID.length() > 0) {
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
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// �ڡ���Ϣ��¼���ı������ú�ɫ��ӡ�XXʱ���¼�ɹ�������Ϣ
			String msgRecord = /*dateFormat.format(new Date())+ */" ��¼�ɹ�\r\n";
//			addMsgRecord(msgRecord, Color.red, 12, false, false);
//			// ��������������̨�����̡߳�,�����������������������Ϣ
//			new Thread(new ListeningHandler()).start();
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
}
