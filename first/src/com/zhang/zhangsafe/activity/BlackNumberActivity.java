/**
 * 2015-9-28
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;

import java.util.ArrayList;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.adapter.BlackNumberAdapter;
//导入adater包内的适配器
//import com.zhang.zhangsafe.adapter.BlackNumberAdapter;
import com.zhang.zhangsafe.db.dao.BlackNumberDao;
import com.zhang.zhangsafe.domin.BlackNumberBean;
import com.zhang.zhangsafe.util.ToastUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BlackNumberActivity extends Activity {

	protected static final int REQUEST_CODE = 0;
	protected static final String tag = "BlackNumberActivity";
	private Button bt_blacknumber_add;
	private ListView lv_blacknumber;
	private Context mContext;
	private EditText et_dialog_blacknum;
	private BlackNumberDao mDao;
	private int mCount;
	private BlackNumberAdapter mBlackNumberAdapter;
	private boolean mIsLoad = false;
	private ArrayList<BlackNumberBean> mList = new ArrayList<BlackNumberBean>();
	private int mode = 1;
	private Button bt_blacknum_refesh;
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			// query = (ArrayList<BlackNumberBean>) msg.obj;
			if (mBlackNumberAdapter == null) {
				mBlackNumberAdapter = new BlackNumberAdapter(
						getApplicationContext(), mList);
				/*mBlackNumberAdapter = new BlackNumberAdapter();*/
				// 测试数据F
				// Log.d(tag, "查询后集合的大小"+queryAll.size());
				lv_blacknumber.setAdapter(mBlackNumberAdapter);
			} else {
				mBlackNumberAdapter.notifyDataSetChanged();
			}

		};
	};

	/*// 创建数据适配器
    class BlackNumberAdapter extends BaseAdapter{
	
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
		viewHolder.iv_blacknumber_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//删除数据库
				mDao.delete(mList.get(position).phone);
				//删除条目
				mList.remove(position);
				//删除按钮更新
				if (mBlackNumberAdapter!=null) {
					mBlackNumberAdapter.notifyDataSetChanged();
				} 
				//删除按钮的吐司显示
				ToastUtil.show(mContext, "轻轻一点,我就被删除了,刷新看一下");
			}
		});
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
		
		return convertView;
		}
    }
    static class ViewHolder
	{
		TextView tv_blacknumber_phone;
		TextView tv_blacknumber_mode;
		ImageView iv_blacknumber_del;
	}*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacknumber_list);
		initUI();
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		new Thread() {

			public void run() {
				// 获取黑名单对象

				mDao = BlackNumberDao.getInstance(mContext);
				// 查询所有数据
				// query = mDao.queryAll();
				// 根据要求查询数据
				mList = mDao.queryByIndex(0);
				// 获取总值

				mCount = mDao.getCount();
				// 发送给主线程
				Message msg = Message.obtain();
				msg.obj = mList;
				mHandler.sendMessage(msg);
				// mHandler.sendEmptyMessage(0);

			}

		}.start();
	}

	/**
	 * 添加的对话框
	 */
	protected void showDialog() {
		// 创建Builder对象
		Builder builder = new AlertDialog.Builder(this);
		// 创建AlertDialog对象
		final AlertDialog dialog = builder.create();
		// 将编写好的布局文件转换成view
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_blacknumber_add, null);
		dialog.setView(view, 0, 0, 0, 0);
		Button bt_setup3_select = (Button) view
				.findViewById(R.id.bt_setup3_select);
		RadioGroup rg_black_group = (RadioGroup) view
				.findViewById(R.id.rg_black_group);
		et_dialog_blacknum = (EditText) view
				.findViewById(R.id.et_dialog_blacknum);
		Button bt_black_submit = (Button) view
				.findViewById(R.id.bt_black_submit);
		Button bt_black_cancle = (Button) view
				.findViewById(R.id.bt_black_cancle);
		// 选择联系人的点击事件
		bt_setup3_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 当点击触发的时候跳转到另一个页面,显示系统所有的联系人
				Intent intent = new Intent(mContext, ContactListActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		//选择的点击事件
		rg_black_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rb_sms:
							mode = 1;
							break;
						case R.id.rb_phone:
							mode = 2;
							break;
						case R.id.rb_all:
							mode = 3;
							break;

						default:
							break;
						}
					}
				});
		// 确定按钮的点击事件
		bt_black_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取输入框的电话号码
				String phone = et_dialog_blacknum.getText().toString();

				if (!TextUtils.isEmpty(phone)) {
					mDao.insert(phone, mode + "");
					// 保持数据的一致
					BlackNumberBean bean = new BlackNumberBean();
					bean.phone = phone;
					bean.mode = mode + "";
					// 插入顶部
					mList.add(0, bean);
					// 刷新
					if (mBlackNumberAdapter != null) {
						mBlackNumberAdapter.notifyDataSetChanged();
					}
					dialog.dismiss();
				} else {
					ToastUtil.show(mContext, "请输入或者选择拦截的电话号码");
				}

			}
		});
		// 对话框上面的取消按钮的点击事件
		bt_black_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		bt_blacknumber_add = (Button) findViewById(R.id.bt_blacknumber_add);
		lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);
		bt_blacknum_refesh = (Button) findViewById(R.id.bt_blacknum_refesh);
		bt_blacknum_refesh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 刷新
				if (mBlackNumberAdapter != null) {
					mBlackNumberAdapter.notifyDataSetChanged();
				}
				ToastUtil.show(mContext, "我已经刷新了,不信你看看");
			}
		});
		bt_blacknumber_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 显示一个对话框
				showDialog();
			}

		});
		//设置条目的点击事件
		lv_blacknumber.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//测试数据
				//Log.d(tag, "条目的点击事件");
				//
			}
			
		});
		//长时间点击事件
		lv_blacknumber.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				//测试数据
				//Log.d(tag, "长时间的点击事件");
				//测试数据
				//Log.d(tag, "取出点击的内容"+mBlackNumberAdapter.getItemId(position));
				//弹出修改对话框
				showModifyDialog();
				return false;
			}
			
		});
		//设置listview的滚动事件的监听
		lv_blacknumber.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (mList != null) {

					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
							&& lv_blacknumber.getLastVisiblePosition() >= mList
									.size() - 1 && !mIsLoad) {
						if (mCount > mList.size()) {
							// 开启一个线程
							new Thread() {

								public void run() {
									// 获取黑名单对象
									mDao = BlackNumberDao.getInstance(mContext);
									// 查询所有数据
									// query = mDao.queryAll();
									// 根据要求查询数据
									ArrayList<BlackNumberBean> moreData = mDao
											.queryByIndex(mList.size());
									// 添加下一个数据
									mList.addAll(moreData);
									// 发送给主线程
									Message msg = Message.obtain();
									msg.obj = mList;
									mHandler.sendMessage(msg);
									// mHandler.sendEmptyMessage(0);
								}

							}.start();
						}

					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	/**
	 * 修改拦截方式
	 */
	protected void showModifyDialog() {

		// 创建Builder对象
		Builder builder = new AlertDialog.Builder(this);
		// 创建AlertDialog对象
		final AlertDialog dialog = builder.create();
		// 将编写好的布局文件转换成view
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_blacknumber_modify, null);
		dialog.setView(view, 0, 0, 0, 0);
		Button bt_setup3_select = (Button) view
				.findViewById(R.id.bt_setup3_select);
		RadioGroup rg_black_group = (RadioGroup) view
				.findViewById(R.id.rg_black_group);
		et_dialog_blacknum = (EditText) view
				.findViewById(R.id.et_dialog_blacknum);
		Button bt_black_submit = (Button) view
				.findViewById(R.id.bt_black_submit);
		Button bt_black_cancle = (Button) view
				.findViewById(R.id.bt_black_cancle);
		// 选择联系人的点击事件
		bt_setup3_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 当点击触发的时候跳转到另一个页面,显示系统所有的联系人
				Intent intent = new Intent(mContext, ContactListActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		rg_black_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rb_sms:
							mode = 1;
							break;
						case R.id.rb_phone:
							mode = 2;
							break;
						case R.id.rb_all:
							mode = 3;
							break;

						default:
							break;
						}
					}
				});
		// 确定按钮的点击事件
		bt_black_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取输入框的电话号码
				String phone = et_dialog_blacknum.getText().toString();

				if (!TextUtils.isEmpty(phone)) {
					mDao.insert(phone, mode + "");
					// 保持数据的一致
					BlackNumberBean bean = new BlackNumberBean();
					bean.phone = phone;
					bean.mode = mode + "";
					// 插入顶部
					mList.add(0, bean);
					// 刷新
					if (mBlackNumberAdapter != null) {
						mBlackNumberAdapter.notifyDataSetChanged();
					}
					dialog.dismiss();
				} else {
					ToastUtil.show(mContext, "请输入或者选择拦截的电话号码");
				}

			}
		});
		// 对话框上面的取消按钮的点击事件
		bt_black_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	
	}

	/**
	 * 接收返回值
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		// 因为就一个返回值,所以这里不作判断了
		if (requestCode == REQUEST_CODE) {
			if (data != null) {
				String mInputphone = data.getStringExtra("phone");
				// 测试数据
				/* Log.d(tag, "从listview中获取的电话号码"+phone); */
				// 由于获得的电话号码格式为x-xxx-xxx-xxxx,不符合要求,要处理一下
				mInputphone = mInputphone.replaceAll("-", "")
						.replaceAll(" ", "").trim();
				// 测试数据
				// Log.d(tag, "测试选择联系人的返回数据"+phone);
				// 将得到的数据赋值给控件
				et_dialog_blacknum.setText(mInputphone);
				// query(phone);
			}

		}
	}
}
