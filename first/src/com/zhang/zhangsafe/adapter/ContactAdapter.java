/**
 * 2015-9-22
 * @author:zhangbin
 */
package com.zhang.zhangsafe.adapter;

import java.util.HashMap;
import java.util.List;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.adapter.BlackNumberAdapter.ViewHolder;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {

	private Context mContext;
	private List<HashMap<String, String>> mList;
	

	/**
	 * @param mContext
	 *            上下文环境
	 * @param mList
	 *            list集合
	 */
	public ContactAdapter(Context mContext, List<HashMap<String, String>> mList) {
		this.mContext = mContext;
		this.mList=mList;
		
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	
	@Override
	public HashMap<String, String> getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//listview的优化
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=View.inflate(mContext, R.layout.listview_contact_item, null);
			viewHolder=new ViewHolder();
			//获得视图上面的控件
			viewHolder.tv_contactlist_name = (TextView) convertView.findViewById(R.id.tv_contactlist_name);
			viewHolder.tv_contactlist_phone = (TextView) convertView.findViewById(R.id.tv_contactlist_phone);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
	
		//进行赋值
		if (!(TextUtils.isEmpty(getItem(position).get("name"))) &&!(TextUtils.isEmpty(getItem(position).get("phone")))) {
			
			viewHolder.tv_contactlist_name.setText(getItem(position).get("name"));
			viewHolder.tv_contactlist_phone.setText(getItem(position).get("phone"));
		}
		
		return convertView;
	}
	static class ViewHolder
	{
		TextView tv_contactlist_name;
		TextView tv_contactlist_phone;
		
	}
}
