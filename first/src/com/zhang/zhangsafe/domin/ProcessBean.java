/**
 * 2015-10-5
 * @author:zhangbin
 */
package com.zhang.zhangsafe.domin;

import android.graphics.drawable.Drawable;

public class ProcessBean {

	    // 包名
		public String packageName;
		// 应用名字
		public String name;
		//大小
		public long memSize;
		// 图标
		public Drawable icon;
		// 是否系统应用
		public boolean isSystem;
		// 是否被选中
		public boolean isCheck;
		
		
		/**
		 * @param packageName
		 * @param name
		 * @param memSize
		 * @param icon
		 * @param isSystem
		 * @param isCheck
		 */
		public ProcessBean(String packageName, String name, long memSize,
				Drawable icon, boolean isSystem, boolean isCheck) {
			super();
			this.packageName = packageName;
			this.name = name;
			this.memSize = memSize;
			this.icon = icon;
			this.isSystem = isSystem;
			this.isCheck = isCheck;
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
		 * @return the memSize
		 */
		public long getMemSize() {
			return memSize;
		}

		/**
		 * @param memSize the memSize to set
		 */
		public void setMemSize(long memSize) {
			this.memSize = memSize;
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
		 * @return the isCheck
		 */
		public boolean isCheck() {
			return isCheck;
		}

		/**
		 * @param isCheck the isCheck to set
		 */
		public void setCheck(boolean isCheck) {
			this.isCheck = isCheck;
		}

		public ProcessBean() {
			super();
			
		}
		
}
