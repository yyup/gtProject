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
import org.joyone.model.IPdTreeModel;

/**
 * 产品配置
 * @author Lizj
 *
 */
@Entity
@Table(name = "WL_CM_SELL_ALLOCATION")
public class WlCmSellAllocation extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ALLOCATION_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String allocationId;

	/**
	 * 对应的产品ID
	 */
	@Column(name = "SELL_ID")
	private String sellId;

	/**
	 * 排序
	 */
	@Column(name = "SEQU")
	private String sequ;

	/**
	 * 关联的产品id
	 */
	@Column(name = "PARENT_SELL_ID")
	private String parentSellId;
	
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
	
	
	/** 父类型名称 **/
	@Transient
	private String typeName;
	
	@Transient
	private String picPath;
	

	
	public String getPicPath() {
		return picPath;
	}


	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}


	public WlCmSellAllocation() {

	}


	public String getAllocationId() {
		return allocationId;
	}


	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}


	public String getSellId() {
		return sellId;
	}


	public void setSellId(String sellId) {
		this.sellId = sellId;
	}


	public String getSequ() {
		return sequ;
	}


	public void setSequ(String sequ) {
		this.sequ = sequ;
	}


	public String getParentSellId() {
		return parentSellId;
	}


	public void setParentSellId(String parentSellId) {
		this.parentSellId = parentSellId;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}



	

}