import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.module.ResolutionException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.CellRendererPane;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class chatWindow extends JFrame {
	ObjectOutputStream oos;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private final DefaultListModel<String> onlinUserDlm = new DefaultListModel<String>();
	private final String srcID;
	private final String dstID;
	private Message msg;
	private final static HashMap<String, chatWindow> openedDstID=new HashMap<String, chatWindow>();
	private final chatWindow thisUI;
	
	private final JPanel contentPane;
	private final JTextField textFieldMsgToSend;
	private final JTextPane textPaneMsgRecord;
	private final JButton btnSendMsg;
	private final JButton btnFile;
//	private final JPopupMenu fileMenu;

	private final Box filePanel=Box.createHorizontalBox();
	private final JProgressBar progressBar=new JProgressBar();
	private final JLabel progressJLabel=new JLabel();
	
	
	public static boolean isOpenned(String dstID) {return openedDstID.containsKey(dstID);}
	public static chatWindow getChatWindow(String dstID) {return openedDstID.get(dstID);}
	
	
	public chatWindow(String srcID,String dstID,String dstName,ObjectOutputStream oosfa) {
		setTitle("对话框");
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		oos = oosfa;
		this.srcID=srcID;
		this.dstID=dstID;
		thisUI=this;
		openedDstID.put(dstID, this);
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e)
			{openedDstID.remove(dstID);}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});

	
		JPanel panelNorth = new JPanel();
		panelNorth.setBorder(new EmptyBorder(0, 0, 5, 0));
		contentPane.add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.X_AXIS));

		panelNorth.add(Box.createHorizontalStrut(20));
		panelNorth.add(Box.createHorizontalStrut(20));

		JSplitPane splitPaneCenter = new JSplitPane();
		splitPaneCenter.setResizeWeight(1.0);
		contentPane.add(splitPaneCenter, BorderLayout.CENTER);

		JScrollPane scrollPaneMsgRecord = new JScrollPane();
		scrollPaneMsgRecord.setViewportBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "\u6D88\u606F\u8BB0\u5F55",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPaneCenter.setLeftComponent(scrollPaneMsgRecord);

		Box detail=Box.createVerticalBox();
		Box nameBox=Box.createHorizontalBox();
		JLabel headImgJLabel=new JLabel();
		ImageIcon headIcon=new ImageIcon("img\\head\\皮卡丘.jpg");
		headIcon.setImage(headIcon.getImage().getScaledInstance(120, 120,Image.SCALE_DEFAULT ));
		JLabel nameJLabel=new JLabel("昵称："+dstName);
		headImgJLabel.setIcon(headIcon);
		
		nameBox.add(Box.createHorizontalGlue());
		nameBox.add(nameJLabel);
		nameBox.add(Box.createHorizontalGlue());
		
		detail.add(headImgJLabel);
		detail.add(nameBox);
		detail.add(new JLabel("ID："+dstID));
		detail.add(Box.createVerticalGlue());
		detail.add(new JLabel("我方："+srcID));
		
		splitPaneCenter.setRightComponent(detail);
		
		textPaneMsgRecord = new JTextPane();
		scrollPaneMsgRecord.setViewportView(textPaneMsgRecord);

		Box panelSend = Box.createHorizontalBox();
		Box panelSouth=Box.createVerticalBox();

		contentPane.add(panelSouth, BorderLayout.SOUTH);

		panelSend.setBorder(new EmptyBorder(5, 0, 0, 0));

		textFieldMsgToSend = new JTextField();
		panelSend.add(textFieldMsgToSend);
		textFieldMsgToSend.setColumns(10);

		panelSend.add(Box.createHorizontalStrut(20));
		
		btnSendMsg = new JButton("发送消息"); // “发送消息”按钮
		btnSendMsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msgContent = textFieldMsgToSend.getText();
				if (msgContent.length() > 0) 
				{
					ChatMessage chatMessage;
					String msgRecord=dateFormat.format(new Date());
					if(dstName.equals("广播"))
					{
						chatMessage = new ChatMessage(srcID, "", msgContent);
						msgRecord +="向大家说:"+ msgContent + "\r\n";
					}
					else 
					{
						chatMessage = new ChatMessage(srcID, dstID, msgContent);
						msgRecord += "对"+dstID+"说:"+ msgContent + "\r\n";
					}
					try {
						synchronized (oos) 
						{
							oos.writeObject(chatMessage);
							oos.flush();
						}
					} 
					catch (IOException e1) {e1.printStackTrace();}
					addMsgRecord(msgRecord, Color.blue, 12, false, false);
					textFieldMsgToSend.setText("");
				}
			}
		});
		btnFile=new JButton("文件传输");

		btnFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				JFileChooser fileChooser=new JFileChooser("E:");
				fileChooser.setDialogTitle("选择文件");
				fileChooser.setApproveButtonText("选择");
				int choice=fileChooser.showOpenDialog(null);
				if(choice==JFileChooser.APPROVE_OPTION){
					File file=fileChooser.getSelectedFile();
					FileSengMessage fileSengMessage=new FileSengMessage(srcID, dstID, file);
					try {
						synchronized (oos) {
							oos.writeObject(fileSengMessage);
							oos.flush();
						}
					} 
					catch (IOException e1) {e1.printStackTrace();}
				}
			}
		});
		panelSend.add(btnSendMsg);
		panelSend.add(Box.createHorizontalStrut(20));
		panelSend.add(btnFile);

		progressJLabel.setText("aaa");
		filePanel.add(progressBar);
		filePanel.add(Box.createHorizontalStrut(10));
		filePanel.add(progressJLabel);
		filePanel.add(Box.createHorizontalGlue());
		
		filePanel.setVisible(false);
		panelSouth.add(panelSend);
		panelSouth.add(filePanel);
		
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//这样才能不影响父窗体
	}//frame构建结束

	//从FriendList给chatWindow传送数据的重要通道
	public void setmsg(Message msgfa)
	{
		msg=msgfa;
		if(msg instanceof UserStateMessage) {System.out.println("这里是window，并不处理状态信息");}
		else if (msg instanceof ChatMessage) {processChatMessage((ChatMessage) msg);} //处理聊天信息
		else if(msg instanceof FileSengMessage) {new Thread(new FileSendMessageHandler((FileSengMessage) msg)).start();}
		else if(msg instanceof FileResponseMessage){new Thread(new FileResponseMessageHandler((FileResponseMessage) msg)).start();}
		else {System.err.println("用户发来的消息格式错误!");}//消息格式错误
	}
	//像对话框中添加信息
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
				try {docs.insertString(docs.getLength(), msgRecord, attrset);} 
				catch (BadLocationException e) {e.printStackTrace();}
			}
		});
	}

	// 处理服务器转发来的聊天消息
	private void processChatMessage(ChatMessage msg) {
		String srcUser = msg.getSrcUser();
		String dstUser = msg.getDstUser();
		String msgContent = msg.getMsgContent();
		if (msg.isPubChatMessage() || dstUser.equals(srcID)) {
			// 用黑色文字将收到消息的时间、发送消息的用户名和消息内容添加到“消息记录”文本框中
			final String msgRecord = dateFormat.format(new Date())+" "+srcUser +"说: "+msgContent+"\r\n";
			addMsgRecord(msgRecord, Color.black, 12, false, false);
		}
	}

	/**
	 * 客户端后台监听线程 “后台监听线程”收到了服务器转发来的用户上线消息 用绿色文字将用户名和用户上线时间添加到“消息记录”文本框中
	 * 在“在线用户”列表中增加新上线的用户名 “后台监听线程”收到了服务器转发来的在线用户消息 在“在线用户”列表中增加在线用户名
	 * “后台监听线程”收到了服务器转发来的广播消息 用黑色文字将收到消息的时间、发送消息的用户名和消息内容添加到“消息记录”文本框中
	 * “后台监听线程”收到了服务器转发来的用户下线消息 在“在线用户”列表中删除下线的用户名
	 * 
	 * @author wmren
	 *
	 */
	// 计算文件传输速率
	private String getSpeed(long readByte, long startTime, long endTime) {
		long speed;
		if (endTime - startTime != 0) {
			speed = (readByte / (endTime - startTime)) * 1000;
			if (speed > 1024 * 1024) {
				return forMatString(speed / (1024 * 1024 + 0.1) + "") + " m/s";
			} else if (speed > 1024) {
				return forMatString(speed / (1024 + 0.1) + "") + " k/s";
			} else {
				return speed + " b/s";
			}
		} else {
			return "0 b/s";
		}
	}
	// 格式化字符串
	private String forMatString(String str) {
		String values;
		int index = str.indexOf(".");
		values = str.substring(0, index + 3);
		return values;
	}

	class FileSendMessageHandler implements Runnable {
		private FileSengMessage fileSengMessage;

		public FileSendMessageHandler(FileSengMessage fileSengMessage) {this.fileSengMessage = fileSengMessage;}

		public void run() {
			File file = fileSengMessage.getFile();
			String srcUser = fileSengMessage.getSrcUser();// 请求发送文件的发件人
			Socket filesocket = null;
			if (JOptionPane.showConfirmDialog(null
					,"是否同意接收"+srcUser+"发送来的文件：\r\n" + file.getName() + "\r\n文件大小：" + file.length()
					, "文件接收确认框"
					,JOptionPane.YES_NO_OPTION) 
					== JOptionPane.OK_OPTION) 
			{
				JFileChooser fileSave = new JFileChooser("D:");//打开文件选择框
				fileSave.setDialogTitle("保存文件");//框名
				fileSave.setSelectedFile(new File(file.getName()));//创建新文件并设置选中
				File fileToSave = fileSave.getSelectedFile();
				System.out.println("此时的filetosave是这样的："+fileToSave);
				while (fileSave.showDialog(null, "保存") == JFileChooser.APPROVE_OPTION)//打开文件选择器并返回按键响应结果
				{
					fileToSave = fileSave.getSelectedFile();
					System.out.println("再次赋值之后的filetosave是这样的："+fileToSave);
					if (fileToSave.exists()) 
					{//如果已经存在了同名文件
//						if (JOptionPane.showConfirmDialog(null, "是否覆盖", "覆盖确认",
//								JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {// 不覆盖就重新选择文件路径
//							JOptionPane.showMessageDialog(null,"请重新选择存储位置");
//							continue;
//						}
						JOptionPane.showMessageDialog(null,"已有同名文件,请重新选择存储位置");
						continue;
					}//如果要覆盖就继续执行下面内容
//					if(fileToSave.getPath().equals("C:\\"+fileToSave.getName()) || 
//							fileToSave.getPath().equals("C:\\Users\\"+fileToSave.getName()) ) {
//						JOptionPane.showMessageDialog(null,"请不要在需要管理员权限的地方存储文件，否则文件将会接收失败");
//						continue;
//					}//限制存储路径
					// 创建进度条并加入消息记录文本框
					JProgressBar fileSendProcessBar = new JProgressBar();
					try {
						ServerSocket serverSocket = new ServerSocket(0);// 打开随机端口
						int fileport = serverSocket.getLocalPort();// 获取端口
						String ipAddress = InetAddress.getLocalHost().getHostAddress();// 获取IP
						FileResponseMessage fileResponseMessage = new FileResponseMessage(srcID, srcUser, true);
						fileResponseMessage.setFile(file);
						fileResponseMessage.setPort(fileport);
						fileResponseMessage.setIpAddress(ipAddress);
						synchronized (oos) {
							oos.writeObject(fileResponseMessage);
							oos.flush();
						}//发送文件响应信息，通知发件方发送文件
						// 在“消息记录”文本框中用蓝色显示‘发送文件响应信息’
						String msgRecord = dateFormat.format(new Date()) +" 正在接收" + srcUser + "发送的文件： " + file.getName()+ "\r\n";
						addMsgRecord(msgRecord, Color.blue, 12, false, false);
						// 进度条并加入消息记录文本框
						EventQueue.invokeLater(new Runnable() 
						{public void run() {textPaneMsgRecord.insertComponent(fileSendProcessBar);}});
						addMsgRecord("\r\n", Color.blue, 12, false, false);
						fileSendProcessBar.setVisible(true);// 进度条可见
						fileSendProcessBar.setStringPainted(true);// 设置进度条上的字符串显示，false则不能显示
						fileSendProcessBar.setForeground(SystemColor.activeCaption);
						System.err.println("-----------" + ipAddress + "----------" + fileport);
						filesocket = serverSocket.accept();
						InputStream fileInputStream = filesocket.getInputStream();
						byte[] buffer = new byte[1024];
						FileOutputStream fileOutputStream = new FileOutputStream(fileToSave.getPath());
						long fileLength = file.length();// 文件总长度
						int nowfilelenth = 0;// 已接收字节数
						int read = 0;// 读入字节数
						String prcent = 0 + "%";// 传输百分比
						fileSendProcessBar.setString(prcent);
						fileSendProcessBar.setMaximum(100);
						fileSendProcessBar.setMinimum(0);
						int value = 0;// 进度条的值
						long startTime = System.currentTimeMillis();// 接收开始时的时间
						long endTime;// 接收时的时间
						String speedStr;// 文件传输的当前速度
						while ((read = fileInputStream.read(buffer)) != -1) {
							fileOutputStream.write(buffer);
							endTime = System.currentTimeMillis();// 接收时的时间
							nowfilelenth = nowfilelenth + read;
							speedStr = getSpeed(nowfilelenth, startTime, endTime);// 文件接收速度
							value = (int) ((nowfilelenth / (fileLength + 0.01)) * 100);
							prcent = value + "%";// 文件接收百分比
							fileSendProcessBar.setValue(value);
							fileSendProcessBar.setString(prcent + " 当前文件接收速度：" + speedStr);// 设置进度条数值
						}
						fileSendProcessBar.setString("接收完成");
						fileOutputStream.close();
						fileInputStream.close();
						serverSocket.close();
						System.out.println("成功接收" + srcUser + "发送的文件： " + file.getName());
					} catch (IOException e) {
						if (e.toString().indexOf("Connection reset") != -1) {
							if (filesocket != null) {
								try {filesocket.close();} 
								catch (IOException e1) {e1.printStackTrace();}
								fileSendProcessBar.setValue(0);
								fileSendProcessBar.setString("文件接收失败");
								System.err.println("用户‘" + srcUser + "’已下线\r\n文件："+file.getName()+"\r\n接收失败");
								JOptionPane.showMessageDialog(null,"用户" + srcUser + "已下线\r\n文件："+file.getName()+"\r\n接收失败");
							}
						}
						else {e.printStackTrace();}
					} finally {
						if (filesocket != null) {
							try {filesocket.close();}
							catch (IOException e) {e.printStackTrace();}
						}
					}
					return;
				}
			}//同意接受文件的操作结束
			// 拒绝接受文件
			// 创建发送文件响应消息
			FileResponseMessage fileResponseMessage = new FileResponseMessage(srcID, srcUser, false);
			fileResponseMessage.setFile(file);
			// 发送文件响应信息
			try {
				synchronized (oos) {
					oos.writeObject(fileResponseMessage);
					oos.flush();
				}
			} catch (IOException e1) {e1.printStackTrace();}
			String messageRecord = "拒绝接收用户‘" + srcUser + "’发送的文件： " + file.getName();
			// 在“消息记录”文本框中用蓝色显示‘发送文件响应信息’
			String msgRecord = dateFormat.format(new Date()) + messageRecord + "\r\n";
			addMsgRecord(msgRecord, Color.blue, 12, false, false);
			System.out.println("拒绝接收用户' " + srcUser + " '发送的文件： " + file.getName());
		}
	}

	class FileResponseMessageHandler implements Runnable {
		private FileResponseMessage fileResponseMessage;

		public FileResponseMessageHandler(FileResponseMessage fileResponseMessage) 
		{this.fileResponseMessage = fileResponseMessage;}

		public void run() {
			String srcUser = fileResponseMessage.getSrcUser();// 发件人
			String fileName = fileResponseMessage.getFile().getName();
			if (fileResponseMessage.isRefuseReceiveFile()) 
			{// 拒绝接收文件
				// 用蓝色文字将收到消息的时间、发送消息的用户名和消息内容添加到“消息记录”文本框中
				final String msgRecord = dateFormat.format(new Date()) + " 用户‘" + srcUser + "’拒绝接收文件：" + fileName
						+ "\r\n";
				addMsgRecord(msgRecord, Color.blue, 12, false, false);
				System.out.println("用户" + srcUser + "拒绝接收您的文件：" + fileName);
			} 
			else if (fileResponseMessage.isAgreeReceiveFile()) 
			{// 同意接收文件
				int filePort = fileResponseMessage.getPort();
				String fileIpAddress = fileResponseMessage.getIpAddress();
				File file = fileResponseMessage.getFile();
				Socket fileSocket = null;
				// 创建进度条并加入消息记录文本框
				JProgressBar fileSendProcessBar = new JProgressBar();
				try {
					System.err.println("======" + fileIpAddress + "=======" + filePort);
					fileSocket = new Socket(fileIpAddress, filePort);
					final String msgRecord = dateFormat.format(new Date()) + " 正在向用户‘" + srcUser + "’发送文件：" + fileName
							+ "\r\n";
					addMsgRecord(msgRecord, Color.blue, 12, false, false);
					// 创建进度条并加入消息记录文本框
					// JProgressBar fileSendProcessBar = new JProgressBar();
					EventQueue.invokeLater(new Runnable() 
					{public void run() {textPaneMsgRecord.insertComponent(fileSendProcessBar);}});
					addMsgRecord("\r\n", Color.blue, 12, false, false);
					fileSendProcessBar.setVisible(true);
					fileSendProcessBar.setStringPainted(true);// 设置进度条上的字符串显示，false则不能显示
					fileSendProcessBar.setForeground(SystemColor.activeCaption);
					FileInputStream fileInputStream = new FileInputStream(file);
					OutputStream outputStream = fileSocket.getOutputStream();
					byte[] buffer = new byte[1024];
					long fileLength = file.length();// 文件总长度
					int nowfilelenth = 0;// 已接收字节数
					int read = 0;// 读入字节数
					String prcent = 0 + "%";// 传输百分比
					fileSendProcessBar.setString(prcent);
					fileSendProcessBar.setMaximum(100);
					fileSendProcessBar.setMinimum(0);
					int value = 0;// 进度条的值
					long startTime = System.currentTimeMillis();// 接收开始时的时间
					long endTime;// 发送结束时的时间
					String speedStr;// 文件传输的当前速度
					while ((read = fileInputStream.read(buffer)) != -1) {
						outputStream.write(buffer);
						endTime = System.currentTimeMillis();// 发送时的时间
						nowfilelenth = nowfilelenth + read;
						speedStr = getSpeed(nowfilelenth, startTime, endTime);// 文件接收速度
						value = (int) ((nowfilelenth / (fileLength + 0.01)) * 100);
						prcent = value + "%";// 文件接收百分比
						fileSendProcessBar.setValue(value);
						fileSendProcessBar.setString(prcent + " 当前文件发送速度：" + speedStr);// 设置进度条数值
					}
					fileSendProcessBar.setString("发送完成");
					fileInputStream.close();
					outputStream.close();
					fileSocket.close();
					System.out.println("文件：" + file.getName() + "-->"+fileResponseMessage.getSrcUser()+"发送完成");
				} 
				catch (UnknownHostException e) {e.printStackTrace();} 
				catch (IOException e) {
					if (e.toString().indexOf("Connection reset") != -1) {
						if (fileSocket != null) {
							try {
								fileSocket.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							fileSendProcessBar.setValue(0);
							fileSendProcessBar.setString("文件发送失败");
							System.err.println("用户‘" + srcUser + "’已下线\r\n文件："+fileName+"\r\n发送失败");
							JOptionPane.showMessageDialog(null,"用户‘" + srcUser + "’已下线\r\n文件："+fileName+"\r\n发送失败");
						}
					} 
					else {e.printStackTrace();}
				} 
				finally 
				{
					if (fileSocket != null) 
					{
						try {fileSocket.close();} 
						catch (IOException e) {e.printStackTrace();}
					}
				}
			}
		}
	}
}