package com.gt.wl.wm.model;

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
 * @created 2015-09-17 16:53:02
 */
@Entity
@Table(name = "WL_WM_STORE_LOSS")
public class WlWmStoreLoss extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "STORE_LOSS_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String storeLossId;

	/**
	 * 单据编号
	 */
	@Column(name = "BILL_NO")
	private String billNo;

	/**
	 * 仓库ID
	 */
	@Column(name = "STORAGE_ID")
	private String storageId;

	/**
	 * 仓库名称
	 */
	@Column(name = "STORAGE_NAME")
	private String storageName;
	/**
	 * 库区ID
	 */
	@Column(name = "STORAGE_AREA_ID")
	private String storageAreaId;

	/**
	 * 库区名称
	 */
	@Column(name = "STORAGE_AREA_NAME")
	private String storageAreaName;
	/**
	 * 业务年度
	 */
	@Column(name = "BIZ_YEAR")
	private int bizYear;

	/**
	 * 制单员代码
	 */
	@Column(name = "CREATE_OPR_ID")
	private String createOprId;

	/**
	 * 制单员
	 */
	@Column(name = "CREATEOR")
	private String createor;

	/**
	 * 制单时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime;

	/**
	 * 损益单来源
	 */
	@Column(name = "PROFIT_LOSS_SOURCE_EK")
	private String profitLossSourceEk;

	/**
	 * 关联单据号
	 */
	@Column(name = "LINKED_BILL_NO")
	private String linkedBillNo;

	/**
	 * 关联单据ID
	 */
	@Column(name = "LINKED_MAS_ID")
	private String linkedMasId;

	/**
	 * 上一个审核环节
	 */
	@Column(name = "PREV_AUDIT_NODE_EK")
	private String prevAuditNodeEk;

	/**
	 * 审核环节代码
	 */
	@Column(name = "AUDIT_NODE_EK")
	private String auditNodeEk;

	/**
	 * 审核人
	 */
	@Column(name = "AUDITOR")
	private String auditor;

	/**
	 * 审核状态
	 */
	@Column(name = "AUDIT_STATE")
	private String auditState;

	/**
	 * 审核时间
	 */
	@Column(name = "AUDIT_TIME")
	private Date auditTime;

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;
	@Transient
	private String allItemName;
	/**
	 * 盘盈
	 */
	@Transient
	double overage;
	/**
	 * 盘亏
	 */
	@Transient
	double shortage;
	/**
	 * 盘点日期
	 */
	@Transient
	private Date checkDate;

	public WlWmStoreLoss() {

	}

	public void setStoreLossId(String storeLossId) {
		this.storeLossId = storeLossId;
	}

	public String getStoreLossId() {
		return this.storeLossId;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillNo() {
		return this.billNo;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	public String getStorageId() {
		return this.storageId;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getStorageName() {
		return this.storageName;
	}

	public String getStorageAreaId() {
		return storageAreaId;
	}

	public void setStorageAreaId(String storageAreaId) {
		this.storageAreaId = storageAreaId;
	}

	public String getStorageAreaName() {
		return storageAreaName;
	}

	public void setStorageAreaName(String storageAreaName) {
		this.storageAreaName = storageAreaName;
	}

	public void setBizYear(int bizYear) {
		this.bizYear = bizYear;
	}

	public int getBizYear() {
		return this.bizYear;
	}

	public void setCreateOprId(String createOprId) {
		this.createOprId = createOprId;
	}

	public String getCreateOprId() {
		return this.createOprId;
	}

	public void setCreateor(String createor) {
		this.createor = createor;
	}

	public String getCreateor() {
		return this.createor;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setProfitLossSourceEk(String profitLossSourceEk) {
		this.profitLossSourceEk = profitLossSourceEk;
	}

	public String getProfitLossSourceEk() {
		return this.profitLossSourceEk;
	}

	public void setLinkedBillNo(String linkedBillNo) {
		this.linkedBillNo = linkedBillNo;
	}

	public String getLinkedBillNo() {
		return this.linkedBillNo;
	}

	public void setLinkedMasId(String linkedMasId) {
		this.linkedMasId = linkedMasId;
	}

	public String getLinkedMasId() {
		return this.linkedMasId;
	}

	public void setPrevAuditNodeEk(String prevAuditNodeEk) {
		this.prevAuditNodeEk = prevAuditNodeEk;
	}

	public String getPrevAuditNodeEk() {
		return this.prevAuditNodeEk;
	}

	public void setAuditNodeEk(String auditNodeEk) {
		this.auditNodeEk = auditNodeEk;
	}

	public String getAuditNodeEk() {
		return this.auditNodeEk;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getAuditState() {
		return this.auditState;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Date getAuditTime() {
		return this.auditTime;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
	}

	public String getAllItemName() {
		return allItemName;
	}

	public void setAllItemName(String allItemName) {
		this.allItemName = allItemName;
	}

	public double getOverage() {
		return overage;
	}

	public void setOverage(double overage) {
		this.overage = overage;
	}

	public double getShortage() {
		return shortage;
	}

	public void setShortage(double shortage) {
		this.shortage = shortage;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

}