package utils;

/**
 * @ClassName: LineNo
 * @Description: 行号
 * @author: Lyle
 * @date: 2017年5月8日 下午1:58:08
 */
public class LineNo {

	private final static int STACKLEVEL = 1;

	public static String position() {
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		System.out.println(stacks);
		if (stacks.length < (STACKLEVEL + 1)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(Thread.currentThread().getName()).append(":" + stacks[STACKLEVEL].getFileName())
				.append(":" + stacks[STACKLEVEL].getClassName()).append(":" + stacks[STACKLEVEL].getMethodName() + "()")
				.append(":" + stacks[STACKLEVEL].getLineNumber()).append("]");
		return sb.toString();
	}
}
