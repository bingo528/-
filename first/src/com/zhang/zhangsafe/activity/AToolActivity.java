/**
 * 2015-9-25
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import java.io.File;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.engine.SmsOpertor;
import com.zhang.zhangsafe.engine.SmsOpertor.CallBack;
import com.zhang.zhangsafe.util.ToastUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AToolActivity extends Activity {
	private Context mContext;
	private ProgressBar pb_bar;
	private ProgressDialog pDialog;
	private static int mSmsMax=0;
	private static int mIndex=0;
	private Handler mHandler =new Handler()
	{
		public void handleMessage(Message msg) {
			if (mIndex<=mSmsMax) {
				
				
			}
		};
	};
	/**
	 * 初始化调用
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.ativity_atool);
		// 按照类别进行
		// 归属地查询
		initLocationSearch();
		// 短信备份
		initSmsBak();
		// 常用电话查询
		initCommSearch();
		// 程序锁
		initProgmLock();
		// 短信恢复
		initSmsRecover();

	}

	/**
	 * 归属地查询
	 */
	private void initLocationSearch() {
		// 找到控件
		TextView tv_atool_loca_search = (TextView) findViewById(R.id.tv_atool_loca_search);
		// 设置点击事件
		tv_atool_loca_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, QueryLocationActivity.class));
			}
		});
	}

	/**
	 * 短信备份
	 * 
	 */
	private void initSmsBak() {
		// 找到控件
		TextView tv_atool_smsbak = (TextView) findViewById(R.id.tv_atool_smsbak);
		// 设置点击事件
		tv_atool_smsbak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				showSmsDialog();
				
			}
		});
	}

	/**
	 * 短信备份的具体实现对话框
	 */
	protected void showSmsDialog() {
		pDialog = new ProgressDialog(this);
		//设置图标
		pDialog.setIcon(R.drawable.ic_launcher);
		//设置标题
		pDialog.setTitle("短信备份");
		//设置进度条的样式
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		pDialog.show();
		//获取短信,有可能是耗时操作,所以在子线程
		new Thread(){
			public void run() {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"sms_backup.xml";
			  
			SmsOpertor.backUp(mContext, path, new CallBack() {

				@Override
				public void setMax(int max) {
					//由开发者自己决定,使用对话框还是进度条
					pDialog.setMax(max);
					//pb_bar.setMax(max);
					mSmsMax = max;
					mHandler.sendEmptyMessage(0);
				}
				

				@Override
				public void setProgress(int index) {
					//由开发者自己决定,使用对话框还是进度条
					pDialog.setProgress(index);
					//pb_bar.setProgress(index);
					mIndex = index;
					mHandler.sendEmptyMessage(0);
					
				}	
			});	
			
			pDialog.dismiss();
			
			};
	
		}.start();
		ToastUtil.show(mContext, "正在备份短信,请不要终止备份");
	}

	/**
	 * 常用电话查询
	 */
	private void initCommSearch() {
		TextView tv_atool_common_phonesear = (TextView) findViewById(R.id.tv_atool_common_phonesear);
		tv_atool_common_phonesear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			startActivity(new Intent(getApplicationContext(), CommonNumberQueryActivity.class));
			}
		});
	}

	/**
	 * 程序锁
	 */
	private void initProgmLock() {
		TextView tv_atool_progm_lock = (TextView) findViewById(R.id.tv_atool_progm_lock);
		// 设置点击事件
		tv_atool_progm_lock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, AppLockActivity.class));
			}
		});
	}

	/**
	 * 短信恢复
	 */
	private void initSmsRecover() {
		TextView tv_atool_smsrecover = (TextView) findViewById(R.id.tv_atool_smsrecover);
		// 设置点击事件
		tv_atool_smsrecover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

}
