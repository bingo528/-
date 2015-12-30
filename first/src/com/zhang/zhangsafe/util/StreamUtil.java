package com.zhang.zhangsafe.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	
	/**
	 * @param is 流对象
	 * @return 流转换成的字符串	返回null代表异常
	 */
	public static String streamToString(InputStream is) {

		//1.读取内容存储到缓存中,一次性转化成字符串返回
		ByteArrayOutputStream bos =new ByteArrayOutputStream();
		int len=0;
		byte[] bys=new byte[1024];
		try {
			while ((len=is.read(bys))!=-1) {
			//2.读写操作
				bos.write(bys, 0, len);
			}
			return bos.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				bos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}

}
