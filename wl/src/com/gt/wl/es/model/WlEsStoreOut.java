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
 * @created 2015-04-27 14:04:16
 */
@Entity
@Table(name = "WL_ES_STORE_OUT")
public class WlEsStoreOut extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "OUT_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String outId;

	/**
	 * 销售日期
	 */
	@Column(name = "SALE_DATE")
	private Date saleDate;

	/**
	 * 发货日期
	 */
	@Column(name = "DELIVERY_DATE")
	private Date deliveryDate;

	/**
	 * 经销商
	 */
	@Column(name = "AGENCY")
	private String agency;

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
	 * 配件
	 */
	@Column(name = "ACCESSORIES")
	private String accessories;

	/**
	 * 配件价格
	 */
	@Column(name = "ACC_PRICE")
	private double accPrice;

	/**
	 * 收货人
	 */
	@Column(name = "RECEIVER")
	private String receiver;

	/**
	 * 联系方式
	 */
	@Column(name = "CONTACT")
	private String contact;

	/**
	 * 地址
	 */
	@Column(name = "ADDR")
	private String addr;

	/**
	 * 邮政编码
	 */
	@Column(name = "ZIP_CD")
	private String zipCd;

	/**
	 * 设备编码
	 */
	@Column(name = "DEVICE_CD")
	private String deviceCd;

	/**
	 * 所属省
	 */
	@Column(name = "PROVINCE")
	private String province;

	/**
	 * 销售年度
	 */
	@Column(name = "SALE_YEAR")
	private int saleYear;

	/**
	 * 销售月份
	 */
	@Column(name = "SALE_MONTH")
	private int saleMonth;

	/**
	 * 销售额
	 */
	@Column(name = "SALE_PRICE")
	private double salePrice;

	/**
	 * 物流公司
	 */
	@Column(name = "LOGISTIC")
	private String logistic;

	/**
	 * 快递单号
	 */
	@Column(name = "TRACK_NO")
	private String trackNo;

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
	 * 创建人
	 */
	@Column(name = "CREATOR")
	private String creator;

	/**
	 * 物料名称
	 */
	@Column(name = "ITEM_NAME")
	private String itemName;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime = new Date();

	/**
	 * 是否已注册
	 */
	@Column(name = "IS_REG_FLAG")
	private String isRegFlag = "0";

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;

	/**
	 * 说明
	 */
	@Column(name = "EXPLAINS")
	private String explains;
	/**
	 * 销售日期字符串格式
	 */
	@Transient
	private String saleDates;

	/**
	 * 发货日期字符串格式
	 */
	@Transient
	private String deliveryDates;

	public WlEsStoreOut() {

	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public String getOutId() {
		return this.outId;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Date getSaleDate() {
		return this.saleDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getAgency() {
		return this.agency;
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

	public void setAccessories(String accessories) {
		this.accessories = accessories;
	}

	public String getAccessories() {
		return this.accessories;
	}

	public void setAccPrice(double accPrice) {
		this.accPrice = accPrice;
	}

	public double getAccPrice() {
		return this.accPrice;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContact() {
		return this.contact;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setZipCd(String zipCd) {
		this.zipCd = zipCd;
	}

	public String getZipCd() {
		return this.zipCd;
	}

	public void setDeviceCd(String deviceCd) {
		this.deviceCd = deviceCd;
	}

	public String getDeviceCd() {
		return this.deviceCd;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
		return this.province;
	}

	public void setSaleYear(int saleYear) {
		this.saleYear = saleYear;
	}

	public int getSaleYear() {
		return this.saleYear;
	}

	public void setSaleMonth(int saleMonth) {
		this.saleMonth = saleMonth;
	}

	public int getSaleMonth() {
		return this.saleMonth;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getSalePrice() {
		return this.salePrice;
	}

	public void setLogistic(String logistic) {
		this.logistic = logistic;
	}

	public String getLogistic() {
		return this.logistic;
	}

	public void setTrackNo(String trackNo) {
		this.trackNo = trackNo;
	}

	public String getTrackNo() {
		return this.trackNo;
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

	public String getIsRegFlag() {
		return isRegFlag;
	}

	public void setIsRegFlag(String isRegFlag) {
		this.isRegFlag = isRegFlag;
	}

	public String getSaleDates() {
		return saleDates;
	}

	public void setSaleDates(String saleDates) {
		this.saleDates = saleDates;
	}

	public String getDeliveryDates() {
		return deliveryDates;
	}

	public void setDeliveryDates(String deliveryDates) {
		this.deliveryDates = deliveryDates;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getExplains() {
		return explains;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}

}