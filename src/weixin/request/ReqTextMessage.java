package weixin.request;

/**
 * 
 * @author Lyle
 *
 */
public class ReqTextMessage extends ReqBaseMessage {
	//文本消息内容
	private String Content;

	public String getContent() {
		return this.Content;
	}

	public void setContent(String content) {
		this.Content = content;
	}
}
