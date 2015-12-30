/**
 * 2015-9-29
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ToastLocationActivity extends Activity {

	private ImageView iv_drag;
	private Button bt_toast_location_top;
	private Button bt_toast_location_bottom;
	private WindowManager mWM;
	private int mScreenHeight;
	private int mScreenWidth;
	private long[] mHits = new long[2];
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toast_location);
		initUI();
		
	}

	

	/**
	 * 初始化数据
	 */
	private void initUI() {
		iv_drag = (ImageView) findViewById(R.id.iv_drag);
		bt_toast_location_top = (Button) findViewById(R.id.bt_toast_location_top);
		bt_toast_location_bottom = (Button) findViewById(R.id.bt_toast_location_bottom);
		// 读取数据
		int locationX = SpUtil.getInt(getApplicationContext(),
				ConstantValue.LOCATION_X, 0);
		int locationY = SpUtil.getInt(getApplicationContext(),
				ConstantValue.LOCATION_Y, 0);
		//得到屏幕管理者
		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		mScreenHeight = mWM.getDefaultDisplay().getHeight();
		mScreenWidth = mWM.getDefaultDisplay().getWidth();
		// 设置宽和高
		LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// 将左上角的坐标作用在iv_drag对应规则参数上
		layoutParams.leftMargin = locationX;
		layoutParams.topMargin = locationY;
		// 将以上规则作用在iv_drag上,回显操作
		iv_drag.setLayoutParams(layoutParams);
		//如果到一半,上面按钮显示,下面不显示
		if(locationY>mScreenHeight/2){
			bt_toast_location_bottom.setVisibility(View.INVISIBLE);
			bt_toast_location_top.setVisibility(View.VISIBLE);
		}else{
			bt_toast_location_bottom.setVisibility(View.VISIBLE);
			bt_toast_location_top.setVisibility(View.INVISIBLE);
		}
		iv_drag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
		        mHits[mHits.length-1] = SystemClock.uptimeMillis();
		        if(mHits[mHits.length-1]-mHits[0]<500){
		        	//满足双击事件后,调用代码
		        	int left = mScreenWidth/2 - iv_drag.getWidth()/2;
		        	int top = mScreenHeight/2 - iv_drag.getHeight()/2;
		        	int right = mScreenWidth/2+iv_drag.getWidth()/2;
		        	int bottom = mScreenHeight/2+iv_drag.getHeight()/2;
		        	
		        	//控件按以上规则显示
		        	iv_drag.layout(left, top, right, bottom);
		        	
		        	//存储最终位置
		        	SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, iv_drag.getLeft());
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, iv_drag.getTop());
		        }
			}
		});
		//拖拽事件
		iv_drag.setOnTouchListener(new OnTouchListener() {

			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 监听状态
				switch (event.getAction()) {
				// 按下
				case MotionEvent.ACTION_DOWN:
					// 起始位置
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;
				// 移动
				case MotionEvent.ACTION_MOVE:
					// 移动的位置
					int moveX = (int) event.getRawX();
					int moveY = (int) event.getRawY();
					// 移动的了多少
					int disX = moveX - startX;
					int disY = moveX - startY;
					// 获得屏幕的左上角
					int left = iv_drag.getLeft() + disX;
					int top = iv_drag.getTop() + disY;
					int right = iv_drag.getRight() + disX;
					int bottom = iv_drag.getBottom() + disY;
					//容错处理(iv_drag不能拖拽出手机屏幕)
					//左边缘不能超出屏幕
					if(left<0){
						return true;
					}
					
					//右边边缘不能超出屏幕
					if(right>mScreenWidth){
						return true;
					}
					
					//上边缘不能超出屏幕可现实区域
					if(top<0){
						return true;
					}
					
					//下边缘(屏幕的高度-22 = 底边缘显示最大值)
					if(bottom>mScreenHeight - 22){
						return true;
					}
					//两个按钮的显示与不显示控制
					if(top>mScreenHeight/2){
						bt_toast_location_bottom.setVisibility(View.INVISIBLE);
						bt_toast_location_top.setVisibility(View.VISIBLE);
					}else{
						bt_toast_location_bottom.setVisibility(View.VISIBLE);
						bt_toast_location_top.setVisibility(View.INVISIBLE);
					}
					
					// 告知控件
					iv_drag.layout(left, top, right, bottom);
					// 重置一次坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;
				// 抬起
				case MotionEvent.ACTION_UP:
					// 存储当前位置
					SpUtil.putInt(getApplicationContext(),
							ConstantValue.LOCATION_X, iv_drag.getLeft());
					SpUtil.putInt(getApplicationContext(),
							ConstantValue.LOCATION_Y, iv_drag.getTop());

					break;

				default:
					break;
				}

				return false;
			}
		});
	}

}
