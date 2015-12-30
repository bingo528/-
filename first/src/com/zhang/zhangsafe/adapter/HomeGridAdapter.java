/**
 * 2015-9-20
 * @author:zhangbin
 */
package com.zhang.zhangsafe.adapter;

import com.zhang.zhangsafe.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeGridAdapter extends BaseAdapter {

	private String[] mTitleStrs;
	private int[] mDrawableIds;
	private Context mContext;

	/**
	 * @param mTitleStrs
	 * @param mDrawableIds
	 */
	public HomeGridAdapter(String[] mTitleStrs, int[] mDrawableIds,Context mContext) {
	  this.mTitleStrs = mTitleStrs;
	  this.mDrawableIds = mDrawableIds;
	  this.mContext =mContext;
	}

	@Override
	public int getCount() {
		
		return mTitleStrs.length;
	}

	@Override
	public Object getItem(int position) {
		
		return mTitleStrs[position];
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView = View.inflate(mContext, R.layout.home_gridview_item, null);
		}
	   
		TextView tv_grid_title = (TextView) convertView.findViewById(R.id.tv_grid_title);
	    ImageView iv_gri_icon = (ImageView) convertView.findViewById(R.id.iv_gri_icon);
		tv_grid_title.setText(mTitleStrs[position]);
		iv_gri_icon.setImageResource(mDrawableIds[position]);
	    return convertView;
	}


}
