package utils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientUtils {

	private static final Logger LOGGER = Logger.getLogger(HttpClientUtils.class);
	private static final Integer timeout = 60000;

	private HttpClientUtils() {
	}

	/**
	 * 发送Get请求
	 * @param requestUrl
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String sendGetRequest(String requestUrl)
			throws ClientProtocolException, IOException {
		String content = "";
		String url = URLDecoder.decode(requestUrl, "utf-8");
		HttpResponse response = Request.Get(url).connectTimeout(timeout)
				.socketTimeout(timeout).execute().returnResponse();
		content = decode(response);
		return content;
	}

	/**
	 * 发送Post请求
	 * @param requestUrl
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String sendPostRequest(String requestUrl,
			Map<String, String> params, Charset charset) throws ClientProtocolException, IOException {
		String content = "";
			Form form = Form.form();
			// 封装参数
			if (params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					form.add(entry.getKey(), entry.getValue());
				}
			}
			HttpResponse response = Request.Post(requestUrl)
					.connectTimeout(timeout).socketTimeout(timeout)
					.bodyForm(form.build(), charset).execute().returnResponse();
			content = decode(response);
			return content;
	}

	/**
	 * 发送Post请求（UTF-8）
	 * @param requestUrl
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String sendPostRequest(String requestUrl,
			Map<String, String> params) throws ClientProtocolException, IOException {
		return sendPostRequest(requestUrl, params, Consts.UTF_8);
	}

	/**
	 * 上传文件和参数
	 * @param requestUrl
	 * @param bin
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String sendMultipartPost(String requestUrl,
			Map<String, File> bin, Map<String, String> params, Charset charset)
			throws IOException {
		String content = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpPost httppost = new HttpPost(requestUrl);
			// 生成ContentType
			ContentType contentType = ContentType.create(
					ContentType.TEXT_PLAIN.getMimeType(), charset);
			// 以浏览器兼容模式运行，防止文件名乱码。
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
					.create().setCharset(charset)
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			// 封装附件
			if (bin != null) {
				for (Map.Entry<String, File> entry : bin.entrySet()) {
					String name = entry.getKey();
					File file = entry.getValue();
					multipartEntityBuilder.addBinaryBody(name, file);
				}
			}
			// 封装参数
			if (bin != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String name = entry.getKey();
					String value = entry.getValue();
					multipartEntityBuilder
							.addTextBody(name, value, contentType);
				}
			}
			HttpEntity reqEntity = multipartEntityBuilder.build();
			httppost.setEntity(reqEntity);
			LOGGER.info("executing request " + httppost.getRequestLine());
			response = httpclient.execute(httppost);
			LOGGER.info("----------------------------------------");
			LOGGER.info(response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					contentType = ContentType.getOrDefault(resEntity);
					Charset respCharset = contentType.getCharset();
					// 使用EntityUtils的toString方法，传递编码，默认编码是ISO-8859-1
					if (respCharset == null) {
						respCharset = Consts.UTF_8;
					}
					content = EntityUtils.toString(resEntity, respCharset);
					LOGGER.info("Response content length: "
							+ resEntity.getContentLength());
				}
				EntityUtils.consume(resEntity);
		} finally {
			CloseStreamUtils.close(response,httpclient);
		}
		return content;
	}

	/**
	 * 上传文件
	 * 
	 * @param requestUrl
	 * @param bin
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String sendMultipartPost(String requestUrl,
			Map<String, File> bin, Map<String, String> params)
			throws IOException {
		return sendMultipartPost(requestUrl, bin, params, Consts.UTF_8);
	}

	/**
	 * 上传文件
	 * 
	 * @param requestUrl
	 * @param bin
	 * @return
	 * @throws IOException
	 */
	public static String sendMultipartPost(String requestUrl,
			Map<String, File> bin) throws IOException {
		return sendMultipartPost(requestUrl, bin, null, Consts.UTF_8);
	}

	/**
	 * response 解码
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private static String decode(HttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			return "";
		}
		ContentType contentType = ContentType.getOrDefault(entity);
		Charset charset = contentType.getCharset();
		// 使用EntityUtils的toString方法，传递编码，默认编码是ISO-8859-1
		if (charset == null) {
			charset = Consts.UTF_8;
		}
		return EntityUtils.toString(entity, charset);
	}

}
