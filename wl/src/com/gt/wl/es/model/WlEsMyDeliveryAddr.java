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
 * @created 2015-04-21 09:04:32
 */
@Entity
@Table(name = "WL_ES_MY_DELIVERY_ADDR")
public class WlEsMyDeliveryAddr extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "DELIVERY_ADDR_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String deliveryAddrId;

	/**
	 * 收货人
	 */
	@Column(name = "RECEIVER")
	private String receiver;

	/**
	 * 手机号码
	 */
	@Column(name = "MOBILE")
	private String mobile;

	/**
	 * 所属省ID
	 */
	@Column(name = "PROVINCE_ID")
	private String provinceId;

	/**
	 * 所属省
	 */
	@Column(name = "PROVINCE")
	private String province;

	/**
	 * 所属市ID
	 */
	@Column(name = "CITY_ID")
	private String cityId;

	/**
	 * 所属市
	 */
	@Column(name = "CITY")
	private String city;

	/**
	 * 区域ID
	 */
	@Column(name = "AREA_ID")
	private String areaId;

	/**
	 * 所属区
	 */
	@Column(name = "AREA")
	private String area;

	/**
	 * 地址
	 */
	@Column(name = "ADDR")
	private String addr;

	/**
	 * 是否默认收货地址
	 */
	@Column(name = "IS_DEFAULT_FLAG")
	private String isDefaultFlag;

	/**
	 * 邮政编码
	 */
	@Column(name = "ZIP_CD")
	private String zipCd;

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

	public WlEsMyDeliveryAddr() {

	}

	public void setDeliveryAddrId(String deliveryAddrId) {
		this.deliveryAddrId = deliveryAddrId;
	}

	public String getDeliveryAddrId() {
		return this.deliveryAddrId;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceId() {
		return this.provinceId;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
		return this.province;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityId() {
		return this.cityId;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return this.city;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaId() {
		return this.areaId;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getArea() {
		return this.area;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setIsDefaultFlag(String isDefaultFlag) {
		this.isDefaultFlag = isDefaultFlag;
	}

	public String getIsDefaultFlag() {
		return this.isDefaultFlag;
	}

	public void setZipCd(String zipCd) {
		this.zipCd = zipCd;
	}

	public String getZipCd() {
		return this.zipCd;
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

}