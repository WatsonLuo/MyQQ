import java.awt.BorderLayout;
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
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
	DefaultTableModel defaultTableModel=new DefaultTableModel(new String[]{"ID","����","���","״̬"}, 0)
	{public boolean isCellEditable(int row, int column){return false;}};
	
	public static void main(String[] args) {
		DerbyDB.prepareDB();
		DerbyDB.createAccountTable();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {new friendList("295579587",null,null,null);} 
				catch (Exception e) {e.printStackTrace();}
			}
		});
	}

	public friendList(String id,Socket fatherSocket,ObjectOutputStream fao,ObjectInputStream fai) {
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
				UserStateMessage userStateMessage = new UserStateMessage(id, "", userState.valueOf(statusString));
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
		
		//���ú����б������
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
					if(chatWindow.isOpenned(dstID)) {/*ȷ����һ���˵ĶԻ���ֻ��һ��*/}
					else {new chatWindow(id,dstID,dstName,fatherSocket,fao,fai);}
					/*��Ӧ����������ΪJFrame2����һ���������࣬ʹ�õ���ģʽ��������ȥ�����ģʽ���飩
					 * �ڰ�ť�¼���ֻ����һ��ʵ�����磺JFrame2.getInstance().setVisible(true) ����
					 * д��������о������ʺ���������*/
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
			defaultTableModel.addRow(new Object[]{"","�㲥","Ⱥ��",""});			
			ResultSet resultSet=DerbyDB.getAccountTable();
			resultSet.next();
			while(resultSet.next())
			{
				id=resultSet.getString("ID");
				name=resultSet.getString("Name");
				type="����";
				status=userState.getName(resultSet.getInt("Status"));
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
		@Override
		protected void process(List<String[]> rows) {
			// TODO Auto-generated method stub
			for(String[] row: rows){defaultTableModel.addRow(row);}
		}
	}
}
