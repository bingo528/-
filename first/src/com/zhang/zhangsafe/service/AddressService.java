/**
 * 2015-9-28
 * @author:zhangbin
 */
package com.zhang.zhangsafe.service;

import com.zhang.zhangsafe.location.listenter.MyPhoneStateListener;
import com.zhang.zhangsafe.receiver.InnerOutCallReceiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class AddressService extends Service {

	private TelephonyManager mTM;
	private MyPhoneStateListener mPhoneStateListener;
	private InnerOutCallReceiver mInnerOutCallReceiver;
	@Override
	public void onCreate() {
		super.onCreate();
		// 监听电话的状态
		// 获得电话的管理者
		mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 创建一个MyPhoneStateListener对象
		mPhoneStateListener = new MyPhoneStateListener(getApplicationContext());
		mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		// 监听播出电话的广播过滤条件(权限)
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		// 创建广播接受者
		mInnerOutCallReceiver = new InnerOutCallReceiver(getApplicationContext());
		registerReceiver(mInnerOutCallReceiver, intentFilter);

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	/*
	 * @author zhangbin
	 * 
	 * @date 2015-9-28
	 */
	@Override
	public void onDestroy() {

		super.onDestroy();
		// 取消对电话的监听,不让其监听
		// 非空判断
		if (mTM != null && mPhoneStateListener != null) {

			mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
		//取消电话注册
		if (mInnerOutCallReceiver!=null) {
			unregisterReceiver(mInnerOutCallReceiver);
		}
	}
}
