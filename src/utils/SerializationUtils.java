package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtils {

	/**
	 * 项目根路径下生成
	 */
	private static final String FILE_NAME = "info.txt"; 
	
	//序列化
	public static void writeObject(Object t){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
			oos.writeObject(t);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T readObject(Class<T> clazz){
		T obj = null;
		try {
			ObjectInput input = new ObjectInputStream(new FileInputStream(FILE_NAME));
			obj = (T)input.readObject();
			input.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
