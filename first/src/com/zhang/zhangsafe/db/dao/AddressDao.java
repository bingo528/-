/**
 * 2015-9-25
 * @author:zhangbin
 */
package com.zhang.zhangsafe.db.dao;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddressDao {

	private static final String tag = "AddressDao";
	//制定数据库路径
	static String path="data/data/com.zhang.zhangsafe/files/address.db";
	//给一个默认值
	
	private static String mLocation="未知号码";
	/**
	 * @param phone 传入一个手机号
	 */
	public static String phone(String phone)
	{
		mLocation="未知号码";
		//开启数据库连接
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		//由于数据库中的电话号码只需要前7位就可以查询出来,需要对我们传过来的数据处理
		//由于没办法保证输入就是手机号,所以这里需要判断一下,使用正则表达式
		String regex="^1[3-8]\\d{9}";
		if (phone.matches(regex)) {
		
		phone = phone.substring(0, 7);
		//查询操作
		Cursor data1cursor = db.query("data1", new String[]{"outkey"}, "id=?", new String[]{phone}, null, null, null);
		if (data1cursor.moveToNext()) {
			//如果查询到数据,根据查询到的数据进行继续查询
			//取出查询到的值
			String data1outkey = data1cursor.getString(0);
			//测试数据
			//Log.d(tag, "数据库表1的outkey为:"+data1outkey);
			Cursor data2cursor = db.query("data2",new String[]{"location"}, "id=?", new String[]{data1outkey}, null, null, null);
			//查看是否有数据
			if (data2cursor.moveToNext()) {
				mLocation = data2cursor.getString(0);
				//测试数据,测试归属地
				//Log.d(tag, "电话的归属地是"+mLocation);
				
			 	}
			data2cursor.close();
			}
		//输入的手机号也匹配,但是数据库没有收录
			else {
				mLocation="未知号码";
			}
		data1cursor.close();
		}
		else {
			int length = phone.length();
			//如果不是手机号,需要判断一下
			switch (length) {
			case 3:
				mLocation="紧急联系电话";
				//测试数据
				//Log.d(tag, "电话的归属地是"+mLocation);
				break;
			/*case 4:
				
				mLocation="模拟器";
				break;*/
			case 5:
				
				mLocation="服务电话";
				break;
			case 7:
				
				mLocation="本地电话";
				break;
			case 8:
				
				mLocation="本地电话";
				break;
			case 10:
				if (phone.startsWith("0")) {
					String area = phone.substring(1, 3);
					//查询数据库
					Cursor cursor = db.query("data2", new String[]{"location"}, "area=?", new String[]{area}, null, null, null);
					if (cursor.moveToNext()) {
						mLocation = cursor.getString(0);
					}
					else {
						
						mLocation="未知号码";
					}
				}
			
				break;
			case 11:
			//电话分3+8和4+7两种
				mLocation="外地电话,稍后实现查询了";
				break;
			case 12:
				mLocation="外地电话,稍后实现查询了";
				break;
				

			default:
				break;
			}
		}
		 db.close();
		//Log.d(tag, "返回的值"+mLocation);
		return mLocation;
       

	}
}
