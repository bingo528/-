/**
 * 2015-9-23
 * @author:zhangbin
 */
package com.zhang.zhangsafe.location.listenter;

import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class MyLocationListenter implements LocationListener {

	

	private static final String tag = "MyLocationListenter";
	private Context mContext;

	/**
	 * @param mContext
	 */
	public MyLocationListenter(Context mContext) {
		this.mContext=mContext;
	}

	/**
	 *当位置发生改变的时候
	 */
	@Override
	public void onLocationChanged(Location location) {
		//获得纬度
		double latitude = location.getLatitude();
		//获得经度
		double longitude = location.getLongitude();
		//测试数据
		/*Log.d(tag, "纬度为:"+latitude);
		Log.d(tag, "经度是:"+longitude);*/
		//获得短信的管理者对象
		SmsManager smsManager = SmsManager.getDefault();
		String number = SpUtil.getString(mContext, ConstantValue.CONTACT_PHONE, "");
		//测试数据
		//Log.d(tag, "从xml中取出的电话号码"+number);
		if (number!=null) {
			//测试数据
			//Log.d(tag, number);
			smsManager.sendTextMessage(number, null, "longitude:"+longitude+",latitude:"+latitude, null, null);
		}
		
		
	}

	/**
	 *
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 *
	 */
	@Override
	public void onProviderEnabled(String provider) {
	}

	/**
	 *
	 */
	@Override
	public void onProviderDisabled(String provider) {
	}
}
