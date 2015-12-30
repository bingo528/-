/**
 * 2015-9-29
 * @author:zhangbin
 */
package com.zhang.zhangsafe.view;

import com.zhang.zhangsafe.R;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingClickView extends RelativeLayout {

	

	
	private static final String tag = "SettingItemView";
	private TextView tv_setclick_title;
	private TextView tv_setclick_des;
	



	/**
	 * @param context 上下文环境
	 */
	public SettingClickView(Context context) {
		this(context,null);
		
	}

	/**
	 * @param context 上下文环境
	 * @param attrs	属性
	 */
	public SettingClickView(Context context, AttributeSet attrs) {
		this(context, attrs,0);

	}

	/**
	 * @param context 上下文环境
	 * @param attrs 属性
	 * @param defStyle	主题
	 */
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//将xml转view对象
		View view = View.inflate(context, R.layout.setting_click_view, this);
		//找到视图上的控件
		
		tv_setclick_title = (TextView) findViewById(R.id.tv_setclick_title);
		tv_setclick_des = (TextView) findViewById(R.id.tv_setclick_des);
	
	}
	//设置标题
	/**
	 * @param title 传递过来的内容
	 * 设置标题
	 */
	public void setTitle(String title)
	{
		tv_setclick_title.setText(title);
	}
	/**
	 * @param des 传递过来的内容
	 * 设置描述信息
	 */
	public void setDes(String des)
	{
		tv_setclick_des.setText(des);
	}

	
	

	
	
}
