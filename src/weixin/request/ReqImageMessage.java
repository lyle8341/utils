package weixin.request;

/**
 * 
 * @author Lyle
 *
 */
public class ReqImageMessage extends ReqBaseMessage {
	// 图片链接（由系统生成）
	private String PicUrl;

	public String getPicUrl() {
		return this.PicUrl;
	}

	public void setPicUrl(String picUrl) {
		this.PicUrl = picUrl;
	}
}
