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
@Table(name = "WL_CM_MEMBER_LEVEL")
public class WlCmMemberLevel extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "LEVEL_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String levelId;

	/**
	 * 等级名称
	 */
	@Column(name = "LEVEL_NAME")
	private String levelName;

	/**
	 * 积分
	 */
	@Column(name = "INTEGRAL")
	private long integral;

	/**
	 * 活跃度
	 */
	@Column(name = "ACTIVITY")
	private long activity;

	public WlCmMemberLevel() {

	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getLevelId() {
		return this.levelId;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLevelName() {
		return this.levelName;
	}

	public void setIntegral(long integral) {
		this.integral = integral;
	}

	public long getIntegral() {
		return this.integral;
	}

	public void setActivity(long activity) {
		this.activity = activity;
	}

	public long getActivity() {
		return this.activity;
	}

}