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
 * @created 2015-09-17 16:52:59
 */
@Entity
@Table(name = "WL_WM_NOTICE_OUT")
public class WlWmNoticeOut extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "NOTICE_OUT_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String noticeOutId;

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
	 * 出库业务类型
	 */
	@Column(name = "WM_OUT_TYPE_EK")
	private String wmOutTypeEk;

	/**
	 * 到货仓库ID
	 */
	@Column(name = "TARGET_STORAGE_ID")
	private String targetStorageId;

	/**
	 * 到货仓库名称
	 */
	@Column(name = "TARGET_STORAGE_NAME")
	private String targetStorageName;

	/**
	 * 发货仓库ID
	 */
	@Column(name = "SOURCE_STORAGE_ID")
	private String sourceStorageId;

	/**
	 * 发货仓库名称
	 */
	@Column(name = "SOURCE_STORAGE_NAME")
	private String sourceStorageName;

	/**
	 * 往来单位ID
	 */
	@Column(name = "ORG_ID")
	private String orgId;

	/**
	 * 往来单位名称
	 */
	@Column(name = "ORG_NAME")
	private String orgName;

	/**
	 * 对象单位类型
	 */
	@Column(name = "OBJECT_TYPE_EK")
	private String objectTypeEk;

	/**
	 * 预计出库日期
	 */
	@Column(name = "EXPECT_OUT_DATE")
	private Date expectOutDate;

	/**
	 * 出库总数量
	 */
	@Column(name = "TOTAL_QTY")
	private double totalQty = 0;

	/**
	 * 收货人
	 */
	@Column(name = "CONSIGNEE")
	private String consignee;

	/**
	 * 联系方式
	 */
	@Column(name = "CONTACT_WAY")
	private String contactWay;

	/**
	 * 地址
	 */
	@Column(name = "ADDR")
	private String addr;

	/**
	 * 业务年度
	 */
	@Column(name = "BIZ_YEAR")
	private int bizYear;

	/**
	 * 合同编号
	 */
	@Column(name = "LINKED_CONT_NO")
	private String linkedContNo;

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

	public WlWmNoticeOut() {

	}

	public void setNoticeOutId(String noticeOutId) {
		this.noticeOutId = noticeOutId;
	}

	public String getNoticeOutId() {
		return this.noticeOutId;
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

	public void setStorageAreaId(String storageAreaId) {
		this.storageAreaId = storageAreaId;
	}

	public String getStorageAreaId() {
		return this.storageAreaId;
	}

	public void setStorageAreaName(String storageAreaName) {
		this.storageAreaName = storageAreaName;
	}

	public String getStorageAreaName() {
		return this.storageAreaName;
	}

	public void setWmOutTypeEk(String wmOutTypeEk) {
		this.wmOutTypeEk = wmOutTypeEk;
	}

	public String getWmOutTypeEk() {
		return this.wmOutTypeEk;
	}

	public void setTargetStorageId(String targetStorageId) {
		this.targetStorageId = targetStorageId;
	}

	public String getTargetStorageId() {
		return this.targetStorageId;
	}

	public void setTargetStorageName(String targetStorageName) {
		this.targetStorageName = targetStorageName;
	}

	public String getTargetStorageName() {
		return this.targetStorageName;
	}

	public void setSourceStorageId(String sourceStorageId) {
		this.sourceStorageId = sourceStorageId;
	}

	public String getSourceStorageId() {
		return this.sourceStorageId;
	}

	public void setSourceStorageName(String sourceStorageName) {
		this.sourceStorageName = sourceStorageName;
	}

	public String getSourceStorageName() {
		return this.sourceStorageName;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setObjectTypeEk(String objectTypeEk) {
		this.objectTypeEk = objectTypeEk;
	}

	public String getObjectTypeEk() {
		return this.objectTypeEk;
	}

	public void setExpectOutDate(Date expectOutDate) {
		this.expectOutDate = expectOutDate;
	}

	public Date getExpectOutDate() {
		return this.expectOutDate;
	}

	public void setTotalQty(double totalQty) {
		this.totalQty = totalQty;
	}

	public double getTotalQty() {
		return this.totalQty;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsignee() {
		return this.consignee;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	public String getContactWay() {
		return this.contactWay;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setBizYear(int bizYear) {
		this.bizYear = bizYear;
	}

	public int getBizYear() {
		return this.bizYear;
	}

	public void setLinkedContNo(String linkedContNo) {
		this.linkedContNo = linkedContNo;
	}

	public String getLinkedContNo() {
		return this.linkedContNo;
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

}