import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class friendList extends JFrame {
	//���ñ�ͷ����ֹ�༭�������
	private DefaultTableModel defaultTableModel=new DefaultTableModel(new String[]{"ID","����","���","״̬"}, 0)
	{public boolean isCellEditable(int row, int column){return false;}};
	
	
	protected DefaultListModel<String> onlinUserDlm = new DefaultListModel<String>();
//	private HashMap<String, chatWindow> windowsMap=new HashMap<String, chatWindow>();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private final int port=9999;
	private final String myID;
//	private String dstID;
	private Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oosServer;
	
	public friendList(String id,Socket fatherSocket,ObjectOutputStream fao,ObjectInputStream fai) {
		myID=id;
		socket=fatherSocket;
		oosServer=fao;
		ois=fai;
		
		new Thread(new ListeningHandler()).start();
		
		Font bigFont=new Font("����",Font.PLAIN,20);
		Font smallFont=new Font("����",Font.PLAIN,16);
		setBounds(0, 0, 250, 700);
		setMinimumSize(new Dimension(270,400));
		
		//����northͼ��
		JLabel headImgJLabel=new JLabel();
		ImageIcon headIcon=new ImageIcon("img\\head\\Ƥ����.jpg");
		headIcon.setImage(headIcon.getImage().getScaledInstance(100, 100,Image.SCALE_DEFAULT ));
		/*���������˼�ǣ��õ���ͼ��� Image��image.getImage()����
		�ڴ˻����ϴ����������Ű汾�����Ű汾�Ŀ�ȣ��߶���JLbleһ�£�getScaledInstance(width, height,Image.SCALE_DEFAULT )��
		����ͼ�������Ϊ�õ������Ű汾��image.setImage��
		*/
		headImgJLabel.setIcon(headIcon);

		JLabel IDJLabel=new JLabel("QQ�ţ�"+id);
		JLabel nameJLabel=new JLabel();
		JLabel statusIMGJLabel=new JLabel();
		JComboBox<String> statusComboBox=new JComboBox<String>();
		nameJLabel.setText("�ǳƣ�"+DerbyDB.getName(id));
//		nameJLabel.setFont(bigFont);
//		IDJLabel.setFont(bigFont);
		statusIMGJLabel.setIcon(new ImageIcon("img\\status\\����.jpg"));
		statusComboBox.setFont(smallFont);
		statusComboBox.addItem("����");
		statusComboBox.addItem("����");
		statusComboBox.addItem("����");
		statusComboBox.addItem("����");
		statusComboBox.addItem("����");
		statusComboBox.setSelectedIndex(0);
		statusComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String statusString=statusComboBox.getSelectedItem().toString();
				if("����".equals(statusString)) 
				{statusIMGJLabel.setIcon(new ImageIcon("img\\status\\����.jpg"));}
				if("����".equals(statusString))
				{statusIMGJLabel.setIcon(new ImageIcon("img\\status\\����.jpg"));}
				if("����".equals(statusString)) 
				{statusIMGJLabel.setIcon(new ImageIcon("img\\status\\����.jpg"));}
				if("����".equals(statusString)) 
				{statusIMGJLabel.setIcon(new ImageIcon("img\\status\\����.jpg"));}
				if("����".equals(statusString)) 
				{statusIMGJLabel.setIcon(new ImageIcon("img\\status\\����.jpg"));}
				// ������������û�������Ϣ�����Լ����û�����IP��ַ���͸�������
				sendStatusMessage(userStatus.getStatus(statusString));
			}
		});
		
		Box statusBox=Box.createHorizontalBox();
		statusBox.add(statusIMGJLabel);
		statusBox.add(statusComboBox);

		Box detailBox=Box.createVerticalBox();
		detailBox.add(IDJLabel);
		detailBox.add(Box.createVerticalStrut(10));
		detailBox.add(nameJLabel);
		detailBox.add(Box.createVerticalStrut(10));
		detailBox.add(statusBox);
		
		Box northBox=Box.createHorizontalBox();
		northBox.add(headImgJLabel);
		northBox.add(detailBox);
		add(northBox,BorderLayout.NORTH);
		
		//���ú����б������
		JTable table=new JTable(defaultTableModel);
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getClickCount()==2)
				{
					int row=table.getSelectedRow();
					String dstID=table.getValueAt(row, 0).toString();
					String dstName=table.getValueAt(row, 1).toString();
					openWindow(dstID,dstName);
				}
			}
		});
		JScrollPane scrollPane= new JScrollPane(table);
		add(scrollPane,BorderLayout.CENTER);
		
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(!statusComboBox.getSelectedItem().equals("����")){
					sendStatusMessage(userStatus.offLine);
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});

		new showFriendList().execute();
		
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void sendStatusMessage(userStatus status)
	{
		UserStateMessage userStateMessage = new UserStateMessage(myID, "",status);
		try 
		{
			oosServer.writeObject(userStateMessage);
			oosServer.flush();
		} 
		catch (IOException e1) {e1.printStackTrace();}
	}
	
 	class showFriendList extends SwingWorker<String, String[]>
	{
		@Override
		protected String doInBackground() throws Exception {
			// TODO Auto-generated method stub
			String id=new String();
			String name=new String();
			String type=new String();
			String status=new String();
			defaultTableModel.addRow(new Object[]{"","�㲥","Ⱥ��",""});			
			ResultSet resultSet=DerbyDB.getAccountTable();
			resultSet.next();
			while(resultSet.next())
			{
				id=resultSet.getString("ID");
				name=resultSet.getString("Name");
				type="����";
				status=userStatus.getName(resultSet.getInt("Status"));
				defaultTableModel.addRow(new Object[]{id,name,type,status});
			}
//			Vector header = new Vector();// ����������ʹ������add()�����������
//			header.add("�ǳ�");
//			header.add("���");
//			header.add("״̬");
//			defaultTableModel.setDataVector(data, header); // ����ģ���е�Ԫ�أ������Զ���ʾ���б���
//			defaultTableModel.addRow(row); // ����ģ���е�Ԫ�أ������Զ���ʾ���б���
//			defaultTableModel.addRow(data);
			return null;
		}
//		@Override
//		protected void process(List<String[]> rows) {
//			// TODO Auto-generated method stub
//			for(String[] row: rows){defaultTableModel.addRow(row);}
//		}
	}//EDT����
	
	boolean openWindow(String winID,String winName)
	{
		if(!chatWindow.isOpenned(winID))
		{new chatWindow(myID,winID,winName,oosServer);}
		return true;
	}
	
	public void changeList(String id,String status)
	{
		String name=new String();
		String type=new String();
		if(status==userStatus.login.getName())status="����";
		for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
			if (defaultTableModel.getValueAt(i, 0).equals(id)) {
				name=defaultTableModel.getValueAt(i, 1).toString();
				type=defaultTableModel.getValueAt(i, 2).toString();
				defaultTableModel.removeRow(i);
				//"ID","����","���","״̬"
				defaultTableModel.addRow(new Object[] { id,name,type,status});
				break;
			}
		}
	}
	
	// ��̨�����߳�
		class ListeningHandler implements Runnable {
			@Override
			public void run() {
				try {
					while (true) {
						Message msg = null;
						synchronized (ois) {msg = (Message) ois.readObject();}
						if (msg instanceof UserStateMessage) // �����û�״̬��Ϣ
						{processUserStateMessage((UserStateMessage) msg);} 
						else if(msg instanceof ChatMessage)//����������Ϣ
						{processChatMessage((ChatMessage) msg);}
						else// ���������Ӧ���û���������Ϣ��ʽ ����Ӧ�÷���Ϣ��ʾ�û����������
						{System.err.println("�û���������Ϣ��ʽ����!");}
					}
				} 
				catch (IOException e)
				{
					if (e.toString().endsWith("Connection reset")) 
					{System.out.println("���������˳�");} 
					else {e.printStackTrace();}
				} 
				catch (ClassNotFoundException e) {e.printStackTrace();} 
				finally {
					if (socket != null) {
						try {socket.close();} 
						catch (IOException e) {e.printStackTrace();}
					}
				}
			}

			// �����û�״̬��Ϣ
			private void processUserStateMessage(UserStateMessage msg) {
				String srcUser = msg.getSrcUser();
				String dstUser = msg.getDstUser();
				if (msg.getUserState()!=userStatus.offLine)//���߻���״̬�ı���Ϣ
				{onlinUserDlm.addElement(srcUser);} 
				else{ // �û�������Ϣ
					if (onlinUserDlm.contains(srcUser)) 
					{onlinUserDlm.removeElement(srcUser);}
				}
				changeList(srcUser, msg.getUserState().getName());
			}
			// ���������ת������������Ϣ
			private void processChatMessage(ChatMessage msg) {
				String srcUser = msg.getSrcUser();//����Ϣ��һ��
				String dstUser = msg.getDstUser();//���ܴ�����Ϣ��һ��
				String msgContent = msg.getMsgContent();
				String srcName=DerbyDB.getName(srcUser);
				if(msg.isPubChatMessage())
				{
					openWindow("", "�㲥");
					chatWindow.getChatWindow("").setmsg(msg);
				}
				else 
				{
					openWindow(srcUser, srcName);
					chatWindow.getChatWindow(srcUser).setmsg(msg);
				}
			}//������Ϣ����
			//���Ӵ���
		}//�����������
	
}//friendList����
