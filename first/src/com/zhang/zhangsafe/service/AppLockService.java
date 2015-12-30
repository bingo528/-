/**
 * 2015-10-8
 * @author:zhangbin
 */
package com.zhang.zhangsafe.service;

import java.util.List;

import com.zhang.zhangsafe.activity.EnterPsdActivity;
import com.zhang.zhangsafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class AppLockService extends Service {
	private boolean isWatch;
	private AppLockDao mAppLockDao;
	private List<String> mQueryAllPackageNameList;
	private String mSkipPackageName;
	private MyContentObserver mContentObserver;
	private InnerReceiver mInnerReceiver;
	@Override
	public void onCreate() {

		super.onCreate();
		mAppLockDao = AppLockDao.getInstance(this);
		isWatch = true;
		// 调用一个监测应用的服务
		watch();
		// 接收广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.SKIP");
		mInnerReceiver = new InnerReceiver();
		//注册广播接收者
		registerReceiver(mInnerReceiver, intentFilter);
		//内容观察者
		
		mContentObserver = new MyContentObserver(new Handler());
		getContentResolver().registerContentObserver(Uri.parse("content://applock/change"), true, mContentObserver);
	}
	class MyContentObserver extends ContentObserver{

		/**
		 * @param handler
		 */
		public MyContentObserver(Handler handler) {
			super(handler);
			
		}
		/**
		 *数据库发生变化的时候调用
		 */
		@Override
		public void onChange(boolean selfChange) {
			
			super.onChange(selfChange);
			new Thread(){
				public void run() {
					
					mQueryAllPackageNameList = mAppLockDao.queryAll();
				};
			}.start();
		}
		
	}
	/**
	 *广播接收者
	 */
	class InnerReceiver extends BroadcastReceiver {

		

		@Override
		public void onReceive(Context context, Intent intent) {
			//获得包名
			mSkipPackageName = intent.getStringExtra("packageName");
		}

	}

	/**
	 * 看门狗服务
	 */
	private void watch() {
		// 一直监测有可能是耗时操作,所以在子线程中
		new Thread() {

			public void run() {
				mQueryAllPackageNameList = mAppLockDao.queryAll();
				while (isWatch) {
					// 获得activity的管理者对象
					ActivityManager mAM = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
					// 获取正在运行的任务栈
					List<RunningTaskInfo> runningTaks = mAM.getRunningTasks(1);
					// 取出第一个栈
					RunningTaskInfo runningTaskInfo = runningTaks.get(0);
					// 获得顶部的activity对应应用的包名
					String packageName = runningTaskInfo.topActivity
							.getPackageName();
					// 如果一致
					if (mQueryAllPackageNameList.contains(packageName)) {
						if (!packageName.equals(mSkipPackageName)) {
							// 弹出拦截界面
							Intent intent = new Intent(getApplicationContext(),
									EnterPsdActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							// 把包名传递过去
							intent.putExtra("packageName", packageName);
							startActivity(intent);
						}
						
					}
					// 睡眠一下
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};

		}.start();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
		isWatch=false;
		//注销广播接收者
		if (mInnerReceiver!=null) {
			unregisterReceiver(mInnerReceiver);
		}
		//注销内容观察者
		if (mContentObserver!=null) {
			getContentResolver().unregisterContentObserver(mContentObserver);
		}
		//
	}
}
