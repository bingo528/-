/**
 * 2015-9-23
 * @author:zhangbin
 */
package com.zhang.zhangsafe.receiver;

import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	
	private static final String tag = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		//一旦接听广播,就发送短信
		//测试数据
		//Log.d(tag, "关机之后是否能够发送广播");
		//获得本地的sim信息
		//1.先获得管理者对象
		TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    //获得本地sim卡信息
		String serialNumber = tManager.getSimSerialNumber();
		//2.从xml中获取数据
		String xmlNumber = SpUtil.getString(context, ConstantValue.SIM_NUMBER, "");
	   //3.判断
		if (!serialNumber.equals(xmlNumber)) {
			//4.获得发短信的管理者
			SmsManager smsManager = SmsManager.getDefault();
			//5.从xml中取出电话号码
			String number = SpUtil.getString(context, ConstantValue.CONTACT_PHONE, "");
			//测试数据
			//Log.d(tag, "从xml中取出的电话号码"+number);
			if (number!=null) {
				
				smsManager.sendTextMessage(number, null, "电话卡发生change", null, null);
			}
		}
	}
}
