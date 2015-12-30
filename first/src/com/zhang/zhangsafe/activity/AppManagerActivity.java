/**
 * 2015-9-30
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import java.util.ArrayList;

import com.zhang.zhangsafe.R;
//在本类中写适配器
//或者引用adapter中的适配器
import com.zhang.zhangsafe.adapter.MyAppAdapter;

import com.zhang.zhangsafe.domin.AppBean;
import com.zhang.zhangsafe.engine.AppInformation;
import com.zhang.zhangsafe.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AppManagerActivity extends Activity implements OnClickListener {

	private TextView tv_app_manager_memory;
	private TextView tv_app_manager_sd;
	private TextView tv_app_title;
	private ListView lv_app_list;
	private ArrayList<AppBean> appInfoList;
	// 用户应用集合
	private ArrayList<AppBean> mCoustomerList;
	// 系统应用集合
	private ArrayList<AppBean> mSystemList;
	private AppBean appBean;
	private MyAppAdapter myAppAdapter;
	private Context mContext;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			appInfoList = (ArrayList<AppBean>) msg.obj;
			// myAppAdapter = new MyAppAdapter();
			// 引用adapter包内的适配器
			myAppAdapter = new MyAppAdapter(mContext, appInfoList,
					mCoustomerList, mSystemList);
			lv_app_list.setAdapter(myAppAdapter);

		};
	};
	private PopupWindow mPopupWindow;

	/*
	 * class MyAppAdapter extends BaseAdapter{
	 * 
	 * @Override public int getViewTypeCount() {
	 * 
	 * return super.getViewTypeCount()+1; }
	 * 
	 * @Override public int getItemViewType(int position) { if (position==0
	 * ||position==mCoustomerList.size()+1) { //返回灰色的那个条目 return 0; } else {
	 * return 1; }
	 * 
	 * 
	 * }
	 * 
	 * @Override public int getCount() {
	 * 
	 * return mCoustomerList.size()+mSystemList.size()+2; }
	 * 
	 * 
	 * @Override public AppBean getItem(int position) { if (position==0
	 * ||position==mCoustomerList.size()+1) { //返回灰色的那个条目 return null; } else {
	 * if (position<mCoustomerList.size()+1) { //使用用户的集合 return
	 * mCoustomerList.get(position-1); } else { return
	 * mSystemList.get(position-mCoustomerList.size()-2); } }
	 * 
	 * }
	 * 
	 * @Override public long getItemId(int position) {
	 * 
	 * return position; }
	 * 
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { int viewType = getItemViewType(position); //如果第一种样式 if
	 * (viewType==0) { ViewTitleHolder viewTitleHolder=null; //复用convertView
	 * 
	 * if (convertView==null) {
	 * convertView=View.inflate(getApplicationContext(),
	 * R.layout.list_item_app_title, null); //复用viewhold viewTitleHolder=new
	 * ViewTitleHolder(); //找到控件 viewTitleHolder.tv_app_des = (TextView)
	 * convertView.findViewById(R.id.tv_app_des);
	 * 
	 * convertView.setTag(viewTitleHolder); } else {
	 * viewTitleHolder=(ViewTitleHolder) convertView.getTag(); } //判断一下 if
	 * (position==0) {
	 * viewTitleHolder.tv_app_des.setText("用户应用("+mCoustomerList.size()+")"); }
	 * else {
	 * 
	 * viewTitleHolder.tv_app_des.setText("系统应用("+mSystemList.size()+")"); }
	 * 
	 * 
	 * return convertView; } else { ViewHolder viewHolder=null; //复用convertView
	 * 
	 * if (convertView==null) {
	 * convertView=View.inflate(getApplicationContext(), R.layout.list_item_app,
	 * null); //复用viewhold viewHolder=new ViewHolder(); //找到控件
	 * viewHolder.tv_app_name = (TextView)
	 * convertView.findViewById(R.id.tv_app_name); viewHolder.tv_app_path =
	 * (TextView) convertView.findViewById(R.id.tv_app_path); viewHolder.iv_icon
	 * = (ImageView) convertView.findViewById(R.id.iv_icon);
	 * convertView.setTag(viewHolder); } else { viewHolder=(ViewHolder)
	 * convertView.getTag(); } //给控件赋值
	 * viewHolder.iv_icon.setBackgroundDrawable(getItem(position).icon);
	 * viewHolder.tv_app_name.setText(getItem(position).name); //判断一下 if
	 * (getItem(position).isSd) { viewHolder.tv_app_path.setText("sd卡应用"); }
	 * else {
	 * 
	 * viewHolder.tv_app_path.setText("手机应用"); }
	 * 
	 * return convertView; } }
	 * 
	 * }
	 */
	static class ViewHolder {
		TextView tv_app_path;
		TextView tv_app_name;
		ImageView iv_icon;
	}

	static class ViewTitleHolder {
		TextView tv_app_des;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		// 加载布局文件
		setContentView(R.layout.activity_app_manager);
		// 初始化数据
		initUI();
		// 获取磁盘信息
		initTitle();

		// 获得应用信息
		initAppData();
	}

	@Override
	protected void onResume() {

		super.onResume();
		new Thread() {
			public void run() {
				appInfoList = AppInformation.getAppList(getApplicationContext());
				mCoustomerList = new ArrayList<AppBean>();
				mSystemList = new ArrayList<AppBean>();
				for (AppBean appBean : appInfoList) {
					// 用户应用在上面
					if (!appBean.isSystem) {
						mCoustomerList.add(appBean);
					}
					// 系统用户
					else {
						mSystemList.add(appBean);
					}
				}

				Message msg = Message.obtain();
				msg.obj = appInfoList;

				mHandler.sendMessage(msg);
				/* mHandler.sendEmptyMessage(0); */

			};
		}.start();
	}

	/**
	 * 初始化ui
	 */
	private void initUI() {
		tv_app_manager_memory = (TextView) findViewById(R.id.tv_app_manager_memory);
		tv_app_manager_sd = (TextView) findViewById(R.id.tv_app_manager_sd);
		lv_app_list = (ListView) findViewById(R.id.lv_app_list);
		tv_app_title = (TextView) findViewById(R.id.tv_app_title);
	}

	/**
	 * 获取磁盘信息,大小
	 */
	private void initTitle() {
		

		// 获得手机空间的路径
		String phonePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		long AvaliSpace = getAvaliSpace(phonePath);
		String AvaliSize = Formatter.formatFileSize(getApplicationContext(),AvaliSpace);
		// 获得可用空间大小
		// 获取sd卡可用大小
		String path = Environment.getDataDirectory().getAbsolutePath();
		long memory = getAvaliSpace(path);
		// 将大小格式化一下

		String memoryAvaliSize = Formatter.formatFileSize(
				getApplicationContext(), memory);
	
		tv_app_manager_memory.setText("手机可用:" + AvaliSize);
		tv_app_manager_sd.setText("sd卡可用:");
	}

	/**
	 * @param path
	 *            传入的路径
	 * @return 磁盘大小
	 */
	private long getAvaliSpace(String path) {
		// 获取可用磁盘大小
		StatFs statFs = new StatFs(path);
		// 获取可用区块
		long count = statFs.getAvailableBlocks();
		// 获得每个区块的大小
		long size = statFs.getBlockSize();
		return count * size;
	}

	/**
	 * 获得app信息
	 */
	private void initAppData() {

		// 监听滚动
		lv_app_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mCoustomerList != null && mSystemList != null) {
					if (firstVisibleItem >= mCoustomerList.size() + 1) {
						tv_app_title.setText("系统应用(" + mSystemList.size() + ")");

					} else {
						tv_app_title.setText("用户应用(" + mCoustomerList.size()
								+ ")");
					}
				}

			}
		});
		// 条目的点击事件
		lv_app_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0 || position == mCoustomerList.size() + 1) {
					// 返回灰色的那个条目
					return;
				} else {
					if (position < mCoustomerList.size() + 1) {
						// 使用用户的集合

						appBean = mCoustomerList.get(position - 1);
					} else {
						appBean = mSystemList.get(position
								- mCoustomerList.size() - 2);
					}
					// 显示窗体
					showPopupWindow(view);
				}

			}

		});

	}

	/**
	 * 显示窗体
	 */
	protected void showPopupWindow(View view) {
		View popupView = View.inflate(mContext, R.layout.popupwindow_layout,
				null);
		// 找到控件
		TextView tv_uninstall = (TextView) popupView
				.findViewById(R.id.tv_uninstall);
		TextView tv_start = (TextView) popupView.findViewById(R.id.tv_start);
		TextView tv_share = (TextView) popupView.findViewById(R.id.tv_share);
		// 设置点击事件
		tv_uninstall.setOnClickListener(this);
		tv_start.setOnClickListener(this);
		tv_share.setOnClickListener(this);
		// 设置动画
		Animation aa = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.alpha);
		// 缩放动画
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setDuration(500);
		scaleAnimation.setFillAfter(true);
		// 动画集合Set
		AnimationSet animationSet = new AnimationSet(true);
		// 添加两个动画
		animationSet.addAnimation(aa);
		animationSet.addAnimation(scaleAnimation);
		mPopupWindow = new PopupWindow(popupView,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, true);
		// 设置背景
		mPopupWindow.setBackgroundDrawable(new ColorDrawable());
		// 设置窗体位置
		mPopupWindow.showAsDropDown(view, 50, -view.getHeight());

		// 挂载动画
		popupView.startAnimation(animationSet);
	}

	/**
	 * 判断条目上面不同的点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_uninstall:
			// 是系统应用
			if (appBean.isSystem) {
				ToastUtil.show(mContext, "此应用不能卸载");
			}
			// 是用户应用,但是这个应用不能卸载
			else {
				Intent intent = new Intent("android.intent.action.DELETE");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setData(Uri.parse("package:" + appBean.getPackageName()));
				startActivity(intent);
			}
			break;
		case R.id.tv_start:
			// 通过桌面图标启动
			PackageManager packageManager = getPackageManager();
			Intent launchIntentForPackage = packageManager
					.getLaunchIntentForPackage(appBean.getPackageName());
			if (launchIntentForPackage != null) {
				startActivity(launchIntentForPackage);
			} else {
				ToastUtil.show(getApplicationContext(), "此应用不能被开启");
			}
			break;
		case R.id.tv_share:

			break;

		default:
			break;
		}
		// 点击了窗体后消失窗体
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

}
