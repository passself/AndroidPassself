package com.ryg.slideview.data;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {
	
	private static DataUtil instance;
	
	private DataUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static DataUtil getInstance(){
		if(instance == null){
			instance = new DataUtil();
		}
		return instance;
	}
	
	/**
	 * @param index 循环次数
	 * @return
	 */
	public List<String> getList(int index){
		List<String> list = new ArrayList<String>();
		for(int i = 0;i<index;i++){
			list.add("测试数据"+i);
		}
		return list;
	}
}
