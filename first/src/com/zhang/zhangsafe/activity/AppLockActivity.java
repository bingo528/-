/**
 * 2015-10-6
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import java.util.ArrayList;
import java.util.List;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.adapter.MyAppLockAdater;
import com.zhang.zhangsafe.db.dao.AppLockDao;
import com.zhang.zhangsafe.domin.AppBean;
import com.zhang.zhangsafe.engine.AppInformation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AppLockActivity extends Activity {
	protected static final String tag = "AppLockActivity";
	private Button bt_applock_unlock;
	private Button bt_applock_lock;
	private LinearLayout ll_applock_unlock;
	private LinearLayout ll_applock_lock;
	private TextView tv_applock_unlock;
	private TextView tv_applock_lock;
	private ListView lv_applock_unlock;
	private ListView lv_applock_lock;
	private Context mContext;
	private List<AppBean> mAppList;
	private List<AppBean> mLockList,mUnLockList;
	private MyAppLockAdater mLockAdater,mUnLockAdater;
	private AppLockDao mDao;
	private Handler mHandler=new Handler()
	{
		

		public void handleMessage(android.os.Message msg) {
			mAppList=(List<AppBean>) msg.obj;
			//设置数据适配器
			//加锁适配器
			
			if (mLockAdater==null) {
				mLockAdater = new MyAppLockAdater(mContext,true,mAppList,mLockList,mUnLockList,tv_applock_lock,tv_applock_unlock,mDao);
				lv_applock_lock.setAdapter(mLockAdater);
			}
			else {
				mLockAdater.notifyDataSetChanged();
				
			}
			//未加锁适配器
			
			if (mUnLockAdater==null) {
				mUnLockAdater = new MyAppLockAdater(mContext,false,mAppList,mLockList,mUnLockList,tv_applock_lock,tv_applock_unlock,mDao);
				lv_applock_unlock.setAdapter(mUnLockAdater);
			}
			else {
				
				mUnLockAdater.notifyDataSetChanged();
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_app_lock);
		// 初始化UI
		initUI();
		// 初始化数据
		initData();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		bt_applock_unlock = (Button) findViewById(R.id.bt_applock_unlock);
		bt_applock_lock = (Button) findViewById(R.id.bt_applock_lock);
		ll_applock_unlock = (LinearLayout) findViewById(R.id.ll_applock_unlock);
		ll_applock_lock = (LinearLayout) findViewById(R.id.ll_applock_lock);
		tv_applock_unlock = (TextView) findViewById(R.id.tv_applock_unlock);
		tv_applock_lock = (TextView) findViewById(R.id.tv_applock_lock);
		lv_applock_unlock = (ListView) findViewById(R.id.lv_applock_unlock);
		lv_applock_lock = (ListView) findViewById(R.id.lv_applock_lock);
		//未加锁按钮的点击事件
		bt_applock_unlock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//两个按钮背景的切换
				bt_applock_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
				bt_applock_lock.setBackgroundResource(R.drawable.tab_right_default);
				//条目的切换
				ll_applock_unlock.setVisibility(View.VISIBLE);
				ll_applock_lock.setVisibility(View.GONE);
				initData();
			}
		});
		//加锁按钮的点击事件
		bt_applock_lock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//两个按钮背景的切换
				bt_applock_unlock.setBackgroundResource(R.drawable.tab_left_default);
				bt_applock_lock.setBackgroundResource(R.drawable.tab_right_pressed);
				//条目的切换
				ll_applock_lock.setVisibility(View.VISIBLE);
				ll_applock_unlock.setVisibility(View.GONE);
				
				
			}
		});

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		new Thread() {

			@Override
			public void run() {

		mAppList = AppInformation.getAppList(mContext);
		//未加锁集合
		mUnLockList=new ArrayList<AppBean>();
		mLockList = new ArrayList<AppBean>();
		//创建数据库对象,调用方法
		mDao = AppLockDao.getInstance(getApplicationContext());
		List<String> lockPackageList = mDao.queryAll();
		for (AppBean appBean: mAppList) {
			if(lockPackageList.contains(appBean.packageName)){
				mLockList.add(appBean);
			}else{
				mUnLockList.add(appBean);
			}
			
		}
		//发送消息机制
		Message msg = Message.obtain();
		msg.obj=mAppList;
		mHandler.sendMessage(msg);
	}
		}.start();
		

	}
}
