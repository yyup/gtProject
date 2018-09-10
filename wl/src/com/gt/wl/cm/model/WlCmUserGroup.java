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
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_USER_GROUP")
public class WlCmUserGroup extends BaseModel implements java.io.Serializable {

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
	 * 部门ID
	 */
	@Column(name = "DEPARTMENT_ID")
	private String departmentId;

	/**
	 * 职务或角色ID
	 */
	@Column(name = "GROUP_ID")
	private String groupId;

	/**
	 * 类型 1表示职务，2表示角色，3表示用户组
	 */
	@Column(name = "TYPE")
	private int type;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

	public WlCmUserGroup() {

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

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentId() {
		return this.departmentId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

}