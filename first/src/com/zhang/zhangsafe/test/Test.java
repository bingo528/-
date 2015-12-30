/**
 * 2015-9-28
 * @author:zhangbin
 */
package com.zhang.zhangsafe.test;

import java.util.Random;

import com.lidroid.xutils.exception.DbException;
import com.zhang.zhangsafe.db.dao.BlackNumberDao;

import android.R.integer;
import android.test.AndroidTestCase;

public class Test extends AndroidTestCase {

	// 测试增删改查操作
	public void test() {
		BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
		// 增加
//		 dao.insert("110", "1");
		// 更新
		// dao.update("110", "2");
		//查询
//		dao.queryAll();
		//删除
		//dao.delete("110");
		 //很多条数据
	/*	 for (int i = 0; i < 100; i++) {
			 if (i<10) {
				
				 dao.insert("1380000000"+i, 1+new Random().nextInt(3)+"");
			}
			 else {
				 dao.insert("138000000"+i, 1+new Random().nextInt(3)+"");
				
			}
		}*/
		//查询
		int mode=dao.getMode("13800000003");
		System.out.println(mode);
	}
}
