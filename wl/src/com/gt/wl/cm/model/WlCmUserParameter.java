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
 * @created 2015-04-08 16:54:24
 */
@Entity
@Table(name = "WL_CM_USER_PARAMETER")
public class WlCmUserParameter extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String id;

	/**
	 * 用户ID
	 */
	@Column(name = "USER_ID")
	private String userId;

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

	public WlCmUserParameter() {

	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
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

}