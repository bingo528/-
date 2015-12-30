/**
 * 2015-9-22
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends Activity {

	private GestureDetector gestureDetector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if (e1.getX()-e2.getX()>0) {
					
					showNextPage();
				} 
				if (e1.getX()-e2.getX()<0){
					showPrePage();
				}
				
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	


	/**
	 * 上一页方法
	 */
	protected abstract void showPrePage();

	/**
	 * 下一页方法
	 */
	protected abstract void showNextPage();
	
	//点击下一页按钮的时候,根据子类的showNextPage方法做相应跳转
		public void nextPage(View view){
			showNextPage(); 
		}
		
		//点击上一页按钮的时候,根据子类的showPrePage方法做相应跳转
		public void prePage(View view){
			showPrePage();
		}
	/**
	*监听屏幕上面相应事件
	*/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	@Override
	public void onBackPressed() {
		showPrePage();
		super.onBackPressed();
	}
	
}
