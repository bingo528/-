/**
 * 2015-9-28
 * @author:zhangbin
 */
package com.zhang.zhangsafe.location.listenter;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.db.dao.AddressDao;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;
import com.zhang.zhangsafe.util.ToastUtil;

import android.app.DownloadManager.Query;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

public class MyPhoneStateListener extends PhoneStateListener {
	/**
	 * @param applicationContext
	 */
	public MyPhoneStateListener(Context mContext) {
		this.mContext = mContext;
	}

	private static final String tag = "MyPhoneStateListener";
	private Context mContext;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	private View mView;
	private WindowManager mWM;
	private TextView tv_toast;
	private String phoneLocation;
	private int[] mDrawableIds;
	private int mScreenHeight;
	private int mScreenWidth;
	private int sta;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			tv_toast.setText(phoneLocation);
		};

	};

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {

		super.onCallStateChanged(state, incomingNumber);
		// 得到屏幕管理者
		mWM = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mScreenHeight = mWM.getDefaultDisplay().getHeight();
		mScreenWidth = mWM.getDefaultDisplay().getWidth();
		sta=state;
		// 判断状态
		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:
			// 测试数据
			// Log.d(tag, "待机状态");
			if (mWM != null && mView != null) {

				mWM.removeView(mView);
			}
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			// 测试数据
			// Log.d(tag, "响铃状态");
			// 当响铃的状态下,弹出一个toast
			showToast(incomingNumber);
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			// 测试数据
			// Log.d(tag, "挂机状态");
			break;

		default:
			break;
		}
	}

	/**
	 * 响铃状态下弹出一个吐司
	 */
	private void showToast(String inPhone) {
		// 测试数据
		// ToastUtil.show(mContext, inPhone);
		// Log.d(tag, "电话监听的手机号"+inPhone);
		final WindowManager.LayoutParams params = mParams;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		// 类型,响铃的时候显示吐司
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("Toast");
		// 指定吐司的所在位置
		params.gravity = Gravity.LEFT + Gravity.TOP;
		// 使用布局文件显示toast
		mView = View.inflate(mContext, R.layout.toast_view, null);
		mView.setOnTouchListener(new OnTouchListener() {
			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
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
					params.x = params.x + disX;
					params.y = params.y + disY;
					// 容错处理
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.x > mScreenWidth - mView.getWidth()) {
						params.x = mScreenWidth - mView.getWidth();
					}
					if (params.y > mScreenHeight - mView.getWidth()) {
						params.y = mScreenHeight - mView.getWidth();
					}
					// 告知窗体按照手势,进行更新
					mWM.updateViewLayout(mView, params);
					// 重置一次坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					// 存储当前位置
					SpUtil.putInt(mContext, ConstantValue.LOCATION_X,
							params.x);
					SpUtil.putInt(mContext,
							ConstantValue.LOCATION_Y, params.y);

					break;

				default:
					break;
				}
				return true;
			}
		});
		tv_toast = (TextView) mView.findViewById(R.id.tv_toast);
		// 读取xml中存储的位置
		params.x = SpUtil.getInt(mContext, ConstantValue.LOCATION_X, 0);
		params.y = SpUtil.getInt(mContext, ConstantValue.LOCATION_Y, 0);
		mDrawableIds = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		// 从xml读取索引
		int toastStyleIndex = SpUtil.getInt(mContext,
				ConstantValue.TOAST_STYLE, 0);
		// 设置一个背景色
		tv_toast.setBackgroundResource(mDrawableIds[toastStyleIndex]);

		mWM = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		// 让窗体挂载view上
		mWM.addView(mView, params);
		// 电话来了,查询电话的归属地
		query(inPhone);

	}

	/**
	 * @param inPhone
	 *            查询的电话号码
	 */
	private void query(final String inPhone) {
		new Thread() {

			public void run() {
				phoneLocation = AddressDao.phone(inPhone);
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}
	public void callShowshowToast(String inPhone) 
	{
		
		if (sta==TelephonyManager.CALL_STATE_IDLE) {
			if (mWM != null && mView != null) {

				mWM.removeView(mView);
			}
		}
		else {
			showToast(inPhone);
		}
	}
}
