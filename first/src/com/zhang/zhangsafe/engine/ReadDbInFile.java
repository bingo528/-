/**
 * 2015-9-25
 * @author:zhangbin
 */
package com.zhang.zhangsafe.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.nfc.tech.IsoDep;
import android.os.Environment;

public class ReadDbInFile {
	private static FileOutputStream fos;
	private static InputStream is;

	/**
	 * @param mContext 上下文环境
	 * @param dbName  将要操作的数据库
	 */
	public static void readInFile(Context mContext, String dbName) {
	//创建一个文件夹,用来存储从资产目录读到的数据库
		//复制到sd卡中
		//String files = Environment.getExternalStorageDirectory().getPath();
		//复制到当前项目的私有目录下
		File files = mContext.getFilesDir();
		//指向当前的文件
		File file = new File(files,dbName);
		//如果文件存在
		if (file.exists()) {
			return;
		}
		 FileOutputStream fos=null;
		 InputStream is=null;
		//读取资产目录文件
		try {
			//获得输入流
			is = mContext.getAssets().open(dbName);
		   //获得输出流
			fos = new FileOutputStream(file);
			//读取操作
			int len=0;
			byte [] bys=new byte[1024];
			while ((len=is.read(bys))!=-1) {
				fos.write(bys, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//释放资源
		finally{
			try {
				if (is!=null && fos!=null) {
					
					fos.close();
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	
	}

	

	
}
