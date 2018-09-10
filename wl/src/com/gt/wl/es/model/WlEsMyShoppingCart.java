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
 * @created 2015-04-21 09:04:33
 */
@Entity
@Table(name = "WL_ES_MY_SHOPPING_CART")
public class WlEsMyShoppingCart extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "SHOPPING_CART_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String shoppingCartId;

	/**
	 * 上架产品ID
	 */
	@Column(name = "SELL_ID")
	private String sellId;

	/**
	 * 产品名称
	 */
	@Column(name = "PRODUCT_NAME")
	private String productName;

	/**
	 * 销售价格
	 */
	@Column(name = "PRICE")
	private double price;

	/**
	 * 数量
	 */
	@Column(name = "NUM")
	private long num;

	/**
	 * 单位名称
	 */
	@Column(name = "UNIT_NAME")
	private String unitName;

	/**
	 * 上架状态
	 */
	@Column(name = "SELL_STATE_EK")
	private String sellStateEk;

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
	private Date modifyTime = new Date();

	public WlEsMyShoppingCart() {

	}

	public void setShoppingCartId(String shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	public String getShoppingCartId() {
		return this.shoppingCartId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public String getSellId() {
		return this.sellId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return this.price;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public long getNum() {
		return this.num;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitName() {
		return this.unitName;
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

	public String getSellStateEk() {
		return sellStateEk;
	}

	public void setSellStateEk(String sellStateEk) {
		this.sellStateEk = sellStateEk;
	}

}