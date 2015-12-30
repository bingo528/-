/**
 * 2015-9-28
 * @author:zhangbin
 */
package com.zhang.zhangsafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyBlackNumberOpenHelper extends SQLiteOpenHelper {

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public MyBlackNumberOpenHelper(Context context) {
		//数据库名称
		super(context, "blacknumber.db", null, 1);
		
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//数据库语句
		String sql="create table blacknumber(_id integer primary key autoincrement , phone varchar(20), mode varchar(5))";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
