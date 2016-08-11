package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtils {

	public static void parseXml(HttpServletRequest request,HttpServletResponse response) throws IOException, DocumentException{
		
		//解析xml请求
		InputStream inputStream = request.getInputStream();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(inputStream);
		//得到xml的跟元素
		Element root = document.getRootElement();
		//得到根节点的所有子节点
		List<Element> elementsList = root.elements();
		Map<String,String> requestMap = new HashMap<String, String>();
		for(Element e:elementsList){
			requestMap.put(e.getName(), e.getText());
		}
		String toUserName = requestMap.get("ToUserName");
		String fromUserName = requestMap.get("FromUserName");
		String createTime = requestMap.get("CreateTime");
		String msgType = requestMap.get("MsgType");
		String content = requestMap.get("Content");
		String msgId = requestMap.get("MsgId");
		
		
		
		
		
		
	}
}
