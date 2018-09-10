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

/**
 * 物料属性
 * @author Lizj
 *
 */
@Entity
@Table(name = "WL_CM_ITEM_ATTR")
public class WlCmItemAttr extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ITEM_ATTR_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String itemAttrId;

	/**
	 * 属性名称
	 */
	@Column(name = "ATTR_NAME")
	private String attrName;

	/**
	 * 物料值
	 */
	@Column(name = "ATTR_VALUE")
	private String attrValue;

	/**
	 * 排序
	 */
	@Column(name = "SEQU")
	private int sequ=0;

	/**
	 * 物料ID
	 */
	@Column(name = "ITEM_ID")
	private String itemId;
	
	/**
	 * 语言类型
	 */
	@Column(name = "LANGUAGE_TYPE_EK")
	private String languageTypeEk;



	public WlCmItemAttr() {

	}


	public String getItemAttrId() {
		return itemAttrId;
	}


	public void setItemAttrId(String itemAttrId) {
		this.itemAttrId = itemAttrId;
	}


	public String getAttrName() {
		return attrName;
	}


	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}


	public String getAttrValue() {
		return attrValue;
	}


	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}




	public int getSequ() {
		return sequ;
	}


	public void setSequ(int sequ) {
		this.sequ = sequ;
	}


	public String getItemId() {
		return itemId;
	}


	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	public String getLanguageTypeEk() {
		return languageTypeEk;
	}


	public void setLanguageTypeEk(String languageTypeEk) {
		this.languageTypeEk = languageTypeEk;
	}

	
}