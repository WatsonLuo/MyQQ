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
		setTitle("�Ի���");
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
		ImageIcon headIcon=new ImageIcon("img\\head\\Ƥ����.jpg");
		headIcon.setImage(headIcon.getImage().getScaledInstance(120, 120,Image.SCALE_DEFAULT ));
		JLabel nameJLabel=new JLabel("�ǳƣ�"+dstName);
		headImgJLabel.setIcon(headIcon);
		
		nameBox.add(Box.createHorizontalGlue());
		nameBox.add(nameJLabel);
		nameBox.add(Box.createHorizontalGlue());
		
		detail.add(headImgJLabel);
		detail.add(nameBox);
		detail.add(new JLabel("ID��"+dstID));
		detail.add(Box.createVerticalGlue());
		detail.add(new JLabel("�ҷ���"+srcID));
		
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
		
		btnSendMsg = new JButton("������Ϣ"); // ��������Ϣ����ť
		btnSendMsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msgContent = textFieldMsgToSend.getText();
				if (msgContent.length() > 0) 
				{
					ChatMessage chatMessage;
					String msgRecord=dateFormat.format(new Date());
					if(dstName.equals("�㲥"))
					{
						chatMessage = new ChatMessage(srcID, "", msgContent);
						msgRecord +="����˵:"+ msgContent + "\r\n";
					}
					else 
					{
						chatMessage = new ChatMessage(srcID, dstID, msgContent);
						msgRecord += "��"+dstID+"˵:"+ msgContent + "\r\n";
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
		btnFile=new JButton("�ļ�����");

		btnFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				JFileChooser fileChooser=new JFileChooser("E:");
				fileChooser.setDialogTitle("ѡ���ļ�");
				fileChooser.setApproveButtonText("ѡ��");
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//�������ܲ�Ӱ�츸����
	}//frame��������

	//��FriendList��chatWindow�������ݵ���Ҫͨ��
	public void setmsg(Message msgfa)
	{
		msg=msgfa;
		if(msg instanceof UserStateMessage) {System.out.println("������window����������״̬��Ϣ");}
		else if (msg instanceof ChatMessage) {processChatMessage((ChatMessage) msg);} //����������Ϣ
		else if(msg instanceof FileSengMessage) {new Thread(new FileSendMessageHandler((FileSengMessage) msg)).start();}
		else if(msg instanceof FileResponseMessage){new Thread(new FileResponseMessageHandler((FileResponseMessage) msg)).start();}
		else {System.err.println("�û���������Ϣ��ʽ����!");}//��Ϣ��ʽ����
	}
	//��Ի����������Ϣ
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

	// ���������ת������������Ϣ
	private void processChatMessage(ChatMessage msg) {
		String srcUser = msg.getSrcUser();
		String dstUser = msg.getDstUser();
		String msgContent = msg.getMsgContent();
		if (msg.isPubChatMessage() || dstUser.equals(srcID)) {
			// �ú�ɫ���ֽ��յ���Ϣ��ʱ�䡢������Ϣ���û�������Ϣ������ӵ�����Ϣ��¼���ı�����
			final String msgRecord = dateFormat.format(new Date())+" "+srcUser +"˵: "+msgContent+"\r\n";
			addMsgRecord(msgRecord, Color.black, 12, false, false);
		}
	}

	/**
	 * �ͻ��˺�̨�����߳� ����̨�����̡߳��յ��˷�����ת�������û�������Ϣ ����ɫ���ֽ��û������û�����ʱ����ӵ�����Ϣ��¼���ı�����
	 * �ڡ������û����б������������ߵ��û��� ����̨�����̡߳��յ��˷�����ת�����������û���Ϣ �ڡ������û����б������������û���
	 * ����̨�����̡߳��յ��˷�����ת�����Ĺ㲥��Ϣ �ú�ɫ���ֽ��յ���Ϣ��ʱ�䡢������Ϣ���û�������Ϣ������ӵ�����Ϣ��¼���ı�����
	 * ����̨�����̡߳��յ��˷�����ת�������û�������Ϣ �ڡ������û����б���ɾ�����ߵ��û���
	 * 
	 * @author wmren
	 *
	 */
	// �����ļ���������
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
	// ��ʽ���ַ���
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
			String srcUser = fileSengMessage.getSrcUser();// �������ļ��ķ�����
			Socket filesocket = null;
			if (JOptionPane.showConfirmDialog(null
					,"�Ƿ�ͬ�����"+srcUser+"���������ļ���\r\n" + file.getName() + "\r\n�ļ���С��" + file.length()
					, "�ļ�����ȷ�Ͽ�"
					,JOptionPane.YES_NO_OPTION) 
					== JOptionPane.OK_OPTION) 
			{
				JFileChooser fileSave = new JFileChooser("D:");//���ļ�ѡ���
				fileSave.setDialogTitle("�����ļ�");//����
				fileSave.setSelectedFile(new File(file.getName()));//�������ļ�������ѡ��
				File fileToSave = fileSave.getSelectedFile();
				System.out.println("��ʱ��filetosave�������ģ�"+fileToSave);
				while (fileSave.showDialog(null, "����") == JFileChooser.APPROVE_OPTION)//���ļ�ѡ���������ذ�����Ӧ���
				{
					fileToSave = fileSave.getSelectedFile();
					System.out.println("�ٴθ�ֵ֮���filetosave�������ģ�"+fileToSave);
					if (fileToSave.exists()) 
					{//����Ѿ�������ͬ���ļ�
//						if (JOptionPane.showConfirmDialog(null, "�Ƿ񸲸�", "����ȷ��",
//								JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {// �����Ǿ�����ѡ���ļ�·��
//							JOptionPane.showMessageDialog(null,"������ѡ��洢λ��");
//							continue;
//						}
						JOptionPane.showMessageDialog(null,"����ͬ���ļ�,������ѡ��洢λ��");
						continue;
					}//���Ҫ���Ǿͼ���ִ����������
//					if(fileToSave.getPath().equals("C:\\"+fileToSave.getName()) || 
//							fileToSave.getPath().equals("C:\\Users\\"+fileToSave.getName()) ) {
//						JOptionPane.showMessageDialog(null,"�벻Ҫ����Ҫ����ԱȨ�޵ĵط��洢�ļ��������ļ��������ʧ��");
//						continue;
//					}//���ƴ洢·��
					// ������������������Ϣ��¼�ı���
					JProgressBar fileSendProcessBar = new JProgressBar();
					try {
						ServerSocket serverSocket = new ServerSocket(0);// ������˿�
						int fileport = serverSocket.getLocalPort();// ��ȡ�˿�
						String ipAddress = InetAddress.getLocalHost().getHostAddress();// ��ȡIP
						FileResponseMessage fileResponseMessage = new FileResponseMessage(srcID, srcUser, true);
						fileResponseMessage.setFile(file);
						fileResponseMessage.setPort(fileport);
						fileResponseMessage.setIpAddress(ipAddress);
						synchronized (oos) {
							oos.writeObject(fileResponseMessage);
							oos.flush();
						}//�����ļ���Ӧ��Ϣ��֪ͨ�����������ļ�
						// �ڡ���Ϣ��¼���ı���������ɫ��ʾ�������ļ���Ӧ��Ϣ��
						String msgRecord = dateFormat.format(new Date()) +" ���ڽ���" + srcUser + "���͵��ļ��� " + file.getName()+ "\r\n";
						addMsgRecord(msgRecord, Color.blue, 12, false, false);
						// ��������������Ϣ��¼�ı���
						EventQueue.invokeLater(new Runnable() 
						{public void run() {textPaneMsgRecord.insertComponent(fileSendProcessBar);}});
						addMsgRecord("\r\n", Color.blue, 12, false, false);
						fileSendProcessBar.setVisible(true);// �������ɼ�
						fileSendProcessBar.setStringPainted(true);// ���ý������ϵ��ַ�����ʾ��false������ʾ
						fileSendProcessBar.setForeground(SystemColor.activeCaption);
						System.err.println("-----------" + ipAddress + "----------" + fileport);
						filesocket = serverSocket.accept();
						InputStream fileInputStream = filesocket.getInputStream();
						byte[] buffer = new byte[1024];
						FileOutputStream fileOutputStream = new FileOutputStream(fileToSave.getPath());
						long fileLength = file.length();// �ļ��ܳ���
						int nowfilelenth = 0;// �ѽ����ֽ���
						int read = 0;// �����ֽ���
						String prcent = 0 + "%";// ����ٷֱ�
						fileSendProcessBar.setString(prcent);
						fileSendProcessBar.setMaximum(100);
						fileSendProcessBar.setMinimum(0);
						int value = 0;// ��������ֵ
						long startTime = System.currentTimeMillis();// ���տ�ʼʱ��ʱ��
						long endTime;// ����ʱ��ʱ��
						String speedStr;// �ļ�����ĵ�ǰ�ٶ�
						while ((read = fileInputStream.read(buffer)) != -1) {
							fileOutputStream.write(buffer);
							endTime = System.currentTimeMillis();// ����ʱ��ʱ��
							nowfilelenth = nowfilelenth + read;
							speedStr = getSpeed(nowfilelenth, startTime, endTime);// �ļ������ٶ�
							value = (int) ((nowfilelenth / (fileLength + 0.01)) * 100);
							prcent = value + "%";// �ļ����հٷֱ�
							fileSendProcessBar.setValue(value);
							fileSendProcessBar.setString(prcent + " ��ǰ�ļ������ٶȣ�" + speedStr);// ���ý�������ֵ
						}
						fileSendProcessBar.setString("�������");
						fileOutputStream.close();
						fileInputStream.close();
						serverSocket.close();
						System.out.println("�ɹ�����" + srcUser + "���͵��ļ��� " + file.getName());
					} catch (IOException e) {
						if (e.toString().indexOf("Connection reset") != -1) {
							if (filesocket != null) {
								try {filesocket.close();} 
								catch (IOException e1) {e1.printStackTrace();}
								fileSendProcessBar.setValue(0);
								fileSendProcessBar.setString("�ļ�����ʧ��");
								System.err.println("�û���" + srcUser + "��������\r\n�ļ���"+file.getName()+"\r\n����ʧ��");
								JOptionPane.showMessageDialog(null,"�û�" + srcUser + "������\r\n�ļ���"+file.getName()+"\r\n����ʧ��");
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
			}//ͬ������ļ��Ĳ�������
			// �ܾ������ļ�
			// ���������ļ���Ӧ��Ϣ
			FileResponseMessage fileResponseMessage = new FileResponseMessage(srcID, srcUser, false);
			fileResponseMessage.setFile(file);
			// �����ļ���Ӧ��Ϣ
			try {
				synchronized (oos) {
					oos.writeObject(fileResponseMessage);
					oos.flush();
				}
			} catch (IOException e1) {e1.printStackTrace();}
			String messageRecord = "�ܾ������û���" + srcUser + "�����͵��ļ��� " + file.getName();
			// �ڡ���Ϣ��¼���ı���������ɫ��ʾ�������ļ���Ӧ��Ϣ��
			String msgRecord = dateFormat.format(new Date()) + messageRecord + "\r\n";
			addMsgRecord(msgRecord, Color.blue, 12, false, false);
			System.out.println("�ܾ������û�' " + srcUser + " '���͵��ļ��� " + file.getName());
		}
	}

	class FileResponseMessageHandler implements Runnable {
		private FileResponseMessage fileResponseMessage;

		public FileResponseMessageHandler(FileResponseMessage fileResponseMessage) 
		{this.fileResponseMessage = fileResponseMessage;}

		public void run() {
			String srcUser = fileResponseMessage.getSrcUser();// ������
			String fileName = fileResponseMessage.getFile().getName();
			if (fileResponseMessage.isRefuseReceiveFile()) 
			{// �ܾ������ļ�
				// ����ɫ���ֽ��յ���Ϣ��ʱ�䡢������Ϣ���û�������Ϣ������ӵ�����Ϣ��¼���ı�����
				final String msgRecord = dateFormat.format(new Date()) + " �û���" + srcUser + "���ܾ������ļ���" + fileName
						+ "\r\n";
				addMsgRecord(msgRecord, Color.blue, 12, false, false);
				System.out.println("�û�" + srcUser + "�ܾ����������ļ���" + fileName);
			} 
			else if (fileResponseMessage.isAgreeReceiveFile()) 
			{// ͬ������ļ�
				int filePort = fileResponseMessage.getPort();
				String fileIpAddress = fileResponseMessage.getIpAddress();
				File file = fileResponseMessage.getFile();
				Socket fileSocket = null;
				// ������������������Ϣ��¼�ı���
				JProgressBar fileSendProcessBar = new JProgressBar();
				try {
					System.err.println("======" + fileIpAddress + "=======" + filePort);
					fileSocket = new Socket(fileIpAddress, filePort);
					final String msgRecord = dateFormat.format(new Date()) + " �������û���" + srcUser + "�������ļ���" + fileName
							+ "\r\n";
					addMsgRecord(msgRecord, Color.blue, 12, false, false);
					// ������������������Ϣ��¼�ı���
					// JProgressBar fileSendProcessBar = new JProgressBar();
					EventQueue.invokeLater(new Runnable() 
					{public void run() {textPaneMsgRecord.insertComponent(fileSendProcessBar);}});
					addMsgRecord("\r\n", Color.blue, 12, false, false);
					fileSendProcessBar.setVisible(true);
					fileSendProcessBar.setStringPainted(true);// ���ý������ϵ��ַ�����ʾ��false������ʾ
					fileSendProcessBar.setForeground(SystemColor.activeCaption);
					FileInputStream fileInputStream = new FileInputStream(file);
					OutputStream outputStream = fileSocket.getOutputStream();
					byte[] buffer = new byte[1024];
					long fileLength = file.length();// �ļ��ܳ���
					int nowfilelenth = 0;// �ѽ����ֽ���
					int read = 0;// �����ֽ���
					String prcent = 0 + "%";// ����ٷֱ�
					fileSendProcessBar.setString(prcent);
					fileSendProcessBar.setMaximum(100);
					fileSendProcessBar.setMinimum(0);
					int value = 0;// ��������ֵ
					long startTime = System.currentTimeMillis();// ���տ�ʼʱ��ʱ��
					long endTime;// ���ͽ���ʱ��ʱ��
					String speedStr;// �ļ�����ĵ�ǰ�ٶ�
					while ((read = fileInputStream.read(buffer)) != -1) {
						outputStream.write(buffer);
						endTime = System.currentTimeMillis();// ����ʱ��ʱ��
						nowfilelenth = nowfilelenth + read;
						speedStr = getSpeed(nowfilelenth, startTime, endTime);// �ļ������ٶ�
						value = (int) ((nowfilelenth / (fileLength + 0.01)) * 100);
						prcent = value + "%";// �ļ����հٷֱ�
						fileSendProcessBar.setValue(value);
						fileSendProcessBar.setString(prcent + " ��ǰ�ļ������ٶȣ�" + speedStr);// ���ý�������ֵ
					}
					fileSendProcessBar.setString("�������");
					fileInputStream.close();
					outputStream.close();
					fileSocket.close();
					System.out.println("�ļ���" + file.getName() + "-->"+fileResponseMessage.getSrcUser()+"�������");
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
							fileSendProcessBar.setString("�ļ�����ʧ��");
							System.err.println("�û���" + srcUser + "��������\r\n�ļ���"+fileName+"\r\n����ʧ��");
							JOptionPane.showMessageDialog(null,"�û���" + srcUser + "��������\r\n�ļ���"+fileName+"\r\n����ʧ��");
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