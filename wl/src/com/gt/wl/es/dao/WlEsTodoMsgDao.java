package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsTodoMsg;

/**
 * 待办消息DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsTodoMsgDao")
public class WlEsTodoMsgDao extends BaseDao {
	public WlEsTodoMsgDao() {
		modelClass = WlEsTodoMsg.class;
		defaultOrder = "";
	}

	/**
	 * 邮箱未发送的待发送信息列表
	 * @return 邮箱未发送的待发送信息列表
	 */
	public List findDataList() {
		try {
			String hql = "from WlEsTodoMsg where mailSend='0'";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 根据对象id查找待发送消息
	 * @param objectId 对象id
	 * @return 待发送消息对象
	 */
	public WlEsTodoMsg getWlEsTodoMsg(String objectId) {
		try {
			String hql = "from WlEsTodoMsg where objectId=" + FormatUtil.formatStrForDB(objectId);
			List list = this.find(hql);
			WlEsTodoMsg wlEsTodoMsg = null;
			if (!ValidateUtil.isEmpty(list)) {
				wlEsTodoMsg = (WlEsTodoMsg) list.get(0);
			}
			return wlEsTodoMsg;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/***
	 * @param mailSend 邮件是否已发送
	 * @param curDate 当前时间
	 * @param msgIds 待发送信息ids ，格式为('1','2','3')
	 */
	public void updateTodoMsgState(String mailSend, String curDate, String msgIds) {
		try {
			if (!ValidateUtil.isEmpty(mailSend)) {// closeTime orderTime
				String hql = "update WlEsTodoMsg  set mailSend=" + FormatUtil.formatStrForDB(mailSend);
				if (!ValidateUtil.isEmpty(curDate)) {
					hql += " ,modifyTime=" + FormatUtil.formatStrForDB(curDate);
				}
				if (!ValidateUtil.isEmpty(msgIds)) {
					hql += " where msgId in" + msgIds;
				}
				this.executeHql(hql);
			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}
