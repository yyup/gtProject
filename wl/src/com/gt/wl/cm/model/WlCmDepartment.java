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
 * @created 2015-04-08 16:33:48
 */
@Entity
@Table(name = "WL_CM_DEPARTMENT")
public class WlCmDepartment extends BaseModel implements IPdTreeModel, java.io.Serializable {

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
	 * 0表示无效、1表示有效
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
	 * 部门负责人
	 */
	@Column(name = "LEADER_ID")
	private String leaderId;

	/**
	 * 部门代码
	 */
	@Column(name = "CODE")
	private String code;

	/**
	 * 部门全称
	 */
	@Column(name = "FULL_NAME")
	private String fullName;

	/**
	 * 部门级别
	 */
	@Column(name = "MAIN_LEVEL")
	private String mainLevel;

	/**
	 * 部门级别2
	 */
	@Column(name = "SUB_LEVEL")
	private String subLevel;

	/**
	 * 部门属性
	 */
	@Column(name = "PROPERTY")
	private String property;

	/**
	 * 地址
	 */
	@Column(name = "ADDRESS")
	private String address;

	/**
	 * 邮编
	 */
	@Column(name = "POST_CODE")
	private String postCode;

	/**
	 * 新政区域
	 */
	@Column(name = "CANTON")
	private String canton;

	/**
	 * 电话
	 */
	@Column(name = "TEL")
	private String tel;

	/**
	 * 传真
	 */
	@Column(name = "FAX")
	private String fax;

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
	 * 父节点名称
	 */
	@Transient
	private String parentName;

	public WlCmDepartment() {

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

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getMainLevel() {
		return mainLevel;
	}

	public void setMainLevel(String mainLevel) {
		this.mainLevel = mainLevel;
	}

	public String getSubLevel() {
		return subLevel;
	}

	public void setSubLevel(String subLevel) {
		this.subLevel = subLevel;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPostCode() {
		return this.postCode;
	}

	public void setCanton(String canton) {
		this.canton = canton;
	}

	public String getCanton() {
		return this.canton;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTel() {
		return this.tel;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return this.fax;
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