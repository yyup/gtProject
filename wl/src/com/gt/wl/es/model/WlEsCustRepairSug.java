package com.gt.wl.es.model;

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
 * @created 2015-05-14 14:57:10
 */
@Entity
@Table(name = "WL_ES_CUST_REPAIR_SUG")
public class WlEsCustRepairSug extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "SUG_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String sugId;

	/**
	 * 维修ID
	 */
	@Column(name = "REPAIR_ID")
	private String repairId;

	/**
	 * 处理状态
	 */
	@Column(name = "PROCESS_STATUS_EK")
	private String processStatusEk;

	/**
	 * 回复
	 */
	@Column(name = "REPLY")
	private String reply;

	/**
	 * 回复时间
	 */
	@Column(name = "REPLY_TIME")
	private Date replyTime;

	/**
	 * 回复人ID
	 */
	@Column(name = "REPLY_ID")
	private String replyId;

	/**
	 * 回复人
	 */
	@Column(name = "REPLY_NAME")
	private String replyName;
	/**
	 * 处理状态对应的枚举值
	 */
	@Transient
	private String processStatusEkValue;

	public WlEsCustRepairSug() {

	}

	public void setSugId(String sugId) {
		this.sugId = sugId;
	}

	public String getSugId() {
		return this.sugId;
	}

	public void setRepairId(String repairId) {
		this.repairId = repairId;
	}

	public String getRepairId() {
		return this.repairId;
	}

	public void setProcessStatusEk(String processStatusEk) {
		this.processStatusEk = processStatusEk;
	}

	public String getProcessStatusEk() {
		return this.processStatusEk;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReply() {
		return this.reply;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public Date getReplyTime() {
		return this.replyTime;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public String getReplyId() {
		return this.replyId;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}

	public String getReplyName() {
		return this.replyName;
	}

	public String getProcessStatusEkValue() {
		return processStatusEkValue;
	}

	public void setProcessStatusEkValue(String processStatusEkValue) {
		this.processStatusEkValue = processStatusEkValue;
	}

}