package com.gt.wl.cm.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author rdsf02
 * @version 1.0
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_FOCUS_PIC")
public class WlCmFocusPic extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "PIC_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String picId;

	/**
	 * 图片地址
	 */
	@Column(name = "PATH")
	private String path;

	/**
	 * 链接地址
	 */
	@Column(name = "URL")
	private String url;

	/**
	 * 说明文字
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;

	/**
	 * 是否启用标志
	 */
	@Column(name = "IS_ENABLE_FLAG")
	private String isEnableFlag;

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
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;
	/**
	 * 审核环节代码
	 */
	@Column(name = "AUDIT_NODE_EK")
	private String auditNodeEk;

	/**
	 * 审核标志
	 */
	@Column(name = "AUDIT_STATE")
	private String auditState;

	/**
	 * 审核时间
	 */
	@Column(name = "AUDIT_TIME")
	private Date auditTime;

	/**
	 * 审核日期列表
	 */
	@Transient
	private List bizLogList;

	public WlCmFocusPic() {

	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getPicId() {
		return this.picId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setIsEnableFlag(String isEnableFlag) {
		this.isEnableFlag = isEnableFlag;
	}

	public String getIsEnableFlag() {
		return this.isEnableFlag;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getSequ() {
		return sequ;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public String getAuditNodeEk() {
		return auditNodeEk;
	}

	public void setAuditNodeEk(String auditNodeEk) {
		this.auditNodeEk = auditNodeEk;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public List getBizLogList() {
		return bizLogList;
	}

	public void setBizLogList(List bizLogList) {
		this.bizLogList = bizLogList;
	}

}