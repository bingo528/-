/**
 * 2015-9-20
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;


import com.zhang.zhangsafe.R;

import com.zhang.zhangsafe.service.AddressService;
import com.zhang.zhangsafe.service.AppLockService;
import com.zhang.zhangsafe.service.BlackNumberService;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.ServiceUtil;
import com.zhang.zhangsafe.util.SpUtil;
import com.zhang.zhangsafe.view.SettingClickView;
import com.zhang.zhangsafe.view.SettingItemView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity{
	private SettingItemView siv_set_update;
	private SettingItemView siv_set_address;
	
	private String [] mToastStyles;
	private Context mContext;
	private int mToast_style;
	private SettingClickView scv_toast_style;
	private SettingClickView scv_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//更新页面
		initUpdate();
		//归属地页面
		initAddress();
		//吐司的样式
		initToastStyle();
		//归属地
		initLocation();
		//黑名单
		initBlackNumber();
		//程序锁
		initAppLock();
		
	}

	


	/**
	 * 更新设置
	 */
	private void initUpdate() {
		siv_set_update = (SettingItemView) findViewById(R.id.siv_set_update);
	    //获取已有的开发状态
		boolean open_update = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, false);
		//是否选中,根据上一次存储的结果去做决定
		siv_set_update.setCheck(open_update);
		//设置点击事件
		siv_set_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取当前的选择状态
				boolean check = siv_set_update.isCheck();
				//取反
				siv_set_update.setCheck(!check);
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !check);
			}
		});
	}

	/**
	 * 是否显示号码归属地
	 */
	private void initAddress() {
		siv_set_address = (SettingItemView) findViewById(R.id.siv_set_address);
		//对服务是否开的状态做显示 ,类似于一个回显的操作
		boolean isRunning = ServiceUtil.isRunning(this, "com.zhang.zhangsafe.service.AddressService");
		siv_set_address.setCheck(isRunning);
		//设置点击事件
		siv_set_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取当前的选择状态
				boolean check = siv_set_address.isCheck();
				//取反
				siv_set_address.setCheck(!check);
				if (!check) {
					startService(new Intent(getApplicationContext(), AddressService.class));
				} 
				else {
					
					stopService(new Intent(getApplicationContext(), AddressService.class));
				}
			}
		});
		
	}
	/**
	 * 吐司样式的设置
	 */
	private void initToastStyle() {
		scv_toast_style = (SettingClickView) findViewById(R.id.scv_toast_style);
		scv_toast_style.setTitle("设置归属地显示风格");
		mToastStyles = new String[]{"透明","橙色","蓝色","灰色","蓝色"};
		mToast_style = SpUtil.getInt(mContext,ConstantValue.TOAST_STYLE,0);
	   //通过索引获得字符串中的值
		scv_toast_style.setDes(mToastStyles[mToast_style]);
		//监听点击事件
		scv_toast_style.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//显示吐司对话框
				showToastStyleDialog();
			}
		});
	}






	/**
	 * 显示吐司对话框
	 */
	protected void showToastStyleDialog() {
		Builder builder = new AlertDialog.Builder(this);
		//设置图案
		builder.setIcon(R.drawable.ic_launcher);
		//设置对话框的标题
		builder.setTitle("请选择归属地样式");
		builder.setSingleChoiceItems(mToastStyles, mToast_style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SpUtil.putInt(mContext, ConstantValue.TOAST_STYLE, which);
				scv_toast_style.setDes(mToastStyles[which]);
				//关闭对话框
				dialog.dismiss();
			}
		});
		//对话框上面的取消按钮
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				dialog.dismiss();
			}
		});
		
		
		builder.show();
	}

	/**
	 * 双击居中
	 */
	private void initLocation() {
		scv_location = (SettingClickView) findViewById(R.id.scv_location);
		scv_location.setTitle("归属地提示框的位置");
		scv_location.setDes("设置归属地提示框的位置");
		//设置点击事件
		scv_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//开启一个透明的activity
				startActivity(new Intent(mContext, ToastLocationActivity.class));
			}
		});
		
	}
	/**
	 * 黑名单拦截设置开关
	 */
	private void initBlackNumber() {
		final SettingItemView siv_set_blacknumber = (SettingItemView) findViewById(R.id.siv_set_blacknumber);
		//对服务是否开的状态做显示 ,类似于一个回显的操作
		boolean isRunning = ServiceUtil.isRunning(this, "com.zhang.zhangsafe.service.BlackNumberService");
		siv_set_blacknumber.setCheck(isRunning);
		//设置点击事件
		siv_set_blacknumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取当前的选择状态
				boolean check = siv_set_blacknumber.isCheck();
				//取反
				siv_set_blacknumber.setCheck(!check);
				if (!check) {
					startService(new Intent(getApplicationContext(), BlackNumberService.class));
				} 
				else {
					
					stopService(new Intent(getApplicationContext(), BlackNumberService.class));
				}
			}
		});
		
	}


	/**
	 * 程序锁设置
	 */
	private void initAppLock() {
		final SettingItemView siv_set_applock = (SettingItemView) findViewById(R.id.siv_set_applock);
		//对服务是否开的状态做显示 ,类似于一个回显的操作
		boolean isRunning = ServiceUtil.isRunning(this, "com.zhang.zhangsafe.service.AppLockService");
		
		siv_set_applock.setCheck(isRunning);
		//设置点击事件
		siv_set_applock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取当前的选择状态
				boolean check = siv_set_applock.isCheck();
				//取反
				siv_set_applock.setCheck(!check);
				if (!check) {
					startService(new Intent(getApplicationContext(), AppLockService.class));
				} 
				else {
					
					stopService(new Intent(getApplicationContext(), AppLockService.class));
				}
			}
		});
	}

}
