package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SimpleDateFormat是线程不安全的。
 * @author Lyle 并发情况下
 */
public class DateFormatUtils {

	private DateFormatUtils() {
		throw new Error("不允许实例化！");
	}
	
	/** 日志输出 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DateFormatUtils.class);

	/** 锁对象 */
	private static final Object lock = new Object();

	/** 存放不同日期模板格式的SimpleDateFormat的map */
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	private static SimpleDateFormat getSimpleDateFormat(final String pattern) {
		ThreadLocal<SimpleDateFormat> tL = sdfMap.get(pattern);
		// 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
		if (null == tL) {
			synchronized (lock) {
				tL = sdfMap.get(pattern);
				if (null == tL) {
					// 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
					LOGGER.info("put new simpleDateFormat of pattern "
							+ pattern + " to map");
					/**
					 * 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接 new
					 * SimpleDateFormat
					 */
					tL = new ThreadLocal<SimpleDateFormat>() {
						@Override
						protected SimpleDateFormat initialValue() {
							LOGGER.info("thread: "
									+ Thread.currentThread()
									+ " init pattern: " + pattern);
							return new SimpleDateFormat(pattern);
						}
					};
					sdfMap.put(pattern, tL);
				}
			}
		}
		return tL.get();
	}

	/**
	 * 使用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,
	 * 这样每个线程只会有一个SimpleDateFormat
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		return getSimpleDateFormat(pattern).format(date);
	}

	public static Date parseDateString(String dateString, String pattern)
			throws ParseException {
		return getSimpleDateFormat(pattern).parse(dateString);
	}
}
