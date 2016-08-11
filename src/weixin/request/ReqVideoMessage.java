package weixin.request;

/**
 * 
 * @author Lyle
 *
 */
public class ReqVideoMessage extends ReqBaseMessage {
	//视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String MediaId;
	//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	private String ThumbMediaId;

	public String getMediaId() {
		return this.MediaId;
	}

	public void setMediaId(String mediaId) {
		this.MediaId = mediaId;
	}

	public String getThumbMediaId() {
		return this.ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.ThumbMediaId = thumbMediaId;
	}
}
