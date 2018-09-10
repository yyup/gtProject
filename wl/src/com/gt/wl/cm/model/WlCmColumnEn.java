package com.gt.wl.cm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;
import org.joyone.model.IPdTreeModel;

/**
 * @author rdsf02
 * @version 1.0
 * @created 2015-04-08 16:54:23
 */
@Entity
@Table(name = "WL_CM_COLUMN_EN")
public class WlCmColumnEn extends BaseModel implements IPdTreeModel, java.io.Serializable {

	@Id
	@Column(name = "COLUMN_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String columnId;

	/**
	 * 栏目名称
	 */
	@Column(name = "COLUMN_NAME")
	private String columnName;

	/**
	 * 类型
	 */
	@Column(name = "COL_TYPE_EK")
	private String colTypeEk;

	/**
	 * 链接
	 */
	@Column(name = "URL")
	private String url;

	/**
	 * 父栏目ID
	 */
	@Column(name = "PARENT_COLUMN_ID")
	private String parentColumnId;

	/**
	 * 是否启用标志
	 */
	@Column(name = "IS_ENABLE_FLAG")
	private String isEnableFlag;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	private String creator;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime;

	/**
	 * 修改人
	 */
	@Column(name = "MODIFIER")
	private String modifier;

	/**
	 * 修改时间
	 */
	@Column(name = "MODIFY_TIME")
	private Date modifyTime = new Date();

	/**
	 * 排列顺序
	 */
	@Column(name = "SEQU")
	private int sequ;

	/**
	 * 数据状态
	 */
	@Column(name = "DATA_STATE")
	private String dataState = "1";

	/**
	 * 级节点标志
	 */
	@Column(name = "IS_LAST_FLAG")
	private String isLastFlag = "0";
	
	/**
	 * 级节点标志
	 */
	@Column(name = "PATH")
	private String path;
	

	/** 父类型名称 **/
	@Transient
	private String parentColumnName;
	/**
	 * 初审数量
	 */
	@Transient
	private long firstAuditNum = 0;
	/**
	 * 终审审数量
	 */
	@Transient
	private long finalAuditNum = 0;

	/**
	 * 用于前端显示的数量（初审或者终审）
	 */
	@Transient
	private String showAuditNum;
	/**
	 * 用于前端显示信息列表
	 */
	@Transient
	private List infoList;
	/**
	 * 子栏目列表
	 */
	@Transient
	private List childColumnList = new ArrayList();
	
	
	public List getInfoList() {
		return infoList;
	}

	public void setInfoList(List infoList) {
		this.infoList = infoList;
	}

	public List getChildColumnList() {
		return childColumnList;
	}

	public void setChildColumnList(List childColumnList) {
		this.childColumnList = childColumnList;
	}

	public WlCmColumnEn() {

	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getColumnId() {
		return this.columnId;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColTypeEk(String colTypeEk) {
		this.colTypeEk = colTypeEk;
	}

	public String getColTypeEk() {
		return this.colTypeEk;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setParentColumnId(String parentColumnId) {
		this.parentColumnId = parentColumnId;
	}

	public String getParentColumnId() {
		return this.parentColumnId;
	}

	public void setIsEnableFlag(String isEnableFlag) {
		this.isEnableFlag = isEnableFlag;
	}

	public String getIsEnableFlag() {
		return this.isEnableFlag;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifier() {
		return this.modifier;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setSequ(int sequ) {
		this.sequ = sequ;
	}

	public int getSequ() {
		return this.sequ;
	}

	public void setDataState(String dataState) {
		this.dataState = dataState;
	}

	public String getDataState() {
		return this.dataState;
	}

	public String getParentColumnName() {
		return parentColumnName;
	}

	public void setParentColumnName(String parentColumnName) {
		this.parentColumnName = parentColumnName;
	}

	public String getIsLastFlag() {
		return isLastFlag;
	}

	public void setIsLastFlag(String isLastFlag) {
		this.isLastFlag = isLastFlag;
	}

	public String getNodeId() {

		return this.getColumnId();
	}

	public String getParentId() {

		return this.getParentColumnId();
	}

	public long getFirstAuditNum() {
		return firstAuditNum;
	}

	public void setFirstAuditNum(long firstAuditNum) {
		this.firstAuditNum = firstAuditNum;
	}

	public long getFinalAuditNum() {
		return finalAuditNum;
	}

	public void setFinalAuditNum(long finalAuditNum) {
		this.finalAuditNum = finalAuditNum;
	}

	public String getShowAuditNum() {
		return showAuditNum;
	}

	public void setShowAuditNum(String showAuditNum) {
		this.showAuditNum = showAuditNum;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}