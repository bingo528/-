/**
 * 2015-10-7
 * @author:zhangbin
 */
package com.zhang.zhangsafe.receiver;

import com.zhang.zhangsafe.engine.ProcessInformation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ProcessInformation.killAll(context);
	}

}
