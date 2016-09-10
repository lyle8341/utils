package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 序列化方式拷贝
 * @author Lyle
 * 也可以用apache commons工具包中的SerializationUtils类
 *
 */
public class CloneUtils {

	/** 日志输出 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CloneUtils.class);
	
	private CloneUtils() {
		throw new Error("不允许实例化！");
	}
	
	/**
	 * 
	 * @param obj 必须实现Serializable接口
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T obj) throws Exception{
		T clonedOjb = null;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		//读取对象字节数据
			try {
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(obj);
				//分配内存空间，写入原始对象，生成新对象
				bais = new ByteArrayInputStream(baos.toByteArray());
				ois = new ObjectInputStream(bais);
				clonedOjb = (T)ois.readObject();
			} catch (ClassNotFoundException | IOException e) {
				LOGGER.error(e.toString());
				//TODO 此处改为自定义异常
				throw new Exception(e.getMessage());
			}finally{
				CloseStreamUtils.close(ois,oos);
			}
		return clonedOjb;
	} 
}
