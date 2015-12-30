/**
 * 2015-10-6
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import java.util.List;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.adapter.MyCommonNumAdapter;
import com.zhang.zhangsafe.db.dao.CommNumberDao;
import com.zhang.zhangsafe.domin.CommGroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class CommonNumberQueryActivity extends Activity {

	private ExpandableListView elv_common_number;
	private List<CommGroup> mGroup;
	private MyCommonNumAdapter mCommonNumAdapter;
    private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mContext=this;
		setContentView(R.layout.ativity_comm_numquery);
		//初始化UI
		initUI();
		//初始化数据
		initData();
	}

	
	/**
	 * 初始化UI
	 */
	private void initUI() {
		elv_common_number = (ExpandableListView) findViewById(R.id.elv_common_number);
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		CommNumberDao commNumberDao = new CommNumberDao();
		mGroup = commNumberDao.queryGroup();
		mCommonNumAdapter = new MyCommonNumAdapter(mContext,mGroup);
		elv_common_number.setAdapter(mCommonNumAdapter);
		//给孩子节点注册点击事件'
		elv_common_number.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				startCall(mCommonNumAdapter.getChild(groupPosition, childPosition).number);
				return false;
			}
		});
	}


	/**
	 * @param number 传过来的电话号码
	 */
	protected void startCall(String number) {
		
		//开启打电话的意图对象
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:"+number));
		
		startActivity(intent);
	}

}
