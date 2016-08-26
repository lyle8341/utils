package utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

	/**
	 * 检查文件是否存在，如果不存在就创建一个
	 * @param path 文件路径
	 * @throws IOException
	 */
	public static void checkFileExistsIfNotCreate(String path) throws IOException{
		File file = new File(path);
		if(!file.exists()){
			file.createNewFile();
		}
	}
	
	/**
	 * 检查文件夹是否存在，如果不存在就创建一个
	 * @param path
	 */
	public static void checkFileDirectoryExistsIfNotCreate(String path){
		File file =new File(path);    
		//如果文件夹不存在则创建    
		if  (!file.exists()  && !file.isDirectory())      
		{       
		    file .mkdir();    
		}  
	}
}
