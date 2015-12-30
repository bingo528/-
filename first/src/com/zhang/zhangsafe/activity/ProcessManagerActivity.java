/**
 * 2015-9-30
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import java.util.ArrayList;
import java.util.List;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.adapter.MyAppAdapter;
import com.zhang.zhangsafe.adapter.MyProcessAdapter;
import com.zhang.zhangsafe.domin.AppBean;
import com.zhang.zhangsafe.domin.ProcessBean;
import com.zhang.zhangsafe.engine.AppInformation;
import com.zhang.zhangsafe.engine.ProcessInformation;
import com.zhang.zhangsafe.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ProcessManagerActivity extends Activity implements OnClickListener {

	/**
	 * 设置按钮的返回码
	 */
	private static final int REQUEST_CODE = 0;
	private TextView tv_process_count;
	private TextView tv_process_size;
	private TextView tv_process_des;
	private ListView lv_process;
	private Button bt_process_select_all;
	private Button bt_process_select_reverse;
	private Button bt_process_clear;
	private Button bt_process_setting;
	private Context mContext;
	private ProcessBean mProcessInfoBean;
	private ArrayList<ProcessBean> mProcessInfo;
	private ArrayList<ProcessBean> mSystemList;
	private ArrayList<ProcessBean> mCustomerList;
	protected MyProcessAdapter myProcessAdapter;
	private int mCount;
	private long mAvaliSpace;
	private String mTotalSize;
	private long mTotalSpace;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mProcessInfo = (ArrayList<ProcessBean>) msg.obj;
			// myAppAdapter = new MyAppAdapter();
			// 引用adapter包内的适配器
			myProcessAdapter = new MyProcessAdapter(mContext, mProcessInfo,
					mCustomerList, mSystemList);
			lv_process.setAdapter(myProcessAdapter);
			if (tv_process_des != null && mCustomerList != null) {
				tv_process_des.setText("用户应用(" + mCustomerList.size() + ")");
			}
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_manager);
		initUI();
		initTitle();
		initData();
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		// 通知适配器刷新
		if (myProcessAdapter != null) {
			myProcessAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 初始化ui
	 */
	private void initUI() {
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		tv_process_size = (TextView) findViewById(R.id.tv_process_size);
		lv_process = (ListView) findViewById(R.id.lv_process);
		bt_process_select_all = (Button) findViewById(R.id.bt_process_select_all);
		bt_process_select_reverse = (Button) findViewById(R.id.bt_process_select_reverse);
		bt_process_clear = (Button) findViewById(R.id.bt_process_clear);
		bt_process_setting = (Button) findViewById(R.id.bt_process_setting);
		tv_process_des = (TextView) findViewById(R.id.tv_process_des);
		// 设置点击事件
		bt_process_select_all.setOnClickListener(this);
		bt_process_select_reverse.setOnClickListener(this);
		bt_process_clear.setOnClickListener(this);
		bt_process_setting.setOnClickListener(this);
		// 设置条目的滚动事件
		lv_process.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mCustomerList != null && mSystemList != null) {
					if (firstVisibleItem >= mCustomerList.size() + 1) {
						// 滚动到了系统条目
						tv_process_des.setText("系统进程(" + mSystemList.size()
								+ ")");
					} else {
						// 滚动到了用户应用条目
						tv_process_des.setText("用户进程(" + mCustomerList.size()
								+ ")");
					}
				}
			}
		});
		lv_process.setOnItemClickListener(new OnItemClickListener() {
			// view选中条目指向的view对象
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0 || position == mCustomerList.size() + 1) {
					return;
				} else {
					if (position <= mCustomerList.size() + 1) {
						mProcessInfoBean = mCustomerList.get(position - 1);
					} else {
						// 返回系统应用对应条目的对象
						mProcessInfoBean = mSystemList.get(position
								- mCustomerList.size() - 2);
					}
					if (mProcessInfo != null) {
						// 选中条目指向的对象和本应用的包名不一致,才需要去状态取反和设置单选框状态
						if (!mProcessInfoBean.packageName
								.equals(getPackageName())) {
							// 状态取反
							mProcessInfoBean.isCheck = !mProcessInfoBean.isCheck;
							// checkbox显示状态切换
							// 通过选中条目的view对象,findViewById找到此条目指向的cb_box,然后切换其状态
							CheckBox cb_box = (CheckBox) view
									.findViewById(R.id.cb_process_box);
							cb_box.setChecked(mProcessInfoBean.isCheck);
						}
					}
				}
			}
		});
	}

	/**  
	 * 标题的信息
	 */
	private void initTitle() {
		mCount = ProcessInformation.getCount(mContext);
		tv_process_count.setText("进程总数:" + mCount);
		mAvaliSpace = ProcessInformation.getAvaliSpace(mContext);
		// 格式化一下
		String avaliSize = Formatter.formatFileSize(mContext, mAvaliSpace);
		mTotalSpace = ProcessInformation.getTotalSpace(mContext);
		mTotalSize = Formatter.formatFileSize(mContext, mTotalSpace);
		// 设置
		tv_process_size.setText("可用/总共:" + avaliSize + "/"+mTotalSize);
	}

	/**
	 * listview的数据
	 */
	private void initData() {
		new Thread() {
			public void run() {
				mProcessInfo = ProcessInformation.getProcessInfo(mContext);
				mSystemList = new ArrayList<ProcessBean>();
				mCustomerList = new ArrayList<ProcessBean>();
				for (ProcessBean info : mProcessInfo) {
					// 用户应用在上面
					if (!info.isSystem) {
						mCustomerList.add(info);
					}
					// 系统用户
					else {
						mSystemList.add(info);
					}
				}

				Message msg = Message.obtain();
				msg.obj = mProcessInfo;

				mHandler.sendMessage(msg);
				/* mHandler.sendEmptyMessage(0); */

			};
		}.start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_process_select_all:
			selectAll();
			break;
		case R.id.bt_process_select_reverse:
			selectReverse();
			break;
		case R.id.bt_process_clear:
			clearAll();
			break;
		case R.id.bt_process_setting:
			setting();
			break;

		default:
			break;
		}
	}

	/**
	 * 全选按钮的点击处理事件
	 */
	private void selectAll() {
		for (ProcessBean processInfo : mCustomerList) {
			if (processInfo.getPackageName().endsWith(getPackageName())) {
				continue;
			}
			processInfo.isCheck = true;
		}
		for (ProcessBean processInfo : mSystemList) {

			processInfo.isCheck = true;
		}
		// 通知适配器刷新
		if (myProcessAdapter != null) {
			myProcessAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 反选按钮的点击处理事件
	 */
	private void selectReverse() {
		for (ProcessBean processInfo : mCustomerList) {
			if (processInfo.getPackageName().endsWith(getPackageName())) {
				continue;
			}
			processInfo.isCheck = !processInfo.isCheck;
		}
		for (ProcessBean processInfo : mSystemList) {

			processInfo.isCheck = !processInfo.isCheck;
		}
		// 通知适配器刷新
		if (myProcessAdapter != null) {
			myProcessAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 一键清除 清除选中的进程
	 */
	private void clearAll() {
		// 获取哪些选中
		// 2,创建一个记录需要杀死的进程的集合
		List<ProcessBean> killProcessList = new ArrayList<ProcessBean>();
		for (ProcessBean processInfo : mCustomerList) {
			if (processInfo.getPackageName().endsWith(getPackageName())) {
				continue;
			}
			if (processInfo.isCheck) {
				// 记录需要杀死的进程
				killProcessList.add(processInfo);
			}

		}
		long totalReleaseSpace = 0;
		// 遍历系统集合
		for (ProcessBean processInfo : mSystemList) {

			if (processInfo.isCheck) {
				// 记录需要杀死的进程
				killProcessList.add(processInfo);
			}
		} 

		// 循环遍历杀死进程的集合
		for (ProcessBean processInfo : killProcessList) {
			if (mCustomerList.contains(processInfo)) {
				mCustomerList.remove(processInfo);
			}
			if (mSystemList.contains(processInfo)) {
				mSystemList.remove(processInfo);
			}
			// 先记录进行大小
			totalReleaseSpace += processInfo.memSize;
			// 杀死进程
			ProcessInformation.killProcess(mContext, processInfo);

		}
		// 通知适配器刷新
		if (myProcessAdapter != null) {
			myProcessAdapter.notifyDataSetChanged();
		}
		// 更新控件
		// 进程总数
		mCount -= killProcessList.size();
		// 更新剩余大小
		mAvaliSpace += totalReleaseSpace;
		// 更新控件
		tv_process_count.setText("进程总数:" + mCount);
		tv_process_size.setText("可用/总共:"
				+ Formatter.formatFileSize(mContext, mAvaliSpace) + "/"
				+ mTotalSize);
		// 吐司告诉用户
		String totalRelease = Formatter.formatFileSize(this, totalReleaseSpace);
		ToastUtil.show(mContext, String.format("杀死了%d进程,释放了%s空间",
				killProcessList.size(), totalRelease));
	}

	/**
	 * 设置按钮的点击事件
	 */
	private void setting() {
		Intent intent = new Intent(mContext, ProcessSettingActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}

}
