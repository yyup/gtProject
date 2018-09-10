package com.gt.wl.util;

/**
 * 枚举类型或键值的数据，需要通过这个结构进行封装
 * 统一配置式枚举量和数据表枚举量的公共处理方法
 * @author liuyj
 * 
 */
public class Type implements java.io.Serializable {
	private String id;
	private String lable;
	private boolean isSelected;

	public Type(String lable, String id) {
		this.id = id;
		this.lable = lable;
	}

	/**
	 * 设置键
	 * @param id 键
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获得键
	 * @return 键
	 */
	public String getId() {
		return id;
	}

	/**
	 * 获得标签
	 * @return 标签
	 */
	public String getLable() {
		return lable;
	}

	/**
	 * 设置标签
	 * @param lable 标签
	 */
	public void setLable(String lable) {
		this.lable = lable;
	}

	/**
	 * 获得是否选中
	 * @return 布尔
	 */
	public boolean getIsSelected() {
		return isSelected;
	}

	/**
	 * 设置是否选中
	 * @param isSelected 是否选中
	 */
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
