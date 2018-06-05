package cn.joyone.base.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.*;

/**
 * @author Administrator
 * @version 1.0
 * @created 2014-06-19 16:35:30
 */
@Entity
@Table(name = "BASE_PERSON")
public class BasePerson extends BaseModel implements java.io.Serializable {

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
	 * 说明
	 */
	@Column(name = "EXPLAINS")
	private String explains;

	/**
	 * 部门ID
	 */
	@Column(name = "DEPT_ID")
	private String deptId;

	/**
	 * 性别
	 */
	@Column(name = "SEX")
	private String sex;

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
	 * 毕业年度
	 */
	@Column(name = "GRADUATE_YEAR")
	private int graduateYear;

	/**
	 * 入职年月
	 */
	@Column(name = "JOIN_WORK_MONTH")
	private String joinWorkMonth;

	/**
	 * 申请时间
	 */
	@Column(name = "APPLY_TIME")
	private String applyTime;

	/**
	 * 注册时间
	 */
	@Column(name = "REGISTER_DATE_TIME")
	private Date registerDateTime;

	/**
	 * 年龄
	 */
	@Column(name = "AGE")
	private int age;

	/**
	 * 兴趣
	 */
	@Column(name = "HOBBY")
	private String hobby;

	/**
	 * 电话
	 */
	@Column(name = "TEL")
	private String tel;

	public BasePerson() {

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

	public void setExplains(String explains) {
		this.explains = explains;
	}

	public String getExplains() {
		return this.explains;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return this.sex;
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

	public void setGraduateYear(int graduateYear) {
		this.graduateYear = graduateYear;
	}

	public int getGraduateYear() {
		return this.graduateYear;
	}

	public void setJoinWorkMonth(String joinWorkMonth) {
		this.joinWorkMonth = joinWorkMonth;
	}

	public String getJoinWorkMonth() {
		return this.joinWorkMonth;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getApplyTime() {
		return this.applyTime;
	}

	public void setRegisterDateTime(Date registerDateTime) {
		this.registerDateTime = registerDateTime;
	}

	public Date getRegisterDateTime() {
		return this.registerDateTime;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAge() {
		return this.age;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getHobby() {
		return this.hobby;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTel() {
		return this.tel;
	}

}