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
 * @created 2015-04-21 09:04:34
 */
@Entity
@Table(name = "WL_ES_ORDER")
public class WlEsOrder extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ORDER_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String orderId;

	/**
	 * 订单号
	 */
	@Column(name = "ORDER_NO")
	private String orderNo;

	/**
	 * 下单时间
	 */
	@Column(name = "ORDER_TIME")
	private Date orderTime;

	/**
	 * 下单人ID
	 */
	@Column(name = "MEMBER_ID")
	private String memberId;

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
	 * 收货地址(省)ID
	 */
	@Column(name = "PROVINCE_ID")
	private String provinceId;

	/**
	 * 收货地址(省)
	 */
	@Column(name = "PROVINCE")
	private String province;

	/**
	 * 收货地址(市)ID
	 */
	@Column(name = "CITY_ID")
	private String cityId;

	/**
	 * 收货地址(市)
	 */
	@Column(name = "CITY")
	private String city;

	/**
	 * 收货地址(区)ID
	 */
	@Column(name = "AREA_ID")
	private String areaId;

	/**
	 * 收货地区(区)
	 */
	@Column(name = "AREA")
	private String area;

	/**
	 * 地址
	 */
	@Column(name = "ADDR")
	private String addr;

	/**
	 * 订单金额
	 */
	@Column(name = "AMT")
	private double amt;

	/**
	 * 支付金额
	 */
	@Column(name = "PAY_AMT")
	private double payAmt;

	/**
	 * 买家留言
	 */
	@Column(name = "BUY_MSG")
	private String buyMsg;

	/**
	 * 发货单号
	 */
	@Column(name = "DELIVERY_NO")
	private String deliveryNo;

	/**
	 * 物流公司
	 */
	@Column(name = "LOGISTIC_EK")
	private String logisticEk;

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
	 * 收货时间
	 */
	@Column(name = "RECEIPT_TIME")
	private Date receiptTime;

	/**
	 * 关闭订单时间
	 */
	@Column(name = "CLOSE_TIME")
	private Date closeTime;

	/**
	 * 订单状态
	 */
	@Column(name = "ORDER_STATE_EK")
	private String orderStateEk;

	/**
	 * 支付方式
	 */
	@Column(name = "PAY_MODE_EK")
	private String payModeEk;

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
	 * 货物件数
	 */
	@Column(name = "GOOD_NUM")
	private int goodNum;
	/**
	 * 购买渠道(默认官网)
	 */
	@Column(name = "BUY_CHANNEL_EK")
	private String buyChannelEk = "3";
	/**
	 * 客户旺旺号
	 */
	@Column(name = "TAOBAO_NO")
	private String taoBaoNo;
	/**
	 * 接单客服旺旺号
	 */
	@Column(name = "SERVICE_TAOBAO_NO")
	private String serviceTaoBaoNo;

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;
	/***
	 * 商品列表
	 */
	@Transient
	private List<WlEsOrderGood> goodList;
	/**
	 * 订单状态对应的值
	 */
	@Transient
	private String orderStateEkValue;
	/***
	 * 会员账号
	 */
	@Transient
	private String memberAccount;
	/**
	 * 会员邮箱
	 */
	@Transient
	private String memberEmail;
	
	/**
	 * 订单状态对应的值(英文)
	 */
	@Transient
	private String orderStateEkEnValue;

	public WlEsOrder() {

	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getOrderTime() {
		return this.orderTime;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberId() {
		return this.memberId;
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

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public double getAmt() {
		return this.amt;
	}

	public void setPayAmt(double payAmt) {
		this.payAmt = payAmt;
	}

	public double getPayAmt() {
		return this.payAmt;
	}

	public void setBuyMsg(String buyMsg) {
		this.buyMsg = buyMsg;
	}

	public String getBuyMsg() {
		return this.buyMsg;
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

	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}

	public Date getReceiptTime() {
		return this.receiptTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Date getCloseTime() {
		return this.closeTime;
	}

	public void setOrderStateEk(String orderStateEk) {
		this.orderStateEk = orderStateEk;
	}

	public String getOrderStateEk() {
		return this.orderStateEk;
	}

	public void setPayModeEk(String payModeEk) {
		this.payModeEk = payModeEk;
	}

	public String getPayModeEk() {
		return this.payModeEk;
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

	public int getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(int goodNum) {
		this.goodNum = goodNum;
	}

	public List<WlEsOrderGood> getGoodList() {
		return goodList;
	}

	public void setGoodList(List<WlEsOrderGood> goodList) {
		this.goodList = goodList;
	}

	public String getLogisticEk() {
		return logisticEk;
	}

	public void setLogisticEk(String logisticEk) {
		this.logisticEk = logisticEk;
	}

	public String getOrderStateEkValue() {
		return orderStateEkValue;
	}

	public void setOrderStateEkValue(String orderStateEkValue) {
		this.orderStateEkValue = orderStateEkValue;
	}

	public String getMemberAccount() {
		return memberAccount;
	}

	public void setMemberAccount(String memberAccount) {
		this.memberAccount = memberAccount;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getBuyChannelEk() {
		return buyChannelEk;
	}

	public void setBuyChannelEk(String buyChannelEk) {
		this.buyChannelEk = buyChannelEk;
	}

	public String getTaoBaoNo() {
		return taoBaoNo;
	}

	public void setTaoBaoNo(String taoBaoNo) {
		this.taoBaoNo = taoBaoNo;
	}

	public String getServiceTaoBaoNo() {
		return serviceTaoBaoNo;
	}

	public void setServiceTaoBaoNo(String serviceTaoBaoNo) {
		this.serviceTaoBaoNo = serviceTaoBaoNo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOrderStateEkEnValue() {
		return orderStateEkEnValue;
	}

	public void setOrderStateEkEnValue(String orderStateEkEnValue) {
		this.orderStateEkEnValue = orderStateEkEnValue;
	}

}