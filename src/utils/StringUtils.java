package utils;

/**
 * 字符串工具类
 * 
 * @author Lyle
 *
 */
public class StringUtils {

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
	 * 测试方法
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
}
