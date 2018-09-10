package com.gt.wl.cm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author Administrator
 * @version 1.0
 * @created 2014-11-19 11:07:56
 */
@Entity
@Table(name = "WL_CM_UNIT")
public class WlCmUnit extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "UNIT_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String unitId;

	/**
	 * GROUP_ID
	 */
	@Column(name = "GROUP_ID")
	private String groupId;

	/**
	 * UNIT_NAME
	 */
	@Column(name = "UNIT_NAME")
	private String unitName;

	/**
	 * SEQU
	 */
	@Column(name = "SEQU")
	private int sequ;

	public WlCmUnit() {

	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUnitId() {
		return this.unitId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

}