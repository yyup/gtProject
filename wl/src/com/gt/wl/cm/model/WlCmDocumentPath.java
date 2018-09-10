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
@Table(name = "WL_CM_DOCUMENT_PATH")
public class WlCmDocumentPath extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String id;

	/**
	 * 来源代号
	 */
	@Column(name = "SOURCE_CODE")
	private String sourceCode;

	/**
	 * 存放路径
	 */
	@Column(name = "PATH")
	private String path;

	/**
	 * 说明
	 */
	@Column(name = "EXPLAINS")
	private String explains;

	public WlCmDocumentPath() {

	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSourceCode() {
		return this.sourceCode;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}

	public String getExplains() {
		return this.explains;
	}

}