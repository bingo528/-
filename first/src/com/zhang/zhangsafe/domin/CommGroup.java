/**
 * 2015-10-6
 * @author:zhangbin
 */
package com.zhang.zhangsafe.domin;

import java.util.List;

public class CommGroup {

	//常用电话的列表名称
	public String name;
	//常用电话的列表idx
	public String idx;
	
	public List<CommChild> childList;
	
	/**
	 * @return the childList
	 */
	public List<CommChild> getChildList() {
		return childList;
	}
	/**
	 * @param childList the childList to set
	 */
	public void setChildList(List<CommChild> childList) {
		this.childList = childList;
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
	 * @return the idx
	 */
	public String getIdx() {
		return idx;
	}
	/**
	 * @param idx the idx to set
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
	/**
	 * 
	 */
	public CommGroup() {
		super();
		
	}
	
	
	
}
