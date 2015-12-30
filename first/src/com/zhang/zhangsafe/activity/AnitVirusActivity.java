/**
 * 2015-10-7
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.db.dao.VirusDao;
import com.zhang.zhangsafe.domin.VirusBean;
import com.zhang.zhangsafe.util.Md5Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AnitVirusActivity extends Activity {
	protected static final int SCANING = 100;
	protected static final int SCAN_FINISH = 101;
	private Context mContext;
	private ImageView iv_anit_scanning;
	private TextView tv_anit_name;
	private ProgressBar pb_anit_bar;
	private LinearLayout ll_anit_add;
	private int index=0;
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANING:
				VirusBean bean=(VirusBean) msg.obj;
				tv_anit_name.setText(bean.name);
				TextView textView = new TextView(mContext);
				if (bean.isVirus) {
					textView.setTextColor(Color.RED);
					textView.setText("发现病毒:"+bean.name);
				} else {

					textView.setText("扫描正常:"+bean.name);
				}
				//添加到布局中
				ll_anit_add.addView(textView, 0);
				break;
			case SCAN_FINISH:
				tv_anit_name.setText("扫描完成");
				iv_anit_scanning.clearAnimation();
				//告知用户卸载病毒
				uninstallVirus();
				break;

			default:
				break;
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
       mContext=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anit_virus);
		//初始化UI
		initUI();
		
		//检测病毒
		checkVirus();
	}

	/**
	 * 卸载有病毒的应用
	 */
	protected void uninstallVirus() {
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		iv_anit_scanning = (ImageView) findViewById(R.id.iv_anit_scanning);
		tv_anit_name = (TextView) findViewById(R.id.tv_anit_name);
		pb_anit_bar = (ProgressBar) findViewById(R.id.pb_anit_bar);
		ll_anit_add = (LinearLayout) findViewById(R.id.ll_anit_add);
		//设置旋转动画
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
		iv_anit_scanning.startAnimation(animation);
	}
	

	/**
	 * 检测病毒
	 */
	private void checkVirus() {
		new Thread(){
			public void run() {
				//获取所有病毒数据库集合
				List<String> virusList = VirusDao.getVirusList();
				//获得包的管理者对象
				PackageManager pm = getPackageManager();
				//获得所有签名文件
				List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_SIGNATURES+PackageManager.GET_UNINSTALLED_PACKAGES);
				//创建记录病毒应用的集合
				List<VirusBean> mVirusBeanList = new ArrayList<VirusBean>();
				//创建所有应用的集合
				List<VirusBean> BeanList = new ArrayList<VirusBean>();
				pb_anit_bar.setMax(installedPackages.size());
				//遍历集合
		 		for (PackageInfo packageInfo : installedPackages) {
					VirusBean virusBean = new VirusBean();
					//得到签名
					Signature[] signatures = packageInfo.signatures;
					//获得签名中第1位也就是数组中的标号为0的那个数组
					Signature signature = signatures[0];
					//先把签名转化成字符串
					String charsString = signature.toCharsString();
					//MD5
					String encoder = Md5Util.encoder(charsString);
					//比对
					if (virusList.contains(encoder)) {
						//是病毒,将那个记录修改成false;
						virusBean.isVirus=true;
						mVirusBeanList.add(virusBean);
						tv_anit_name.setText("发现病毒:");
					}
					else {
						//不是病毒
						virusBean.isVirus=false;
					}
					//只有扫描过我就记录下来
					BeanList.add(virusBean);
					virusBean.packName=packageInfo.packageName;
					virusBean.name=packageInfo.applicationInfo.loadLabel(pm).toString();
					index ++;
					//设置进度条
					pb_anit_bar.setProgress(index);
					try {
						Thread.sleep(50+new Random().nextInt(100));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//发送消息
					Message msg = Message.obtain();
					msg.what=SCANING;
					msg.obj=virusBean;
					mHandler.sendMessage(msg);
				}
				//发送消息
				Message msg = Message.obtain();
				msg.what=SCAN_FINISH;
				
				mHandler.sendMessage(msg);
			};
			
		}.start();
		
		
	}

}
