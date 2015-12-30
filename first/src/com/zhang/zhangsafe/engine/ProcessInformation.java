/**
 * 2015-10-5
 * @author:zhangbin
 */
package com.zhang.zhangsafe.engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.domin.AppBean;
import com.zhang.zhangsafe.domin.ProcessBean;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

public class ProcessInformation {

	

	// 获得进程总数
	/**
	 * @param mContext
	 *            上下文环境
	 * @return 进行总数
	 */
	public static int getCount(Context mContext) {
		// 获得activitymanager
		ActivityManager aManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获得正在运行的进行集合
		List<RunningAppProcessInfo> runningAppProcesses = aManager
				.getRunningAppProcesses();
		// 返回集合总数
		return runningAppProcesses.size();
	}

	/**
	 * @param mContext
	 * @return 返回运行可用内存ram数 bytes
	 */
	public static long getAvaliSpace(Context mContext) {
		// 获得activitymanager
		ActivityManager aManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 构建存储可用运行内存ram对象
		MemoryInfo memoryInfo = new MemoryInfo();
		// 赋值
		aManager.getMemoryInfo(memoryInfo);
		// 获得可用运行内存
		
		return memoryInfo.availMem;
	}

	/**
	 * @param mContext
	 * @return 返回总内存数 bytes
	 */
	public static long getTotalSpace(Context mContext) {
		BufferedReader br = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader("proc/meminfo");
			// 使用高效
			br = new BufferedReader(fileReader);
			String readLine = br.readLine();
			char[] charArray = readLine.toCharArray();
			StringBuffer sb = new StringBuffer();
			// 将字符串转化成字符
			
			// 遍历字符数组
			for (char c : charArray) {
				if (c >= '0' && c <= '9') {
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString()) * 1024;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileReader != null && br != null) {
				try {
					fileReader.close();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return 0;
	}

	/**
	 * @param mContext
	 * @return 返回当前正在运行当前的进程
	 */
	public static ArrayList<ProcessBean> getProcessInfo(Context mContext) {
		// 创建一个集合
		ArrayList<ProcessBean> processInfoList = new ArrayList<ProcessBean>();

		// 获得正在运行的
		// 1,activityManager管理者对象和PackageManager管理者对象
		ActivityManager am = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = mContext.getPackageManager();
		// 2,获取正在运行的进程的集合
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		// 循环遍历
		for (RunningAppProcessInfo info : runningAppProcesses) {
			// 获取进程的名称
			ProcessBean processBean = new ProcessBean();
			processBean.packageName = info.processName;
			// 获得进程的占用内存大小
			android.os.Debug.MemoryInfo[] processMemoryInfo = am
					.getProcessMemoryInfo(new int[] { info.pid });
			android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
			// 获得已经使用大小
			processBean.memSize = memoryInfo.getTotalPrivateDirty() * 1024;
			// 获取应用名称
			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(
						processBean.packageName, 0);
				processBean.icon = applicationInfo.loadIcon(pm);
				processBean.name = applicationInfo.loadLabel(pm).toString();
				// 判断是否是系统应用
				if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
					processBean.isSystem = true;
				} else {
					processBean.isSystem = false;
				}
			} catch (NameNotFoundException e) {
				// 如果没有application节点
				processBean.name = info.processName;
				processBean.icon = mContext.getResources().getDrawable(
						R.drawable.ic_launcher);
				processBean.isSystem = true;
				e.printStackTrace();
			}
			processInfoList.add(processBean);
		}
		return processInfoList;
	}

	/**
	 * @param mContext
	 *            上下文环境
	 * @param processInfo
	 *            杀死进程所在的javabean的对象
	 */
	public static void killProcess(Context mContext, ProcessBean processInfo) {
		// 获取activitymanager对象
		ActivityManager am = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 杀死指定的包名
		am.killBackgroundProcesses(processInfo.packageName);
	}

	/**
	 * @param context
	 *            上下文环境
	 */
	public static void killAll(Context context) {
		// 获得activitymanager
		ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// 获得正在运行的进行集合
		List<RunningAppProcessInfo> runningAppProcesses = aManager.getRunningAppProcesses();
		//循环遍历集合
		for (RunningAppProcessInfo info : runningAppProcesses) {
			//如果是自己,不能自杀
			if (info.processName.equals(context.getPackageName())) {
				continue;
			}
			// 杀死指定的包名
			aManager.killBackgroundProcesses(info.processName);
		}
	}
}
