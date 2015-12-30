/**
 * 2015-10-4
 * @author:zhangbin
 */
package com.zhang.zhangsafe.domin;

import android.graphics.drawable.Drawable;

public class AppBean {
	// 包名
	public String packageName;
	// 应用名字
	public String name;
	// 图标
	public Drawable icon;
	// 是否系统应用
	public boolean isSystem;
	// 是否在SD卡内
	public boolean isSd;
	
	/**
	 * 无参数构造
	 */
	public AppBean() {
		super();
		
	}
	/**
	 * @param packageName
	 * @param name
	 * @param icon
	 * @param isSystem
	 * @param isSd
	 */
	public AppBean(String packageName, String name, Drawable icon,
			boolean isSystem, boolean isSd) {
		super();
		this.packageName = packageName;
		this.name = name;
		this.icon = icon;
		this.isSystem = isSystem;
		this.isSd = isSd;
	}
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the icon
	 */
	public Drawable getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	/**
	 * @return the isSystem
	 */
	public boolean isSystem() {
		return isSystem;
	}
	/**
	 * @param isSystem the isSystem to set
	 */
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	/**
	 * @return the isSd
	 */
	public boolean isSd() {
		return isSd;
	}
	/**
	 * @param isSd the isSd to set
	 */
	public void setSd(boolean isSd) {
		this.isSd = isSd;
	}
	

}
