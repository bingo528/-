/**
 * 2015-9-21
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;



import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SetupOverActivity extends Activity {
	private TextView tv_setupover_reset;
	private TextView tv_setup_over_safenumber;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext=this;
		super.onCreate(savedInstanceState);
		boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER, false);
		if(setup_over){
			//密码输入成功,并且四个导航界面设置完成----->停留在设置完成功能列表界面
			setContentView(R.layout.activity_setup_over);
			initUI();
			initData();
		}else{
			//密码输入成功,四个导航界面没有设置完成----->跳转到导航界面第1个
			Intent intent = new Intent(this, Setup1Activity.class);
			startActivity(intent);
			
			//开启了一个新的界面以后,关闭功能列表界面
			finish();
		}
		
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		//从xml中取出数据
		String phone = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
       tv_setup_over_safenumber.setText(phone);
    
       
	
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		tv_setup_over_safenumber = (TextView) findViewById(R.id.tv_setup_over_safenumber);
		
		tv_setupover_reset = (TextView) findViewById(R.id.tv_setupover_reset);
		
		tv_setupover_reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,Setup1Activity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
