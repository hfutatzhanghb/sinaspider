package com.zhb.sinaspider.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author zhanghaobo
 * @date 2017年7月27日下午6:41:28
 * @todo TODO
 */
public class FileUtil {

	/**
	 * 生成指定路径文件的文件输出流
	 * @param pathname 文件路径
	 * @return  文件输出流
	 */
	public static FileOutputStream getOutputStream(String pathname) {
		FileOutputStream fos = null;
		File file = null;
		try {
			file = new File(pathname);
			return new FileOutputStream(file,true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("文件不存在！！！");
		}
		return fos;
	}

}
