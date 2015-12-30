/**
 * 2015-10-7
 * @author:zhangbin
 */
package com.zhang.zhangsafe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.engine.ProcessInformation;
import com.zhang.zhangsafe.receiver.MyAppWidgetProvider;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	private static final int REQUEST_CODE = 0;

	@Override
	public void onCreate() {

		super.onCreate();
		//定时更新小部件上面的信息,如进程总数和可用内存
		startTimer();
	}

	/**
	 * 开启一个定时器
	 */
	private void startTimer() {
		Timer timer=new Timer();
		//timertask是一个抽象类
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				//更新小窗体控件
				updateAppWidget();
			}
		}, 0, 1000);
	}

	/**
	 * 更新小窗体控件
	 */
	protected void updateAppWidget() {
		//获得窗体小部件的管理者对象
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		//根据包名确定要操作的是哪一个窗体小部件,哪一个包名的东西
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.process_widget);
		//通过view给控件赋值
		remoteViews.setTextViewText(R.id.tv_process_count, "正在运行的进程总数:"+ProcessInformation.getCount(this));
		//可用内存
		String avaliSize = Formatter.formatFileSize(this, ProcessInformation.getAvaliSpace(this));
		remoteViews.setTextViewText(R.id.tv_process_memory, "可用内存大小:"+avaliSize);
		//点击窗体进入应用的主页面,服务不能直接开启activity
		Intent intent = new Intent("android.intent.action.HomeActivity");
		intent.addCategory("android.intent.category.DEFAULT");
		PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.ll_root, pendingIntent);
		//一键清理的点击事件,通过发送广播
		Intent broadIntent = new Intent("android.intent.action.KillProcessReceiver");
		PendingIntent pendingBroadIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.btn_clear, pendingBroadIntent);
		ComponentName componentName=new ComponentName(this, MyAppWidgetProvider.class);
		appWidgetManager.updateAppWidget(componentName, remoteViews);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}
}
