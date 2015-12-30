package com.zhang.zhangsafe.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	/**
	 * @param context 上下文环境
	 * @param text 需要显示的内容
	 */
	public static void show(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

}
