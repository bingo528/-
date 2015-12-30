package com.zhang.zhangsafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.engine.ReadDbInFile;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;
import com.zhang.zhangsafe.util.StreamUtil;
import com.zhang.zhangsafe.util.ToastUtil;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private TextView tv_version_name;
	private RelativeLayout rl_splash_root;
	private Context mContext;

	protected static final String tag = "SplashActivity";
	/**
	 * 更新新版本的状态码100
	 */
	protected static final int UPDATE_VERSION = 100;
	/**
	 * 进入应用程序主界面状态码
	 */
	protected static final int ENTER_HOME = 101;
	/**
	 * url地址出错状态码
	 */
	protected static final int URL_ERROR = 102;
	protected static final int IO_ERROR = 103;
	protected static final int JSON_ERROR = 104;
	private int mLocalVersionCode;
	private String mLocalVersionName;
	private String mDescription;
	private String mDownload_url;
	//
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// 对于常量不同,进行判断
			switch (msg.what) {
			case UPDATE_VERSION:
				// 弹出一个对话框
				showUpdateDialog();
				break;
			case ENTER_HOME:
				// 进入主页面
				enterHome();
				break;
			case URL_ERROR:
				ToastUtil.show(getApplicationContext(), "url异常");
				enterHome();
				break;
			case IO_ERROR:
				ToastUtil.show(getApplicationContext(), "io异常");
				enterHome();
				break;
			case JSON_ERROR:
				ToastUtil.show(getApplicationContext(), "json异常");
				enterHome();
				break;

			default:
				break;
			}

		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		mContext = this;
		// 找到相应的控件,初始化UI
		initUI();
		// 初始化数据
		initData();
		// 初始化动画
		initAnimation();

		/*
		 * 初始化引入的数据库
		 * 
		 * @author zhangbin time 2015-09-25
		 */

		initDB();
		// 生成快捷方式
		//如果已产生快捷方式,就不再执行该语句
		if (!SpUtil.getBoolean(mContext, ConstantValue.SHORT_CUT, false)) {
			
			initShortCut();
		}

	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		tv_version_name = (TextView) findViewById(R.id.tv_version_name);
		rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mLocalVersionName = getVersionName();
		// 测试一下

		/* Log.d(tag, "本地版本名称:"+mLocalVersionName); */

		// 2.将获得到的版本信息赋值给控件
		tv_version_name.setText("版本名称:" + mLocalVersionName);
		mLocalVersionCode = getVersionCode();
		// 测试数据
		/* Log.d(tag, "本地版本号" + mLocalVersionCode); */
		// 3.从设置中心获取自动更新开关是否开启
		if (SpUtil.getBoolean(getApplicationContext(),
				ConstantValue.OPEN_UPDATE, false)) {

			// 4.从网络上获取服务器上的版本号
			getIntentFromVersionCode();
		} else {
			/* enterHome(); */

			mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
		}
	}

	/**
	 * 从服务器获取服务器端的版本号
	 */
	private void getIntentFromVersionCode() {
		// 请求网络是耗时操作,放在子线程中实现
		new Thread() {

			@Override
			public void run() {
				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {
					// 1.封装url地址
					String path = "http://10.0.2.2:8080/update74.json";
					URL url = new URL(path);
					// 2.开启一个连接

					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					// 3.设置一些参数
					connection.setConnectTimeout(2000);
					connection.setReadTimeout(3000);
					// 4.获得响应码并进行判断
					int code = connection.getResponseCode();
					// 5.对响应码进行判断
					// 如果响应码是200说明成功
					if (code == 200) {

						// 6.以流的形式将数据获取下来
						InputStream is = connection.getInputStream();
						// 7.将流转化成字符串
						String json = StreamUtil.streamToString(is);
						// 测试数据
						/* Log.d(tag, "请求服务器获得的json"+json); */
						// 8.解析json
						JSONObject jsonObject = new JSONObject(json);
						String version_name = jsonObject
								.getString("version_name");
						int version_code = jsonObject.getInt("version_code");
						mDescription = jsonObject.getString("description");
						mDownload_url = jsonObject.getString("download_url");
						// 测试数据
						/*
						 * Log.d(tag, version_name); Log.d(tag, "" +
						 * version_code); Log.d(tag, mDescription); Log.d(tag,
						 * mDownload_url);
						 */
						// 9.进行比对版本号和版本名字

						// 如果服务器版本高于本地版本给予对话框提示更新
						if (version_code > mLocalVersionCode) {
							// 给出对话框
							msg.what = UPDATE_VERSION;
						} else {
							// 进入应用程序主界面
							msg.what = ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {

					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = IO_ERROR;
				} catch (JSONException e) {
					e.printStackTrace();

					msg.what = JSON_ERROR;

				} finally {
					// 指定睡眠时间,请求网络的时长超过4秒则不做处理
					// 请求网络的时长小于4秒,强制让其睡眠满4秒钟
					long endTime = System.currentTimeMillis();
					if (endTime - startTime < 4000) {
						try {
							Thread.sleep(4000 - (endTime - startTime));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mHandler.sendMessage(msg);
				}
			}
		}.start();
	}

	/**
	 * 生成快捷方式
	 */
	private void initShortCut() {

		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 设置图标
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
				.decodeResource(getResources(), R.drawable.ic_launcher));
		// 设置显示的名称
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "安全卫士bingo版");
		//开启意图
		Intent shortCutIntent = new Intent("android.intent.action.HomeActivity");
		shortCutIntent.addCategory("android.intent.category.DEFAULT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);
		sendBroadcast(intent);
		//存储到xml中
		SpUtil.putBoolean(mContext, ConstantValue.SHORT_CUT, true);
	}
    
	/**
	 * 初始化数据库
	 */
	private void initDB() {
		// 可能需要处理的数据库多个,这里按照类别再进行分类
		// 手机归属地的数据库
		initCopyDB("address.db");
		// 初始化常用号码查询数据库
		initCopyDB("commonnum.db");
		//拷贝病毒库
		initCopyDB("antivirus.db");
		//缓存清理拷贝到私有目录
		initCopyDB("clearpath.db");
		

	}

	/**
	 * @param dbName
	 *            接收的数据库名称
	 */
	private void initCopyDB(String dbName) {
		ReadDbInFile.readInFile(mContext, dbName);

	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		Animation aa = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.alpha);
		rl_splash_root.startAnimation(aa);
		// 测试是否执行了
		/* Log.d(tag, "初始化动画"); */
	}

	/**
	 * 进入主页面
	 */
	protected void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		// 关闭导航页面
		finish();
	}

	/**
	 * 弹出更新对话框
	 */
	protected void showUpdateDialog() {

		// 1.创建对象
		Builder builder = new AlertDialog.Builder(this);
		// 2.设置一些数据
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("版本更新");
		// 设置描述内容将json内的说明设置进去
		builder.setMessage(mDescription);
		// 按钮的事件
		builder.setPositiveButton("立即更新",
				new DialogInterface.OnClickListener() {

					/*
					 * 确定按钮的
					 */
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 如果点立即就下载apk
						downloadApk();
					}
				});
		builder.setNegativeButton("稍后再说",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 取消对话框,进入主界面
						enterHome();
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * 下载apk
	 */
	protected void downloadApk() {
		// 1.判断sd卡是否可用和是否挂载
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 2,获取sd路径
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + "mobilesafe74.apk";
			// 3.创建对象
			HttpUtils httpUtils = new HttpUtils();
			// 4.发送请求
			// 把xml中获取到url放入
			httpUtils.download(mDownload_url, path,
					new RequestCallBack<File>() {

						@Override
						public void onSuccess(ResponseInfo<File> responseInfo) {
							// 下载成功(下载过后的放置在sd卡中apk)
							// 测试数据
							/* Log.d(tag, "下载成功"); */
							File file = responseInfo.result;
							// 提示用户安装
							installApk(file);
						}

						/*
						 * 下载失败
						 */
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							ToastUtil.show(getApplicationContext(), "下载失败");
						}

						// 刚刚开始下载方法
						@Override
						public void onStart() {
							// 测试数据
							/* Log.d(tag, "刚刚开始下载"); */
							super.onStart();
						}

						// 下载过程中的方法(下载apk总大小,当前的下载位置,是否正在下载)
						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							// 测试数据
							/*
							 * Log.d(tag, "下载中........"); Log.d(tag, "total = "
							 * + total); Log.d(tag, "current = " + current);
							 */
							super.onLoading(total, current, isUploading);
						}

					});
		}
	}

	/**
	 * @param file
	 *            从本地获取下载路径并安装
	 */
	protected void installApk(File file) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivityForResult(intent, 0);
	}

	/**
	 * 获得版本名称:从清单文件中获取
	 * 
	 * @return 版本名称,如果为null则出现异常
	 */
	private String getVersionName() {
		// 1.获得getPackageManager对象
		PackageManager pm = getPackageManager();
		// 2.获得版本名称和版本号
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			// 3.返回版本信息
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得版本号
	 * 
	 * @return 返回版本号,当为0时为异常
	 */
	private int getVersionCode() {
		// 1.获得getPackageManager对象
		PackageManager pm = getPackageManager();
		// 2.获得版本名称和版本号
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			// 3.返回版本信息
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 进入主程序
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
