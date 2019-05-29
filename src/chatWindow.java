import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class chatWindow extends JFrame {
	ObjectOutputStream oos;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private final DefaultListModel<String> onlinUserDlm = new DefaultListModel<String>();
	private final String srcID;
	private final String dstID;
	private Message msg;
	private final static HashMap<String, chatWindow> openedDstID=new HashMap<String, chatWindow>();
	
	private final JPanel contentPane;
	private final JTextField textFieldMsgToSend;
	private final JTextPane textPaneMsgRecord;
	private final JButton btnSendMsg;
	private final JButton btnSendFile;

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
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				openedDstID.remove(dstID);
			}
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
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

		JPanel panelSouth = new JPanel();
		
		panelSouth.setBorder(new EmptyBorder(5, 0, 0, 0));
		contentPane.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new BoxLayout(panelSouth, BoxLayout.X_AXIS));

		textFieldMsgToSend = new JTextField();
		panelSouth.add(textFieldMsgToSend);
		textFieldMsgToSend.setColumns(10);

		panelSouth.add(Box.createHorizontalStrut(20));
		
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
		panelSouth.add(btnSendMsg);
		panelSouth.add(Box.createHorizontalStrut(20));
		btnSendFile = new JButton("�����ļ�");
		panelSouth.add(btnSendFile);
		
		
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//�������ܲ�Ӱ�츸����
	}

	public void setmsg(Message msgfa)
	{
		msg=msgfa;
		if(msg instanceof UserStateMessage) {System.out.println("������window����������״̬��Ϣ");}
		else if (msg instanceof ChatMessage) {processChatMessage((ChatMessage) msg);} //����������Ϣ
		else {System.err.println("�û���������Ϣ��ʽ����!");}//��Ϣ��ʽ����
	}
	
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

	// ���������ת�����Ĺ�����Ϣ
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
}
