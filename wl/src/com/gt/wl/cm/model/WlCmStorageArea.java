package com.gt.wl.cm.model;

import java.util.Date;

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
 * @created 2015-09-17 16:52:55
 */
@Entity
@Table(name = "WL_CM_STORAGE_AREA")
public class WlCmStorageArea extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "STORAGE_AREA_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String storageAreaId;

	/**
	 * 仓库ID2
	 */
	@Column(name = "STORAGE_ID")
	private String storageId;

	/**
	 * 库区名称
	 */
	@Column(name = "STORAGE_AREA_NAME")
	private String storageAreaName;

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
	 * 创建人
	 */
	@Column(name = "CREATOR")
	private String creator;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime;

	/**
	 * 启用标志
	 */
	@Column(name = "IS_ENABLE_FLAG")
	private String isEnableFlag = "0";

	public WlCmStorageArea() {

	}

	public void setStorageAreaId(String storageAreaId) {
		this.storageAreaId = storageAreaId;
	}

	public String getStorageAreaId() {
		return this.storageAreaId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	public String getStorageId() {
		return this.storageId;
	}

	public void setStorageAreaName(String storageAreaName) {
		this.storageAreaName = storageAreaName;
	}

	public String getStorageAreaName() {
		return this.storageAreaName;
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

	public void setIsEnableFlag(String isEnableFlag) {
		this.isEnableFlag = isEnableFlag;
	}

	public String getIsEnableFlag() {
		return this.isEnableFlag;
	}

}