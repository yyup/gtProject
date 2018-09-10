package com.gt.wl.wm.model;

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
 * @created 2015-09-17 16:53:00
 */
@Entity
@Table(name = "WL_WM_STORE_CHECK_DETL")
public class WlWmStoreCheckDetl extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "STORE_CHECK_DETL_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String storeCheckDetlId;

	/**
	 * 盘点单据ID
	 */
	@Column(name = "STORE_CHECK_ID")
	private String storeCheckId;

	/**
	 * 仓库ID
	 */
	@Column(name = "STORAGE_ID")
	private String storageId;

	/**
	 * 仓库名称
	 */
	@Column(name = "STORAGE_NAME")
	private String storageName;

	/**
	 * 物料ID
	 */
	@Column(name = "ITEM_ID")
	private String itemId;

	/**
	 * 物料代码
	 */
	@Column(name = "ITEM_CD")
	private String itemCd;

	/**
	 * 物料名称
	 */
	@Column(name = "ITEM_NAME")
	private String itemName;

	/**
	 * 物料类别ID
	 */
	@Column(name = "CATEGORY_ID")
	private String categoryId;

	/**
	 * 规格型号
	 */
	@Column(name = "SPEC")
	private String spec;

	/**
	 * 基本计量单位
	 */
	@Column(name = "BASE_UNIT_ID")
	private String baseUnitId;

	/**
	 * 单位名称
	 */
	@Column(name = "BASE_UNIT_NAME")
	private String baseUnitName;

	/**
	 * 账面基本数量
	 */
	@Column(name = "BOOK_BASE_QTY")
	private double bookBaseQty;

	/**
	 * 实盘基本数量
	 */
	@Column(name = "CHECK_BASE_QTY")
	private double checkBaseQty;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;

	/**
	 * 修改时间
	 */
	@Column(name = "MODIFY_TIME")
	private Date modifyTime = new Date();

	/**
	 * 修改人
	 */
	@Column(name = "MODIFIER")
	private String modifier;
	/**
	 * 异常信息
	 */
	@Transient
	private String result;
	/**
	 * 盈亏数量
	 */
	@Transient
	private double profitOrLoss;
	/**
	 * 盘盈数量
	 */
	@Transient
	private double overage;
	/**
	 * 盘亏数量
	 */
	@Transient
	private double shortage;

	/**
	 * 已扫数量
	 */
	@Transient
	private double hasUnitQty;

	@Transient
	private double baseUnitQty;

	@Transient
	private String detlId;

	public WlWmStoreCheckDetl() {

	}

	public void setStoreCheckDetlId(String storeCheckDetlId) {
		this.storeCheckDetlId = storeCheckDetlId;
	}

	public String getStoreCheckDetlId() {
		return this.storeCheckDetlId;
	}

	public void setStoreCheckId(String storeCheckId) {
		this.storeCheckId = storeCheckId;
	}

	public String getStoreCheckId() {
		return this.storeCheckId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	public String getStorageId() {
		return this.storageId;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getStorageName() {
		return this.storageName;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemCd(String itemCd) {
		this.itemCd = itemCd;
	}

	public String getItemCd() {
		return this.itemCd;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryId() {
		return this.categoryId;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setBaseUnitId(String baseUnitId) {
		this.baseUnitId = baseUnitId;
	}

	public String getBaseUnitId() {
		return this.baseUnitId;
	}

	public void setBaseUnitName(String baseUnitName) {
		this.baseUnitName = baseUnitName;
	}

	public String getBaseUnitName() {
		return this.baseUnitName;
	}

	public void setBookBaseQty(double bookBaseQty) {
		this.bookBaseQty = bookBaseQty;
	}

	public double getBookBaseQty() {
		return this.bookBaseQty;
	}

	public void setCheckBaseQty(double checkBaseQty) {
		this.checkBaseQty = checkBaseQty;
	}

	public double getCheckBaseQty() {
		return this.checkBaseQty;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifier() {
		return this.modifier;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public double getProfitOrLoss() {
		return profitOrLoss;
	}

	public void setProfitOrLoss(double profitOrLoss) {
		this.profitOrLoss = profitOrLoss;
	}

	public double getOverage() {
		return overage;
	}

	public void setOverage(double overage) {
		this.overage = overage;
	}

	public double getShortage() {
		return shortage;
	}

	public void setShortage(double shortage) {
		this.shortage = shortage;
	}

	public double getHasUnitQty() {
		return hasUnitQty;
	}

	public void setHasUnitQty(double hasUnitQty) {
		this.hasUnitQty = hasUnitQty;
	}

	public double getBaseUnitQty() {
		return baseUnitQty;
	}

	public void setBaseUnitQty(double baseUnitQty) {
		this.baseUnitQty = baseUnitQty;
	}

	public String getDetlId() {
		return detlId;
	}

	public void setDetlId(String detlId) {
		this.detlId = detlId;
	}

}