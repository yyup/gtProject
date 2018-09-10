package com.gt.wl.cm.model;

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
 * @created 2015-09-17 16:52:54
 */
@Entity
@Table(name = "WL_CM_ORG")
public class WlCmOrg extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ORG_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String orgId;

	/**
	 * 单位编码
	 */
	@Column(name = "ORG_CD")
	private String orgCd;

	/**
	 * 单位名称
	 */
	@Column(name = "ORG_NAME")
	private String orgName;

	/**
	 * 类型
	 */
	@Column(name = "ORG_TYPE_EK")
	private String orgTypeEk;

	/**
	 * 联系人
	 */
	@Column(name = "CONTACT")
	private String contact;

	/**
	 * 手机号码
	 */
	@Column(name = "MOBILE")
	private String mobile;

	/**
	 * 地址
	 */
	@Column(name = "ADDR")
	private String addr;

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;

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
	 * 启用标志
	 */
	@Column(name = "IS_ENABLE_FLAG")
	private String isEnableFlag = "0";
	/**
	 * 传真
	 */
	@Column(name = "FAX")
	private String fax;
	/**
	 * 办公电话
	 */
	@Column(name = "TEL")
	private String tel;

	/**
	 * 电子邮件
	 */
	@Column(name = "EMAIL")
	private String email;

	/**
	 * 类型对应的value
	 */
	@Transient
	private String orgTypeEkValue;

	public WlCmOrg() {

	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}

	public String getOrgCd() {
		return this.orgCd;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgTypeEk(String orgTypeEk) {
		this.orgTypeEk = orgTypeEk;
	}

	public String getOrgTypeEk() {
		return this.orgTypeEk;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContact() {
		return this.contact;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
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

	public void setIsEnableFlag(String isEnableFlag) {
		this.isEnableFlag = isEnableFlag;
	}

	public String getIsEnableFlag() {
		return this.isEnableFlag;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getOrgTypeEkValue() {
		return orgTypeEkValue;
	}

	public void setOrgTypeEkValue(String orgTypeEkValue) {
		this.orgTypeEkValue = orgTypeEkValue;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}