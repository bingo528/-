/**
 * 2015-9-22
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.adapter.ContactAdapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactListActivity extends Activity {

	protected static final String tag = "ContactListActivity";
	protected static final int RESULT_CODE = 0;
	private ListView lv_contact;
	private List<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
	private ContactAdapter mContactAdapter;
	private Handler mHandler = new Handler() {
		

		public void handleMessage(android.os.Message msg) {
			mList = (List<HashMap<String, String>>) msg.obj;
			//调用联系人的适配器
			mContactAdapter = new ContactAdapter(
					getApplicationContext(), mList);
			lv_contact.setAdapter(mContactAdapter);
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		initUI();
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		new Thread() {

			@Override
			public void run() {
				// 获得ContentResolver对象
				ContentResolver resolver = getContentResolver();
				// 查询操作
				String spec = "content://com.android.contacts/raw_contacts";
				Uri uri = Uri.parse(spec);
				Cursor cursor = resolver.query(uri,
						new String[] { "contact_id" }, null, null, null);
				mList.clear();
				// 循环遍历

				while (cursor.moveToNext()) {
					// 取出数据
					
					String id = cursor.getString(0);
					// 测试一下
					/* Log.d(tag, "id=" + id); */
					// 然后根据id查询
					if (id!=null) {
						
						Cursor indexCursor = resolver.query(
								Uri.parse("content://com.android.contacts/data"),
								new String[] { "data1", "mimetype" },
								"raw_contact_id = ?", new String[] { id }, null);
						HashMap<String, String> hashMap = new HashMap<String, String>();
						// 遍历查询
						while (indexCursor.moveToNext()) {
							// 取出数据
							String data = indexCursor.getString(0);
							String type = indexCursor.getString(1);
							// 测试一下
							/*Log.d(tag, data); 
							 * Log.d(tag, type);
							 */
						// 数据封装
						// 先判断是什么样的数据类型
						if (!(TextUtils.isEmpty(data))) {

							if ((type.equals("vnd.android.cursor.item/name"))  ) {
								hashMap.put("name", data);
							} else if ((type.equals("vnd.android.cursor.item/phone_v2")) ) {
								hashMap.put("phone", data);
							}
						}
					}
						indexCursor.close();
						mList.add(hashMap);
					}
				}
				// 释放资源
				cursor.close();
				// 发送一个空消息告知遍历完成
				// mHandler.sendEmptyMessage(Empty_Message);
				Message msg = Message.obtain();
				msg.obj = mList;
				mHandler.sendMessage(msg); 
			}
		}.start();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		lv_contact = (ListView) findViewById(R.id.lv_contact);
		// 设置listview的点击事件
		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mContactAdapter!=null) {
					
					HashMap<String, String> hashMap = mContactAdapter.getItem(position);
				   //取出map中key对应的值
					String phone = hashMap.get("phone");
					//将得到的结果返回给调用者
					Intent intent = new Intent();
					intent.putExtra("phone", phone);
					setResult(RESULT_CODE, intent);
					finish();
				}
			}
		
		
		});
	}
}
