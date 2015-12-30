/**
 * 2015-10-9
 * @author:zhangbin
 */
package com.zhang.zhangsafe.domin;

import android.graphics.drawable.Drawable;

public class CacheBean {
	//应用
	public String  name;
	//包名
	public String packageName;
	//应用图标
	public Drawable icon;
	//缓存大小
	public long cacheSize;
	/**
	 * 
	 */
	public CacheBean() {
		super();
		
	}
	/**
	 * @param name
	 * @param packageName
	 * @param icon
	 * @param cacheSize
	 */
	public CacheBean(String name, String packageName, Drawable icon,
			long cacheSize) {
		super();
		this.name = name;
		this.packageName = packageName;
		this.icon = icon;
		this.cacheSize = cacheSize;
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
	 * @return the cacheSize
	 */
	public long getCacheSize() {
		return cacheSize;
	}
	/**
	 * @param cacheSize the cacheSize to set
	 */
	public void setCacheSize(long cacheSize) {
		this.cacheSize = cacheSize;
	}
	
	

}
