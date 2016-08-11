package utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 处理xml的工具类
 * @author Lyle
 *
 */
public class XmlUtils {
	
	/**
	 * 从请求request中解析出xml信息
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String,String> parseXml(HttpServletRequest request,HttpServletResponse response) throws IOException, DocumentException{
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
		//关闭流
		CloseStreamUtils.close(inputStream);
		return requestMap;
	}
	
	/**
	 * 扩展xstream使其支持CDATA
	 */
	private static final XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

//				public void startNode(String name, Class clazz) {
//					super.startNode(name, clazz);
//				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
	
	/**
	 * 消息对象转成xml
	 * @param message 需要转换的消息对象
	 * @return
	 */
	public static <T> String messageToXml(T message){
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);
	}
}
