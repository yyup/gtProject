package com.gt.wl.wm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author liuyj
 * @version 1.0
 * @created 2015-09-17 16:53:02
 */
@Entity
@Table(name = "WL_WM_STORE_IN_DETL_INFO")
public class WlWmStoreInDetlInfo extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "STORE_IN_DETL_INFO_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String storeInDetlInfoId;

	/**
	 * 入库单从表ID
	 */
	@Column(name = "STORE_IN_DETL_ID")
	private String storeInDetlId;

	/**
	 * 设备序列号
	 */
	@Column(name = "SERIAL_NO")
	private String serialNo;

	@Transient
	private String itemNo;

	@Transient
	private String seqBarCd;

	public WlWmStoreInDetlInfo() {

	}

	public void setStoreInDetlInfoId(String storeInDetlInfoId) {
		this.storeInDetlInfoId = storeInDetlInfoId;
	}

	public String getStoreInDetlInfoId() {
		return this.storeInDetlInfoId;
	}

	public void setStoreInDetlId(String storeInDetlId) {
		this.storeInDetlId = storeInDetlId;
	}

	public String getStoreInDetlId() {
		return this.storeInDetlId;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getSerialNo() {
		return this.serialNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getSeqBarCd() {
		return seqBarCd;
	}

	public void setSeqBarCd(String seqBarCd) {
		this.seqBarCd = seqBarCd;
	}

}