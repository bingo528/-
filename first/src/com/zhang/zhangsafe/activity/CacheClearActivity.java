/**
 * 2015-10-8
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.domin.CacheBean;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CacheClearActivity extends Activity {
	
	/**
	 *更新ui
	 */
	protected static final int UPDATE_UI = 100;
	/**
	 *更新进度条下面的显示名称
	 */
	protected static final int UPDATE_NAME = 101;
	
	/**
	 *扫描完成
	 */
	protected static final int CHECK_FINISH = 102;
	
	protected static final int CLEAR_CACHE = 103;
	private Button bt_cache_clear;
	private ProgressBar pb_cache_bar;
	private LinearLayout ll_cache_add;
	private TextView tv_name;
	private PackageManager mPackageManager;
	private Handler mHandler =new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_UI:
	            View view = View.inflate(getApplicationContext(), R.layout.linearlayout_cache_item, null);
 				
				ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				TextView tv_item_name = (TextView) view.findViewById(R.id.tv_name);
				TextView tv_memory_info = (TextView)view.findViewById(R.id.tv_memory_info);
				ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
				
				final CacheBean cacheBean=(CacheBean) msg.obj;
				iv_icon.setBackgroundDrawable(cacheBean.icon);
				tv_item_name.setText(cacheBean.name);
				tv_memory_info.setText(Formatter.formatFileSize(getApplicationContext(), cacheBean.cacheSize));
				ll_cache_add.addView(view, 0);
				iv_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						//源码开发课程(源码(handler机制,AsyncTask(异步请求,手机启动流程)源码))
						//通过查看系统日志,获取开启清理缓存activity中action和data
						Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
						intent.setData(Uri.parse("package:"+cacheBean.packageName));
						startActivity(intent);
					}
				});
				break;
			case UPDATE_NAME:
				//设置
				tv_name.setText("正在扫描:"+(String)msg.obj);
				break;
			case CHECK_FINISH:
				//设置
				tv_name.setText("扫描完成");
				break;
			case CLEAR_CACHE:
				//设置
				ll_cache_add.removeAllViews();
				break;

			default:
				break;
			}
		};
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cache_clear);
		// 初始化UI
		initUI();
		// 初始化数据
		initData();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		bt_cache_clear = (Button) findViewById(R.id.bt_cache_clear);
		pb_cache_bar = (ProgressBar) findViewById(R.id.pb_cache_bar);
		tv_name = (TextView) findViewById(R.id.tv_name);
		ll_cache_add = (LinearLayout) findViewById(R.id.ll_cache_add);
		// 设置立即清理的点击事件
		bt_cache_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Class<?> clazz = Class.forName("android.content.pm.PackageManager");
					//2.获取调用方法对象
					Method method = clazz.getMethod("freeStorageAndNotify", long.class,IPackageDataObserver.class);
					//3.获取对象调用方法
					method.invoke(mPackageManager, Long.MAX_VALUE,new IPackageDataObserver.Stub() {
						@Override
						public void onRemoveCompleted(String packageName, boolean succeeded)
								throws RemoteException {
							//清除缓存完成后调用的方法(考虑权限)
							Message msg = Message.obtain();
							msg.what = CLEAR_CACHE;
							mHandler.sendMessage(msg);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		new Thread() {
			private int mIndex=0;
			public void run() {
				// 获得包的管理者对象

				mPackageManager = getPackageManager();
				// 获得安装在手机上所有的应用
				List<ApplicationInfo> installedApplications = mPackageManager.getInstalledApplications(0);
				// 设置进度条的最大值
				pb_cache_bar.setMax(installedApplications.size());
				// 获得每个应用的相关信息
				for (ApplicationInfo applicationInfo : installedApplications) {
					String packageName = applicationInfo.packageName;
					// 调用获得缓存的方法
					getPackageCache(packageName);
					try {
						Thread.sleep(500+new Random().nextInt(100));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mIndex++;
					pb_cache_bar.setProgress(mIndex);
					//更新名称
					Message msg = Message.obtain();
					msg.what=UPDATE_NAME;
					String name=null;
					try {
						name=mPackageManager.getApplicationInfo(packageName, 0).loadLabel(mPackageManager).toString();
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					msg.obj=name;
					mHandler.sendMessage(msg);
				  
				}
				//扫描完成
				Message msg = Message.obtain();
				msg.what=CHECK_FINISH;
				mHandler.sendMessage(msg);
			};

		}.start();

	}

	/**
	 * 获得缓存的方法
	 * 
	 * @param packageName
	 *            包名
	 */
	protected void getPackageCache(String packageName) {

		IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {

			

			@Override
			public void onGetStatsCompleted(PackageStats stats,
					boolean succeeded) throws RemoteException {
				//得到缓存大小
				long cacheSize = stats.cacheSize;
				//判断缓存大小
				if (cacheSize>0) {
					CacheBean cacheBean=null;
					//更新UI,又因为子线程不能更新UI,使用消息机制
					Message msg = Message.obtain();
					msg.what=UPDATE_UI;
					try {
						//存储这些应用的相关信息
						
						cacheBean = new CacheBean();
						cacheBean.cacheSize=cacheSize;
						cacheBean.packageName=stats.packageName;
						cacheBean.name=mPackageManager.getApplicationInfo(stats.packageName, 0).loadLabel(mPackageManager).toString();
					    cacheBean.icon=mPackageManager.getApplicationInfo(stats.packageName, 0).loadIcon(mPackageManager);
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					msg.obj=cacheBean;
					mHandler.sendMessage(msg);
				}
			}
		};

		try {
			Class<?> clazz = Class.forName("android.content.pm.PackageManager");
			// 2.获取调用方法对象
			Method method = clazz.getMethod("getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			// 3.获取对象调用方法
			method.invoke(mPackageManager, packageName, mStatsObserver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
