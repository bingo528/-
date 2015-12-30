/**
 * 2015-10-8
 * @author:zhangbin
 */
package com.zhang.zhangsafe.adapter;

import java.util.List;

import com.zhang.zhangsafe.R;
import com.zhang.zhangsafe.adapter.BlackNumberAdapter.ViewHolder;
import com.zhang.zhangsafe.db.dao.AppLockDao;
import com.zhang.zhangsafe.domin.AppBean;
import com.zhang.zhangsafe.util.ToastUtil;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAppLockAdater extends BaseAdapter {

	private List<AppBean> mUnLockList;
	private List<AppBean> mAppList;
	private List<AppBean> mLockList;
	private Context mContext;
	private AppLockDao mDao;
	// 判断是否加锁
	private boolean isLock;
	private TextView tv_applock_unlock;
	private TextView tv_applock_lock;
	private Animation mAnimation;

	/**
	 * 
	 */
	public MyAppLockAdater() {
		super();

	}

	/**
	 * @param mContext
	 * @param mAppList
	 * @param mLockList
	 * @param mUnLockList
	 */
	public MyAppLockAdater(Context mContext, boolean isLock,
			List<AppBean> mAppList, List<AppBean> mLockList,
			List<AppBean> mUnLockList,TextView tv_applock_lock,TextView tv_applock_unlock,AppLockDao mDao) {
		this.mContext = mContext;
		this.isLock = isLock;
		this.mAppList = mAppList;
		this.mLockList = mLockList;
		this.mUnLockList = mUnLockList;
		this.tv_applock_unlock=tv_applock_unlock;
		this.tv_applock_lock=tv_applock_lock;
		this.mDao=mDao;
	}

	
	@Override
	public int getCount() {
		if (isLock) {
			tv_applock_lock.setText("已加锁的总数:"+mLockList.size());
			return mLockList.size();
			
		} else {
			tv_applock_unlock.setText("未加锁的总数:"+mUnLockList.size());
			return mUnLockList.size();
		}
	}

	@Override
	public AppBean getItem(int position) {

		if (isLock) {
			return mLockList.get(position);
		} else {

			return mUnLockList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.list_islock_item,null);
			viewHolder=new ViewHolder();
			viewHolder.iv_islock_icon = (ImageView) convertView.findViewById(R.id.iv_islock_icon);
			viewHolder.tv_islock_name=(TextView) convertView.findViewById(R.id.tv_islock_name);
			viewHolder.iv_islock_lockbg=(ImageView) convertView.findViewById(R.id.iv_islock_lockbg);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		final AppBean appBean = getItem(position);
		//赋值
		viewHolder.iv_islock_icon.setBackgroundDrawable(appBean.icon);
		viewHolder.tv_islock_name.setText(appBean.name);
		final View animationView=convertView;
		mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.translate);
		
		//判断一下
		if (isLock) {
			
			viewHolder.iv_islock_lockbg.setBackgroundResource(R.drawable.lock);
		}
		else {
			
			viewHolder.iv_islock_lockbg.setBackgroundResource(R.drawable.unlock);
		}
		viewHolder.iv_islock_lockbg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				animationView.startAnimation(mAnimation);
				//对动画进行监听,只有动画执行完成后在移除数据
				mAnimation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						//判断一下,是加锁的还是不加锁的那个
						if (isLock) {
							
							//从已加锁到到加锁飞,已加锁集合少一个,未加锁集合多一个
							mLockList.remove(appBean);
							mUnLockList.add(0,appBean);
							//删除一条数据
							
							mDao.delete(appBean.packageName);
							//更新UI
							notifyDataSetChanged();
//							ToastUtil.show(mContext, "你的轻轻一点,我就瞬间变成不加锁程序了");
						} else {
							
							//从未加锁到已加锁飞,未加锁集合少一个,加锁集合多一个
							mUnLockList.remove(appBean);
							
							mLockList.add(0,appBean);
							//添加一条数据
							
							mDao.insert(appBean.packageName);
//							ToastUtil.show(mContext, "我变成加锁软件了,下次想打开我需要输密码哟");
							//更新UI
							notifyDataSetChanged();
							
						}
					}
				});
				
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView iv_islock_icon;
		TextView tv_islock_name;
		ImageView iv_islock_lockbg;
	}
}
