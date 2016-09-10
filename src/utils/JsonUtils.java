package utils;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {

	/**
	 * 格式化输出json数据<br>
	 <pre>
{
  "sex" : "男",
  "name" : "lyle",
  "age" : "20"
}
	 </pre>
	 * @param obj
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String format(Object obj) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		return mapper.writeValueAsString(obj);
	}
	
	/**
	 * 格式化输出json数据<br>
	 *<pre>
{
	"sex":"男",
	"name":"lyle",
	"age":"20"
}
	 *</pre>
	 * @param obj
	 * @return
	 */
	public static String format1(Object obj){
		return JSON.toJSONString(obj, true);
	}
	
	/**
	 * 格式化输出json，单引号
	 * <pre>{'sex':'男','name':'lyle','age':'20'}</pre>
	 * @param obj
	 * @return
	 */
	public static String format2(Object obj){
		return JSON.toJSONString(obj, SerializerFeature.UseSingleQuotes);
		
	}
	
	/**
	 * "2016-09-10 17:49:08"
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		return JSON.toJSONString(date,SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**
	 * 由于序列化带了类型信息，使得反序列化时能够自动进行类型识别
	 * @param obj
	 * @return
	 */
	public static String formatWithClassInfo(Object obj){
		return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
	}
	
	
	
}
