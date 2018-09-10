package com.gt.wl.cm.model;

import java.util.Date;

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
 * @author rdsf02
 * @version 1.0
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_PRODUCT_TYPE")
public class WlCmProductType extends BaseModel implements IPdTreeModel, java.io.Serializable {

	@Id
	@Column(name = "TYPE_ID")
	private String typeId;

	/**
	 * 名称
	 */
	@Column(name = "TYPE_NAME")
	private String typeName;

	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * 类型父ID
	 */
	@Column(name = "PARENT_TYPE_ID")
	private String parentTypeId;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	private String creator;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime;

	/**
	 * 修改人
	 */
	@Column(name = "MODIFIER")
	private String modifier;

	/**
	 * 修改时间
	 */
	@Column(name = "MODIFY_TIME")
	private Date modifyTime = new Date();

	/**
	 * 数据状态
	 */
	@Column(name = "DATA_STATE")
	private String dataState = "1";
	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;
	
	/**
	 * 缩略图地址
	 */
	@Column(name = "PATH")
	private String path;
	
	/**
	 * 背景图地址
	 */
	@Column(name = "BACKGROUND_PATH")
	private String backgroundPath;

	/** 父类型名称 **/
	@Transient
	private String parentTypeName;

	public WlCmProductType() {

	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setParentTypeId(String parentTypeId) {
		this.parentTypeId = parentTypeId;
	}

	public String getParentTypeId() {
		return this.parentTypeId;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifier() {
		return this.modifier;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setDataState(String dataState) {
		this.dataState = dataState;
	}

	public String getDataState() {
		return this.dataState;
	}

	public String getParentTypeName() {
		return parentTypeName;
	}

	public void setParentTypeName(String parentTypeName) {
		this.parentTypeName = parentTypeName;
	}

	public int getSequ() {
		return sequ;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public String getNodeId() {

		return this.getTypeId();
	}

	public String getParentId() {

		return this.parentTypeId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBackgroundPath() {
		return backgroundPath;
	}

	public void setBackgroundPath(String backgroundPath) {
		this.backgroundPath = backgroundPath;
	}
	

}