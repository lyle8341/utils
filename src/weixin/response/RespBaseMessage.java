package weixin.response;

public class RespBaseMessage {

	// 接收方帐号（收到的OpenID）
	private String ToUserName;
	// 开发者微信号
	private String FromUserName;
	// 消息创建时间 （整型）
	private long CreateTime;
	// 消息类型
	private String MsgType;

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
}
