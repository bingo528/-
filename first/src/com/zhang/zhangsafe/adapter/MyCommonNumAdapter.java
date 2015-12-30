/**
 * 2015-10-6
 * @author:zhangbin
 */
package com.zhang.zhangsafe.adapter;

import java.util.List;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.domin.CommChild;
import com.zhang.zhangsafe.domin.CommGroup;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyCommonNumAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<CommGroup> mGroup;

	/**
	 * @param mContext
	 * @param mGroup
	 */
	public MyCommonNumAdapter(Context mContext, List<CommGroup> mGroup) {
		this.mContext=mContext;
		this.mGroup=mGroup;
	}

	@Override
	public int getGroupCount() {

		return mGroup.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return mGroup.get(groupPosition).childList.size();
	}

	@Override
	public CommGroup getGroup(int groupPosition) {

		return mGroup.get(groupPosition);
	}

	@Override
	public CommChild getChild(int groupPosition, int childPosition) {

		return mGroup.get(groupPosition).childList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public boolean hasStableIds() {

		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView textView = new TextView(mContext);
		textView.setText("      "+getGroup(groupPosition).name);
		textView.setTextColor(Color.RED);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		return textView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
          if (convertView==null) {
			convertView=View.inflate(mContext, R.layout.elv_child_item, null);
		}
          //找到控件
          TextView tv_comm_number_name = (TextView) convertView.findViewById(R.id.tv_comm_number_name);
          TextView tv_comm_number_phone = (TextView) convertView.findViewById(R.id.tv_comm_number_phone);
         //赋值操作
          tv_comm_number_name.setText(getChild(groupPosition, childPosition).name);
          tv_comm_number_phone.setText(getChild(groupPosition, childPosition).number);
		return convertView;
	}

	/**
	 *孩子节点能否被选中
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;
	}

}
