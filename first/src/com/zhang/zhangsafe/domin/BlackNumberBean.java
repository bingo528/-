/**
 * 2015-9-28
 * @author:zhangbin
 */
package com.zhang.zhangsafe.domin;

public class BlackNumberBean {

	public String phone;
	public String mode;
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	@Override
	public String toString() {
		return "BlackNumberBean [phone=" + phone + ", mode=" + mode + "]";
	}
	
}
