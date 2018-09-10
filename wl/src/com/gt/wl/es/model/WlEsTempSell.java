package com.gt.wl.es.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author huangbj
 * @version 1.0
 * @created 2015-12-04 11:10:25
 */
@Entity
@Table(name = "WL_ES_TEMP_SELL")
public class WlEsTempSell extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "TEMP_SELL_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String tempSellId;

	/**
	 * 上架商品id
	 */
	@Column(name = "SELL_ID")
	private String sellId;

	/**
	 * 商品名称
	 */
	@Column(name = "PRODUCT_NAME")
	private String productName;

	/**
	 * 运费模板id
	 */
	@Column(name = "TEMP_ID")
	private String tempId;

	public WlEsTempSell() {

	}

	public String getTempSellId() {
		return tempSellId;
	}

	public void setTempSellId(String tempSellId) {
		this.tempSellId = tempSellId;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

}