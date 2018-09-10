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
 * @author liuyj
 * @version 1.0
 * @created 2015-09-17 16:52:52
 */
@Entity
@Table(name = "WL_CM_CATEGORY")
public class WlCmCategory extends BaseModel implements IPdTreeModel, java.io.Serializable {

	@Id
	@Column(name = "CATEGORY_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String categoryId;

	/**
	 * 物料类别代码
	 */
	@Column(name = "CATEGORY_CD")
	private String categoryCd;

	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * 父项类别
	 */
	@Column(name = "PARENT_CATEGORY_ID")
	private String parentCategoryId;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

	/**
	 * 启用标志
	 */
	@Column(name = "IS_ENABLE_FLAG")
	private String isEnableFlag;

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
	 * 级节点标志
	 */
	@Column(name = "IS_LAST_FLAG")
	private String isLastFlag = "0";

	/**
	 * 是否显示给经销商 1显示，0不显示
	 */
	@Column(name = "IS_SHOW_AGENCY")
	private String isShowAgency = "1";

	/** 父类型名称 **/
	@Transient
	private String parentName;

	public WlCmCategory() {

	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryCd(String categoryCd) {
		this.categoryCd = categoryCd;
	}

	public String getCategoryCd() {
		return this.categoryCd;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setParentCategoryId(String parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public String getParentCategoryId() {
		return this.parentCategoryId;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

	public void setIsEnableFlag(String isEnableFlag) {
		this.isEnableFlag = isEnableFlag;
	}

	public String getIsEnableFlag() {
		return this.isEnableFlag;
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

	public String getNodeId() {
		// TODO Auto-generated method stub
		return this.getCategoryId();
	}

	public String getParentId() {
		// TODO Auto-generated method stub
		return this.getParentCategoryId();
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getIsLastFlag() {
		return isLastFlag;
	}

	public void setIsLastFlag(String isLastFlag) {
		this.isLastFlag = isLastFlag;
	}

	public String getIsShowAgency() {
		return isShowAgency;
	}

	public void setIsShowAgency(String isShowAgency) {
		this.isShowAgency = isShowAgency;
	}

}