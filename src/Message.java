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
	offLine("离线",0),
	onLine("在线",1),
	login("登录",2),
	active("等撩",3),
	hidden("隐身",4),
	busy("勿扰",5);
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