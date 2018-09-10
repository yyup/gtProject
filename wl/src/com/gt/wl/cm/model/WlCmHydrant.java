package com.gt.wl.cm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author rdsf02
 * @version 1.0
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_HYDRANT")
public class WlCmHydrant extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "HYDRANT_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String hydrantId;

	/**
	 * 标签号
	 */
	@Column(name = "LABEL_NO")
	private String labelNo;

	/**
	 * 自编号
	 */
	@Column(name = "CUSTOM_NO")
	private String customNo;

	/**
	 * 地址
	 */
	@Column(name = "ADDR")
	private String addr;

	/**
	 * 位置
	 */
	@Column(name = "POSITION")
	private String position;

	@Transient
	private String longitude;

	@Transient
	private String latitude;

	public WlCmHydrant() {

	}

	public void setHydrantId(String hydrantId) {
		this.hydrantId = hydrantId;
	}

	public String getHydrantId() {
		return this.hydrantId;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

	public String getLabelNo() {
		return this.labelNo;
	}

	public void setCustomNo(String customNo) {
		this.customNo = customNo;
	}

	public String getCustomNo() {
		return this.customNo;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition() {
		return this.position;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}