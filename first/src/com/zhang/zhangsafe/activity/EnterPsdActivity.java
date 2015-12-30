/**
 * 2015-10-9
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EnterPsdActivity extends Activity {
	private String packageName;
	private TextView tv_enter_psd_name;
	private ImageView iv_enter_psd_icon;
	private EditText et_dialog_submit_psd;
	private Button bt_submit_psd;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext=this;
		packageName = getIntent().getStringExtra("packageName");
		setContentView(R.layout.activity_enter_psd);
		//初始化UI
		initUI();
		//初始化数据
		initData();
		
	}
	/**
	 *回退按钮
	 */
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		//跳转回桌面
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	
	/**
	 * 初始化UI
	 */
	private void initUI() {
		tv_enter_psd_name = (TextView) findViewById(R.id.tv_enter_psd_name);
		iv_enter_psd_icon = (ImageView) findViewById(R.id.iv_enter_psd_icon);
		et_dialog_submit_psd = (EditText) findViewById(R.id.et_dialog_submit_psd);
		bt_submit_psd = (Button) findViewById(R.id.bt_submit_psd);

	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		try {
			//获得包的管理者对象
			PackageManager packageManager = getPackageManager();
			ApplicationInfo applicationInfo= packageManager.getApplicationInfo(packageName, 0);
			//通过对象获得相关信息
			 iv_enter_psd_icon.setBackgroundDrawable(applicationInfo.loadIcon(packageManager));
			 tv_enter_psd_name.setText(applicationInfo.loadLabel(packageManager).toString());
		} catch (NameNotFoundException e) {
			
			e.printStackTrace();
		}
		//对输入密码进行校验
		// TODO 暂时使用字符串,有时间改成从数据库获取进行比对
		bt_submit_psd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//先获得输入框的内容
				String psd = et_dialog_submit_psd.getText().toString();
				if (!TextUtils.isEmpty(psd)) {
					 if (psd.equals("123")) {
						//解锁进入应用
						 //告知服务,不要再监听了
						 Intent intent = new Intent("android.intent.action.SKIP");
						 intent.putExtra("packageName", packageName);
						 sendBroadcast(intent);
						 finish();
					}
					 else {
						ToastUtil.show(mContext, "密码不正确");
					}
				}
				else {
					ToastUtil.show(mContext, "请输入密码");
				}
			}
		});
	}

}
