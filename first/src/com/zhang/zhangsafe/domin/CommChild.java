/**
 * 2015-10-6
 * @author:zhangbin
 */
package com.zhang.zhangsafe.domin;

public class CommChild {
	public String _id;
	public String number;
	public String name;
	/**
	 * @param _id
	 * @param number
	 * @param name
	 */
	public CommChild(String _id, String number, String name) {
		super();
		this._id = _id;
		this.number = number;
		this.name = name;
	}
	/**
	 * 
	 */
	public CommChild() {
		super();
		
	}
	/**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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
	
}
