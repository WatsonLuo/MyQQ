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

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public boolean isPubChatMessage() {
		return getDstUser().equals("");
	}
}


enum userState
{onLine,offLine,busy,active}
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