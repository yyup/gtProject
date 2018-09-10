package com.gt.wl.es.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * @author liuyj
 * @version 1.0
 * @created 2015-04-30 15:07:50
 */
@Entity
@Table(name = "WL_ES_SERIAL_REG_PIC")
public class WlEsSerialRegPic extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "PIC_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String picId;

	/**
	 * 登记ID
	 */
	@Column(name = "REG_ID")
	private String regId;

	/**
	 * 路径
	 */
	@Column(name = "PATH")
	private String path;

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

	public WlEsSerialRegPic() {

	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getPicId() {
		return this.picId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getRegId() {
		return this.regId;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

}