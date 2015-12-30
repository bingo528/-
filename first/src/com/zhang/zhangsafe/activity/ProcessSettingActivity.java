/**
 * 2015-10-6
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.service.LockScreenService;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.ServiceUtil;
import com.zhang.zhangsafe.util.SpUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ProcessSettingActivity extends Activity {

	private CheckBox cb_process_lock_clear;
	private CheckBox cb_process_setting;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_process_setting);
		// 进程管理设置
		initSystemShow();
		// 锁屏清理设置
		initLockClear();
	}


	/**
	 * 进程管理设置
	 */
	private void initSystemShow() {
		cb_process_setting = (CheckBox) findViewById(R.id.cb_process_setting);
		// 回显操作
		// 读取xml中的数据
		boolean showSystem = SpUtil.getBoolean(mContext,
				ConstantValue.SHOW_SYSTEM, false);
		// 把得到的结果设置给控件
		cb_process_setting.setChecked(showSystem);
		// 判断
		if (showSystem) {
			cb_process_setting.setText("显示系统进程");
		} else {
			cb_process_setting.setText("隐藏系统进程");
		}
		// 设置控件的选择状态的监听
		cb_process_setting
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 如果选中,设置文字内容
						if (isChecked) {
							cb_process_setting.setText("显示系统进程");

						} else {
							cb_process_setting.setText("隐藏系统进程");
						}
						SpUtil.putBoolean(mContext, ConstantValue.SHOW_SYSTEM,
								isChecked);
					}
				});
	}

	/**
	 * 锁屏清理设置
	 */
	private void initLockClear() {

		cb_process_lock_clear = (CheckBox) findViewById(R.id.cb_process_lock_clear);
		// 回显操作
		boolean running = ServiceUtil.isRunning(mContext,"com.zhang.zhangsafe.service.LockScreenService");
		// 把得到的结果设置给控件
		cb_process_lock_clear.setChecked(running);
		// 如果选中,设置文字内容
		if (running) {
			cb_process_lock_clear.setText("锁屏清理已开启");
		} else {
			cb_process_lock_clear.setText("锁屏清理已关闭");
		}
		// 设置控件的选择状态的监听
		cb_process_lock_clear
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 如果选中,设置文字内容
						if (isChecked) {
							cb_process_lock_clear.setText("锁屏清理已开启");
							// 开启服务
							startService(new Intent(mContext,
									LockScreenService.class));
						} else {
							cb_process_lock_clear.setText("锁屏清理已关闭");
							stopService(new Intent(mContext,
									LockScreenService.class));
						}
					}
				});
	}
}
