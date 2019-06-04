import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Server extends JFrame {
	private ServerSocket serverSocket;
	private final int port = 9999;
	// 保存在线用户的用户名与Socket信息
	private final UserManager userManager = new UserManager();
	// “在线用户列表ListModel”,用于维护“在线用户列表”中显示的内容
	final DefaultTableModel onlineUsersDtm = new DefaultTableModel();
	// 用于控制时间信息显示格式
	// private final SimpleDateFormat dateFormat = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	private final JPanel contentPane;
	private final JTable tableOnlineUsers;
	private final JTextPane textPaneMsgRecord;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		 
		DerbyDB.prepareDB();
		DerbyDB.createAccountTable();
//		System.out.println("修改前");
		DerbyDB.showAccountTable();
//		DerbyDB.deleteAccountTable("295579588");
//		DerbyDB.insertAccountTable("295579587", "luojingyi", "100", 10);
//		System.out.println("修改后");
//		DerbyDB.showAccountTable();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					/*你应该这样做：为JFrame2创建一个单独的类，使用单例模式（不懂的去查设计模式的书）
					 * 在按钮事件中只处理一个实例（如：JFrame2.getInstance().setVisible(true) ）。
					 * 写在这里，但感觉并不适合我这个情况*/
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Server() {
		setTitle("\u670D\u52A1\u5668");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 561, 403);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JSplitPane splitPaneNorth = new JSplitPane();
		splitPaneNorth.setResizeWeight(0.5);
		contentPane.add(splitPaneNorth, BorderLayout.CENTER);

		JScrollPane scrollPaneMsgRecord = new JScrollPane();
		scrollPaneMsgRecord.setPreferredSize(new Dimension(100, 300));
		scrollPaneMsgRecord.setViewportBorder(new TitledBorder(null,
				"\u6D88\u606F\u8BB0\u5F55", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		splitPaneNorth.setLeftComponent(scrollPaneMsgRecord);

		textPaneMsgRecord = new JTextPane();
		textPaneMsgRecord.setPreferredSize(new Dimension(100, 100));
		scrollPaneMsgRecord.setViewportView(textPaneMsgRecord);

		JScrollPane scrollPaneOnlineUsers = new JScrollPane();
		scrollPaneOnlineUsers.setPreferredSize(new Dimension(100, 300));
		splitPaneNorth.setRightComponent(scrollPaneOnlineUsers);

		onlineUsersDtm.addColumn("ID");
		onlineUsersDtm.addColumn("IP");
		onlineUsersDtm.addColumn("端口");
		onlineUsersDtm.addColumn("登录时间");
		onlineUsersDtm.addColumn("状态");
		tableOnlineUsers = new JTable(onlineUsersDtm);
		tableOnlineUsers.setPreferredSize(new Dimension(120, 270));
		tableOnlineUsers.setFillsViewportHeight(true); // 让JTable充满它的容器
		scrollPaneOnlineUsers.setViewportView(tableOnlineUsers);

		JPanel panelSouth = new JPanel();
		contentPane.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		final JButton btnStart = new JButton("启动服务器");
		// "启动"按钮
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// 创建ServerSocket打开端口8888监听客户端连接
					serverSocket = new ServerSocket(port);
					// 在“消息记录”文本框中用红色显示“服务器启动成功X”和启动时间信息
					String msgRecord = dateFormat.format(new Date())
							+ " 服务器启动成功！" + "\r\n";
					addMsgRecord(msgRecord, Color.red, 12, false, false);
					// 创建并启动“接受用户连接线程”，接受并处理客户端连接请求
					new Thread() {
						@Override
						public void run() {
							while (true) {
								try {
									// 调用serverSocket.accept()方法接受用户连接请求
									Socket socket = serverSocket.accept();
									// 为新来的用户创建并启动“用户服务线程”
									// 并把serverSocket.accept()方法返回的socket对象交给“用户服务线程”来处理
									UserHandler userHandler = new UserHandler(socket);
									new Thread(userHandler).start();
								} 
								catch (IOException e) {e.printStackTrace();}
							}
						};
					}.start();
					btnStart.setEnabled(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panelSouth.add(btnStart);
	}

	// 向消息记录文本框中添加一条消息记录
	private void addMsgRecord(final String msgRecord, Color msgColor,
			int fontSize, boolean isItalic, boolean isUnderline) {
		final SimpleAttributeSet attrset = new SimpleAttributeSet();
		StyleConstants.setForeground(attrset, msgColor);
		StyleConstants.setFontSize(attrset, fontSize);
		StyleConstants.setUnderline(attrset, isUnderline);
		StyleConstants.setItalic(attrset, isItalic);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Document docs = textPaneMsgRecord.getDocument();
				try {
					docs.insertString(docs.getLength(), msgRecord, attrset);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
	}

	//对客户端发来的消息进行处理
	class UserHandler implements Runnable {
		private final Socket currentUserSocket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		public UserHandler(Socket currentUserSocket) {
			this.currentUserSocket = currentUserSocket;
			try {
				ois = new ObjectInputStream(currentUserSocket.getInputStream());
				oos = new ObjectOutputStream(currentUserSocket.getOutputStream());
			} 
			catch (IOException e) {e.printStackTrace();}
		}

		@Override
		public void run() {
			try {
				while (true) {
					Message msg = (Message) ois.readObject();
					System.out.println("服务器收到来自"+msg.getSrcUser()+"发送到"+msg.getDstUser()+"的信息");
					if (msg instanceof UserStateMessage) // 处理用户状态消息
					{processUserStateMessage((UserStateMessage) msg);} 
					else if (msg instanceof ChatMessage) // 处理聊天消息
					{processChatMessage((ChatMessage) msg);} 
					else if(msg instanceof FileSengMessage)//处理发送文件消息
					{processFileSendMessage((FileSengMessage)msg);}
					else if(msg instanceof FileResponseMessage)//处理发送文件消息
					{processFileResponseMessage((FileResponseMessage)msg);}
					else // 这种情况对应着用户发来的消息格式 错误，应该发消息提示用户，这里从略
					{System.err.println("用户发来的消息格式错误!");}
				}
			} 
			catch (IOException e) {
				// 如果用户未发送下线消息就直接关闭了客户端，应该在这里补充代码，删除用户在线信息
				if (e.toString().endsWith("Connection reset")) 
				{System.out.println("客户端退出");} 
				else {e.printStackTrace();}
			} 
			catch (ClassNotFoundException e) {e.printStackTrace();} 
			finally {
				if (currentUserSocket != null) {
					try {currentUserSocket.close();} 
					catch (IOException e) {e.printStackTrace();}
				}
			}
		}

		// 向其它用户转发消息
		private void transferMsgToOtherUsers(Message msg) {
			String[] users = userManager.getAllUsers();
			for (String user : users) {
				//当前在线用户非消息发送方
				if (userManager.getUserSocket(user) != currentUserSocket) {
					try {
						ObjectOutputStream o = userManager.getUserOos(user);
						synchronized (o) {
							o.writeObject(msg);
							o.flush();
						}
					} 
					catch (IOException e) {e.printStackTrace();}
				}
			}
		}
		private void transferMsgToTheUsers(Message msg,String dstID) {
			try {
				ObjectOutputStream o = userManager.getUserOos(dstID);
				if(o==null)System.out.println("用户："+dstID+"未上线！存储为离线消息！");
				else 
				{
					synchronized (o) {
						o.writeObject(msg);
						o.flush();
					}
				} 
			}	
			catch (IOException e) {e.printStackTrace();}
		}

		
		private void processUserStateMessage(UserStateMessage msg) {
			String srcUser = msg.getSrcUser();
			UserStateMessage userStateMessage=null;
			if (msg.getUserState()!=userStatus.offLine) { // 用户上线消息
				if (userManager.hasUser(srcUser)) {
					if(msg.getUserState()==userStatus.login)
					{
						// 这种情况对应着用户重复登录
						try 
						{
							userStateMessage = new UserStateMessage(srcUser,srcUser,userStatus.offLine);
							synchronized (userStateMessage) {
								oos.writeObject(userStateMessage);
								oos.flush();
							}
						}
						catch (IOException e) {e.printStackTrace();}
						System.err.println("用户重复登录");
						return;
					}
					else{
						//更改用户状态
						transferMsgToOtherUsers(msg);
						transferMsgToTheUsers(msg, srcUser);
						// 将用户信息加入到“在线用户”列表中
						for (int i = 0; i < onlineUsersDtm.getRowCount(); i++) {
							if (onlineUsersDtm.getValueAt(i, 0).equals(srcUser)) {
								onlineUsersDtm.removeRow(i);
								onlineUsersDtm.addRow(new Object[] { srcUser,
										currentUserSocket.getInetAddress().getHostAddress(),
										currentUserSocket.getPort(),
										dateFormat.format(new Date()),
										msg.getUserState().getName()});
								break;
							}
						}
						userManager.addUser(srcUser, currentUserSocket, msg.getUserState(), oos, ois);
						// 用绿色文字将用户名和用户上线时间添加到“消息记录”文本框中
						String ip = currentUserSocket.getInetAddress().getHostAddress();
						final String msgRecord = dateFormat.format(new Date()) + " "
								+ srcUser + "(" + ip + ")" + "更改状态为："+msg.getUserState().getName()+"!\r\n";
						addMsgRecord(msgRecord, Color.green, 12, false, false);
						DerbyDB.resetStatus(srcUser, msg.getUserState().getIndex());
					}
				}
				else {
					//新用户登录
					try 
					{
						userStateMessage = new UserStateMessage(srcUser,srcUser,userStatus.onLine);
						synchronized (userStateMessage) {
							oos.writeObject(userStateMessage);
							oos.flush();
						}
					}
					catch (IOException e) {e.printStackTrace();}

					// 向新上线的用户转发当前在线用户列表
					String[] users = userManager.getAllUsers();
					try {
						for (String user : users) {
							userStateMessage = new UserStateMessage(user, srcUser, msg.getUserState());
							synchronized (userStateMessage) {
								oos.writeObject(userStateMessage);
								oos.flush();
							}
						}
					} 
					catch (IOException e) {e.printStackTrace();}
					// 向所有其它在线用户转发用户上线消息
					transferMsgToOtherUsers(msg);
					transferMsgToTheUsers(msg, srcUser);
					// 将用户信息加入到“在线用户”列表中
					onlineUsersDtm.addRow(new Object[] { srcUser,
							currentUserSocket.getInetAddress().getHostAddress(),
							currentUserSocket.getPort(),
							dateFormat.format(new Date()),
							(msg.getUserState()==userStatus.login?"在线":msg.getUserState().getName())});
					userManager.addUser(srcUser, currentUserSocket, msg.getUserState(), oos, ois);
					// 用绿色文字将用户名和用户上线时间添加到“消息记录”文本框中
					String ip = currentUserSocket.getInetAddress().getHostAddress();
					final String msgRecord = dateFormat.format(new Date()) + " "
							+ srcUser + "(" + ip + ")" + "上线了!\r\n";
					addMsgRecord(msgRecord, Color.green, 12, false, false);
					userStatus s=msg.getUserState()==userStatus.login?userStatus.onLine:msg.getUserState();
					DerbyDB.resetStatus(srcUser, s.getIndex());
				}
			}
			else { // 用户下线消息
				if (!userManager.hasUser(srcUser)) {
					// 这种情况对应着用户未发送上线消息就直接发送了下线消息，应该发消息提示客户端，这里从略
					System.err.println("用户未发送登录消息就发送了下线消息");
					return;
				}
				// 将用户下线消息转发给所有其它在线用户
				transferMsgToOtherUsers(msg);
				transferMsgToTheUsers(msg, srcUser);
				// 用绿色文字将用户名和用户下线时间添加到“消息记录”文本框中
				String ip = userManager.getUserSocket(srcUser).getInetAddress()
						.getHostAddress();
				final String msgRecord = dateFormat.format(new Date()) + " "
						+ srcUser + "(" + ip + ")" + "下线了!\r\n";
				addMsgRecord(msgRecord, Color.green, 12, false, false);
				// 在“在线用户列表”中删除下线用户
				userManager.removeUser(srcUser);
				for (int i = 0; i < onlineUsersDtm.getRowCount(); i++) {
					if (onlineUsersDtm.getValueAt(i, 0).equals(srcUser)) {
						onlineUsersDtm.removeRow(i);
					}
				}
				DerbyDB.resetStatus(srcUser, msg.getUserState().getIndex());
			}
		}

		// 处理用户发来的聊天消息
		private void processChatMessage(ChatMessage msg) {
			String srcUser = msg.getSrcUser();
			String dstUser = msg.getDstUser();
			String msgContent = msg.getMsgContent();
			if (userManager.hasUser(srcUser)) {
				// 用黑色文字将收到消息的时间、发送消息的用户名和消息内容添加到“消息记录”文本框中
				if (msg.isPubChatMessage()) {
					// 将公聊消息转发给所有其它在线用户
					final String msgRecord = dateFormat.format(new Date()) + " "
							+ srcUser + "说: " + msgContent + "\r\n";
					addMsgRecord(msgRecord, Color.black, 12, false, false);
					transferMsgToOtherUsers(msg);
				} else {
					// 将私聊消息转发给目标用户
					final String msgRecord = dateFormat.format(new Date()) + " "
							+ srcUser + "对"+dstUser+"说: " + msgContent + "\r\n";
					addMsgRecord(msgRecord, Color.pink, 12, false, false);
					transferMsgToTheUsers(msg,dstUser);
				}
			} else {
				// 这种情况对应着用户未发送上线消息就直接发送了聊天消息，应该发消息提示客户端，这里从略
				System.err.println("用启未发送上线消息就直接发送了聊天消息");
				return;
			}
		}
	
		//处理用户的发送文件请求
		private void processFileSendMessage(FileSengMessage msg) {
			String srcUser = msg.getSrcUser();
			String dstUser = msg.getDstUser();
			File msgFile = msg.getFile();
			if (userManager.hasUser(srcUser)) {
				// 用黑色文字将收到消息的时间、发送消息的用户名和消息内容添加到“消息记录”文本框中
				if (dstUser.equals("")) {
					// 将公聊消息转发给所有其它在线用户
					final String msgRecord = dateFormat.format(new Date()) + " 公聊文件发送请求：来自"
							+ srcUser + "文件："+msgFile+"发送请求\r\n";
					addMsgRecord(msgRecord, Color.orange, 12, false, false);
					transferMsgToOtherUsers(msg);
				} else {
					// 将私聊消息转发给目标用户
					final String msgRecord = dateFormat.format(new Date()) + " 私聊文件发送请求："
							+ srcUser + "-->"+dstUser+" 文件:"+msgFile+"\r\n";
					addMsgRecord(msgRecord, Color.orange, 12, false, false);
					transferMsgToTheUsers(msg,dstUser);
				}
			} 
			else {
				// 这种情况对应着用户未发送上线消息就直接发送了聊天消息，应该发消息提示客户端，这里从略
				System.err.println("用启未发送上线消息就直接发送了聊天消息");
				return;
			}
		}
	
		private void processFileResponseMessage(FileResponseMessage msg)
		{
			String srcUser = msg.getSrcUser();
			String dstUser = msg.getDstUser();
			File msgFile = msg.getFile();
			if (userManager.hasUser(srcUser)) {
				// 用黑色文字将收到消息的时间、发送消息的用户名和消息内容添加到“消息记录”文本框中
				if (dstUser.equals("")) {
					// 将公聊消息转发给所有其它在线用户
//					final String msgRecord = dateFormat.format(new Date()) + " 公聊文件发送请求：来自"
//							+ srcUser + "文件："+msgFile+"发送请求\r\n";
//					addMsgRecord(msgRecord, Color.orange, 12, false, false);
//					transferMsgToOtherUsers(msg);
					System.out.println("暂不支持公聊文件上传,该response消息在服务器端呗忽略");
				} else {
					// 将私聊消息转发给目标用户
					final String msgRecord = dateFormat.format(new Date()) + " "+srcUser+"接受了来自"
							+dstUser+" 的文件:"+msgFile+"\r\n";
					addMsgRecord(msgRecord, Color.orange, 12, false, false);
					transferMsgToTheUsers(msg,dstUser);
				}
			}
		}
	
	}

}

// 管理在线用户信息,全靠内部Map类型的onLineUsers
class UserManager {
	private final Map<String, User> onLineUsers;

	public UserManager() {onLineUsers = new HashMap<String, User>();}
	
	// 判断某用户是否在线
	public boolean hasUser(String userName) {return onLineUsers.containsKey(userName);}

	// 判断在线用户列表是否空
	public boolean isEmpty() {return onLineUsers.isEmpty();}

	// 获取在线用户的Socket的的输出流封装成的对象输出流
	public ObjectOutputStream getUserOos(String userName) {
		if (hasUser(userName)) 
		{return onLineUsers.get(userName).getOos();}
		return null;
	}

	// 获取在线用户的Socket的的输入流封装成的对象输入流
	public ObjectInputStream getUserOis(String userName) {
		if (hasUser(userName)) 
		{return onLineUsers.get(userName).getOis();}
		return null;
	}

	// 获取在线用户的Socket
	public Socket getUserSocket(String userName) {
		if (hasUser(userName)) 
		{return onLineUsers.get(userName).getSocket();}
		return null;
	}

	// 添加在线用户
	public boolean addUser(String userName, Socket userSocket,userStatus userStatus) {
		if ((userName != null) && (userSocket != null)) {
			onLineUsers.put(userName, new User(userSocket,userStatus));
			return true;
		}
		return false;
	}

	// 添加在线用户
	public boolean addUser(String userName, Socket userSocket,userStatus userStatus,
			ObjectOutputStream oos, ObjectInputStream ios) {
		if ((userName != null) && (userSocket != null) && (oos != null)
				&& (ios != null)) {
			onLineUsers.put(userName, new User(userSocket, userStatus,oos, ios));
			return true;
		}
		return false;
	}

	public boolean resetStatus(String userName,userStatus userStatus) {
		if (hasUser(userName)) {
			User user=new User(onLineUsers.get(userName), userStatus);
			onLineUsers.put(userName, user);
			return true;
		}
		return false;
	}
	
	// 删除在线用户
	public boolean removeUser(String userName) {
		if (hasUser(userName)) {
			onLineUsers.remove(userName);
			return true;
		}
		return false;
	}

	// 获取所有在线用户名
	public String[] getAllUsers() {
		String[] users = new String[onLineUsers.size()];
		int i = 0;
		for (Map.Entry<String, User> entry : onLineUsers.entrySet()) 
		{users[i++] = entry.getKey();}
		return users;
	}

	// 获取在线用户个数
	public int getOnlineUserCount() {return onLineUsers.size();}
}

class User {
	private final Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private final Date logonTime;
	private final userStatus status;

	public Socket getSocket() {return socket;}
	public ObjectOutputStream getOos() {return oos;}
	public ObjectInputStream getOis() {return ois;}
	public Date getLogonTime() {return logonTime;}
	public userStatus getStatus() {return status;}
	
	public User(User user,userStatus status) {
		socket=user.getSocket();
		oos=user.getOos();
		ois=user.getOis();
		logonTime=user.getLogonTime();
		this.status=status;
	}
	
	public User(Socket socket,userStatus status) {
		this.socket = socket;
		this.status=status;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logonTime = new Date();
	}

	public User(Socket socket, userStatus status,ObjectOutputStream oos, ObjectInputStream ois) {
		this.socket = socket;
		this.status=status;
		this.oos = oos;
		this.ois = ois;
		logonTime = new Date();
	}

	public User(Socket socket,userStatus status, ObjectOutputStream oos, ObjectInputStream ois,Date logonTime) {
		this.socket = socket;
		this.status=status;
		this.oos = oos;
		this.ois = ois;
		this.logonTime = logonTime;
	}

}
