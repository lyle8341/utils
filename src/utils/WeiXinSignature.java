package utils;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import constant.WXServerConfig;
import utils.wx.AesException;
import utils.wx.WXBizMsgCrypt;

/**
 * 微信验证
 * @author Lyle 参考http://blog.csdn.net/xiaojimanman/article/details/45042549
 */
public class WeiXinSignature {

	private WeiXinSignature() {
		throw new Error("不允许实例化！");
	}
	
	/**
	 * 服务器配置页面对应url所做的token验证
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean checkSignature(String signature, String timestamp,
			String nonce) throws NoSuchAlgorithmException {
		String[] arr = new String[] { WXServerConfig.TOKEN, timestamp, nonce };
		// 按字典排序
		Arrays.sort(arr);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
		}
		// 加密并返回验证结果
		return signature == null ? false : signature.equals(EncryptUtils
				.encrypt(sb.toString(), "SHA-1"));
	}

	/**
	 * 解密微信个人发给公众号的加密消息
	 * @param token
	 * @param encodingAESKey
	 * @param appid
	 * @param msg_signature
	 * @param timeStamp
	 * @param nonce
	 * @param postData 微信post过来的数据
	 * @return
	 * @throws AesException
	 */
	public static String decrypt(String msg_signature, String timestamp, String nonce,
			String postData) throws AesException {
		return WXBizMsgCrypt.getInstance().decryptMsg(msg_signature, timestamp, nonce, postData);
	}
	
	/**
	 * @param replyMsg 回复用户的消息，xml格式字符串
	 * @param timestamp 时间戳
	 * @param nonce 随机数
	 * @return 
	 * @throws AesException
	 */
	public static String encrypt(String replyMsg,String timestamp,String nonce) throws AesException{
		return WXBizMsgCrypt.getInstance().encryptMsg(replyMsg, timestamp, nonce);
	}
	
}
