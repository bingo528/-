/**
 * 2015-9-22
 * @author:zhangbin
 */
package com.zhang.zhangsafe.adapter;

import java.util.ArrayList;


import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.db.dao.BlackNumberDao;
import com.zhang.zhangsafe.domin.BlackNumberBean;
import com.zhang.zhangsafe.util.ToastUtil;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BlackNumberAdapter extends BaseAdapter {

	private static final String tag = "BlackNumberAdapter";
	private Context mContext;
	private ArrayList<BlackNumberBean> mList;
	private BlackNumberDao mDao;
	
	/**
	 * @param mContext
	 *            上下文环境
	 * @param mList
	 *            list集合
	 */
	public BlackNumberAdapter(Context mContext, ArrayList<BlackNumberBean> mList) {
		this.mContext = mContext;
		this.mList = mList;
		//测试数据
		//Log.d(tag, "数据适配器的集合长度:"+mList.size());

	}
 
	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public BlackNumberBean getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		mDao = BlackNumberDao.getInstance(mContext);
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=View.inflate(mContext, R.layout.listview_blacknumber_item, null);;
		 viewHolder=new ViewHolder();
		// 获得视图上面的控件
		  viewHolder.tv_blacknumber_phone = (TextView) convertView.findViewById(R.id.tv_blacknumber_phone);
		  viewHolder.tv_blacknumber_mode = (TextView) convertView.findViewById(R.id.tv_blacknumber_mode);
		  viewHolder.iv_blacknumber_del = (ImageView) convertView.findViewById(R.id.iv_blacknumber_del);
		  convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		//listview上的删除操作
		if (mList!=null && mList.size()>0) {
			viewHolder.iv_blacknumber_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
						//删除数据库
						mDao.delete(mList.get(position).phone);
						//删除条目
						mList.remove(position);
					//刷新
						BlackNumberAdapter.this.notifyDataSetChanged();

					ToastUtil.show(mContext, "轻轻一点,我就被删除了");
				}
			});
		}
		
		viewHolder.tv_blacknumber_phone.setText(mList.get(position).phone);
		int mode = Integer.parseInt(mList.get(position).mode);
		switch (mode) {
		case 1:
			viewHolder.tv_blacknumber_mode.setText("拦截短信");
			break;
		case 2:

			viewHolder.tv_blacknumber_mode.setText("拦截电话");
			break;
		case 3:

			viewHolder.tv_blacknumber_mode.setText("拦截所有");
			break;

		default:
			break;
		}
		

		// 进行赋值

		return convertView;
	}
	static class ViewHolder
	{
		TextView tv_blacknumber_phone;
		TextView tv_blacknumber_mode;
		ImageView iv_blacknumber_del;
	}
}
