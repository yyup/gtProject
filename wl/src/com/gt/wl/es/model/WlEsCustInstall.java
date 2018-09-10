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
@Table(name = "WL_ES_CUST_INSTALL")
public class WlEsCustInstall extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "INSTALL_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String installId;

	/**
	 * 安装单号
	 */
	@Column(name = "INSTALL_NO")
	private String installNo;
	
	/**
	 * 手机
	 */
	@Column(name = "MOBILE")
	private String mobile;
	
	/**
	 * 其他描述
	 */
	@Column(name = "OTHER_DESCRIPTION")
	private String otherDescription;

	/**
	 * 接收人
	 */
	@Column(name = "RECEIVER")
	private String receiver;
	
	/**
	 * 地址
	 */
	@Column(name = "ADDR")
	private String addr;
	
	
	/**
	 * 购买型号
	 */
	@Column(name = "BUY_SPEC")
	private String buySpec;
	
	/**
	 * 设备编码
	 */
	@Column(name = "DEVICE_CD")
	private String deviceCd;
	
	
	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	private String creator;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime = new Date();

	/**
	 * 修改人
	 */
	@Column(name = "MODIFIER")
	private String modifier;

	/**
	 * 修改时间
	 */
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;
	
	/**
	 * 安装状态
	 */
	@Column(name = "INSTALL_STATE")
	private String installState;
	
	/**
	 * 安装状态修改人
	 */
	@Column(name = "INSTALL_STATE_MAN")
	private String installStateMan;
	
	/**
	 * 安装状态修改时间
	 */
	@Column(name = "INSTALL_STATE_TIME")
	private Date installStateTime;
	
	/**
	 * 安装师傅电话
	 */
	@Column(name = "INSTALL_MOBILE")
	private String installMobile;
	

	public WlEsCustInstall() {

	}

	public String getInstallStateMan() {
		return installStateMan;
	}

	public void setInstallStateMan(String installStateMan) {
		this.installStateMan = installStateMan;
	}

	public Date getInstallStateTime() {
		return installStateTime;
	}

	public void setInstallStateTime(Date installStateTime) {
		this.installStateTime = installStateTime;
	}


	public String getInstallId() {
		return installId;
	}

	public void setInstallId(String installId) {
		this.installId = installId;
	}

	public String getInstallNo() {
		return installNo;
	}

	public void setInstallNo(String installNo) {
		this.installNo = installNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOtherDescription() {
		return otherDescription;
	}

	public void setOtherDescription(String otherDescription) {
		this.otherDescription = otherDescription;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}


	public String getBuySpec() {
		return buySpec;
	}

	public void setBuySpec(String buySpec) {
		this.buySpec = buySpec;
	}

	public String getDeviceCd() {
		return deviceCd;
	}

	public void setDeviceCd(String deviceCd) {
		this.deviceCd = deviceCd;
	}


	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getInstallState() {
		return installState;
	}

	public void setInstallState(String installState) {
		this.installState = installState;
	}

	public String getInstallMobile() {
		return installMobile;
	}

	public void setInstallMobile(String installMobile) {
		this.installMobile = installMobile;
	}
	
	
}