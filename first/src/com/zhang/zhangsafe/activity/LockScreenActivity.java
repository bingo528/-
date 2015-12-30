/**
 * 2015-9-24
 * @author:zhangbin
 */
package com.zhang.zhangsafe.activity;


import com.zhang.zhangsafe.receiver.DeviceReceiver;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class LockScreenActivity extends Activity {
	private DevicePolicyManager mDPM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//实例化出来
		mDPM=(DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
	  // mDPM.lockNow();
	   openAdmin();
	   finish();
	}

	/**
	 * 开启设备管理员权限
	 */
	private void openAdmin() {
		//定义一个意图对象
		 Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		 //激活的组件
		 ComponentName  mDeviceAdminSample = new ComponentName(this, DeviceReceiver.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        //激活的说明
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "开启可以一键锁屏,更加快捷安全");
        startActivity(intent);
	}

}
