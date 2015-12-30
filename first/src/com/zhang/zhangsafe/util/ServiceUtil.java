/**
 * 2015-9-28
 * @author:zhangbin
 */
package com.zhang.zhangsafe.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import com.zhang.zhangsafe.activity.SettingActivity;

public class ServiceUtil {

	
	/**
	 * @param mContext  上下文环境
	 * @param serviceNameg  服务名称
	 * @return true表示运行,false表示未运行
	 */
	public static boolean isRunning(Context mContext,String serviceNameg) {
		
		//获得activityManager管理者对象,因为可以获取当前手机所有正在运行的服务
		ActivityManager mAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		//获取手机正在运行的程序
		List<RunningServiceInfo> servicesList = mAM.getRunningServices(100);
		//遍历服务,比对成功说明服务正在运行
		for (RunningServiceInfo runningServiceInfo : servicesList) {
			//获取到服务名称
			String name = runningServiceInfo.service.getClassName();
		   //如果传递过来的服务名称与获得到的服务名称一致
			if (serviceNameg.equals(name)) {
				return true;
			}
		}
		return false;
	}



}
