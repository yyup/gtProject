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

/**
 * @author liuyj
 * @version 1.0
 * @created 2015-09-17 16:52:53
 */
@Entity
@Table(name = "WL_CM_ITEM")
public class WlCmItem extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ITEM_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String itemId;

	/**
	 * 物料类别ID
	 */
	@Column(name = "CATEGORY_ID")
	private String categoryId;

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
	 * 厂商代码
	 */
	@Column(name = "MANU_ITEM_CD")
	private String manuItemCd;

	/**
	 * 厂商名称
	 */
	@Column(name = "MANU_ITEM_NAME")
	private String manuItemName;

	/**
	 * 厂商型号
	 */
	@Column(name = "MANU_SPEC")
	private String manuSpec;

	/**
	 * 条形码
	 */
	@Column(name = "BAR_CD")
	private String barCd;

	/**
	 * 库存上限
	 */
	@Column(name = "UPPER_LIMIT")
	private double upperLimit;

	/**
	 * 库存下限
	 */
	@Column(name = "LOWER_LIMIT")
	private double lowerLimit;

	/**
	 * 出入库强制扫序号
	 */
	@Column(name = "IS_SCAN_FLAG")
	private String isScanFlag;

	/**
	 * 启用标志
	 */
	@Column(name = "IS_ENABLE_FLAG")
	private String isEnableFlag;

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
	 * 图片地址
	 */
	@Column(name = "PATH")
	private String path;

	/**
	 * 是否显示给经销商 1显示，0不显示
	 */
	@Column(name = "IS_SHOW_AGENCY")
	private String isShowAgency = "1";

	/** 父类型名称 **/
	@Transient
	private String categoryName;

	/**
	 * 物料编码是否允许修改
	 */
	@Transient
	private boolean isItemCdEdit;

	public WlCmItem() {

	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryId() {
		return this.categoryId;
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

	public void setManuItemCd(String manuItemCd) {
		this.manuItemCd = manuItemCd;
	}

	public String getManuItemCd() {
		return this.manuItemCd;
	}

	public void setManuItemName(String manuItemName) {
		this.manuItemName = manuItemName;
	}

	public String getManuItemName() {
		return this.manuItemName;
	}

	public void setManuSpec(String manuSpec) {
		this.manuSpec = manuSpec;
	}

	public String getManuSpec() {
		return this.manuSpec;
	}

	public void setBarCd(String barCd) {
		this.barCd = barCd;
	}

	public String getBarCd() {
		return this.barCd;
	}

	public void setUpperLimit(double upperLimit) {
		this.upperLimit = upperLimit;
	}

	public double getUpperLimit() {
		return this.upperLimit;
	}

	public void setLowerLimit(double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public double getLowerLimit() {
		return this.lowerLimit;
	}

	public void setIsEnableFlag(String isEnableFlag) {
		this.isEnableFlag = isEnableFlag;
	}

	public String getIsEnableFlag() {
		return this.isEnableFlag;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getIsScanFlag() {
		return isScanFlag;
	}

	public void setIsScanFlag(String isScanFlag) {
		this.isScanFlag = isScanFlag;
	}

	public boolean isItemCdEdit() {
		return isItemCdEdit;
	}

	public void setItemCdEdit(boolean isItemCdEdit) {
		this.isItemCdEdit = isItemCdEdit;
	}

	public String getIsShowAgency() {
		return isShowAgency;
	}

	public void setIsShowAgency(String isShowAgency) {
		this.isShowAgency = isShowAgency;
	}

}