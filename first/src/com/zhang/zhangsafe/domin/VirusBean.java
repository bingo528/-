/**
 * 2015-10-7
 * @author:zhangbin
 */
package com.zhang.zhangsafe.domin;

public class VirusBean {
	//包名
	public String packName;
	//是否是病毒
	public boolean isVirus;
	//应用名称
	public String name;
	/**
	 * 
	 */
	public VirusBean() {
		super();
		
	}
	/**
	 * @return the packName
	 */
	public String getPackName() {
		return packName;
	}
	/**
	 * @param packName the packName to set
	 */
	public void setPackName(String packName) {
		this.packName = packName;
	}
	/**
	 * @return the isVirus
	 */
	public boolean isVirus() {
		return isVirus;
	}
	/**
	 * @param isVirus the isVirus to set
	 */
	public void setVirus(boolean isVirus) {
		this.isVirus = isVirus;
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
	 * @param packName
	 * @param isVirus
	 * @param name
	 */
	public VirusBean(String packName, boolean isVirus, String name) {
		super();
		this.packName = packName;
		this.isVirus = isVirus;
		this.name = name;
	}
	

}
