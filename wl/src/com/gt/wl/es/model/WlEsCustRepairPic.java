package com.gt.wl.es.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author liuyj
 * @version 1.0
 * @created 2015-05-14 14:57:10
 */
@Entity
@Table(name = "WL_ES_CUST_REPAIR_PIC")
public class WlEsCustRepairPic extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "PIC_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String picId;

	/**
	 * 维修ID
	 */
	@Column(name = "REPAIR_ID")
	private String repairId;

	/**
	 * 图片地址
	 */
	@Column(name = "PATH")
	private String path;

	/**
	 * 是否视频
	 */
	@Column(name = "IS_VIDEO_FLAG")
	private String isVideoFlag;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

	/**
	 * 文件真实路径
	 */
	@Transient
	private String filePath;

	/**
	 * 文件相对路径
	 */
	@Transient
	private String relativeFilePath;

	public WlEsCustRepairPic() {

	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getPicId() {
		return this.picId;
	}

	public void setRepairId(String repairId) {
		this.repairId = repairId;
	}

	public String getRepairId() {
		return this.repairId;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public void setIsVideoFlag(String isVideoFlag) {
		this.isVideoFlag = isVideoFlag;
	}

	public String getIsVideoFlag() {
		return this.isVideoFlag;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getRelativeFilePath() {
		return relativeFilePath;
	}

	public void setRelativeFilePath(String relativeFilePath) {
		this.relativeFilePath = relativeFilePath;
	}

}