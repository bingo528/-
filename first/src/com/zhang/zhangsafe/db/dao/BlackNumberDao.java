/**
 * 2015-9-28
 * @author:zhangbin
 */
package com.zhang.zhangsafe.db.dao;

import java.util.ArrayList;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhang.zhangsafe.db.MyBlackNumberOpenHelper;
import com.zhang.zhangsafe.domin.BlackNumberBean;

public class BlackNumberDao {
	private static Context context;

	// 单例模式
	// 私有化构造方法
	/**
	 * 私有化构造方法
	 */
	private BlackNumberDao(Context context) {
		super();
		// 创建数据库

		myOpenHelper = new MyBlackNumberOpenHelper(context);
		this.context = context;
	}

	// 创建一个当前类的对象
	private static BlackNumberDao numberDao = null;
	private MyBlackNumberOpenHelper myOpenHelper;

	// 提供一个静态的方法
	/**
	 * 提供一个静态公共方法,用于外部使用调用
	 * 
	 * @return
	 */
	public static BlackNumberDao getInstance(Context context) {
		if (numberDao == null) {
			numberDao = new BlackNumberDao(context);
		}
		return numberDao;

	}

	/**
	 * @param phone
	 *            传递过来的手机号
	 * @param mode
	 *            传递过来的模式
	 */
	public void insert(String phone, String mode) {
		// 开启数据库
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		// 对数据库进行操作
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		db.insert("blacknumber", null, values);

		// 释放资源
		db.close();
	}

	/**
	 * 从数据库中删除一个数据
	 * 
	 * @param phone
	 */
	public void delete(String phone) {
		// 开启数据库
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		db.delete("blacknumber", "phone=?", new String[] { phone });
		db.close();
	}

	/**
	 * @param phone
	 *            传递过来的手机号
	 * @param mode
	 *            传递过来的模式(1:短信,2:电话,3:所有)
	 */
	public void update(String phone, String mode) {
		// 开启数据库
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		db.update("blacknumber", values, "phone=?", new String[] { phone });
		db.close();
	}

	/**
	 * @return 返回查询到的所有数据
	 */
	public ArrayList<BlackNumberBean> queryAll() {
		// 开启数据库
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("blacknumber",
				new String[] { "phone", "mode" }, null, null, null, null,
				"_id desc");
		ArrayList<BlackNumberBean> arrayList = new ArrayList<BlackNumberBean>();
		while (cursor.moveToNext()) {
			// 将得到的数据进行封装
			BlackNumberBean bean = new BlackNumberBean();
			bean.phone = cursor.getString(0);
			bean.mode = cursor.getString(1);
			arrayList.add(bean);
		}
		// 释放资源
		cursor.close();
		db.close();
		return arrayList;
	}

	/**
	 * @param index
	 *            根据索引值
	 * @return 根据查询返回数据
	 */
	public ArrayList<BlackNumberBean> queryByIndex(int index) {
		// 开启数据库
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		String sql = "select phone,mode from blacknumber order by _id desc limit ?,20;";
		Cursor cursor = db.rawQuery(sql, new String[] { index + "" });
		ArrayList<BlackNumberBean> arrayList = new ArrayList<BlackNumberBean>();
		while (cursor.moveToNext()) {
			// 将得到的数据进行封装
			BlackNumberBean bean = new BlackNumberBean();
			bean.phone = cursor.getString(0);
			bean.mode = cursor.getString(1);
			arrayList.add(bean);
		}
		// 释放资源
		cursor.close();
		db.close();
		return arrayList;
	}

	/**
	 * @return 总条目
	 */
	public int getCount() {// 开启数据库
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		String sql = "select count(*) from blacknumber;";
		int count = 0;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}

		cursor.close();
		db.close();
		return count;
	}

	/**
	 * @param originatingAddress  电话号码
	 *            
	 * @return  传递过来的模式(1:短信,2:电话,3:所有)
	 */
	public int getMode(String phone) {
		// 开启数据库
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		
		int mode = 0;
		Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "phone=?", new String[]{phone}, null, null, null);
		if (cursor.moveToNext()) {
			mode = cursor.getInt(0);
			
		}

		cursor.close();
		db.close();
		return mode;
	}
}
