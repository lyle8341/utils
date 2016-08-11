package utils;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseStreamUtils {

	
	/** 日志输出 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CloseStreamUtils.class);
	
	/**
	 * 关闭流的方法
	 * @param stream 需要关闭的流
	 * 增强for需要判断所遍历的值不是null，因为是null会报空指针
	 * 此处没有判断stream不为null，因为可变参数中的stream是一个数组
	 * 其本身不会是null，只是数组中的值可能是null，故不需要判断
	 */
	public static <T extends Closeable> void close(T... stream) {
		for (T temp : stream) {
			try {
				if (null != temp) {
					temp.close();
				}
			} catch (IOException e) {
				LOGGER.error("关闭流时发生错误{}",e.getMessage());
			}finally{
				temp = null;
			}
		}
	}
}