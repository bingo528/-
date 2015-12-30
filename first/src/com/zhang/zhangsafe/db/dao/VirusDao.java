/**
 * 2015-9-25
 * @author:zhangbin
 */
package com.zhang.zhangsafe.db.dao;


import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class VirusDao {

	private static final String tag = "VirusDao";
	//制定数据库路径
	static String path="data/data/com.zhang.zhangsafe/files/antivirus.db";
	
	/**
	 * @return 所有病毒数据库的中md5
	 */
	public static List<String> getVirusList()
	{
		
		//开启数据库连接
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.query("datable", new String[]{"md5"}, null, null, null, null, null);
		List<String> virusList = new ArrayList<String>();
		//循环
		while (cursor.moveToNext()) {
			
			virusList.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return virusList;
	}
}
