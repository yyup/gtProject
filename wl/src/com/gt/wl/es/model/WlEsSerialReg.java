package com.gt.wl.es.model;

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
 * @author liuyj
 * @version 1.0
 * @created 2015-04-30 15:07:49
 */
@Entity
@Table(name = "WL_ES_SERIAL_REG")
public class WlEsSerialReg extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "REG_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String regId;

	/**
	 * 登记日期
	 */
	@Column(name = "REG_DATE")
	private Date regDate;

	/**
	 * 收货人
	 */
	@Column(name = "RECEIVER")
	private String receiver;

	/**
	 * 联系方式
	 */
	@Column(name = "CONTACT")
	private String contact;

	/**
	 * 经销商
	 */
	@Column(name = "AGENCY")
	private String agency;

	/**
	 * 产品名称
	 */
	@Column(name = "PRODUCT_NAME")
	private String productName;

	/**
	 * 设备编码
	 */
	@Column(name = "DEVICE_CD")
	private String deviceCd;

	/**
	 * 购买渠道
	 */
	@Column(name = "BUY_CHANNEL_EK")
	private String buyChannelEk;

	/**
	 * 购买日期
	 */
	@Column(name = "BUY_DATE")
	private Date buyDate;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * 是否人工审核
	 */
	@Column(name = "IS_MANUAL_FLAG")
	private String isManualFlag;

	/**
	 * 审核标志0-未审核,1-审核通过,2-驳回,3-终审通过
	 */
	@Column(name = "AUDIT_STATE")
	private String auditState;

	/**
	 * 拒绝原因
	 */
	@Column(name = "REASON")
	private String reason;

	/**
	 * 框架保修到期日期
	 */
	@Column(name = "FRAME_EXP_DATE")
	private Date frameExpDate;

	/**
	 * 配件保修到期日期
	 */
	@Column(name = "ACC_EXP_DATE")
	private Date accExpDate;

	/**
	 * 会员ID
	 */
	@Column(name = "MEMBER_ID")
	private String memberId;

	/**
	 * 我的帐号
	 */
	@Column(name = "ACCOUNT")
	private String account;

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
	 * 修改时间
	 */
	@Column(name = "TAOBAO_NO")
	private String taobaoNo;

	@Transient
	private List picList;
	/**
	 * 审核对应的枚举值
	 */
	@Transient
	private String auditStateValue;
	/**
	 * 购买渠道对应的枚举值
	 */
	@Transient
	private String buyChannelEkValue;

	public WlEsSerialReg() {

	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getRegId() {
		return this.regId;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Date getRegDate() {
		return this.regDate;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContact() {
		return this.contact;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getAgency() {
		return this.agency;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setDeviceCd(String deviceCd) {
		this.deviceCd = deviceCd;
	}

	public String getDeviceCd() {
		return this.deviceCd;
	}

	public void setBuyChannelEk(String buyChannelEk) {
		this.buyChannelEk = buyChannelEk;
	}

	public String getBuyChannelEk() {
		return this.buyChannelEk;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public Date getBuyDate() {
		return this.buyDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setIsManualFlag(String isManualFlag) {
		this.isManualFlag = isManualFlag;
	}

	public String getIsManualFlag() {
		return this.isManualFlag;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getAuditState() {
		return this.auditState;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return this.reason;
	}

	public void setFrameExpDate(Date frameExpDate) {
		this.frameExpDate = frameExpDate;
	}

	public Date getFrameExpDate() {
		return this.frameExpDate;
	}

	public void setAccExpDate(Date accExpDate) {
		this.accExpDate = accExpDate;
	}

	public Date getAccExpDate() {
		return this.accExpDate;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberId() {
		return this.memberId;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return this.account;
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

	public List getPicList() {
		return picList;
	}

	public void setPicList(List picList) {
		this.picList = picList;
	}

	public String getAuditStateValue() {
		return auditStateValue;
	}

	public void setAuditStateValue(String auditStateValue) {
		this.auditStateValue = auditStateValue;
	}

	public String getBuyChannelEkValue() {
		return buyChannelEkValue;
	}

	public void setBuyChannelEkValue(String buyChannelEkValue) {
		this.buyChannelEkValue = buyChannelEkValue;
	}

	public String getTaobaoNo() {
		return taobaoNo;
	}

	public void setTaobaoNo(String taobaoNo) {
		this.taobaoNo = taobaoNo;
	}

}