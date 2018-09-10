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
 * @created 2015-05-14 14:57:09
 */
@Entity
@Table(name = "WL_ES_CUST_REPAIR")
public class WlEsCustRepair extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "REPAIR_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String repairId;

	/**
	 * 维修单号
	 */
	@Column(name = "REPAIR_NO")
	private String repairNo;
	/**
	 * 订单号
	 */
	@Column(name = "ORDER_NO")
	private String orderNo;
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
	 * 名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 联系方式
	 */
	@Column(name = "CONTACT")
	private String contact;

	/**
	 * 申请时间
	 */
	@Column(name = "APPLY_TIME")
	private Date applyTime = new Date();

	/**
	 * 设备编码
	 */
	@Column(name = "DEVICE_CD")
	private String deviceCd;

	/**
	 * 问题描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * 问题模块
	 */
	@Column(name = "MODULE_EK")
	private String moduleEk;

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
	 * 处理状态
	 */
	@Column(name = "PROCESS_STATUS_EK")
	private String processStatusEk;

	/**
	 * 维修费
	 */
	@Column(name = "UPKEEP")
	private double upkeep;

	/**
	 * 运费
	 */
	@Column(name = "FREIGHT")
	private double freight;

	/**
	 * 费用支付状态
	 */
	@Column(name = "PAYMENT_STATUS_EK")
	private String paymentStatusEk;

	/**
	 * 序列号验证状态
	 */
	@Column(name = "SERIAL_VERF_EK")
	private String serialVerfEk;

	/**
	 * 是否退回维修配件
	 */
	@Column(name = "IS_RETURN_FLAG")
	private String isReturnFlag;

	/**
	 * 用户发货单号
	 */
	@Column(name = "USER_DELIVERY_NO")
	private String userDeliveryNo;

	/**
	 * 用户物流公司
	 */
	@Column(name = "USER_LOGISTIC")
	private String userLogistic;

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
	 * 返修配件
	 */
	@Column(name = "ACC_DESC")
	private String accDesc;
	/***
	 * 单据来源
	 */
	@Column(name = "BILL_SOURCE_EK")
	private String billSourceEk;
	/**
	 * 购买时间
	 */
	@Column(name = "BUY_DATE")
	private Date buyDate;
	/***
	 * 购买型号
	 */
	@Column(name = "BUY_SPEC")
	private String buySpec;

	/***
	 * 分析原因
	 */
	@Column(name = "REASON")
	private String reason;

	/***
	 * 处理结果
	 */
	@Column(name = "DEAL_RESULT")
	private String dealResult;

	/***
	 * 其他说明
	 */
	@Column(name = "OTHER_DESCRIPTION")
	private String otherDescription;
	/***
	 * 不良品是否返回
	 */
	@Column(name = "BAD_PRODUCT_RETURN_EK")
	private String badProductReturnEk;
	/***
	 * 运费归属
	 */
	@Column(name = "FREIGHT_HOME_EK")
	private String freightHomeEk;
	/***
	 * 保修类型
	 */
	@Column(name = "WARRANTY_TYPE_EK")
	private String warrantyTypeEk;
	/***
	 * 是否上门维修
	 */
	@Column(name = "ON_SITE_REPAIR_EK")
	private String onSiteRepairEk;

	/**
	 * 服务时间
	 */
	@Column(name = "SERVICE_TIME")
	private Date serviceTime;
	
	/**
	 * 是否发货
	 */
	@Column(name = "IS_SEND_FLAG")
	private String isSendFlag;
	
	
	/***
	 * /**
	 * 处理意见
	 */
	@Transient
	private String reply;

	/**
	 * 图片列表
	 */
	@Transient
	private List<WlEsCustRepairPic> picList;

	/**
	 * 视频列表
	 */
	@Transient
	private List<WlEsCustRepairPic> videoList;

	/**
	 * 处理意见列表
	 */
	@Transient
	private List<WlEsCustRepairSug> sugList;

	/**
	 * 序列号验证状态值
	 */
	@Transient
	private String serialVerfEkValue;

	/**
	 * 问题模块值
	 */
	@Transient
	private String moduleEkValue;
	/**
	 * 支付状态对应的值
	 * */
	@Transient
	private String paymentStatusEkValue;
	/**
	 * 出库日期
	 */
	@Transient
	private Date outDate;
	/**
	 * 型号
	 */
	@Transient
	private String spec;
	/**
	 * 购买平台（即经销商）
	 */
	@Transient
	private String agency;

	public WlEsCustRepair() {

	}

	public void setRepairId(String repairId) {
		this.repairId = repairId;
	}

	public String getRepairId() {
		return this.repairId;
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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContact() {
		return this.contact;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getApplyTime() {
		return this.applyTime;
	}

	public void setDeviceCd(String deviceCd) {
		this.deviceCd = deviceCd;
	}

	public String getDeviceCd() {
		return this.deviceCd;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setModuleEk(String moduleEk) {
		this.moduleEk = moduleEk;
	}

	public String getModuleEk() {
		return this.moduleEk;
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

	public void setProcessStatusEk(String processStatusEk) {
		this.processStatusEk = processStatusEk;
	}

	public String getProcessStatusEk() {
		return this.processStatusEk;
	}

	public void setUpkeep(double upkeep) {
		this.upkeep = upkeep;
	}

	public double getUpkeep() {
		return this.upkeep;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}

	public double getFreight() {
		return this.freight;
	}

	public void setPaymentStatusEk(String paymentStatusEk) {
		this.paymentStatusEk = paymentStatusEk;
	}

	public String getPaymentStatusEk() {
		return this.paymentStatusEk;
	}

	public void setSerialVerfEk(String serialVerfEk) {
		this.serialVerfEk = serialVerfEk;
	}

	public String getSerialVerfEk() {
		return this.serialVerfEk;
	}

	public void setIsReturnFlag(String isReturnFlag) {
		this.isReturnFlag = isReturnFlag;
	}

	public String getIsReturnFlag() {
		return this.isReturnFlag;
	}

	public void setUserDeliveryNo(String userDeliveryNo) {
		this.userDeliveryNo = userDeliveryNo;
	}

	public String getUserDeliveryNo() {
		return this.userDeliveryNo;
	}

	public void setUserLogistic(String userLogistic) {
		this.userLogistic = userLogistic;
	}

	public String getUserLogistic() {
		return this.userLogistic;
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

	public String getAccDesc() {
		return accDesc;
	}

	public void setAccDesc(String accDesc) {
		this.accDesc = accDesc;
	}

	public String getBillSourceEk() {
		return billSourceEk;
	}

	public void setBillSourceEk(String billSourceEk) {
		this.billSourceEk = billSourceEk;
	}

	public List<WlEsCustRepairPic> getPicList() {
		return picList;
	}

	public void setPicList(List<WlEsCustRepairPic> picList) {
		this.picList = picList;
	}

	public List<WlEsCustRepairPic> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<WlEsCustRepairPic> videoList) {
		this.videoList = videoList;
	}

	public List<WlEsCustRepairSug> getSugList() {
		return sugList;
	}

	public void setSugList(List<WlEsCustRepairSug> sugList) {
		this.sugList = sugList;
	}

	public String getRepairNo() {
		return repairNo;
	}

	public void setRepairNo(String repairNo) {
		this.repairNo = repairNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getSerialVerfEkValue() {
		return serialVerfEkValue;
	}

	public void setSerialVerfEkValue(String serialVerfEkValue) {
		this.serialVerfEkValue = serialVerfEkValue;
	}

	public String getModuleEkValue() {
		return moduleEkValue;
	}

	public void setModuleEkValue(String moduleEkValue) {
		this.moduleEkValue = moduleEkValue;
	}

	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public String getBuySpec() {
		return buySpec;
	}

	public void setBuySpec(String buySpec) {
		this.buySpec = buySpec;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDealResult() {
		return dealResult;
	}

	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}

	public String getOtherDescription() {
		return otherDescription;
	}

	public void setOtherDescription(String otherDescription) {
		this.otherDescription = otherDescription;
	}

	public String getBadProductReturnEk() {
		return badProductReturnEk;
	}

	public void setBadProductReturnEk(String badProductReturnEk) {
		this.badProductReturnEk = badProductReturnEk;
	}

	public String getFreightHomeEk() {
		return freightHomeEk;
	}

	public void setFreightHomeEk(String freightHomeEk) {
		this.freightHomeEk = freightHomeEk;
	}

	public String getWarrantyTypeEk() {
		return warrantyTypeEk;
	}

	public void setWarrantyTypeEk(String warrantyTypeEk) {
		this.warrantyTypeEk = warrantyTypeEk;
	}

	public String getOnSiteRepairEk() {
		return onSiteRepairEk;
	}

	public void setOnSiteRepairEk(String onSiteRepairEk) {
		this.onSiteRepairEk = onSiteRepairEk;
	}

	public Date getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Date serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getIsSendFlag() {
		return isSendFlag;
	}

	public void setIsSendFlag(String isSendFlag) {
		this.isSendFlag = isSendFlag;
	}

}