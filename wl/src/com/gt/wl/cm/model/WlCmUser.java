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
import org.joyone.model.IEnumModel;

/**
 * @author 用户对象
 * @version 1.0
 * @created 2015-04-09 16:33:53
 */
@Entity
@Table(name = "WL_CM_USER")
public class WlCmUser extends BaseModel implements IEnumModel, java.io.Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String id;

	/**
	 * 姓名
	 */

	@Column(name = "NAME")
	private String name;

	/**
	 * 登录名
	 */
	@Column(name = "LOGIN_NAME")
	private String loginName;

	/**
	 * 密码
	 */
	@Column(name = "PASSWORD")
	private String password;

	/**
	 * 部门ID
	 */
	@Column(name = "DEPARTMENT_ID")
	private String departmentId;

	/**
	 * 性别
	 */
	@Column(name = "SEX")
	private String sex;

	/**
	 * 英文名
	 */
	@Column(name = "ENAME")
	private String ename;

	/**
	 * 国籍
	 */
	@Column(name = "NATIONALITY")
	private String nationality;

	/**
	 * 人员编号
	 */
	@Column(name = "CODE")
	private String code;

	/**
	 * 身份证号码
	 */
	@Column(name = "IDENTITY_CARD")
	private String identityCard;

	/**
	 * 生日
	 */
	@Column(name = "BIRTHDAY")
	private Date birthday;

	/**
	 * 籍贯
	 */
	@Column(name = "NATIVE_PLACE")
	private String nativePlace;

	/**
	 * 党派
	 */
	@Column(name = "PARTY")
	private String party;

	/**
	 * 办公室电话
	 */
	@Column(name = "TEL")
	private String tel;

	/**
	 * 办公室传真
	 */
	@Column(name = "FAX")
	private String fax;

	/**
	 * 手机号码
	 */
	@Column(name = "MOBILE_TEL")
	private String mobileTel;

	/**
	 * 电子邮件
	 */
	@Column(name = "EMAIL")
	private String email;

	/**
	 * 来本单位日期
	 */
	@Column(name = "JOIN_DATE")
	private Date joinDate;

	/**
	 * 来本单位方式
	 */
	@Column(name = "JOIN_TYPE")
	private String joinType;

	/**
	 * 上级
	 */
	@Column(name = "BOSS")
	private String boss;

	/**
	 * 助理
	 */
	@Column(name = "ASSISTANT")
	private String assistant;

	/**
	 * IP
	 */
	@Column(name = "IP")
	private String ip;

	/**
	 * 显示顺序号
	 */
	@Column(name = "SEQU")
	private long sequ;

	/**
	 * 类型
	 */
	@Column(name = "TYPE")
	private String type;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	private int state;

	/**
	 * 是否程序员
	 */
	@Column(name = "IS_PROGRAMMER")
	private int isProgrammer;

	/**
	 * 是否管理员
	 */
	@Column(name = "IS_ADMIN")
	private int isAdmin;

	/**
	 * 登录时间
	 */
	@Transient
	private Date loginDate;

	/**
	 * 组织机构名称
	 */
	@Transient
	private String departmentName;

	public WlCmUser() {

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// @JSONField(serialize = false)

	public String getPassword() {
		return this.password;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return this.sex;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getEname() {
		return this.ename;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getNationality() {
		return this.nationality;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getIdentityCard() {
		return this.identityCard;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getNativePlace() {
		return this.nativePlace;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTel() {
		return this.tel;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return this.fax;
	}

	public void setMobileTel(String mobileTel) {
		this.mobileTel = mobileTel;
	}

	public String getMobileTel() {
		return this.mobileTel;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Date getJoinDate() {
		return this.joinDate;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return this.ip;
	}

	public void setSequ(long sequ) {
		this.sequ = sequ;
	}

	public long getSequ() {
		return this.sequ;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setIsProgrammer(int isProgrammer) {
		this.isProgrammer = isProgrammer;
	}

	public int getIsProgrammer() {
		return this.isProgrammer;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int getIsAdmin() {
		return this.isAdmin;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getParty() {
		return party;
	}

	public void setParty(String party) {
		this.party = party;
	}

	public String getJoinType() {
		return joinType;
	}

	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	public String getBoss() {
		return boss;
	}

	public void setBoss(String boss) {
		this.boss = boss;
	}

	public String getAssistant() {
		return assistant;
	}

	public void setAssistant(String assistant) {
		this.assistant = assistant;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public String getText() {
		return this.getName();
	}

	public String getValue() {
		return this.getId();
	}

}