package weixin.response;

/**
 * 回复文本消息
 * @author Lyle
 *
 */
public class RespTextMessage extends RespBaseMessage{

	// 回复的消息内容
		private String Content;

		public String getContent() {
			return this.Content;
		}

		public void setContent(String content) {
			this.Content = content;
		}
}
