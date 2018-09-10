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
@Table(name = "WL_ES_ARRIVE_AREA")
public class WlEsArriveArea extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ARRIVE_AREA_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String arriveAreaId;

	/**
	 * 到达地区id
	 */
	@Column(name = "AREA_ID")
	private String areaId;
	/**
	 * 地区名称
	 */
	@Column(name = "NAME")
	private String name;
	/**
	 * 模板行id
	 */
	@Column(name = "ROW_ID")
	private String rowId;

	public String getArriveAreaId() {
		return arriveAreaId;
	}

	public void setArriveAreaId(String arriveAreaId) {
		this.arriveAreaId = arriveAreaId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

}