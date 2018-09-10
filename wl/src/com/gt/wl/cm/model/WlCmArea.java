package com.gt.wl.cm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;
import org.joyone.model.IPdTreeModel;

/**
 * @author rdsf02
 * @version 1.0
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_AREA")
public class WlCmArea extends BaseModel implements IPdTreeModel, java.io.Serializable {

	@Id
	@Column(name = "AREA_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String areaId;

	/**
	 * 区域名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 等级
	 */
	@Column(name = "LEVEL_EK")
	private String levelEk;

	/**
	 * 父级ID
	 */
	@Column(name = "PARENT_AREA_ID")
	private String parentAreaId;

	public WlCmArea() {

	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaId() {
		return this.areaId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setLevelEk(String levelEk) {
		this.levelEk = levelEk;
	}

	public String getLevelEk() {
		return this.levelEk;
	}

	public void setParentAreaId(String parentAreaId) {
		this.parentAreaId = parentAreaId;
	}

	public String getParentAreaId() {
		return this.parentAreaId;
	}

	public String getNodeId() {
		// TODO Auto-generated method stub
		return this.getAreaId();
	}

	public String getParentId() {
		// TODO Auto-generated method stub
		return this.getParentAreaId();
	}

}