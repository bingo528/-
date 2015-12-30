/**
 * 2015-9-23
 * @author:zhangbin
 */
package com.zhang.zhangsafe.receiver;


import junit.framework.Test;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.activity.LockScreenActivity;
import com.zhang.zhangsafe.service.LocationService;
import com.zhang.zhangsafe.util.ToastUtil;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String tag = "SmsReceiver";
	private DevicePolicyManager mDPM;
	private ComponentName mWho;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 测试数据
		// Log.d(tag, "接收到短信,根据短信内容不同处理不同的业务逻辑");
		mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		// 遍历短信内容
		for (Object object : objects) {
			// 获得SmsMessage对象
			SmsMessage createFromPdu = SmsMessage
					.createFromPdu((byte[]) object);
			// 获得发送短信的电话号码和短信内容
			String address = createFromPdu.getOriginatingAddress();
			String body = createFromPdu.getMessageBody();
			if (body.contains("#*alarm*#")) {
				// 播放音乐
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);

				// 循环播放
				player.setLooping(true);
				player.start();
				// 终止广播继续下发
				abortBroadcast();
			}
			if (body.contains("#*location*#")) {
				// 进行定位然后发送坐标到安全手机上
				context.startService(new Intent(context, LocationService.class));
				// 终止广播继续下发
				abortBroadcast();
			}
			if (body.contains("#*wipedata*#")) {

				

				// 测试数据
				// Log.d(tag, "清除数据");
				mWho = new ComponentName(context, DeviceReceiver.class);
				if (mDPM.isAdminActive(mWho)) {
					mDPM.wipeData(0);
				}
				else {
					
					openAdmin(context);
				}
				
				abortBroadcast();

			}
			if (body.contains("#*lockscreen*#")) {
				// 测试数据
				// Log.d(tag, "屏幕锁屏");
				// 判断激活的状态
				mWho = new ComponentName(context, DeviceReceiver.class);
				if (mDPM.isAdminActive(mWho)) {
					mDPM.lockNow();
					//添加锁屏密码
					//设置密码为空
					mDPM.resetPassword("", 0);
					//设置密码123
					//mDPM.resetPassword("123", 0);
					
				}
				else {
					
					openAdmin(context);
				   //test(context);
					ToastUtil.show(context, "请先激活");
				}

				abortBroadcast();

			}
		}
	}

	/**
	 * @param context
	 */
	private void openAdmin(Context context) {
		Intent openAdmin = new Intent(context,LockScreenActivity.class);
		openAdmin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(openAdmin);
	}
	

	
}
