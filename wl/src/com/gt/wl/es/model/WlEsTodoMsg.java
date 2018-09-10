package com.gt.wl.es.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joyone.model.BaseModel;

/**
 * 待办消息列表
 * @author liuyj
 * 
 */
@Entity
@Table(name = "WL_ES_TODO_MSG")
public class WlEsTodoMsg extends BaseModel implements java.io.Serializable {
	@Id
	@Column(name = "MSG_ID")
	@GeneratedValue(generator = "ID")
	@GenericGenerator(name = "ID", strategy = "uuid")
	private String msgId;

	/**
	 * 对象ID
	 */
	@Column(name = "OBJECT_ID")
	private String objectId;

	/**
	 * 对象类型 ORDER-订单 APPLY-配件申请 REPAIR-返修
	 */
	@Column(name = "OBJECT_TYPE_EK")
	private String objectTypeEk;

	/**
	 * 邮件是否已发送
	 */
	@Column(name = "MAIL_SEND")
	private String mailSend;

	/**
	 * 短信是否已发送
	 */
	@Column(name = "SMS_SEND")
	private String smsSend;

	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	private String creator;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime = new Date();

	/**
	 * 修改人
	 */
	@Column(name = "MODIFIER")
	private String modifier;

	/**
	 * 修改时间
	 */
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectTypeEk() {
		return objectTypeEk;
	}

	public void setObjectTypeEk(String objectTypeEk) {
		this.objectTypeEk = objectTypeEk;
	}

	public String getMailSend() {
		return mailSend;
	}

	public void setMailSend(String mailSend) {
		this.mailSend = mailSend;
	}

	public String getSmsSend() {
		return smsSend;
	}

	public void setSmsSend(String smsSend) {
		this.smsSend = smsSend;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}
