package utils;

/**
 * 字符串工具类
 * 
 * @author Lyle
 *
 */
public class StringUtils {

	private StringUtils() {
		throw new Error("不允许实例化！");
	}
	
	/**
	 * true表示不为空
	 * @param text 需要判断的参数
	 * @return
	 */
	public static boolean isNotEmpty(String... text) {
		for (String s : text) {
			if (null == s || s == "")
				return false;
		}
		return true;
	}
	
	/**
	 * 根据给定格式填充参数
	 * 第一个参数%1$s	
	 * 第二个参数%2$s	
	 * 第n个参数%n$s	
	 * @param format格式
	 * @param args填充参数，可变参数
	 * @return
	 */
	@SuppressWarnings("all")
	public static String format(String format,String... args){
		return String.format(format, args);
	}
	
}
