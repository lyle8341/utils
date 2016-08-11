package weixin.request;

/**
 * 
 * @author Lyle
 *
 */
public class ReqVoiceMessage extends ReqBaseMessage {
	//语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String MediaId;
	//语音格式，如amr，speex等
	private String Format;
	//Recognition
	private String Recognition;

	public String getMediaId() {
		return this.MediaId;
	}

	public void setMediaId(String mediaId) {
		this.MediaId = mediaId;
	}

	public String getFormat() {
		return this.Format;
	}

	public void setFormat(String format) {
		this.Format = format;
	}

	public String getRecognition() {
		return this.Recognition;
	}

	public void setRecognition(String recognition) {
		this.Recognition = recognition;
	}
}
