/**
 * 2015-10-4
 * @author:zhangbin
 */
package com.zhang.zhangsafe.adapter;

import java.util.ArrayList;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.domin.AppBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAppAdapter extends BaseAdapter {

	
	private Context mContext;
	private ArrayList<AppBean> appList;
	private ArrayList<AppBean> mCoustomerList;
	private ArrayList<AppBean> mSystemList;

	/**
	 * 构造方法
	 */
	public MyAppAdapter(Context mContext,ArrayList<AppBean> appList,ArrayList<AppBean> mCoustomerList,ArrayList<AppBean> mSystemList) {
		super();
		this.mContext=mContext;
		this.appList=appList;
		this.mCoustomerList=mCoustomerList;
		this.mSystemList=mSystemList;
	}



	@Override
	public int getCount() {

		return appList.size()+2;
	}
	@Override
	public int getViewTypeCount() {
		
		return super.getViewTypeCount()+1;
	}
	@Override
	public int getItemViewType(int position) {
		if (position==0 ||position==mCoustomerList.size()+1) {
			//返回灰色的那个条目
			return 0;
		}
		else {
			return 1;
		}
		
		
	}
	@Override
	public AppBean getItem(int position) {
		if (position==0 ||position==mCoustomerList.size()+1) {
			//返回灰色的那个条目
			return null;
		}
		else {
			if (position<mCoustomerList.size()+1) {
				//使用用户的集合
			return	mCoustomerList.get(position-1);
			}
			else {
				return	mSystemList.get(position-mCoustomerList.size()-2);
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
		//如果第一种样式
		if (viewType==0) {
			ViewTitleHolder viewTitleHolder=null;
			//复用convertView
			
			if (convertView==null) {
				convertView=View.inflate(mContext, R.layout.list_item_app_title, null);
			  //复用viewhold
				viewTitleHolder=new ViewTitleHolder();
				//找到控件
				viewTitleHolder.tv_app_des = (TextView) convertView.findViewById(R.id.tv_app_des);
				
			   convertView.setTag(viewTitleHolder);
			}
			else {
				viewTitleHolder=(ViewTitleHolder) convertView.getTag();
			}
			//判断一下
			if (position==0) {
				viewTitleHolder.tv_app_des.setText("用户应用("+mCoustomerList.size()+")");
			}
			else {
				
				viewTitleHolder.tv_app_des.setText("系统应用("+mSystemList.size()+")");
			}
			
			
			return convertView;
		}
		else {
		ViewHolder viewHolder=null;
		//复用convertView
		
		if (convertView==null) {
			convertView=View.inflate(mContext, R.layout.list_item_app, null);
		  //复用viewhold
			viewHolder=new ViewHolder();
			//找到控件
			viewHolder.tv_app_name = (TextView) convertView.findViewById(R.id.tv_process_name);
			viewHolder.tv_app_path = (TextView) convertView.findViewById(R.id.tv_process_memory);
			viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
		   convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		//给控件赋值
		viewHolder.iv_icon.setBackgroundDrawable(getItem(position).icon);
		viewHolder.tv_app_name.setText(getItem(position).name);
		//判断一下
		if (getItem(position).isSd) {
			viewHolder.tv_app_path.setText("sd卡应用");
		}
		else {
			
			viewHolder.tv_app_path.setText("手机应用");
		}
		
		return convertView;
		}
	}
	 static class ViewHolder{
	    	TextView tv_app_path;
	    	TextView tv_app_name;
	    	ImageView iv_icon;
	    } 
	    static class ViewTitleHolder{ 
	    	TextView tv_app_des;
	    	
	    }
}

