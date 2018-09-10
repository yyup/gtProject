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
 * @created 2015-12-04 11:10:25
 */
@Entity
@Table(name = "WL_ES_AGENCY_ORDER")
public class WlEsAgencyOrder extends BaseModel implements java.io.Serializable {

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
	 * 下单人
	 */
	@Column(name = "USER_ID")
	private String userId;

	/**
	 * 姓名
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 单位ID
	 */
	@Column(name = "ORG_ID")
	private String orgId;

	/**
	 * 单位名称
	 */
	@Column(name = "ORG_NAME")
	private String orgName;

	/**
	 * 联系人
	 */
	@Column(name = "CONTACT")
	private String contact;

	/**
	 * 联系方式
	 */
	@Column(name = "CONTACT_WAY")
	private String contactWay;

	/**
	 * 订单状态
	 */
	@Column(name = "AGENCY_ORDER_STATE_EK")
	private String agencyOrderStateEk;

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
	 * 订单总数量
	 */
	@Column(name = "TOTAL_QTY")
	private double totalQty;

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
	 * 所有物料的名字组合（如：滑轮机等2种物料）
	 */
	@Transient
	private String allItemName;

	public WlEsAgencyOrder() {

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

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	public String getContactWay() {
		return this.contactWay;
	}

	public void setAgencyOrderStateEk(String agencyOrderStateEk) {
		this.agencyOrderStateEk = agencyOrderStateEk;
	}

	public String getAgencyOrderStateEk() {
		return this.agencyOrderStateEk;
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

	public double getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(double totalQty) {
		this.totalQty = totalQty;
	}

	public String getAllItemName() {
		return allItemName;
	}

	public void setAllItemName(String allItemName) {
		this.allItemName = allItemName;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getLogisticEk() {
		return logisticEk;
	}

	public void setLogisticEk(String logisticEk) {
		this.logisticEk = logisticEk;
	}

	public String getCorpUserId() {
		return corpUserId;
	}

	public void setCorpUserId(String corpUserId) {
		this.corpUserId = corpUserId;
	}

	public String getCorpUser() {
		return corpUser;
	}

	public void setCorpUser(String corpUser) {
		this.corpUser = corpUser;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

}