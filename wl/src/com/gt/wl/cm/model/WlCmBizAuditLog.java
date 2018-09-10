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
 * @created 2016-07-18 17:58:24
 */
@Entity
@Table(name = "WL_CM_BIZ_AUDIT_LOG")
public class WlCmBizAuditLog extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "BIZ_AUDIT_LOG_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String bizAuditLogId;

	/**
	 * 业务类型
	 */
	@Column(name = "BIZ_TYPE_EK")
	private String bizTypeEk;

	/**
	 * 业务对象ID
	 */
	@Column(name = "BIZ_OBJECT_ID")
	private String bizObjectId;

	/**
	 * 审核人代码
	 */
	@Column(name = "AUDITOR_ID")
	private String auditorId;

	/**
	 * 审核人姓名
	 */
	@Column(name = "AUDITOR_NAME")
	private String auditorName;

	/**
	 * 审核环节
	 */
	@Column(name = "AUDIT_NODE_EK")
	private String auditNodeEk;

	/**
	 * 审核时间
	 */
	@Column(name = "AUDIT_TIME")
	private Date auditTime;

	/**
	 * 审核操作类型
	 */
	@Column(name = "AUDIT_OPERATE_EK")
	private String auditOperateEk;

	/**
	 * 审核意见
	 */
	@Column(name = "AUDIT_REMARK")
	private String auditRemark;

	/**
	 * 修改时间
	 */
	@Column(name = "MODIFY_TIME")
	private Date modifyTime = new Date();

	public WlCmBizAuditLog() {

	}

	public void setBizAuditLogId(String bizAuditLogId) {
		this.bizAuditLogId = bizAuditLogId;
	}

	public String getBizAuditLogId() {
		return this.bizAuditLogId;
	}

	public void setBizTypeEk(String bizTypeEk) {
		this.bizTypeEk = bizTypeEk;
	}

	public String getBizTypeEk() {
		return this.bizTypeEk;
	}

	public void setBizObjectId(String bizObjectId) {
		this.bizObjectId = bizObjectId;
	}

	public String getBizObjectId() {
		return this.bizObjectId;
	}

	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditorId() {
		return this.auditorId;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getAuditorName() {
		return this.auditorName;
	}

	public void setAuditNodeEk(String auditNodeEk) {
		this.auditNodeEk = auditNodeEk;
	}

	public String getAuditNodeEk() {
		return this.auditNodeEk;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Date getAuditTime() {
		return this.auditTime;
	}

	public void setAuditOperateEk(String auditOperateEk) {
		this.auditOperateEk = auditOperateEk;
	}

	public String getAuditOperateEk() {
		return this.auditOperateEk;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	public String getAuditRemark() {
		return this.auditRemark;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

}