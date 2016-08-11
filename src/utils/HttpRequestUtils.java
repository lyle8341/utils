package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author http://blog.csdn.net/nupt123456789/article/details/42721003
 */
public class HttpRequestUtils {

	/** 日志输出 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HttpRequestUtils.class);

	/**
	 * 最大线程池
	 */
	public static final int THREAD_POOL_SIZE = 5;

	public interface HttpClientDownLoadProgress {
		public void onProgress(int progress);
	}

	private static HttpRequestUtils httpRequestUtils;

	private ExecutorService downloadExcutorService;

	private HttpRequestUtils() {
		this.downloadExcutorService = Executors
				.newFixedThreadPool(THREAD_POOL_SIZE);
	}

	public static HttpRequestUtils getInstance() {
		if (httpRequestUtils == null) {
			httpRequestUtils = new HttpRequestUtils();
		}
		return httpRequestUtils;
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param filePath
	 */
	public void download(final String url, final String filePath){
		downloadExcutorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					httpDownloadFile(url, filePath, null, null);
				} catch (IOException e) {
					LOGGER.error("下载文件出错！{}",e.getMessage());
				}
			}
		});
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param filePath
	 * @param progress
	 *            进度回调
	 */
	public void download(final String url, final String filePath,
			final HttpClientDownLoadProgress progress) {
		downloadExcutorService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					httpDownloadFile(url, filePath, progress, null);
				} catch (IOException e) {
					LOGGER.error("下载文件出错！{}",e.getMessage());
				}
			}
		});
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param filePath
	 * @throws IOException
	 */
	private void httpDownloadFile(String url, String filePath,
			HttpClientDownLoadProgress progress, Map<String, String> headMap)
			throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpEntity httpEntity = null;
		FileOutputStream fos = null;
		ByteArrayOutputStream output = null;
		CloseableHttpResponse response1 = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			setGetHead(httpGet, headMap);
			response1 = httpclient.execute(httpGet);
			//System.out.println(response1.getStatusLine());
			httpEntity = response1.getEntity();
			long contentLength = httpEntity.getContentLength();
			InputStream is = httpEntity.getContent();
			// 根据InputStream 下载文件
			output = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int r = 0;
			long totalRead = 0;
			while ((r = is.read(buffer)) > 0) {
				output.write(buffer, 0, r);
				totalRead += r;
				if (progress != null) {// 回调进度
					progress.onProgress((int) (totalRead * 100 / contentLength));
				}
			}
			fos = new FileOutputStream(filePath);
			output.writeTo(fos);
			output.flush();
			EntityUtils.consume(httpEntity);
		} finally {
			CloseStreamUtils.close(fos,output,response1,httpclient);
		}
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception 
	 */
	public String httpGet(String url) throws Exception {
		return httpGet(url, null);
	}

	/**
	 * http get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception 
	 */
	public String httpGet(String url, Map<String, String> headMap) throws Exception {
		String responseContent = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response1 = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			response1 = httpclient.execute(httpGet);
			setGetHead(httpGet, headMap);
				HttpEntity entity = response1.getEntity();
				responseContent = getRespString(entity);
				EntityUtils.consume(entity);
			} finally {
			CloseStreamUtils.close(response1,httpclient);
		}
		return responseContent;
	}

	public String httpPost(String url, Map<String, String> paramsMap) throws Exception {
		return httpPost(url, paramsMap, null);
	}

	/**
	 * http的post请求
	 * 
	 * @param url
	 * @param paramsMap
	 * @return
	 * @throws Exception 
	 */
	public String httpPost(String url, Map<String, String> paramsMap,
			Map<String, String> headMap) throws Exception {
		String responseContent = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			setPostHead(httpPost, headMap);
			setPostParams(httpPost, paramsMap);
			response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				responseContent = getRespString(entity);
				EntityUtils.consume(entity);
			}finally {
			CloseStreamUtils.close(response,httpclient);
		}
		return responseContent;
	}

	/**
	 * 设置POST的参数
	 * @param httpPost
	 * @param paramsMap
	 * @throws Exception
	 */
	private void setPostParams(HttpPost httpPost, Map<String, String> paramsMap)
			throws Exception {
		if (paramsMap != null && paramsMap.size() > 0) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<String> keySet = paramsMap.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		}
	}

	/**
	 * 设置http的HEAD
	 * @param httpPost
	 * @param headMap
	 */
	private void setPostHead(HttpPost httpPost, Map<String, String> headMap) {
		if (headMap != null && headMap.size() > 0) {
			Set<String> keySet = headMap.keySet();
			for (String key : keySet) {
				httpPost.addHeader(key, headMap.get(key));
			}
		}
	}

	/**
	 * 设置http的HEAD
	 * @param httpGet
	 * @param headMap
	 */
	private void setGetHead(HttpGet httpGet, Map<String, String> headMap) {
		if (headMap != null && headMap.size() > 0) {
			Set<String> keySet = headMap.keySet();
			for (String key : keySet) {
				httpGet.addHeader(key, headMap.get(key));
			}
		}
	}

	/**
	 * 上传文件
	 * @param serverUrl 服务器地址
	 * @param localFilePath 本地文件路径
	 * @param serverFieldName
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String uploadFileImpl(String serverUrl, String localFilePath,
			String serverFieldName, Map<String, String> params)
			throws Exception {
		String respStr = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpPost httppost = new HttpPost(serverUrl);
			FileBody binFileBody = new FileBody(new File(localFilePath));
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
					.create();
			// add the file params
			multipartEntityBuilder.addPart(serverFieldName, binFileBody);
			// 设置上传的其他参数
			setUploadParams(multipartEntityBuilder, params);

			HttpEntity reqEntity = multipartEntityBuilder.build();
			httppost.setEntity(reqEntity);

			response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();
				respStr = getRespString(resEntity);
				EntityUtils.consume(resEntity);
			}finally {
				CloseStreamUtils.close(response,httpclient);
		}
		return respStr;
	}

	/**
	 * 设置上传文件时所附带的其他参数
	 * @param multipartEntityBuilder
	 * @param params
	 */
	private void setUploadParams(MultipartEntityBuilder multipartEntityBuilder,
			Map<String, String> params) {
		if (params != null && params.size() > 0) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				multipartEntityBuilder
						.addPart(key, new StringBody(params.get(key),
								ContentType.TEXT_PLAIN));
			}
		}
	}

	/**
	 * 将返回结果转化为String
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	private String getRespString(HttpEntity entity) throws Exception {
		if (entity == null) {
			return null;
		}
		InputStream is = entity.getContent();
		StringBuffer strBuf = new StringBuffer();
		byte[] buffer = new byte[4096];
		int r = 0;
		while ((r = is.read(buffer)) > 0) {
			strBuf.append(new String(buffer, 0, r, "UTF-8"));
		}
		return strBuf.toString();
	}
}
