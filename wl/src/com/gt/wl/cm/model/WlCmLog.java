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
 * @author rdsf02
 * @version 1.0
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_LOG")
public class WlCmLog extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String id;

	/**
	 * 操作者ID
	 */
	@Column(name = "USER_ID")
	private String userId;

	/**
	 * 内容
	 */
	@Column(name = "CONTENT")
	private String content;

	/**
	 * IP地址
	 */
	@Column(name = "IP")
	private String ip;

	/**
	 * 操作时间
	 */
	@Column(name = "NODE_TIME")
	private Date nodeTime;

	/**
	 * 机器名称
	 */
	@Column(name = "PC_NAME")
	private String pcName;

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;

	/**
	 * 系统名称
	 */
	@Column(name = "SYSTEM_NAME")
	private String systemName;

	/**
	 * 模块名称
	 */
	@Column(name = "MODULE_NAME")
	private String moduleName;

	/**
	 * 用户名称
	 */
	@Column(name = "USER_NAME")
	private String userName;

	public WlCmLog() {

	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return this.ip;
	}

	public void setNodeTime(Date nodeTime) {
		this.nodeTime = nodeTime;
	}

	public Date getNodeTime() {
		return this.nodeTime;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	public String getPcName() {
		return this.pcName;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemName() {
		return this.systemName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}

}