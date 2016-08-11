package weixin.request;

public class ReqBaseMessage {

	// 开发者微信号
	private String ToUserName;
	// 发送方帐号（一个OpenID）
	private String FromUserName;
	// 消息创建时间 （整型）
	private long CreateTime;
	// 消息类型
	private String MsgType;
	// 消息id，64位整型
	private long MsgId;

	public String getToUserName() {
		return this.ToUserName;
	}

	public void setToUserName(String toUserName) {
		this.ToUserName = toUserName;
	}

	public String getFromUserName() {
		return this.FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return this.CreateTime;
	}

	public void setCreateTime(long createTime) {
		this.CreateTime = createTime;
	}

	public String getMsgType() {
		return this.MsgType;
	}

	public void setMsgType(String msgType) {
		this.MsgType = msgType;
	}

	public long getMsgId() {
		return this.MsgId;
	}

	public void setMsgId(long msgId) {
		this.MsgId = msgId;
	}
}
