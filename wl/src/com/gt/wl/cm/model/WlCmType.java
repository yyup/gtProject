package com.gt.wl.cm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;
import org.joyone.model.IEnumModel;
import org.joyone.model.IPdTreeModel;

/**
 * @author liuyj
 * @version 1.0
 * @created 2015-04-08 16:33:53
 */
@Entity
@Table(name = "WL_CM_TYPE")
public class WlCmType extends BaseModel implements IPdTreeModel, IEnumModel, java.io.Serializable {

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
	 * 状态：0表示无效、1表示有效
	 */
	@Column(name = "STATE")
	private int state;

	/**
	 * 是否默认：0表示不是、1表示默认
	 */
	@Column(name = "IS_DEFAULT")
	private int isDefault;

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
	 * 允许访问的用户名集合
	 */
	@Column(name = "USER_NAMES")
	private String userNames;

	/**
	 * 允许访问的用户ID集合
	 */
	@Column(name = "USER_IDS")
	private String userIds;

	/**
	 * 值
	 */
	@Column(name = "VALUE")
	private String value;

	/**
	 * NORMAL普通、TREE树
	 */
	@Column(name = "TYPE")
	private String type;

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
	 * 安全级别、0为普通级别,1为开发者可管理,2为在类型管理中不能管理
	 */
	@Column(name = "SECURITY_LEVEL")
	private int securityLevel;

	/**
	 * 是否为根字典，1表示是,0表示不是
	 */
	@Column(name = "IS_PARENT")
	private int isParent;

	/**
	 * 父字典名称
	 */
	@Transient
	private String parentName;

	public WlCmType() {

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}

	public int getSecurityLevel() {
		return this.securityLevel;
	}

	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}

	public int getIsParent() {
		return this.isParent;
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

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentName() {
		return parentName;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return this.getName();
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