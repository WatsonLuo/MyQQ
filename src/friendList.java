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
import java.sql.ResultSet;
import java.util.Date;
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
	//设置表头并禁止编辑表格内容
	protected DefaultTableModel defaultTableModel=new DefaultTableModel(new String[]{"ID","名称","类别","状态"}, 0)
	{public boolean isCellEditable(int row, int column){return false;}};

	private final DefaultListModel<String> onlinUserDlm = new DefaultListModel<String>();
	
//	public static void main(String[] args) {
//		DerbyDB.prepareDB();
//		DerbyDB.createAccountTable();
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {new friendList("295579587",null,null,null);} 
//				catch (Exception e) {e.printStackTrace();}
//			}
//		});
//	}

	public friendList(String id,Socket fatherSocket,ObjectOutputStream fao,ObjectInputStream fai) {
		Font bigFont=new Font("宋体",Font.PLAIN,20);
		Font smallFont=new Font("宋体",Font.PLAIN,16);
		setBounds(0, 0, 250, 700);
		setMinimumSize(new Dimension(270,400));
		
		//设置north图标
		JLabel headImgJLabel=new JLabel();
		ImageIcon headIcon=new ImageIcon("img\\head\\皮卡丘.jpg");
		headIcon.setImage(headIcon.getImage().getScaledInstance(100, 100,Image.SCALE_DEFAULT ));
		/*下面这句意思是：得到此图标的 Image（image.getImage()）；
		在此基础上创建它的缩放版本，缩放版本的宽度，高度与JLble一致（getScaledInstance(width, height,Image.SCALE_DEFAULT )）
		最后该图像就设置为得到的缩放版本（image.setImage）
		*/
		headImgJLabel.setIcon(headIcon);

		JLabel IDJLabel=new JLabel("QQ号："+id);
		JLabel nameJLabel=new JLabel();
		JLabel statusIMGJLabel=new JLabel();
		JComboBox<String> statusComboBox=new JComboBox<String>();
		nameJLabel.setText("昵称："+DerbyDB.getName(id));
//		nameJLabel.setFont(bigFont);
//		IDJLabel.setFont(bigFont);
		statusIMGJLabel.setIcon(new ImageIcon("img\\status\\在线.jpg"));
		statusComboBox.setFont(smallFont);
		statusComboBox.addItem("在线");
		statusComboBox.addItem("离线");
		statusComboBox.addItem("隐身");
		statusComboBox.addItem("勿扰");
		statusComboBox.addItem("等撩");
		statusComboBox.setSelectedIndex(0);
		statusComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String statusString=statusComboBox.getSelectedItem().toString();
				if("离线".equals(statusString)) 
				{statusIMGJLabel.setIcon(new ImageIcon("img\\status\\离线.jpg"));}
				if("在线".equals(statusString))
				{statusIMGJLabel.setIcon(new ImageIcon("img\\status\\在线.jpg"));}
				if("勿扰".equals(statusString)) 
				{statusIMGJLabel.setIcon(new ImageIcon("img\\status\\勿扰.jpg"));}
				if("等撩".equals(statusString)) 
				{statusIMGJLabel.setIcon(new ImageIcon("img\\status\\等撩.jpg"));}
				if("隐身".equals(statusString)) 
				{statusIMGJLabel.setIcon(new ImageIcon("img\\status\\隐身.jpg"));}
				// 向服务器发送用户上线信息，将自己的用户名和IP地址发送给服务器
				UserStateMessage userStateMessage = new UserStateMessage(id, "", userStatus.getStatus(statusString));
				try 
				{
					fao.writeObject(userStateMessage);
					fao.flush();
				} 
				catch (IOException e1) {e1.printStackTrace();}
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
		
		//设置好友列表滚动条
		JTable table=new JTable(defaultTableModel);
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getClickCount()==2)
				{
					int row=table.getSelectedRow();
					String dstID=table.getValueAt(row, 0).toString();
					String dstName=table.getValueAt(row, 1).toString();
					if(chatWindow.isOpenned(dstID)) {/*确保与一个人的对话框只打开一次*/}
					else {new chatWindow(id,dstID,dstName,fatherSocket,fao,fai);}
				}
			}
		});
		JScrollPane scrollPane= new JScrollPane(table);
		add(scrollPane,BorderLayout.CENTER);
		
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(!statusComboBox.getSelectedItem().equals("离线")){
					UserStateMessage userStateMessage = new UserStateMessage(id, "", userStatus.offLine);
					try 
					{
						fao.writeObject(userStateMessage);
						fao.flush();
					} 
					catch (IOException e1) {e1.printStackTrace();}
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		new showFriendList().execute();
		
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			defaultTableModel.addRow(new Object[]{"","广播","群聊",""});			
			ResultSet resultSet=DerbyDB.getAccountTable();
			resultSet.next();
			while(resultSet.next())
			{
				id=resultSet.getString("ID");
				name=resultSet.getString("Name");
				type="好友";
				status=userStatus.getName(resultSet.getInt("Status"));
				defaultTableModel.addRow(new Object[]{id,name,type,status});
			}
//			Vector header = new Vector();// 列名向量，使用它的add()方法添加列名
//			header.add("昵称");
//			header.add("类别");
//			header.add("状态");
//			defaultTableModel.setDataVector(data, header); // 设置模型中的元素，它会自动显示在列表中
//			defaultTableModel.addRow(row); // 设置模型中的元素，它会自动显示在列表中
//			defaultTableModel.addRow(data);
			return null;
		}
		@Override
		protected void process(List<String[]> rows) {
			// TODO Auto-generated method stub
			for(String[] row: rows){defaultTableModel.addRow(row);}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
//	// 后台监听线程
//		class ListeningHandler implements Runnable {
//			@Override
//			public void run() {
//				try {
//					while (true) {
//						Message msg = null;
//						synchronized (ois) {msg = (Message) ois.readObject();}
//						if (msg instanceof UserStateMessage) {
//							// 处理用户状态消息
//							processUserStateMessage((UserStateMessage) msg);
//						} 
//						else {
//							// 这种情况对应着用户发来的消息格式 错误，应该发消息提示用户，这里从略
//							System.err.println("用户发来的消息格式错误!");
//						}
//					}
//				} catch (IOException e) {
//					if (e.toString().endsWith("Connection reset")) 
//					{System.out.println("服务器端退出");} 
//					else {e.printStackTrace();}
//				} 
//				catch (ClassNotFoundException e) {e.printStackTrace();} 
//				finally {
//					if (socket != null) {
//						try {socket.close();} 
//						catch (IOException e) {e.printStackTrace();}
//					}
//				}
//			}
//
//			// 处理用户状态消息
//			private void processUserStateMessage(UserStateMessage msg) {
//				String srcUser = msg.getSrcUser();
//				String dstUser = msg.getDstUser();
//				if (msg.getUserState()!=userStatus.offLine) {
//					if (msg.isPubUserStateMessage())// 新用户上线消息
//					{onlinUserDlm.addElement(srcUser);}
//					if (dstUser.equals(srcID))// 用户在线消息
//					{onlinUserDlm.addElement(srcUser);}
//				} 
//				else if (msg.getUserState()==userStatus.offLine) { // 用户下线消息
//					if (onlinUserDlm.contains(srcUser)) 
//					{onlinUserDlm.removeElement(srcUser);}
//				}
//			}
//
//			
//		}
//	}

	
	
	
	
}
