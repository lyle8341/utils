package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import test.SanYuanYunSuan;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

/**
 * 处理xml的工具类
 * 
 * @author Lyle
 * 
 */
public class XmlUtils {

	private XmlUtils() {
		throw new Error("不能实例化对象");
	}

	/**
	 * <h1>重要</h1> 从请求request中解析出xml信息
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> parseXml(HttpServletRequest request)
			throws IOException, DocumentException {
		// 解析xml请求
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();
		Document document = getDocument(request);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());
		// 关闭流
		return map;
	}

	/**
	 * 扩展xstream使其支持CDATA
	 */
	private static final XStream xstream = new XStream(new DomDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				// public void startNode(String name, Class clazz) {
				// super.startNode(name, clazz);
				// }
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
	 * <h1>重要</h1> 消息对象转成xml
	 * 
	 * @param message
	 *            需要转换的消息对象
	 * @return
	 */
	public static <T> String messageToXml(T message) {
		xstream.alias("xml", message.getClass());
		//TODO 转为简写，否则标签名就是全类名
		xstream.alias("job", SanYuanYunSuan.class);
		return xstream.toXML(message);
	}

	/**
	 * <h1>重要</h1> 从request请求中获取xml并且转为字符串
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static String getXmlString(HttpServletRequest request)
			throws IOException, DocumentException {
		String xml = getFullXmlString(request);
		xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		return xml.trim();
	}

	/**
	 * 从request请求中获取完整的xml字符串
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	private static String getFullXmlString(HttpServletRequest request)
			throws IOException, DocumentException {
		Document document = getDocument(request);
		String xml = document.asXML();
		return xml;
	}


	/**
	 * <h1>重要</h1> 从request请求中获取Document(org.dom4j.Document)
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Document getDocument(HttpServletRequest request)
			throws IOException, DocumentException {
		InputStream inputStream = request.getInputStream();
		SAXReader reader = new SAXReader();
		Document doc = reader.read(inputStream);
		CloseStreamUtils.close(inputStream);
		return doc;
	}

	/**
	 * JAXB 使用方法:<br>
	 * 1.在JavaBean上加@XmlRootElement(name = "xml")，xml是根节点名。<br>
	 * 2.在每个属性上加@XmlElement(name = "Event")，Event是标签名。
	 * 
	 * @param xmlStr
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static <T> T xmlStrToBean(String xmlStr, Class<T> clazz)
			throws Exception {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(new StringReader(xmlStr));
		} catch (JAXBException e) {
			throw new Exception("字符串转对象出错，原因是" + e);
		}
		return t;
	}

	/**
	 * 使用xstream,xml字符串转为javabean
	 * 
	 * @param xml格式的字符串
	 * @param clazz
	 * @return
	 */
	public static <T> T xmlToObject(String xml, Class<T> clazz) {
		if (clazz != null) // 微信公众平台开发的时候需要把更节点替换成xml ！！！！
		{
			xml = xml.replace("<xml>", "<" + clazz.getName() + ">");
			xml = xml.replace("</xml>", "</" + clazz.getName() + ">");
		}
		@SuppressWarnings("unchecked")
		T fromXML = (T) xstream.fromXML(xml);
		return fromXML;
	}

	/**
	 * xml数据流转为javabean
	 * 
	 * @param xml
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static <T> T xmlToObject(InputStream xml, Class<T> clazz)
			throws IOException {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new InputStreamReader(xml, "UTF-8"));
			// 最好在将字节流转换为字符流的时候 进行转码
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = bf.readLine()) != null) {
				buffer.append(line);
			}
			return xmlToObject(buffer.toString(), clazz);
		} finally {
			CloseStreamUtils.close(bf);
		}
	}

	/**
	 * json格式数据转换为对象/可以直接用FastJson的方法更好<br>
	 * {"sex":"男","name":"lyle","time":"1473231695012","info":{"grade":"大班","age":"28"}}这种格式需要转换<br>
	 * {<br>
     * "lyleMessage": {<br>
     *  "sex": "男",<br>
     *  "name": "lyle",<br>
     *  "time": "1473232525911",<br>
     *  "info": {<br>
     *      "grade": "大班",<br>
     *      "age": "28"<br>
     *  }<br>
     * }<br>
	 *}<br>
	 * @param json 格式有要求<br>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonToObject(String json,Class<T> clazz){
		XStream stream = null;
		stream = new XStream(new JettisonMappedXmlDriver());
		//如果不加下面这行，无法处理，第一个参数是类名首字母小写。
		stream.alias(StringUtils.benginCharToLower(clazz.getSimpleName(), 1), clazz); 
		return (T) stream.fromXML(json);
	}
	
	/**
	 * <h1>重要</h1>
	 * xml字符串转为map
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	 @SuppressWarnings("unchecked")
	public static Map<String,Object> xmlToMap(String xml) throws DocumentException {
	        Document doc = DocumentHelper.parseText(xml);
	        Element root = doc.getRootElement();
	        List<Element> list = root.elements();
	        Map<String,Object> resultMap = new HashMap<String,Object>();
	        for (Element e : list) {
	            String name = e.getName();
	            String text = e.getTextTrim();
	            if (e.elements().size() == 0) {
	                resultMap.put(name, text);
	            } else {
	                if (!resultMap.containsKey(name)) {
	                	Map<String,Object> temp = xmlToMap(e.asXML());
	                   /*
	                    * 原始方法
	                    */
	                	resultMap.put(name, temp);
	                	/*
	                	 * 改造后
	                	 * JSON格式处理后才能用map->json->对象的转换
	                	 */
//	                	if(temp.containsKey("job")){
//	                		resultMap.put(name, temp.get("job"));
//	                	}else{
//	                		resultMap.put(name, temp);
//	                	}
	                	//改造后
	                } else {
	                    List<Map<String,Object>> tmplist = null;
	                    if (resultMap.get(name) instanceof java.util.Map) {
	                        tmplist = new ArrayList<Map<String,Object>>();
	                        tmplist.add((Map<String,Object>) resultMap.get(name));
	                    } else if (resultMap.get(name) instanceof java.util.List) {
	                        tmplist = (List<Map<String,Object>>) resultMap.get(name);
	                    }
	                    tmplist.add(xmlToMap(e.asXML()));
	                    resultMap.put(name, tmplist);
	                }
	            }
	        }
	        return resultMap;
	    }
	 
//	 public static <T> T xmlToObject2(String xml,Class<T> clazz) throws DocumentException, IOException{
//		 Map<String, Object> map = xmlToMap(xml);
//		 String json = JSON.toJSONString(map);
//		 return JSON.parseObject(json, clazz);
//	 }
}