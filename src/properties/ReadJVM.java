package properties;

import java.util.Properties;

/**
 * 获取JVM的系统属性
 * @author Lyle
 *
 */
public class ReadJVM {

	public static void main(String[] args) {
		Properties pps = System.getProperties();
		pps.list(System.out);
	}
}
