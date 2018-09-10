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
@Table(name = "WL_CM_INFO_EN")
public class WlCmInfoEn extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "INFO_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String infoId;

	/**
	 * 栏目ID
	 */
	@Column(name = "COLUMN_ID")
	private String columnId;

	/**
	 * 标题
	 */
	@Column(name = "TITLE")
	private String title;

	/**
	 * 内容
	 */
	@Column(name = "CONTENT")
	private String content;

	/**
	 * 点击量
	 */
	@Column(name = "HIT_AMT")
	private long hitAmt;

	/**
	 * 图片地址
	 */
	@Column(name = "PATH")
	private String path;

	/**
	 * 预计发布时间
	 */
	@Column(name = "PRE_ISSUE_TIME")
	private Date preIssueTime;

	/**
	 * 发布状态
	 */
	@Column(name = "ISSUE_STATE_EK")
	private String issueStateEk;

	/**
	 * 发布人
	 */
	@Column(name = "ISSUER")
	private String issuer;

	/**
	 * 发布时间
	 */
	@Column(name = "ISSUE_TIME")
	private Date issueTime;

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
	 * 数据状态
	 */
	@Column(name = "DATA_STATE")
	private String dataState = "1";

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
	 * 布局类型
	 */
	@Column(name = "LAYOUT_TYPE_Ek")
	private String layoutTypeEk;

	/**
	 * 审核日期列表
	 */
	@Transient
	private List bizLogList;

	public WlCmInfoEn() {

	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public String getInfoId() {
		return this.infoId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getColumnId() {
		return this.columnId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setHitAmt(long hitAmt) {
		this.hitAmt = hitAmt;
	}

	public long getHitAmt() {
		return this.hitAmt;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public void setPreIssueTime(Date preIssueTime) {
		this.preIssueTime = preIssueTime;
	}

	public Date getPreIssueTime() {
		return this.preIssueTime;
	}

	public void setIssueStateEk(String issueStateEk) {
		this.issueStateEk = issueStateEk;
	}

	public String getIssueStateEk() {
		return this.issueStateEk;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getIssuer() {
		return this.issuer;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	public Date getIssueTime() {
		return this.issueTime;
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

	public void setDataState(String dataState) {
		this.dataState = dataState;
	}

	public String getDataState() {
		return this.dataState;
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

	public String getLayoutTypeEk() {
		return layoutTypeEk;
	}

	public void setLayoutTypeEk(String layoutTypeEk) {
		this.layoutTypeEk = layoutTypeEk;
	}
	

}