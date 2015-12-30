/**
 * 2015-10-4
 * @author:zhangbin
 */
package com.zhang.zhangsafe.receiver;

import com.zhang.zhangsafe.location.listenter.MyLocationListenter;
import com.zhang.zhangsafe.location.listenter.MyPhoneStateListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InnerOutCallReceiver extends BroadcastReceiver {

	private static final String tag = "InnerOutCallReceiver";
	private Context mContext;

	/**
	 * @param applicationContext
	 */
	public InnerOutCallReceiver(Context mContext) {
		this.mContext=mContext;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String phone = getResultData();
		//测试数据
		//Log.d(tag, "播出的电话号码是:"+phone);
		MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener(mContext);
	   myPhoneStateListener.callShowshowToast(phone);
	}

}
