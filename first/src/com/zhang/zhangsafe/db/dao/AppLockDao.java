/**
 * 2015-10-7
 * @author:zhangbin
 */
package com.zhang.zhangsafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.zhang.zhangsafe.db.MyAppLockOpenHelper;
import com.zhang.zhangsafe.db.MyAppLockOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AppLockDao {

	private static Context context;
	// 创建一个当前类的对象
	private static AppLockDao appLockDao = null;
	private MyAppLockOpenHelper myOpenHelper;

	// 单例模式
	// 私有化构造方法
	/**
	 * 私有化构造方法
	 */
	private AppLockDao(Context context) {
		super();
		// 创建数据库
		myOpenHelper = new MyAppLockOpenHelper(context);
		this.context = context;
	}

	// 提供一个静态的方法
	/**
	 * 提供一个静态公共方法,用于外部使用调用
	 * 
	 * @return
	 */
	public static AppLockDao getInstance(Context context) {
		if (appLockDao == null) {
			appLockDao = new AppLockDao(context);
		}
		return appLockDao;

	}

	/**
	 * 添加数据
	 * 
	 * @param packagename
	 *            包名
	 */
	public void insert(String packagename) {
		// 开启数据库
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		// 对数据库进行操作
		ContentValues values = new ContentValues();
		values.put("packagename", packagename);

		db.insert("applock", null, values);

		// 释放资源
		db.close();
		// 内容观察者,当数据发生改变的时候使用
		context.getContentResolver().notifyChange(
				Uri.parse("content://applock/change"), null);
	}

	/**
	 * 按照包名从数据库中删除一个数据
	 * 
	 * @param phone
	 */
	public void delete(String packagename) {
		// 开启数据库
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		db.delete("applock", "packagename=?", new String[] { packagename });
		db.close();
		// 内容观察者,当数据发生改变的时候使用
		context.getContentResolver().notifyChange(
				Uri.parse("content://applock/change"), null);
	}

	/**
	 * @return 返回包名的集合
	 */
	public List<String> queryAll() {

		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("applock", new String[] { "packagename" },
				null, null, null, null, null);
		List<String> lockPackageList = new ArrayList<String>();
		while (cursor.moveToNext()) {
			lockPackageList.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return lockPackageList;

	}

}
