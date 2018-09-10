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
 * @author liuyj
 * @version 1.0
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_SELL")
public class WlCmSell extends BaseModel implements java.io.Serializable, IPdTreeModel {

	@Id
	@Column(name = "SELL_ID")
	private String sellId;

	/**
	 * 产品名称
	 */
	@Column(name = "PRODUCT_NAME")
	private String productName;

	/**
	 * 产品类型ID
	 */
	@Column(name = "TYPE_ID")
	private String typeId;

	/**
	 * 内容
	 */
	@Column(name = "CONTENT")
	private String content;

	/**
	 * 单位名称
	 */
	@Column(name = "UNIT_NAME")
	private String unitName;

	/**
	 * 销售价格
	 */
	@Column(name = "PRICE")
	private double price;

	/**
	 * 市场价格
	 */
	@Column(name = "DISCT")
	private double disct;

	/**
	 * 净重
	 */
	@Column(name = "NET_WEIGHT")
	private String netWeight;

	/**
	 * 外形尺寸
	 */
	@Column(name = "OUTLINE_DIM")
	private String outlineDim;

	/**
	 * 销量
	 */
	@Column(name = "SALE_TOTAL")
	private long saleTotal;

	/**
	 * 上架状态
	 */
	@Column(name = "SELL_STATE_EK")
	private String sellStateEk;

	/**
	 * 是否有赠品
	 */
	@Column(name = "IS_HAS_GIFT")
	private String isHasGift;

	/**
	 * 赠品描述
	 */
	@Column(name = "GIFT_DESC")
	private String giftDesc;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

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
	private Date modifyTime;

	/**
	 * 上架时间
	 */
	@Column(name = "SELL_TIME")
	private Date sellTime;
	/**
	 * 物料代码
	 */
	@Column(name = "ITEM_ID")
	private String itemId;
	/**
	 * 物料代码
	 */
	@Column(name = "ITEM_CD")
	private String itemCd;
	/**
	 * 缺货警示数量
	 */
	@Column(name = "OUT_STOCK_NUM")
	private long outStockNum;
	/**
	 * 库存警示数量
	 */
	@Column(name = "LACK_STOCK_NUM")
	private long lackStockNum;

	/**
	 * 审核环节代码
	 */
	@Column(name = "AUDIT_NODE_EK")
	private String auditNodeEk;

	/**
	 * 审核标志
	 */
	@Column(name = "AUDIT_STATE")
	private String auditState;
	
	/**
	 * 天猫链接
	 */
	@Column(name = "TMALL_LINK")
	private String tmallLink;

	/**
	 * 京东链接
	 */
	@Column(name = "JD_LINK")
	private String jdLink;

	/**
	 * 简介
	 */
	@Column(name = "MEMO")
	private String memo;
	
	/**
	 * 审核时间
	 */
	@Column(name = "AUDIT_TIME")
	private Date auditTime;
	
	/**
	 * 首页是否显示(1显示，0不显示)
	 */
	@Column(name = "IS_INDEX_EK")
	private String isIndexEk;

	/** 父类型名称 **/
	@Transient
	private String typeName;

	/**
	 * 库存状态， 0正常，1缺货警示，2库存紧张警示
	 */
	@Transient
	private String inventoryState = "0";

	
	public WlCmSell() {

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

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return this.price;
	}

	public void setDisct(double disct) {
		this.disct = disct;
	}

	public double getDisct() {
		return this.disct;
	}

	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}

	public String getNetWeight() {
		return this.netWeight;
	}

	public void setOutlineDim(String outlineDim) {
		this.outlineDim = outlineDim;
	}

	public String getOutlineDim() {
		return this.outlineDim;
	}

	public void setSaleTotal(long saleTotal) {
		this.saleTotal = saleTotal;
	}

	public long getSaleTotal() {
		return this.saleTotal;
	}

	public void setSellStateEk(String sellStateEk) {
		this.sellStateEk = sellStateEk;
	}

	public String getSellStateEk() {
		return this.sellStateEk;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
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

	public Date getSellTime() {
		return sellTime;
	}

	public void setSellTime(Date sellTime) {
		this.sellTime = sellTime;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemCd() {
		return itemCd;
	}

	public void setItemCd(String itemCd) {
		this.itemCd = itemCd;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getIsHasGift() {
		return isHasGift;
	}

	public void setIsHasGift(String isHasGift) {
		this.isHasGift = isHasGift;
	}

	public String getGiftDesc() {
		return giftDesc;
	}

	public void setGiftDesc(String giftDesc) {
		this.giftDesc = giftDesc;
	}

	public String getNodeId() {

		return this.getSellId();
	}

	public String getParentId() {

		return this.typeId;
	}

	public long getOutStockNum() {
		return outStockNum;
	}

	public void setOutStockNum(long outStockNum) {
		this.outStockNum = outStockNum;
	}

	public long getLackStockNum() {
		return lackStockNum;
	}

	public void setLackStockNum(long lackStockNum) {
		this.lackStockNum = lackStockNum;
	}

	public String getInventoryState() {
		return inventoryState;
	}

	public void setInventoryState(String inventoryState) {
		this.inventoryState = inventoryState;
	}

	public String getAuditNodeEk() {
		return auditNodeEk;
	}

	public void setAuditNodeEk(String auditNodeEk) {
		this.auditNodeEk = auditNodeEk;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getTmallLink() {
		return tmallLink;
	}

	public void setTmallLink(String tmallLink) {
		this.tmallLink = tmallLink;
	}

	public String getJdLink() {
		return jdLink;
	}

	public void setJdLink(String jdLink) {
		this.jdLink = jdLink;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getIsIndexEk() {
		return isIndexEk;
	}

	public void setIsIndexEk(String isIndexEk) {
		this.isIndexEk = isIndexEk;
	}


}