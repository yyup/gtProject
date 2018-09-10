package com.gt.wl.cm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author rdsf02
 * @version 1.0
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_COMMON_SET")
public class WlCmCommonSet extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "COMMON_SET_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String commonSetId;

	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 设置KEY
	 */
	@Column(name = "SET_KEY")
	private String setKey;

	/**
	 * 设置值
	 */
	@Column(name = "SET_VALUE")
	private String setValue;

	public WlCmCommonSet() {

	}

	public void setCommonSetId(String commonSetId) {
		this.commonSetId = commonSetId;
	}

	public String getCommonSetId() {
		return this.commonSetId;
	}

	public void setSetKey(String setKey) {
		this.setKey = setKey;
	}

	public String getSetKey() {
		return this.setKey;
	}

	public void setSetValue(String setValue) {
		this.setValue = setValue;
	}

	public String getSetValue() {
		return this.setValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}