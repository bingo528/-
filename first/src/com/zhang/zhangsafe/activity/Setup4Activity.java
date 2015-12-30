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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cb_setup4_box;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		cb_setup4_box = (CheckBox) findViewById(R.id.cb_setup4_box);
		// 回显操作
		boolean open_security = SpUtil.getBoolean(this,
				ConstantValue.OPEN_SECURITY, false);
		// 根据状态
		cb_setup4_box.setChecked(open_security);
		if (open_security) {
			cb_setup4_box.setText("安全设置已开启");
		} else {
			cb_setup4_box.setText("安全设置已关闭");

		}
		// 设置点击事件
		cb_setup4_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SpUtil.putBoolean(getApplicationContext(),
						ConstantValue.OPEN_SECURITY, isChecked);
				if (isChecked) {
					cb_setup4_box.setText("安全设置已开启");

				} else {
					cb_setup4_box.setText("安全设置已关闭");
				}
			}
		});
	}

	/**
	 * 上一页实现
	 */
	@Override
	protected void showPrePage() {
		// 启动意图
		Intent intent = new Intent(getApplicationContext(),
				Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.pre_in_translate,
				R.anim.pre_out_translate);

	}

	/**
	 * 下一页实现
	 */
	@Override
	protected void showNextPage() {
		boolean open_security = SpUtil.getBoolean(getApplicationContext(),
				ConstantValue.OPEN_SECURITY, false);
		if (open_security) {

			// 启动意图
			Intent intent = new Intent(getApplicationContext(),
					SetupOverActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.next_in_translate,
					R.anim.next_out_translate);
			SpUtil.putBoolean(getApplicationContext(),
					ConstantValue.SETUP_OVER, true);
		} else {
			ToastUtil.show(getApplicationContext(), "安全设置开关必须开启");
		}
	}
}
