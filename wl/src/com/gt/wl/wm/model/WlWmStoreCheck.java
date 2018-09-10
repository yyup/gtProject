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
 * @created 2015-09-17 16:53:00
 */
@Entity
@Table(name = "WL_WM_STORE_CHECK")
public class WlWmStoreCheck extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "STORE_CHECK_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String storeCheckId;

	/**
	 * 单据编号
	 */
	@Column(name = "BILL_NO")
	private String billNo;

	/**
	 * 通知单号
	 */
	@Column(name = "NOTICE_NO")
	private String noticeNo;

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
	 * 盘点业务类型
	 */
	@Column(name = "WM_CHECK_TYPE_EK")
	private String wmCheckTypeEk;

	/**
	 * 盘点日期
	 */
	@Column(name = "CHECK_DATE")
	private Date checkDate;

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
	 * 审核时间
	 */
	@Column(name = "AUDIT_TIME")
	private Date auditTime;

	/**
	 * 审核状态
	 */
	@Column(name = "AUDIT_STATE")
	private String auditState;
	/**
	 * 审核人
	 */
	@Column(name = "AUDITOR")
	private String auditor;
	/**
	 * 审核环节代码
	 */
	@Column(name = "AUDIT_NODE_EK")
	private String auditNodeEk;

	/**
	 * 上一个审核环节
	 */
	@Column(name = "PREV_AUDIT_NODE_EK")
	private String prevAuditNodeEk;

	/**
	 * 是否人工审核
	 */
	@Column(name = "IS_MANUAL_FLAG")
	private String isManualFlag;

	/**
	 * 打印次数
	 */
	@Column(name = "PRINT_COUNT")
	private int printCount;

	/**
	 * 单据性质
	 */
	@Column(name = "LOCK_FLAG_EK")
	private String lockFlagEk;

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;
	/**
	 * 所有物料的名字组合（如：滑轮机等2种物料）
	 */
	@Transient
	private String allItemName;
	/**
	 * 盘盈数量
	 */
	@Transient
	private double overage;
	/**
	 * 盘亏数量
	 */
	@Transient
	private double shortage;

	public WlWmStoreCheck() {

	}

	public void setStoreCheckId(String storeCheckId) {
		this.storeCheckId = storeCheckId;
	}

	public String getStoreCheckId() {
		return this.storeCheckId;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillNo() {
		return this.billNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getNoticeNo() {
		return this.noticeNo;
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

	public void setWmCheckTypeEk(String wmCheckTypeEk) {
		this.wmCheckTypeEk = wmCheckTypeEk;
	}

	public String getWmCheckTypeEk() {
		return this.wmCheckTypeEk;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getCheckDate() {
		return this.checkDate;
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

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Date getAuditTime() {
		return this.auditTime;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getAuditState() {
		return this.auditState;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public void setAuditNodeEk(String auditNodeEk) {
		this.auditNodeEk = auditNodeEk;
	}

	public String getAuditNodeEk() {
		return this.auditNodeEk;
	}

	public void setPrevAuditNodeEk(String prevAuditNodeEk) {
		this.prevAuditNodeEk = prevAuditNodeEk;
	}

	public String getPrevAuditNodeEk() {
		return this.prevAuditNodeEk;
	}

	public void setIsManualFlag(String isManualFlag) {
		this.isManualFlag = isManualFlag;
	}

	public String getIsManualFlag() {
		return this.isManualFlag;
	}

	public void setPrintCount(int printCount) {
		this.printCount = printCount;
	}

	public int getPrintCount() {
		return this.printCount;
	}

	public void setLockFlagEk(String lockFlagEk) {
		this.lockFlagEk = lockFlagEk;
	}

	public String getLockFlagEk() {
		return this.lockFlagEk;
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

}