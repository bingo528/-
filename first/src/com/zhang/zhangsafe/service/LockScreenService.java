/**
 * 2015-10-6
 * @author:zhangbin
 */
package com.zhang.zhangsafe.service;

import com.zhang.zhangsafe.receiver.LockScreenReceiver;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockScreenService extends Service {
	private IntentFilter intentFilter;
	private LockScreenReceiver mLockScreenReceiver;
	@Override
	public void onCreate() {

		super.onCreate();
		// 锁屏action
		intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		mLockScreenReceiver = new LockScreenReceiver();
		registerReceiver(mLockScreenReceiver, intentFilter);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		if (mLockScreenReceiver!=null) {
			//注销
			unregisterReceiver(mLockScreenReceiver);
		}
	}
}
