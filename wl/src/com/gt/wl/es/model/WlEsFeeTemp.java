package com.gt.wl.es.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author huangbj
 * @version 1.0
 * @created 2015-12-04 11:10:25
 */
@Entity
@Table(name = "WL_ES_FEE_TEMP")
public class WlEsFeeTemp extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "TEMP_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String tempId;

	/**
	 * 模板名称
	 */
	@Column(name = "TEMP_NAME")
	private String tempName;
	/**
	 * 起始地区
	 */
	@Column(name = "START_AREA_ID")
	private String startAreaId;

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
	 * 上架商品ids，逗号隔开
	 */
	@Transient
	private String sellIds;

	public WlEsFeeTemp() {

	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public String getStartAreaId() {
		return startAreaId;
	}

	public void setStartAreaId(String startAreaId) {
		this.startAreaId = startAreaId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getSellIds() {
		return sellIds;
	}

	public void setSellIds(String sellIds) {
		this.sellIds = sellIds;
	}

}