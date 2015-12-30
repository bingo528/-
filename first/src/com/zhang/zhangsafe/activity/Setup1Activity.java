/**
 * 2015-9-21
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import com.zhang.zhangsafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends BaseSetupActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}
	
	/**
	*上一页没有就是空
	*/
	protected void showPrePage() {
	}
	/**
	*下一页的具体实现方法
	*/
	protected void showNextPage() {
		//启动意图
				Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.next_in_translate, R.anim.next_out_translate);
	}

	/*public void nextPage(View view)
	{
		Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
		startActivity(intent);
		finish();
	}*/
	
}

