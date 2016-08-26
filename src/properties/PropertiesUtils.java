package properties;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import utils.FileUtils;

public class PropertiesUtils {

	/**
	 * 获取属性文件.properties的内容
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Properties readPropertiesFileObj(String path)
			throws IOException {
		Properties properties = new OrderedProperties();
		InputStream inputStream = new FileInputStream(path);
		BufferedReader bf = new BufferedReader(new InputStreamReader(
				inputStream, "utf-8"));
		properties.load(bf);
		inputStream.close(); // 关闭流
		return properties;
	}

	/**
	 * 写入key-value
	 * @param path
	 * @param properties
	 * @throws IOException
	 */
	public static void writePropertiesFileObj(String path, Properties properties) throws IOException {
		//如果文件不存在就创建一个
		FileUtils.checkFileExistsIfNotCreate(path);
		if (properties == null) {
			properties = new OrderedProperties();
		}
		//--------------读取之前文件内容，为了追加内容----------------
		InputStream in = new FileInputStream(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));//解决乱码
		properties.load(reader);
		//----------------------------------------------------
		OutputStream outputStream = new FileOutputStream(path);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));//解决乱码
        properties.store(bw, null);
        outputStream.close();
	}
	
	/**
	 * 写入key-value
	 * @param path
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public static void writePropertiesFileObj(String path, String key,String value) throws IOException{
		Properties pro = new OrderedProperties();
		pro.setProperty(key, value);
		writePropertiesFileObj(path,pro);
	}

}
