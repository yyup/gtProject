package com.gt.wl.es.model;

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
 * @created 2015-12-07 14:00:50
 */
@Entity
@Table(name = "WL_ES_AGENCY_ORDER_AUDIT")
public class WlEsAgencyOrderAudit extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "AUDIT_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String auditId;

	/**
	 * 订单ID
	 */
	@Column(name = "ORDER_ID")
	private String orderId;

	/**
	 * 审核人
	 */
	@Column(name = "AUDITOR")
	private String auditor;

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
	 * 审核结论
	 */
	@Column(name = "AUDIT_RESULT")
	private String auditResult;

	public WlEsAgencyOrderAudit() {

	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getAuditId() {
		return this.auditId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getAuditor() {
		return this.auditor;
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

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}

	public String getAuditResult() {
		return this.auditResult;
	}

}