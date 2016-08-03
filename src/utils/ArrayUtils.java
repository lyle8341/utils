package utils;

import java.util.List;

public class ArrayUtils {

	/**
	 * 控制台输出数组所有元素
	 * @param obj
	 */
	public static <T> void printArry(T[] obj){
		//需要判断数组是否不为null
		if(null != obj){
			for(T t:obj){
				System.out.print(t + "  ");
			}
			System.out.println();
		}
	}
	
	/**
	 * 控制台输出List的所有元素
	 * @param list
	 */
	public static <T> void printList(List<T> list){
		if(null != list){
			for(T t:list){
				System.out.println(t);
			}
		}
	}
}
