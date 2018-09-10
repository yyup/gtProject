package com.gt.wl.cm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;
import org.joyone.model.IPdTreeModel;

/**
 * @author liuyj
 * @version 1.0
 * @created 2015-04-08 16:33:51
 */
@Entity
@Table(name = "WL_CM_MENU")
public class WlCmMenu extends BaseModel implements IPdTreeModel, java.io.Serializable {

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
	 * 状态：0表示无效、1表示有效
	 */
	@Column(name = "STATE")
	private int state;

	/**
	 * 顺序种子
	 */
	@Column(name = "SEQU")
	private int sequ;

	/**
	 * 父ID号
	 */
	@Column(name = "PARENT_ID")
	private String parentId;

	/**
	 * 值
	 */
	@Column(name = "VALUE")
	private String value;

	/**
	 * 目标
	 */
	@Column(name = "TARGET")
	private String target;

	/**
	 * 安全ID
	 */
	@Column(name = "SECURITY_ID")
	private String securityId;

	/**
	 * 说明
	 */
	@Column(name = "EXPLAINS")
	private String explains;

	/**
	 * 备注,表示图标文件名称
	 */
	@Column(name = "MEMO")
	private String memo;

	/**
	 * 父菜单名称
	 */
	@Transient
	private String parentName;

	/**
	 * 权限名称
	 */
	@Transient
	private String securityName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getSecurityName() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public WlCmMenu() {

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return this.target;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getNodeId() {
		return this.getId();
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return memo;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}

	public String getExplains() {
		return explains;
	}

}