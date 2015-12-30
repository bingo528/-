/**
 * 2015-10-6
 * @author:zhangbin
 */
package com.zhang.zhangsafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.zhang.zhangsafe.domin.CommChild;
import com.zhang.zhangsafe.domin.CommGroup;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CommNumberDao {

	private static final String tag = "CommNumberDao";
	// 制定数据库路径
	static String path = "data/data/com.zhang.zhangsafe/files/commonnum.db";
	private static List<CommGroup> mGroupList;
	private static List<CommChild> mChildList;
	private static String queryByNumbername;

	/**
	 * @return 返回群组的集合
	 */
	public static List<CommGroup> queryGroup() {
		// 开启数据库连接
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// 查询
		Cursor cursor = db.query("classlist", new String[] { "name", "idx" },
				null, null, null, null, null);
		mGroupList = new ArrayList<CommGroup>();
		// 遍历
		while (cursor.moveToNext()) {
			// 利用集合封装
			CommGroup commGroup = new CommGroup();

			// 取出数据
			String name = cursor.getString(0);
			String idx = cursor.getString(1);
			commGroup.name = name;
			commGroup.idx = idx;
			commGroup.childList = queryChildByGroup(commGroup.idx);
			mGroupList.add(commGroup);
		}

		cursor.close();
		db.close();
		return mGroupList;
	}

	/**
	 * @return 返回群组的集合
	 */
	public static List<CommChild> queryChildByGroup(String idx) {
		// 开启数据库连接
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// 查询
		String sql = "select * from table" + idx + ";";
		Cursor cursor = db.rawQuery(sql, null);
		mChildList = new ArrayList<CommChild>();
		// 遍历
		while (cursor.moveToNext()) {
			// 利用集合封装
			CommChild commChild = new CommChild();

			// 取出数据
			commChild._id = cursor.getString(0);
			commChild.number = cursor.getString(1);
			commChild.name = cursor.getString(2);

			mChildList.add(commChild);
		}

		cursor.close();
		db.close();
		return mChildList;
	}

	/**
	 * 此方法待用,请注意
	 * @param number 传入的电话号码
	 * @param idx
	 * @return 返回姓名
	 */
	public static String queryByNumber(String number,String idx) {
		// 开启数据库连接
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READONLY);
		String sqlString="select name from table" + idx + "where number=?;";
		Cursor cursor = db.query("table"+idx, new String[]{"name"}, "number=?", new String[]{number}, null, null, null);
		if (cursor.moveToNext()) {
			queryByNumbername = cursor.getString(0);
		}
		db.close();
		cursor.close();
		return queryByNumbername;

	}
}
