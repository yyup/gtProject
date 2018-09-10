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
 * @created 2015-04-08 16:33:52
 */
@Entity
@Table(name = "WL_CM_SECURITY")
public class WlCmSecurity extends BaseModel implements IPdTreeModel, java.io.Serializable {

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
	 * 类型，1.模块，2.权限
	 */
	@Column(name = "TYPE")
	private int type;

	/**
	 * 说明
	 */
	@Column(name = "EXPLAINS")
	private String explains;

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;

	/**
	 * 安全级别、0为普通级别,1为开发者可管理
	 */
	@Column(name = "SECURITY_LEVEL")
	private int securityLevel;

	/**
	 * 父节点名称
	 */
	@Transient
	private String parentName;

	public WlCmSecurity() {

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}

	public int getSecurityLevel() {
		return this.securityLevel;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentName() {
		return parentName;
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