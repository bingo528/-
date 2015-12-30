/**
 * 2015-9-23
 * @author:zhangbin
 */
package com.zhang.zhangsafe.service;



import com.zhang.zhangsafe.location.listenter.MyLocationListenter;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

public class LocationService extends Service {
	public static final String tag = "LocationService";
	private Context mContext;
	private LocationManager lm;
	private MyLocationListenter mLocation;

	/**
	 * 第一次开启的调用
	 */
	@Override
	public void onCreate() {
		mContext = this;
		super.onCreate();
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		// 选择最优的方式返回经纬度
		Criteria criteria = new Criteria();
		// 准许话费网络定位
		criteria.setCostAllowed(true);
		// 获得GPS精确度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 选择最优
		String bestProvider = lm.getBestProvider(criteria, true);
		mLocation = new MyLocationListenter(mContext);
		lm.requestLocationUpdates(bestProvider, 0, 0, mLocation);

	}

	
	
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	/**
	 * 多次调用的时候
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 销毁的时候
	 */
	@Override
	public void onDestroy() {
//判断一下
		if (lm!=null) {
			lm=null;
		}
		super.onDestroy();
	}
}
