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
 * @created 2015-09-17 16:52:58
 */
@Entity
@Table(name = "WL_WM_NOTICE_CHECK")
public class WlWmNoticeCheck extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "NOTICE_CHECK_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String noticeCheckId;

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
	 * 单据状态
	 */
	@Column(name = "BILL_STATE_EK")
	private String billStateEk;

	/**
	 * 发布时间
	 */
	@Column(name = "ISSUE_TIME")
	private Date issueTime;

	/**
	 * 发布人
	 */
	@Column(name = "ISSUER")
	private String issuer;

	/**
	 * 结果状态
	 */
	@Column(name = "RESULT_EK")
	private String resultEk;

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
	 * 是否已执行标志
	 */
	@Column(name = "IS_EXECUTED_FLAG")
	private String isExecutedFlag;

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;
	/**
	 * 异常备注
	 */
	@Column(name = "ABNORMAL_DESC")
	private String abnormalDesc;
	/**
	 * 所有物料的名字组合（如：滑轮机等2种物料）
	 */
	@Transient
	private String allItemName;
	/**
	 * 结果对应的值
	 */
	@Transient
	private String resultEkValue;
	/**
	 * 盘点业务类型对应的值
	 */
	@Transient
	private String wmCheckTypeEkValue;

	public WlWmNoticeCheck() {

	}

	public void setNoticeCheckId(String noticeCheckId) {
		this.noticeCheckId = noticeCheckId;
	}

	public String getNoticeCheckId() {
		return this.noticeCheckId;
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

	public void setBillStateEk(String billStateEk) {
		this.billStateEk = billStateEk;
	}

	public String getBillStateEk() {
		return this.billStateEk;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	public Date getIssueTime() {
		return this.issueTime;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getIssuer() {
		return this.issuer;
	}

	public void setResultEk(String resultEk) {
		this.resultEk = resultEk;
	}

	public String getResultEk() {
		return this.resultEk;
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

	public void setIsExecutedFlag(String isExecutedFlag) {
		this.isExecutedFlag = isExecutedFlag;
	}

	public String getIsExecutedFlag() {
		return this.isExecutedFlag;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
	}

	public String getAbnormalDesc() {
		return abnormalDesc;
	}

	public void setAbnormalDesc(String abnormalDesc) {
		this.abnormalDesc = abnormalDesc;
	}

	public String getAllItemName() {
		return allItemName;
	}

	public void setAllItemName(String allItemName) {
		this.allItemName = allItemName;
	}

	public String getResultEkValue() {
		return resultEkValue;
	}

	public void setResultEkValue(String resultEkValue) {
		this.resultEkValue = resultEkValue;
	}

	public String getWmCheckTypeEkValue() {
		return wmCheckTypeEkValue;
	}

	public void setWmCheckTypeEkValue(String wmCheckTypeEkValue) {
		this.wmCheckTypeEkValue = wmCheckTypeEkValue;
	}

}