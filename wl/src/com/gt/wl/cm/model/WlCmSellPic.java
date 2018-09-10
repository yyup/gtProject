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
@Table(name = "WL_CM_SELL_PIC")
public class WlCmSellPic extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "PIC_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String picId;

	/**
	 * 上架产品ID
	 */
	@Column(name = "SELL_ID")
	private String sellId;

	/**
	 * 图片地址
	 */
	@Column(name = "PATH")
	private String path;

	/**
	 * 是否缩略图
	 */
	@Column(name = "IS_THUM_FLAG")
	private String isThumFlag;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

	public WlCmSellPic() {

	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getPicId() {
		return this.picId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public String getSellId() {
		return this.sellId;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public void setIsThumFlag(String isThumFlag) {
		this.isThumFlag = isThumFlag;
	}

	public String getIsThumFlag() {
		return this.isThumFlag;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

}