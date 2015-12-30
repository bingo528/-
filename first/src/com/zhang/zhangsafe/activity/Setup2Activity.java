/**
 * 2015-9-21
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;
import com.zhang.zhangsafe.util.ToastUtil;
import com.zhang.zhangsafe.view.SettingItemView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

/**
 *
 */
public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView siv_set_bound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		siv_set_bound = (SettingItemView) findViewById(R.id.siv_set_bound);
		// 回显
		// 读取xml中数据
		String sim_number = SpUtil.getString(getApplicationContext(),
				ConstantValue.SIM_NUMBER, "");
		if (TextUtils.isEmpty(sim_number)) {
			siv_set_bound.setCheck(false);
		} else {
			siv_set_bound.setCheck(true);
		}
		// 设置点击事件
		siv_set_bound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获得选择状态
				boolean check = siv_set_bound.isCheck();
				// 如果选中
				siv_set_bound.setCheck(!check);
				if (!check) {
					TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					// 获得sim卡序列化
					String serialNumber = manager.getSimSerialNumber();
					// 存储
					SpUtil.putString(getApplicationContext(),
							ConstantValue.SIM_NUMBER, serialNumber);

				} else {
					// 移除
					SpUtil.remove(getApplicationContext(),
							ConstantValue.SIM_NUMBER);
				}
			}
		});
	}

	
	/**
	 * 上一页
	 */
	protected void showPrePage() {
		// 启动意图
		Intent intent = new Intent(getApplicationContext(),
				Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.pre_in_translate,
				R.anim.pre_out_translate);
	}

	/**
	 * 下一页
	 */
	protected void showNextPage() {
		String serialNumber = SpUtil.getString(getApplicationContext(),
				ConstantValue.SIM_NUMBER, "");

		if (!TextUtils.isEmpty(serialNumber)) {

			// 启动意图
			Intent intent = new Intent(getApplicationContext(),
					Setup3Activity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.next_in_translate,
					R.anim.next_out_translate);
		} else {
			ToastUtil.show(getApplicationContext(), "请绑定sim卡");
		}
	}
	
}
