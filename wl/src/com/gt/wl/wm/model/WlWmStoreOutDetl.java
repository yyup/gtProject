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
 * @created 2015-09-17 16:53:03
 */
@Entity
@Table(name = "WL_WM_STORE_OUT_DETL")
public class WlWmStoreOutDetl extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "STORE_OUT_DETL_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String storeOutDetlId;

	/**
	 * 出库单ID
	 */
	@Column(name = "STORE_OUT_ID")
	private String storeOutId;

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
	 * 基本计量单位数量
	 */
	@Column(name = "BASE_UNIT_QTY")
	private double baseUnitQty;

	/**
	 * 通知基本计量单位数量
	 */
	@Column(name = "NOTICE_BASE_QTY")
	private double noticeBaseQty;

	/**
	 * 单位名称
	 */
	@Column(name = "BASE_UNIT_NAME")
	private String baseUnitName;

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
	 * 收货人
	 */
	@Column(name = "CONSIGNEE")
	private String consignee;

	/**
	 * 联系方式
	 */
	@Column(name = "CONTACT_WAY")
	private String contactWay;

	/**
	 * 地址
	 */
	@Column(name = "ADDR")
	private String addr;

	/**
	 * 已扫数量
	 */
	@Transient
	private double hasUnitQty;

	@Transient
	private String detlId;

	public WlWmStoreOutDetl() {

	}

	public void setStoreOutDetlId(String storeOutDetlId) {
		this.storeOutDetlId = storeOutDetlId;
	}

	public String getStoreOutDetlId() {
		return this.storeOutDetlId;
	}

	public void setStoreOutId(String storeOutId) {
		this.storeOutId = storeOutId;
	}

	public String getStoreOutId() {
		return this.storeOutId;
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

	public void setBaseUnitQty(double baseUnitQty) {
		this.baseUnitQty = baseUnitQty;
	}

	public double getBaseUnitQty() {
		return this.baseUnitQty;
	}

	public double getNoticeBaseQty() {
		return noticeBaseQty;
	}

	public void setNoticeBaseQty(double noticeBaseQty) {
		this.noticeBaseQty = noticeBaseQty;
	}

	public void setBaseUnitName(String baseUnitName) {
		this.baseUnitName = baseUnitName;
	}

	public String getBaseUnitName() {
		return this.baseUnitName;
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

	public double getHasUnitQty() {
		return hasUnitQty;
	}

	public void setHasUnitQty(double hasUnitQty) {
		this.hasUnitQty = hasUnitQty;
	}

	public String getDetlId() {
		return detlId;
	}

	public void setDetlId(String detlId) {
		this.detlId = detlId;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

}