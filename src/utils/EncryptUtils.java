package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密工具类
 */
public class EncryptUtils {

	private EncryptUtils() {
		throw new Error("不允许实例化！");
	}
	
	/** 日志输出 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtils.class);
	
	/**
	 * @Description: 实现对字符串的加密 
	 * @param text 明文
	 * @param algorithm 算法 MD5 SHA-1 SHA-256
	 * @return
	 */
	public static String encrypt(String text,String algorithm) throws NoSuchAlgorithmException{
		String result = null;
		if(StringUtils.isNotEmpty(text,algorithm)){
			try {
				MessageDigest md = MessageDigest.getInstance(algorithm);
				byte[] bytes = md.digest(text.getBytes());
				result = bytesToStr(bytes);
			} finally {
				LOGGER.error("加密算法{}不存在，请核对!",algorithm);
			}
		}
		return result;
	}

	/**
	 * 字节数组转换为十六进制字符串
	 * @param bytes
	 * @return
	 */
	private static String bytesToStr(byte[] bytes) {
		String result;
		StringBuffer sb = new StringBuffer();
		for(byte b:bytes){
			  int bt = b&0xff;  
		        if (bt < 16){  
		            sb.append(0);  
		        }   
		        sb.append(Integer.toHexString(bt));  
		}
		result = sb.toString();
		return result;
	}
}
