/**
 * 2015-9-21
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;
import com.zhang.zhangsafe.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Setup3Activity extends BaseSetupActivity {
	protected static final int REQUEST_CODE = 0;
	private static final String tag = "Setup3Activity";
	private Context mContext;
	private Button bt_setup3_select;
	private TextView et_setup3_phone;

	/**
	*
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		// 初始化UI
		initUI();
		// 初始化数据
		initData();
	}


	
	/**
	 * 初始化数据
	 */
	private void initData() {
		// 回显操作
		// 先从xml中取出数据并赋值给输入框
		String phonexml = SpUtil.getString(mContext,
				ConstantValue.CONTACT_PHONE, "");
		// 判断一下是否为空
		if (phonexml != null) {
			et_setup3_phone.setText(phonexml);
		}
		// 按钮选择联系人的点击事件
		bt_setup3_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 当点击触发的时候跳转到另一个页面,显示系统所有的联系人
				Intent intent = new Intent(mContext, ContactListActivity.class);
				startActivityForResult(intent, REQUEST_CODE);

			}
		});
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		et_setup3_phone = (TextView) findViewById(R.id.et_setup3_phone);
		bt_setup3_select = (Button) findViewById(R.id.bt_setup3_select);

	}

	/**
	 * 接收返回值
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		// 因为就一个返回值,所以这里不作判断了
		if (data != null) {
			String phone = data.getStringExtra("phone");
			// 测试数据
			/* Log.d(tag, "从listview中获取的电话号码"+phone); */
			// 由于获得的电话号码格式为x-xxx-xxx-xxxx,不符合要求,要处理一下
			phone = phone.replaceAll("-", "").replaceAll(" ", "").trim();
			// 测试数据
			/* Log.d(tag, phone); */
			// 将得到的数据赋值给控件
			et_setup3_phone.setText(phone);
			// 存储联系人

			SpUtil.putString(mContext, ConstantValue.CONTACT_PHONE, phone);
		}

	}

	/**
	 * 上一页
	 */
	@Override
	protected void showPrePage() {
		// 启动意图
		Intent intent = new Intent(mContext, Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.pre_in_translate,
				R.anim.pre_out_translate);
	}

	/**
	 * 下一页
	 */
	@Override
	protected void showNextPage() {
		// 先进行判断输入框是否有值
				String phone = et_setup3_phone.getText().toString().trim();
				// 判断是否为空
				if (!TextUtils.isEmpty(phone)) {

					// 启动意图
					Intent intent = new Intent(mContext, Setup4Activity.class);
					startActivity(intent);
					finish();
					// 既然输入框不为空那么就应该存储到xml中
					SpUtil.putString(mContext, ConstantValue.CONTACT_PHONE, phone);

					overridePendingTransition(R.anim.next_in_translate,
							R.anim.next_out_translate);
				}
				// 如果里面为空
				else {
					ToastUtil.show(mContext, "请输入或者选择电话号码");
				}
	}
}
