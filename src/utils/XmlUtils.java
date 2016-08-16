package utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
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
	 * 消息对象转成xml字符串
	 * @param message 需要转换的消息对象
	 * @return
	 */
	public static <T> String messageToXml(T message){
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);
	}
	
	/** 
     * xml字符串转换成bean对象 
     *  
     * @param xmlStr xml字符串 
     * @param clazz 待转换的class 
     * @return 转换后的对象 
     */  
    public static <T> T xmlStrToBean(String xmlStr, Class<T> clazz) {  
        T obj = null;  
        try {  
            // 将xml格式的数据转换成Map对象  
            Map<String, Object> map = xmlStrToMap(xmlStr);  
            //将map对象的数据转换成Bean对象  
            obj = mapToBean(map, clazz);  
        } catch(Exception e) {  
            e.printStackTrace();  
        }  
        return obj;  
    }  
      
    /** 
     * 将xml格式的字符串转换成Map对象 
     *  
     * @param xmlStr xml格式的字符串 
     * @return Map对象 
     * @throws Exception 异常 
     */  
    public static Map<String, Object> xmlStrToMap(String xmlStr) throws Exception {  
        if(!StringUtils.isNotEmpty(xmlStr)) {  
            return null;  
        }  
        Map<String, Object> map = new HashMap<String, Object>();  
        //将xml格式的字符串转换成Document对象  
        Document doc = DocumentHelper.parseText(xmlStr);  
        //获取根节点  
        Element root = doc.getRootElement();  
        //获取根节点下的所有元素  
        List<Element> children = root.elements();  
        //循环所有子元素  
        if(children != null && children.size() > 0) {  
            for(int i = 0; i < children.size(); i++) {  
                Element child = (Element)children.get(i);  
                map.put(child.getName(), child.getTextTrim());  
            }  
        }  
        return map;  
    }  
      
    /** 
     * 将Map对象通过反射机制转换成Bean对象 
     *  
     * @param map 存放数据的map对象 
     * @param clazz 待转换的class 
     * @return 转换后的Bean对象 
     * @throws Exception 异常 
     */  
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) throws Exception {  
        T obj = clazz.newInstance();  
        if(map != null && map.size() > 0) {  
            for(Map.Entry<String, Object> entry : map.entrySet()) {  
                String propertyName = entry.getKey();  
                Object value = entry.getValue();  
                String setMethodName = "set"  
                        + propertyName.substring(0, 1).toUpperCase()  
                        + propertyName.substring(1);  
                Field field = getClassField(clazz, propertyName);  
                Class<?> fieldTypeClass = field.getType();  
                value = convertValType(value, fieldTypeClass);  
                clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);  
            }  
        }  
        return obj;  
    }  
      
    /** 
     * 将Object类型的值，转换成bean对象属性里对应的类型值 
     *  
     * @param value Object对象值 
     * @param fieldTypeClass 属性的类型 
     * @return 转换后的值 
     */  
    private static Object convertValType(Object value, Class<?> fieldTypeClass) {  
        Object retVal = null;  
        if(Long.class.getName().equals(fieldTypeClass.getName())  
                || long.class.getName().equals(fieldTypeClass.getName())) {  
            retVal = Long.parseLong(value.toString());  
        } else if(Integer.class.getName().equals(fieldTypeClass.getName())  
                || int.class.getName().equals(fieldTypeClass.getName())) {  
            retVal = Integer.parseInt(value.toString());  
        } else if(Float.class.getName().equals(fieldTypeClass.getName())  
                || float.class.getName().equals(fieldTypeClass.getName())) {  
            retVal = Float.parseFloat(value.toString());  
        } else if(Double.class.getName().equals(fieldTypeClass.getName())  
                || double.class.getName().equals(fieldTypeClass.getName())) {  
            retVal = Double.parseDouble(value.toString());  
        } else {  
            retVal = value;  
        }  
        return retVal;  
    }   
  
    /** 
     * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类) 
     *  
     * @param clazz 指定的class 
     * @param fieldName 字段名称 
     * @return Field对象 
     */  
    private static Field getClassField(Class<?> clazz, String fieldName) {  
        if( Object.class.getName().equals(clazz.getName())) {  
            return null;  
        }  
        Field []declaredFields = clazz.getDeclaredFields();  
        for (Field field : declaredFields) {  
            if (field.getName().equals(fieldName)) {  
                return field;  
            }  
        }  
  
        Class<?> superClass = clazz.getSuperclass();  
        if(superClass != null) {// 简单的递归一下  
            return getClassField(superClass, fieldName);  
        }  
        return null;  
    }    
      
	
}
