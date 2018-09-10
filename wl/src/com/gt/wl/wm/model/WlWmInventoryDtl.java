package com.gt.wl.wm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author liuyj
 * @version 1.0
 * @created 2015-09-17 16:52:57
 */
@Entity
@Table(name = "WL_WM_INVENTORY_DTL")
public class WlWmInventoryDtl extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "INVENTORY_DTL_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String inventoryDtlId;

	/**
	 * 库存表ID
	 */
	@Column(name = "INVENTORY_ID")
	private String inventoryId;

	/**
	 * 设备序列号
	 */
	@Column(name = "SERIAL_NO")
	private String serialNo;

	/**
	 * 是否在库
	 */
	@Column(name = "IS_STORE")
	private String isStore;

	public WlWmInventoryDtl() {

	}

	public void setInventoryDtlId(String inventoryDtlId) {
		this.inventoryDtlId = inventoryDtlId;
	}

	public String getInventoryDtlId() {
		return this.inventoryDtlId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getInventoryId() {
		return this.inventoryId;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getSerialNo() {
		return this.serialNo;
	}

	public void setIsStore(String isStore) {
		this.isStore = isStore;
	}

	public String getIsStore() {
		return this.isStore;
	}

}