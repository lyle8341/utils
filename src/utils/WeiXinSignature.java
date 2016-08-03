package utils;

import java.util.Arrays;

/**
 * 微信验证
 * @author Lyle
 * 参考http://blog.csdn.net/xiaojimanman/article/details/45042549
 */
public class WeiXinSignature {

	/** 服务器配置页面所传的token */
	public static final String TOKEN = "FUCK THE WORLD";

	/**
	 * 服务器配置页面对应url所做的token验证
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp,
			String nonce) {
		String[] arr = new String[] { TOKEN, timestamp, nonce };
		// 按字典排序
		Arrays.sort(arr);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
		}
		System.out.println(EncryptUtils.encrypt(sb.toString(), "SHA-1"));
		// 加密并返回验证结果
		return signature == null ? false : signature.equals(EncryptUtils
				.encrypt(sb.toString(), "SHA-1"));
	}

	public static void main(String[] args) {
		boolean result = checkSignature(
				"57d1e045687dc64c6ff6becfa46e1e91a5b99f16", "1470215142342",
				"474");
		System.out.println(result);
	}
}
