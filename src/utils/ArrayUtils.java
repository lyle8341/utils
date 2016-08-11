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
			for(int i=0;i<obj.length;i++){
				System.out.print(obj[i]);
				if(i != obj.length-1){
					System.out.print(" 卐   ");
				}
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
			for(int i=0;i<list.size();i++){
				System.out.print(list.get(i));
				if(i != list.size()-1){
					System.out.print(" 卐  ");
				}
			}
			System.out.println();
		}
	}
}
