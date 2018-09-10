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
 * @author rdsf02
 * @version 1.0
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_DOCUMENT")
public class WlCmDocument extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String id;

	/**
	 * 显示名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 原有文件名称
	 */
	@Column(name = "FILE_NAME")
	private String fileName;

	/**
	 * 后缀
	 */
	@Column(name = "POSTFIX")
	private String postfix;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime;

	/**
	 * 来源代号
	 */
	@Column(name = "SOURCE_CODE")
	private String sourceCode;

	/**
	 * 来源ID
	 */
	@Column(name = "SOURCE_ID")
	private String sourceId;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

	/**
	 * 文件大小
	 */
	@Column(name = "FILE_SIZE")
	private long fileSize;

	/**
	 * 用户ID
	 */
	@Column(name = "USER_ID")
	private String userId;
	@Transient
	private String filePath;

	@Transient
	private String fileType;
	public WlCmDocument() {

	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	public String getPostfix() {
		return this.postfix;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSourceCode() {
		return this.sourceCode;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceId() {
		return this.sourceId;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public long getFileSize() {
		return this.fileSize;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}