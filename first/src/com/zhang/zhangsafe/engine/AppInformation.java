/**
 * 2015-10-4
 * @author:zhangbin
 */
package com.zhang.zhangsafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.zhang.zhangsafe.domin.AppBean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppInformation {
	
	/**
	 * @param mContext 上下文环境
	 * @return 返回所有每一个应用的信息
	 */
	public static ArrayList<AppBean> getAppList(Context mContext) {
		//创建一个集合
		ArrayList<AppBean> appInfoList = new ArrayList<AppBean>();
		// 获取包的管理者
		PackageManager packageManager = mContext.getPackageManager();
		// 获得所有应用
		List<PackageInfo> list = packageManager.getInstalledPackages(0);
		// 循环遍历
		for (PackageInfo packageInfo : list) {
			AppBean appBean = new AppBean();
			// 获得包名
			String packageName = packageInfo.packageName;
			appBean.packageName = packageName;
			// 得到应用的所有信息就是得到application节点
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			// 获得应用名字
			String name = applicationInfo.loadLabel(packageManager).toString();
			appBean.name = name;
			// 获得图标
			Drawable icon = applicationInfo.loadIcon(packageManager);
			appBean.icon = icon;
			// 判断是否是系统应用
			if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
				appBean.isSystem = true;
			}
			// 否则就是用户应用
			else {
				appBean.isSystem = false;

			}
			//判断存储在sd卡
			if ((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
				appBean.isSd=true;
			}
			else {
				appBean.isSd=false;
			}
			appInfoList.add(appBean);
		}
		return appInfoList;
	}

}
