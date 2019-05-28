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


enum userState
{
	offLine("离线",0),
	onLine("在线",1),
	busy("勿扰",2),
	active("等撩",3),
	hidden("隐身",4);
	private String name;
	private int index;
	private userState(String name,int index)
	{
		this.name=name;
		this.index=index;
	}
	public static String getName(int index) {  
		for (userState s : userState.values())
		{if (s.ordinal() == index) {return s.name;}}  
	    return null;
	}
}
class UserStateMessage extends Message {
	private userState userState;

	public UserStateMessage(String srcUser, String dstUser, userState userState) {
		super(srcUser, dstUser);
		this.userState = userState;
	}

	public userState getUserState() {return userState;}
	public void setUserState(userState userState) {this.userState = userState;}

	public boolean isPubUserStateMessage() {return getDstUser().equals("");}
}