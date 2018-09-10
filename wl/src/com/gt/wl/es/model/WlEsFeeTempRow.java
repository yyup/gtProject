package com.gt.wl.es.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author huangbj
 * @version 1.0
 * @created 2015-12-04 11:10:25
 */
@Entity
@Table(name = "WL_ES_FEE_TEMP_ROW")
public class WlEsFeeTempRow extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ROW_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String rowId;

	/**
	 * 运费模板id
	 */
	@Column(name = "TEMP_ID")
	private String tempId;

	/**
	 * 首件运费
	 */
	@Column(name = "FIRST_FEE")
	private double firstFee;
	/**
	 * 首件区间
	 */
	@Column(name = "FIRST_INTERVAL")
	private long firstInterval;
	/**
	 * 续件运费
	 */
	@Column(name = "ADD_FEE")
	private double addFee;
	/**
	 * 续件区间
	 */
	@Column(name = "ADD_INTERVAL")
	private long addInterval;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;
	/**
	 * 地区显示简称
	 */
	@Column(name = "AREA_ATTR")
	private String areaAttr;
	/***
	 * 到达地区列表
	 */
	@Transient
	private List<WlEsArriveArea> arriveAreaList;
	/**
	 * 所有到达地区id，逗号隔开
	 */
	@Transient
	private String allAreaId;

	public WlEsFeeTempRow() {

	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public double getFirstFee() {
		return firstFee;
	}

	public void setFirstFee(double firstFee) {
		this.firstFee = firstFee;
	}

	public long getFirstInterval() {
		return firstInterval;
	}

	public void setFirstInterval(long firstInterval) {
		this.firstInterval = firstInterval;
	}

	public double getAddFee() {
		return addFee;
	}

	public void setAddFee(double addFee) {
		this.addFee = addFee;
	}

	public long getAddInterval() {
		return addInterval;
	}

	public void setAddInterval(long addInterval) {
		this.addInterval = addInterval;
	}

	public int getSequ() {
		return sequ;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public List<WlEsArriveArea> getArriveAreaList() {
		return arriveAreaList;
	}

	public void setArriveAreaList(List<WlEsArriveArea> arriveAreaList) {
		this.arriveAreaList = arriveAreaList;
	}

	public String getAllAreaId() {
		return allAreaId;
	}

	public void setAllAreaId(String allAreaId) {
		this.allAreaId = allAreaId;
	}

	public String getAreaAttr() {
		return areaAttr;
	}

	public void setAreaAttr(String areaAttr) {
		this.areaAttr = areaAttr;
	}

}