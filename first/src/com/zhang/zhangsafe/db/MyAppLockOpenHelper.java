/**
 * 2015-10-7
 * @author:zhangbin
 */
package com.zhang.zhangsafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyAppLockOpenHelper extends SQLiteOpenHelper {

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public MyAppLockOpenHelper(Context context) {
		//数据库名称
		super(context, "applock.db", null, 1);
		
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//数据库语句
		
		String progmmSql="create table applock(_id integer primary key autoincrement , packagename varchar(50))";
		db.execSQL(progmmSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
