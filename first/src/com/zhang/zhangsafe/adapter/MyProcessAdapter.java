/**
 * 2015-10-4
 * @author:zhangbin
 */
package com.zhang.zhangsafe.adapter;

import java.util.ArrayList;
import java.util.List;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.domin.AppBean;
import com.zhang.zhangsafe.domin.ProcessBean;
import com.zhang.zhangsafe.util.ConstantValue;
import com.zhang.zhangsafe.util.SpUtil;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProcessAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ProcessBean> mProcessInfo;
	private ArrayList<ProcessBean> mCoustomerList;
	private ArrayList<ProcessBean> mSystemList;

	/**
	 * @param mContext
	 * @param mProcessInfo
	 * @param mCustomerList
	 * @param mSystemList
	 */
	public MyProcessAdapter(Context mContext,
			ArrayList<ProcessBean> mProcessInfo,
			ArrayList<ProcessBean> mCoustomerList,
			ArrayList<ProcessBean> mSystemList) {
		this.mContext = mContext;
		this.mProcessInfo = mProcessInfo;
		this.mCoustomerList = mCoustomerList;
		this.mSystemList = mSystemList;
	}

	@Override
	public int getCount() {

		if (SpUtil.getBoolean(mContext, ConstantValue.SHOW_SYSTEM, false)) {

			return mCoustomerList.size() + mSystemList.size() + 2;
		} else {
			return mCoustomerList.size() + 1;
		}
	}

	@Override
	public int getViewTypeCount() {

		return super.getViewTypeCount() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0 || position == mCoustomerList.size() + 1) {
			// 返回灰色的那个条目
			return 0;
		} else {
			return 1;
		}

	}

	@Override
	public ProcessBean getItem(int position) {
		if (position == 0 || position == mCoustomerList.size() + 1) {
			// 返回灰色的那个条目
			return null;
		} else {
			if (mCoustomerList!=null && mSystemList!=null) {
				if (position < mCoustomerList.size() + 1) {
					// 使用用户的集合
					return mCoustomerList.get(position - 1);
				} else {
					return mSystemList.get(position -mCoustomerList.size() - 2);
				}
			}
			else {
				return null;
			}
			
		}
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int viewType = getItemViewType(position);
		// 如果第一种样式
		if (viewType == 0) {
			ViewTitleHolder viewTitleHolder = null;
			// 复用convertView

			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.list_item_app_title, null);
				// 复用viewhold
				viewTitleHolder = new ViewTitleHolder();
				// 找到控件
				viewTitleHolder.tv_app_des = (TextView) convertView
						.findViewById(R.id.tv_app_des);

				convertView.setTag(viewTitleHolder);
			} else {
				viewTitleHolder = (ViewTitleHolder) convertView.getTag();
			}
			// 判断一下
			if (position == 0) {
				viewTitleHolder.tv_app_des.setText("用户进程("
						+ mCoustomerList.size() + ")");
			} else {

				viewTitleHolder.tv_app_des.setText("系统进程(" + mSystemList.size()
						+ ")");
			}

			return convertView;
		} else {
			ViewHolder viewHolder = null;
			// 复用convertView

			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.list_item_process, null);
				// 复用viewhold
				viewHolder = new ViewHolder();
				// 找到控件
				viewHolder.tv_process_name = (TextView) convertView
						.findViewById(R.id.tv_process_name);
				viewHolder.tv_process_memory = (TextView) convertView
						.findViewById(R.id.tv_process_memory);
				viewHolder.iv_icon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				viewHolder.cb_process_box = (CheckBox) convertView
						.findViewById(R.id.cb_process_box);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			// 给控件赋值
			viewHolder.iv_icon.setBackgroundDrawable(getItem(position).icon);
			viewHolder.tv_process_name.setText(getItem(position).name);
			String strSize = Formatter.formatFileSize(mContext,
					getItem(position).memSize);
			viewHolder.tv_process_memory.setText(strSize);
			// 判断一下,不让操作这个手机卫士
			if (getItem(position).getPackageName().equals(
					mContext.getPackageName())) {
				viewHolder.cb_process_box.setVisibility(View.GONE);
			} else {

				viewHolder.cb_process_box.setVisibility(View.VISIBLE);
			}
			viewHolder.cb_process_box.setChecked(getItem(position).isCheck);
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv_process_memory;
		TextView tv_process_name;
		ImageView iv_icon;
		CheckBox cb_process_box;
	}

	static class ViewTitleHolder {
		TextView tv_app_des;

	}
}
