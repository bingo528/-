package com.zhang.zhangsafe.activity;



import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.adapter.HomeGridAdapter;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.Md5Util;
import com.zhang.zhangsafe.util.SpUtil;
import com.zhang.zhangsafe.util.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class HomeActivity extends Activity implements OnItemClickListener
		 {
	protected static final String tag = "HomeActivity";
	private GridView gv_home;
	private String[] mTitleStrs;
	private int[] mDrawableIds;
	private Context mContext;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mContext = this;
		// 找到相应的控件,初始化UI
		initUI();
		// 初始化数据
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		// 准备文字

		mTitleStrs = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计",
				"手机杀毒", "缓存清理", "高级工具", "设置中心" };
		mDrawableIds = new int[] { R.drawable.home_safe,
				R.drawable.home_callmsgsafe, R.drawable.home_apps,
				R.drawable.home_taskmanager, R.drawable.home_netmanager,
				R.drawable.home_trojan, R.drawable.home_sysoptimize,
				R.drawable.home_tools, R.drawable.home_settings };
		// 设置数据适配器
		gv_home.setAdapter(new HomeGridAdapter(mTitleStrs, mDrawableIds, mContext));
		// 设置条目的点击事件
		gv_home.setOnItemClickListener(this);
		
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		gv_home = (GridView) findViewById(R.id.gv_home);
		
	}

	/**
	 *条目的点击事件
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		/*
		 * 当选择为0的时候就选择这些条目的第一个条目
		 */
		switch (position) {
		case 0:
			showDialog();

			break;
		case 1:
			//跳到通讯卫士页面
               startActivity(new Intent(mContext,BlackNumberActivity.class));
			break;
		case 2: 
			//跳转到软件管理模块
			startActivity(new Intent(getApplicationContext(), AppManagerActivity.class));
			break;
		case 3:
			//跳转到进程管理模块
			startActivity(new Intent(getApplicationContext(), ProcessManagerActivity.class));
			break;
		case 4:

			break;
		case 5:
			//跳转到杀毒模块
			startActivity(new Intent(getApplicationContext(), AnitVirusActivity.class));
			break;
		case 6:
			//跳转到缓存清理
			
			startActivity(new Intent(getApplicationContext(), CacheClearActivity.class));
			break;
		case 7:
			//跳转到高级工具
            startActivity(new Intent(mContext, AToolActivity.class));
			break;
		case 8:
			//获得设置中心
			Intent intent = new Intent(mContext, SettingActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	/**
	 * 创建一个对话框
	 */
	private void showDialog() {
		// 判断本地是否存储密码
		String psd = SpUtil.getString(mContext, ConstantValue.MOBILE_SAFE_PSD,
				"");
		if (TextUtils.isEmpty(psd)) {
			
			showSetDialog();
		}
		else {
			showConfirmDialog();
		}	
		
		
	}

	/**
	 * 输入密码初始化对话框
	 */
	private void showSetDialog() {
		// 创建Builder对象
		Builder builder = new AlertDialog.Builder(this);
		// 创建AlertDialog对象
		final AlertDialog dialog = builder.create();
		// 将xml转化view
		final View view = View.inflate(this, R.layout.dialog_set_psd, null);
		// 对话框设置
		//dialog.setView(view);
		//为了兼容低版本,给对话框设置布局的时候,让其没有内边距(android系统默认提供出来的)
		dialog.setView(view, 0, 0, 0, 0);
		// 展示出来
		dialog.show();
		//找到视图上面的控件
		Button bt_submit_psd = (Button) view.findViewById(R.id.bt_submit_psd);
		Button bt_cancel_psd = (Button) view.findViewById(R.id.bt_cancel_psd);
		//对话框上面的确定与取消按钮的点击事件
		bt_submit_psd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//首先获得输入的信息
				TextView et_dialog_submit_psd = (TextView)view.findViewById(R.id.et_dialog_submit_psd);
			    TextView et_dialog_confirm_psd = (TextView) view.findViewById(R.id.et_dialog_confirm_psd);
			    //转化成字符串
			    String confirmPsd = et_dialog_confirm_psd.getText().toString().trim();
			    String psd = et_dialog_submit_psd.getText().toString().trim();
			    //测试一下
			    Log.i(tag, psd);
			    Log.i(tag, confirmPsd);
			    //判断两次密码
			    if (!(TextUtils.isEmpty(psd)) && !(TextUtils.isEmpty(confirmPsd))) {
			    	if (psd.equals(confirmPsd)) {
			    		Intent intent = new Intent(mContext, SetupOverActivity.class);
						startActivity(intent);
						dialog.dismiss();
						//存储密码
						SpUtil.putString(getApplicationContext(), 
								ConstantValue.MOBILE_SAFE_PSD,
								Md5Util.encoder(confirmPsd));
			    	  }
			    	else {
						
			    		ToastUtil.show(mContext, "两次密码不一致");
					}
				}
			    else {
					ToastUtil.show(mContext, "请输入密码");
				}
			}
		});
		//视图上取消按钮的点击事件
		bt_cancel_psd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 确认密码对话框
	 */
	private void showConfirmDialog() {
		// 创建Builder对象
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		final View view = View.inflate(this, R.layout.dialog_confirm_psd, null);
		// 对话框设置
		dialog.setView(view, 0, 0, 0, 0);
		// 展示出来
		dialog.show();
		//确认密码视图上面确定和取消按钮的点击事件
		Button bt_confirm_submit = (Button) view.findViewById(R.id.bt_confirm_submit);
		Button bt_confirm_cancel = (Button) view.findViewById(R.id.bt_confirm_cancel);
	   //设置点击事件
		bt_confirm_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//找到控件输入的内容
				EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);
			   String confirmPsd = et_confirm_psd.getText().toString().trim();
			  //从xml取出数据
			   String psdxml = SpUtil.getString(mContext, ConstantValue.MOBILE_SAFE_PSD, "");
			   if (!TextUtils.isEmpty(confirmPsd)) {
				if (psdxml.equals(Md5Util.encoder(confirmPsd))) {
					
					Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
					startActivity(intent);
					//跳转到新的界面以后需要去隐藏对话框
					dialog.dismiss();
				}
				else {
					ToastUtil.show(mContext, "确认密码错误");
				}
			}
			   else {
				ToastUtil.show(mContext, "请输入密码");
			}
			}
		});
		bt_confirm_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	

	
}
