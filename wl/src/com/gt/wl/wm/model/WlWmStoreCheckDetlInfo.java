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
 * @created 2015-09-17 16:53:03
 */
@Entity
@Table(name = "WL_WM_STORE_CHECK_DETL_INFO")
public class WlWmStoreCheckDetlInfo extends BaseModel implements java.io.Serializable {
	@Id
	@Column(name = "STORE_CHECK_DETL_INFO_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String storeCheckDetlInfoId;

	/**
	 * 盘点单从表ID
	 */
	@Column(name = "STORE_CHECK_DETL_ID")
	private String storeCheckDetlId;

	/**
	 * 设备序列号
	 */
	@Column(name = "SERIAL_NO")
	private String serialNo;

	@Transient
	private String itemNo;

	@Transient
	private String seqBarCd;

	public WlWmStoreCheckDetlInfo() {

	}

	public String getStoreCheckDetlInfoId() {
		return storeCheckDetlInfoId;
	}

	public void setStoreCheckDetlInfoId(String storeCheckDetlInfoId) {
		this.storeCheckDetlInfoId = storeCheckDetlInfoId;
	}

	public String getStoreCheckDetlId() {
		return storeCheckDetlId;
	}

	public void setStoreCheckDetlId(String storeCheckDetlId) {
		this.storeCheckDetlId = storeCheckDetlId;
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