package com.gt.wl.cm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author liuyj
 * @version 1.0
 * @created 2015-04-08 16:54:24
 */
@Entity
@Table(name = "WL_CM_USER_SECURITY")
public class WlCmUserSecurity extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String id;

	/**
	 * 模块权限ID
	 */
	@Column(name = "SECURITY_ID")
	private String securityId;

	/**
	 * 权限ID集
	 */
	@Column(name = "SECURITY_ARRAY")
	private String securityArray;

	/**
	 * 类型
	 */
	@Column(name = "TYPE")
	private int type;

	/**
	 * 部门ID
	 */
	@Column(name = "DEPARTMENT_ID")
	private String departmentId;

	/**
	 * 所有者ID，0表示所有
	 */
	@Column(name = "OWNER_ID")
	private String ownerId;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

	public WlCmUserSecurity() {

	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public String getSecurityId() {
		return this.securityId;
	}

	public void setSecurityArray(String securityArray) {
		this.securityArray = securityArray;
	}

	public String getSecurityArray() {
		return this.securityArray;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentId() {
		return this.departmentId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerId() {
		return this.ownerId;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

}