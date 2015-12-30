package com.zhang.zhangsafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * @author zhangbin
 * 自定义TextView
 */
public class FocusTextView extends TextView {

	public FocusTextView(Context context) {
		super(context);
	}

	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override

	public boolean isFocused() {
		return true;
	}

}
