package com.gt.wl.cm.model;

import java.util.Date;

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
 * @created 2015-04-08 16:54:24
 */
@Entity
@Table(name = "WL_CM_WATER_DATA")
public class WlCmWaterData extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "DATA_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String dataId;

	/**
	 * 消防栓ID
	 */
	@Column(name = "HYDRANT_ID")
	private String hydrantId;

	/**
	 * 水压
	 */
	@Column(name = "WATER")
	private double water;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	private String creator;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime = new Date();

	/**
	 * 修改人
	 */
	@Column(name = "MODIFIER")
	private String modifier;

	/**
	 * 修改时间
	 */
	@Column(name = "MODIFY_TIME")
	private Date modifyTime = new Date();

	/**
	 * 数据状态
	 */
	@Column(name = "DATA_STATE")
	private String dataState = "1";

	@Transient
	private String labelNo;

	@Transient
	private String addr;

	public WlCmWaterData() {

	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getDataId() {
		return this.dataId;
	}

	public void setHydrantId(String hydrantId) {
		this.hydrantId = hydrantId;
	}

	public String getHydrantId() {
		return this.hydrantId;
	}

	public void setWater(double water) {
		this.water = water;
	}

	public double getWater() {
		return this.water;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifier() {
		return this.modifier;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setDataState(String dataState) {
		this.dataState = dataState;
	}

	public String getDataState() {
		return this.dataState;
	}

	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

}