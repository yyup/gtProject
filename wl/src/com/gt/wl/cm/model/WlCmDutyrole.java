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
@Table(name = "WL_CM_DUTYROLE")
public class WlCmDutyrole extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String id;

	/**
	 * 职务（角色）名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 职务（角色）代码
	 */
	@Column(name = "CODE")
	private String code;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	private int state;

	/**
	 * 是否默认
	 */
	@Column(name = "IS_DEFAULT")
	private int isDefault;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

	/**
	 * 备注
	 */
	@Column(name = "REMARKS")
	private String remarks;

	/**
	 * 说明
	 */
	@Column(name = "EXPLAINS")
	private String explains;

	public WlCmDutyrole() {

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

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public int getIsDefault() {
		return this.isDefault;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}

	public String getExplains() {
		return this.explains;
	}

}