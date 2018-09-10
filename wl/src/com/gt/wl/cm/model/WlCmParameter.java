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
@Table(name = "WL_CM_PARAMETER")
public class WlCmParameter extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String id;

	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 代码
	 */
	@Column(name = "CODE")
	private String code;

	/**
	 * 值
	 */
	@Column(name = "VALUE")
	private String value;

	/**
	 * 单位
	 */
	@Column(name = "UNIT")
	private String unit;

	/**
	 * 类型
	 */
	@Column(name = "TYPE")
	private String type;

	/**
	 * 有权限用户名集合
	 */
	@Column(name = "USER_NAMES")
	private String userNames;

	/**
	 * 有权限用户ID集合
	 */
	@Column(name = "USER_IDS")
	private String userIds;

	/**
	 * 说明
	 */
	@Column(name = "EXPLAINS")
	private String explains;

	public WlCmParameter() {

	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	public String getUserNames() {
		return this.userNames;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUserIds() {
		return this.userIds;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}

	public String getExplains() {
		return this.explains;
	}

}