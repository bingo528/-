/**
 * 2015-10-6
 * @author:zhangbin
 */
package com.zhang.zhangsafe.receiver;

import com.zhang.zhangsafe.engine.ProcessInformation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LockScreenReceiver extends BroadcastReceiver {

	private static final String tag = "LockScreenReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		//测试数据
		//Log.d(tag, "锁屏广播接收者");
		//杀死进程
		ProcessInformation.killAll(context);
	}

}
