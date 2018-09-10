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
 * @created 2015-04-29 11:58:12
 */
@Entity
@Table(name = "WL_ES_APPLY")
public class WlEsApply extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "APPLY_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String applyId;

	/**
	 * 会员ID
	 */
	@Column(name = "MEMBER_ID")
	private String memberId;
	/**
	 * 订单号
	 */
	@Column(name = "ORDER_NO")
	private String orderNo;
	/**
	 * 会员帐号
	 */
	@Column(name = "ACCOUNT")
	private String account;

	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 申请时间
	 */
	@Column(name = "APPLY_TIME")
	private Date applyTime = new Date();

	/**
	 * 配件类型
	 */
	@Column(name = "MACHINE_TYPE_EK")
	private String machineTypeEk;

	/**
	 * 支付方式
	 */
	@Column(name = "PAY_MODE_EK")
	private String payModeEk;

	/**
	 * 设备编码
	 */
	@Column(name = "DEVICE_CD")
	private String deviceCd;

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
	 * 物流公司
	 */
	@Column(name = "LOGISTIC_EK")
	private String logisticEk;

	/**
	 * 发货单号
	 */
	@Column(name = "DELIVERY_NO")
	private String deliveryNo;

	/**
	 * 发货人ID
	 */
	@Column(name = "CORP_USER_ID")
	private String corpUserId;

	/**
	 * 发货人
	 */
	@Column(name = "CORP_USER")
	private String corpUser;

	/**
	 * 发货时间
	 */
	@Column(name = "DELIVERY_TIME")
	private Date deliveryTime;

	/**
	 * 是否已发货
	 */
	@Column(name = "IS_DELIVERY_FLAG")
	private String isDeliveryFlag = "0";

	/**
	 * 申请状态
	 */
	@Column(name = "APPLY_STATUE_EK")
	private String applyStatueEk;

	/**
	 * 拒绝原因
	 */
	@Column(name = "REASON")
	private String reason;

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
	 * 费用支付状态
	 */
	@Column(name = "PAYMENT_STATUS_EK")
	private String paymentStatusEk;

	/**
	 * 邮费支付方式
	 */
	@Transient
	private String payMoreEkValue;

	/**
	 * 物流公司
	 */
	@Transient
	private String logiSticEkValue;

	/**
	 * 发货状态
	 */
	@Transient
	private String processStatusValue;
	/**
	 * 配件类型对应的值
	 */
	@Transient
	private String machineTypeEkValue;
	/**
	 * 费用支付状态值
	 */
	@Transient
	private String paymentStatusEkValue;
	/**
	 * 出库日期
	 */
	@Transient
	private Date outDate;
	/**
	 * 出库日期
	 */
	@Transient
	private String outDateStr;
	/**
	 * 型号
	 */
	@Transient
	private String spec;
	/**
	 * 经销商
	 */
	@Transient
	private String agency;

	public WlEsApply() {

	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getApplyId() {
		return this.applyId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberId() {
		return this.memberId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return this.account;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getApplyTime() {
		return this.applyTime;
	}

	public void setMachineTypeEk(String machineTypeEk) {
		this.machineTypeEk = machineTypeEk;
	}

	public String getMachineTypeEk() {
		return this.machineTypeEk;
	}

	public void setPayModeEk(String payModeEk) {
		this.payModeEk = payModeEk;
	}

	public String getPayModeEk() {
		return this.payModeEk;
	}

	public void setDeviceCd(String deviceCd) {
		this.deviceCd = deviceCd;
	}

	public String getDeviceCd() {
		return this.deviceCd;
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

	public void setLogisticEk(String logisticEk) {
		this.logisticEk = logisticEk;
	}

	public String getLogisticEk() {
		return this.logisticEk;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getDeliveryNo() {
		return this.deliveryNo;
	}

	public void setCorpUserId(String corpUserId) {
		this.corpUserId = corpUserId;
	}

	public String getCorpUserId() {
		return this.corpUserId;
	}

	public void setCorpUser(String corpUser) {
		this.corpUser = corpUser;
	}

	public String getCorpUser() {
		return this.corpUser;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Date getDeliveryTime() {
		return this.deliveryTime;
	}

	public void setIsDeliveryFlag(String isDeliveryFlag) {
		this.isDeliveryFlag = isDeliveryFlag;
	}

	public String getIsDeliveryFlag() {
		return this.isDeliveryFlag;
	}

	public void setApplyStatueEk(String applyStatueEk) {
		this.applyStatueEk = applyStatueEk;
	}

	public String getApplyStatueEk() {
		return this.applyStatueEk;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return this.reason;
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

	public String getPayModeEkValue() {
		return payMoreEkValue;
	}

	public void setPayMoreEkValue(String payMoreEkValue) {
		this.payMoreEkValue = payMoreEkValue;
	}

	public String getPayMoreEkValue() {
		return payMoreEkValue;
	}

	public String getLogiSticEkValue() {
		return logiSticEkValue;
	}

	public void setLogiSticEkValue(String logiSticEkValue) {
		this.logiSticEkValue = logiSticEkValue;
	}

	public String getProcessStatusValue() {
		return processStatusValue;
	}

	public void setProcessStatusValue(String processStatusValue) {
		this.processStatusValue = processStatusValue;
	}

	public String getMachineTypeEkValue() {
		return machineTypeEkValue;
	}

	public void setMachineTypeEkValue(String machineTypeEkValue) {
		this.machineTypeEkValue = machineTypeEkValue;
	}

	public String getPaymentStatusEk() {
		return paymentStatusEk;
	}

	public void setPaymentStatusEk(String paymentStatusEk) {
		this.paymentStatusEk = paymentStatusEk;
	}

	public String getPaymentStatusEkValue() {
		return paymentStatusEkValue;
	}

	public void setPaymentStatusEkValue(String paymentStatusEkValue) {
		this.paymentStatusEkValue = paymentStatusEkValue;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getOutDateStr() {
		return outDateStr;
	}

	public void setOutDateStr(String outDateStr) {
		this.outDateStr = outDateStr;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}
}