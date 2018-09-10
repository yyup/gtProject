package com.gt.wl.wm.model;

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
 * @created 2015-09-17 16:53:02
 */
@Entity
@Table(name = "WL_WM_STORE_LOSS_DETL")
public class WlWmStoreLossDetl extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "STORE_LOSS_DETL_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String storeLossDetlId;

	/**
	 * 损益ID
	 */
	@Column(name = "STORE_LOSS_ID")
	private String storeLossId;

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
	 * 基本计量单位名称
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
	 * 差异基本数量
	 */
	@Column(name = "DIFF_BASE_QTY")
	private double diffBaseQty;
	/**
	 * 盘盈
	 */
	@Transient
	double overage;
	/**
	 * 盘亏
	 */
	@Transient
	double shortage;

	public WlWmStoreLossDetl() {

	}

	public void setStoreLossDetlId(String storeLossDetlId) {
		this.storeLossDetlId = storeLossDetlId;
	}

	public String getStoreLossDetlId() {
		return this.storeLossDetlId;
	}

	public void setStoreLossId(String storeLossId) {
		this.storeLossId = storeLossId;
	}

	public String getStoreLossId() {
		return this.storeLossId;
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

	public void setDiffBaseQty(double diffBaseQty) {
		this.diffBaseQty = diffBaseQty;
	}

	public double getDiffBaseQty() {
		return this.diffBaseQty;
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

}