/**
 * 2015-9-20
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

public class SettingItemView extends RelativeLayout {

	

	private static final String NAME_SPACE = "http://schemas.android.com/apk/res/com.zhang.zhangsafe";
	
	private static final String tag = "SettingItemView";
	private CheckBox cb_set_box;
	private TextView tv_set_des;

	private String mDestitle;

	private String mDesoff;

	private String mDeson;



	/**
	 * @param context 上下文环境
	 */
	public SettingItemView(Context context) {
		this(context,null);
		
	}

	/**
	 * @param context 上下文环境
	 * @param attrs	属性
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs,0);

	}

	/**
	 * @param context 上下文环境
	 * @param attrs 属性
	 * @param defStyle	主题
	 */
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//将xml转view对象
		View.inflate(context, R.layout.setting_item_view, this);
		TextView tv_set_title = (TextView) findViewById(R.id.tv_set_title);
        tv_set_des = (TextView) findViewById(R.id.tv_set_des);
        cb_set_box = (CheckBox) findViewById(R.id.cb_set_box);
	    cb_set_box.isChecked();
	    //获取自定义属性
	    initAttrs(attrs,context);
	    //获得布局文件中定义的字符串并赋值
	    tv_set_title.setText(mDestitle);
	}

	
	/**
	 * 获得自定义属性
	 */
	private void initAttrs(AttributeSet attrs,Context context) {
		/*int title = attrs.getAttributeResourceValue(NAME_SPACE, "destitle",0);
		mDestitle=context.getResources().getText(title).toString();
		int desoff = attrs.getAttributeResourceValue(NAME_SPACE, "desoff",0);
		mDesoff=context.getResources().getText(desoff).toString();
		int deson = attrs.getAttributeResourceValue(NAME_SPACE, "deson",0);
		mDeson=context.getResources().getText(deson).toString();*/
		
		mDestitle = attrs.getAttributeValue(NAME_SPACE, "destitle");
		mDesoff = attrs.getAttributeValue(NAME_SPACE, "desoff");
		mDeson = attrs.getAttributeValue(NAME_SPACE, "deson");
		//测试
		/* mDestitle = getResources().getString(Integer.parseInt(mDestitle.replace("@","")));
		 mDesoff = getResources().getString(Integer.parseInt(mDesoff.replace("@","")));
		 mDeson = getResources().getString(Integer.parseInt(mDeson.replace("@","")));*/
		

		
		//测试一下
		/*Log.d(tag, mDestitle);
		Log.d(tag, mDesoff);
		Log.d(tag, mDeson);*/
	}

	/**
	 * 设置绑定在一起
	 */
	public void setCheck(boolean isCheck) {
		cb_set_box.setChecked(isCheck);
		if (isCheck) {
			tv_set_des.setText(mDeson);
		} 
		else {
			tv_set_des.setText(mDesoff);

		}
	}

	/**
	 * 判断是否开启
	 * @return 是否开启
	 */
	public boolean isCheck() {
		return cb_set_box.isChecked();
	}
	
}
