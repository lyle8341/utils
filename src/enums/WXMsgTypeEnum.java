package enums;

public enum WXMsgTypeEnum {

	TEXT("text","文本消息"),
	IMAGE("image","图片消息"),
	VOICE("voice","语音消息"),
	VIDEO("video","视频消息"),
	SHORTVIDEO("shortvideo","小视频消息"),
	LOCATION("location","地理位置消息"),
	LINK("link","链接消息");
	
	private String type;
	private String desc;
	
	private WXMsgTypeEnum(String type,String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getDesc(){
		return this.desc;
	}
	
}
