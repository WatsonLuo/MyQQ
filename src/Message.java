import java.io.File;
import java.io.Serializable;

public class Message implements Serializable {
	private String srcUser;
	private String dstUser;

	public Message(String srcUser, String dstUser) {
		this.srcUser = srcUser;
		this.dstUser = dstUser;
	}

	public String getSrcUser() {return srcUser;}
	public String getDstUser() {return dstUser;}
	public void setSrcUser(String srcUser) {this.srcUser = srcUser;}
	public void setDstUser(String dstUser) {this.dstUser = dstUser;}
}

class ChatMessage extends Message {
	private String msgContent;

	public ChatMessage(String srcUser, String dstUser, String msgContent) {
		super(srcUser, dstUser);
		this.msgContent = msgContent;
	}

	public String getMsgContent() {return msgContent;}

	public void setMsgContent(String msgContent) {this.msgContent = msgContent;}

	public boolean isPubChatMessage() {return getDstUser().equals("");}
}

enum userStatus
{
	offLine("����",0),
	onLine("����",1),
	login("��¼",2),
	active("����",3),
	hidden("����",4),
	busy("����",5);
	private String name;
	private int index;
	private userStatus(String name,int index)
	{
		this.name=name;
		this.index=index;
	}
	public static userStatus getStatus(String name) {
		for (userStatus s : userStatus.values())
		{if (s.name.equals(name)) {return s;}}  
	    return null;
	}
	public int getIndex(){return index;}
	public String getName() {return name;}
	public static String getName(int index) {  
		for (userStatus s : userStatus.values())
		{if (s.ordinal() == index) {return s.name;}}  
	    return null;
	}
}
class UserStateMessage extends Message {
	private userStatus userStatus;

	public UserStateMessage(String srcUser, String dstUser, userStatus userStatus) {
		super(srcUser, dstUser);
		this.userStatus = userStatus;
	}

	public userStatus getUserState() {return userStatus;}
	public void setUserState(userStatus userStatus) {this.userStatus = userStatus;}

	public boolean isPubUserStateMessage() {return getDstUser().equals("");}
}

class FileSengMessage extends Message{
	private File file;
	public FileSengMessage(String srcUser, String dstUser, File file) {
		super(srcUser, dstUser);
		this.file = file;
	}
	public File getFile() {return file;}
	public void setFile(File file) {this.file = file;}
}
class FileResponseMessage extends Message{
	private int port;
	private String ipAddress;
	private File file;
	private boolean agreeReceiveFile;
	public FileResponseMessage(String srcUser, String dstUser, boolean agreeReceiveFile)
	{
		super(srcUser, dstUser);
		this.agreeReceiveFile = agreeReceiveFile;
	}
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
	public String getIpAddress() {return ipAddress;}
	public void setIpAddress(String ipAddress) {this.ipAddress = ipAddress;}
		
	public File getFile() {return file;}
	public void setFile(File file) {this.file = file;}
	public boolean isAgreeReceiveFile() {return agreeReceiveFile;}
	public boolean isRefuseReceiveFile() {return !agreeReceiveFile;}
	public void setIfAgreeReceiveFile(boolean agreeReceiveFile) {this.agreeReceiveFile = agreeReceiveFile;}
	
	
}
