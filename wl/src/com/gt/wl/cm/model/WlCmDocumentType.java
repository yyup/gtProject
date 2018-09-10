package com.gt.wl.cm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author rdsf02
 * @version 1.0
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_DOCUMENT_TYPE")
public class WlCmDocumentType extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String id;

	/**
	 * 后缀(只须填写大写后缀)
	 */
	@Column(name = "POSTFIX")
	private String postfix;

	/**
	 * 图标文件名称
	 */
	@Column(name = "ICON")
	private String icon;

	/**
	 * 文件类型
	 */
	@Column(name = "TYPE")
	private String type;

	/**
	 * 打开MIME类型
	 */
	@Column(name = "MIME")
	private String mime;

	public WlCmDocumentType() {

	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	public String getPostfix() {
		return this.postfix;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public String getMime() {
		return this.mime;
	}

}