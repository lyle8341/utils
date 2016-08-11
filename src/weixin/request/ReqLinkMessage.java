package weixin.request;

/**
 * 
 * @author Lyle
 *
 */
public class ReqLinkMessage extends ReqBaseMessage {
	//消息标题
	private String Title;
	//消息描述
	private String Description;
	//消息链接
	private String Url;

	public String getTitle() {
		return this.Title;
	}

	public void setTitle(String title) {
		this.Title = title;
	}

	public String getDescription() {
		return this.Description;
	}

	public void setDescription(String description) {
		this.Description = description;
	}

	public String getUrl() {
		return this.Url;
	}

	public void setUrl(String url) {
		this.Url = url;
	}
}
