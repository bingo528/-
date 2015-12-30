/**
 * 2015-10-7
 * @author:zhangbin
 */
package com.zhang.zhangsafe.receiver;

import com.zhang.zhangsafe.service.UpdateWidgetService;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MyAppWidgetProvider extends AppWidgetProvider {

	private static final String tag = "MyAppWidgetProvider";

	@Override
	public void onReceive(Context context, Intent intent) {

		super.onReceive(context, intent);

	}

	@Override
	public void onEnabled(Context context) {

		super.onEnabled(context);
		Log.d(tag, "onEnabled方法的调用");
		// 开启服务
		context.startService(new Intent(context, UpdateWidgetService.class));
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d(tag, "onUpdate方法的调用");
		// 开启服务
		context.startService(new Intent(context, UpdateWidgetService.class));
	}

	@SuppressLint("NewApi")
	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {

		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
		Log.d(tag, "onAppWidgetOptionsChanged方法的调用");
		// 开启服务
		context.startService(new Intent(context, UpdateWidgetService.class));
	}

	/**
	 * 删除小部件的时候调用,只要删除就调用此方法
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		super.onDeleted(context, appWidgetIds);
		Log.d(tag, "onDeleted方法的调用");
	}

	/**
	 * 最后一个窗体不可见的时候调用
	 */
	@Override
	public void onDisabled(Context context) {

		super.onDisabled(context);
		Log.d(tag, "onDisabled方法的调用");
		//销毁服务
		context.stopService(new Intent(context, UpdateWidgetService.class));
	}
}
