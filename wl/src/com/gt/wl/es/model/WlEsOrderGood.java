package com.gt.wl.es.model;

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
@Table(name = "WL_ES_ORDER_GOOD")
public class WlEsOrderGood extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ORDER_GOOD_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String orderGoodId;

	/**
	 * 订单ID
	 */
	@Column(name = "ORDER_ID")
	private String orderId;

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
	 * 商品图片
	 */
	@Transient
	private String path;

	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public WlEsOrderGood() {

	}

	public void setOrderGoodId(String orderGoodId) {
		this.orderGoodId = orderGoodId;
	}

	public String getOrderGoodId() {
		return this.orderGoodId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderId() {
		return this.orderId;
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

}