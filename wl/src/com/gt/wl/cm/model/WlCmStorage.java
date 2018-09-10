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
 * @created 2015-09-17 16:52:54
 */
@Entity
@Table(name = "WL_CM_STORAGE")
public class WlCmStorage extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "STORAGE_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String storageId;

	/**
	 * 仓库CD
	 */
	@Column(name = "STORAGE_CD")
	private String storageCd;

	/**
	 * 名称
	 */
	@Column(name = "STORAGE_NAME")
	private String storageName;

	/**
	 * 默认进货仓库
	 */
	@Column(name = "IS_IN_FLAG")
	private String isInFlag;

	/**
	 * 默认出库仓库
	 */
	@Column(name = "IS_OUT_FLAG")
	private String isOutFlag;

	/**
	 * 负责人
	 */
	@Column(name = "BOSSHEAD")
	private String bosshead;

	/**
	 * 联系人
	 */
	@Column(name = "CONTACT")
	private String contact;

	/**
	 * 联系电话
	 */
	@Column(name = "TEL")
	private String tel;

	/**
	 * 地址
	 */
	@Column(name = "ADDR")
	private String addr;
	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;

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
	 * 启用标志
	 */
	@Column(name = "IS_ENABLE_FLAG")
	private String isEnableFlag = "0";

	public WlCmStorage() {

	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	public String getStorageId() {
		return this.storageId;
	}

	public void setStorageCd(String storageCd) {
		this.storageCd = storageCd;
	}

	public String getStorageCd() {
		return this.storageCd;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getStorageName() {
		return this.storageName;
	}

	public void setIsInFlag(String isInFlag) {
		this.isInFlag = isInFlag;
	}

	public String getIsInFlag() {
		return this.isInFlag;
	}

	public void setIsOutFlag(String isOutFlag) {
		this.isOutFlag = isOutFlag;
	}

	public String getIsOutFlag() {
		return this.isOutFlag;
	}

	public void setBosshead(String bosshead) {
		this.bosshead = bosshead;
	}

	public String getBosshead() {
		return this.bosshead;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContact() {
		return this.contact;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTel() {
		return this.tel;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return this.addr;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public void setIsEnableFlag(String isEnableFlag) {
		this.isEnableFlag = isEnableFlag;
	}

	public String getIsEnableFlag() {
		return this.isEnableFlag;
	}

}