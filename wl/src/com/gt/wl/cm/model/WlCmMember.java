package com.gt.wl.cm.model;

import java.util.Date;

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
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_MEMBER")
public class WlCmMember extends BaseModel implements java.io.Serializable {

	@Id
	@Column(name = "MEMBER_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String memberId;

	/**
	 * 等级ID
	 */
	@Column(name = "LEVEL_ID")
	private String levelId;

	/**
	 * 姓名
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 登录帐号
	 */
	@Column(name = "ACCOUNT")
	private String account;

	/**
	 * 密码
	 */
	@Column(name = "PASSWORD")
	private String password;

	/**
	 * 电子邮件
	 */
	@Column(name = "EMAIL")
	private String email;

	/**
	 * 性别
	 */
	@Column(name = "SEX_EK")
	private String sexEk;

	/**
	 * 手机号码
	 */
	@Column(name = "MOBILE")
	private String mobile;

	/**
	 * 注册时间
	 */
	@Column(name = "REG_TIME")
	private Date regTime;

	/**
	 * 上次登录时间
	 */
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;

	/**
	 * 登录次数
	 */
	@Column(name = "LOGIN_NUM")
	private long loginNum;

	/**
	 * 个人签名
	 */
	@Column(name = "SIGNATURE")
	private String signature;

	/**
	 * 头像图片路径
	 */
	@Column(name = "PATH")
	private String path;

	/**
	 * 所属省ID
	 */
	@Column(name = "PROVINCE_ID")
	private String provinceId;

	/**
	 * 所属省
	 */
	@Column(name = "PROVINCE")
	private String province;

	/**
	 * 所属市ID
	 */
	@Column(name = "CITY_ID")
	private String cityId;

	/**
	 * 所属市
	 */
	@Column(name = "CITY")
	private String city;

	/**
	 * 区域ID
	 */
	@Column(name = "AREA_ID")
	private String areaId;

	/**
	 * 所属区
	 */
	@Column(name = "AREA")
	private String area;

	/**
	 * 出生日期
	 */
	@Column(name = "BIRTHDAY")
	private Date birthday;

	/**
	 * 状态
	 */
	@Column(name = "MEMBER_STATE_EK")
	private String memberStateEk;

	/**
	 * 登录IP
	 */
	@Column(name = "IP")
	private String ip;

	/**
	 * 固定电话
	 */
	@Column(name = "TEL")
	private String tel;

	/**
	 * 来源途径(获悉沃特罗伦)
	 */
	@Column(name = "SOURCE_WAY_EK")
	private String sourceWayEk;

	/**
	 * 来源途径(二级)
	 */
	@Column(name = "SOURCE_WAY_TWO_EK")
	private String sourceWayTwoEk;

	/**
	 * 来源说明
	 */
	@Column(name = "SOURCE_DESC")
	private String sourceDesc;
	/**
	 * 是否有购买商品
	 */
	@Column(name = "IS_BUY_FLAG")
	private String isBuyFlag = "0";
	
	/**
	 * QQ账号
	 */
	@Column(name = "QQ_ACCOUNT")
	private String qqAccount;	 

	public WlCmMember() {

	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberId() {
		return this.memberId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getLevelId() {
		return this.levelId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return this.account;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setSexEk(String sexEk) {
		this.sexEk = sexEk;
	}

	public String getSexEk() {
		return this.sexEk;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Date getRegTime() {
		return this.regTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLoginNum(long loginNum) {
		this.loginNum = loginNum;
	}

	public long getLoginNum() {
		return this.loginNum;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return this.signature;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceId() {
		return this.provinceId;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
		return this.province;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityId() {
		return this.cityId;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return this.city;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaId() {
		return this.areaId;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getArea() {
		return this.area;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setMemberStateEk(String memberStateEk) {
		this.memberStateEk = memberStateEk;
	}

	public String getMemberStateEk() {
		return this.memberStateEk;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSourceWayEk() {
		return sourceWayEk;
	}

	public void setSourceWayEk(String sourceWayEk) {
		this.sourceWayEk = sourceWayEk;
	}

	public String getSourceWayTwoEk() {
		return sourceWayTwoEk;
	}

	public void setSourceWayTwoEk(String sourceWayTwoEk) {
		this.sourceWayTwoEk = sourceWayTwoEk;
	}

	public String getSourceDesc() {
		return sourceDesc;
	}

	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}

	public String getIsBuyFlag() {
		return isBuyFlag;
	}

	public void setIsBuyFlag(String isBuyFlag) {
		this.isBuyFlag = isBuyFlag;
	}

	
	public String getQqAccount() {
		return qqAccount;
	}

	public void setQqAccount(String qqAccount) {
		this.qqAccount = qqAccount;
	} 

}