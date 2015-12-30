/**
 * 2015-9-25
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.db.dao.AddressDao;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;
import com.zhang.zhangsafe.util.ToastUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryLocationActivity extends Activity {
	protected static final String tag = "QueryAddressActivity";
	private EditText et_query_address;
	private TextView tv_query_show;
	private Button bt_query_search;
	private Context mContext;
	private String mInputphone;
	private String mLocation;
	private Button bt_setup3_select;
	protected static final int REQUEST_CODE = 0;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tv_query_show.setText("号码的归属地是:" + mLocation);

		};
	};

	/**
	 * 初始化使用
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_address);
		mContext = this;
		initUI();
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 选择联系人的点击事件
		bt_setup3_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 当点击触发的时候跳转到另一个页面,显示系统所有的联系人
				Intent intent = new Intent(mContext, ContactListActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		// 查询的点击事件
		bt_query_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mInputphone = et_query_address.getText().toString();
				// 测试数据,测试输入框的内容
				// Log.d(tag, mInputphone);
				// 测试数据,当没有获得输入框的测试
				/*
				 * mLocation = AddressDao.phone("112");
				 * tv_query_show.setText("查询的电话号码是:"+mLocation);
				 */
				// Log.d(tag, location);
				// 因为查询是耗时操作,所有需要在子线程中实现
				if (!TextUtils.isEmpty(mInputphone)) {

					query(mInputphone);
				}
				else {
					//输入框抖动
					Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
					et_query_address.startAnimation(shake);
					//手机的抖动
					Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					//设置一些参数
					//友好提示
					vibrator.vibrate(new long[]{500,1000,500,1000}, 2);
					ToastUtil.show(mContext, "亲,你还没输入或者选择电话号码呢");
				}
			
			}
		});
		// 实时监控输入的内容,然后变化查询
		et_query_address.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				String phone = et_query_address.getText().toString();
				query(phone);
			}
		});

	}

	/**
	 * @param inputPhone
	 *            查询方法
	 */
	protected void query(final String inputPhone) {
		// 开启一个子线程
		new Thread() {

			@Override
			public void run() {
				mLocation = AddressDao.phone(inputPhone);
				// 测试数据
				// Log.d(tag, mLocation);
				// 将得到的数据利用消息机制传递给主线程中,
				// 又因为查询结果是全局变量,所以这里发送一个空消息就行
				
				/*  Message msg = Message.obtain(); msg.obj=mLocation;
				 mHandler.sendMessage(msg);*/
				 
				mHandler.sendEmptyMessage(0);

			}
		}.start();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		et_query_address = (EditText) findViewById(R.id.et_query_address);
		tv_query_show = (TextView) findViewById(R.id.tv_query_show);
		bt_query_search = (Button) findViewById(R.id.bt_query_search);
		bt_setup3_select = (Button) findViewById(R.id.bt_setup3_select);

	}

	/**
	 * 接收返回值
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		// 因为就一个返回值,所以这里不作判断了
		if (requestCode==0) {
			if (data != null) {
				mInputphone = data.getStringExtra("phone");
				// 测试数据
				/* Log.d(tag, "从listview中获取的电话号码"+phone); */
				// 由于获得的电话号码格式为x-xxx-xxx-xxxx,不符合要求,要处理一下
				mInputphone = mInputphone.replaceAll("-", "").replaceAll(" ", "").trim();
				// 测试数据
				// Log.d(tag, "测试选择联系人的返回数据"+phone); 
				// 将得到的数据赋值给控件
				et_query_address.setText(mInputphone);
				//query(phone);
           
				
			}
		}
		

	}

}
