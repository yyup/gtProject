package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmInfo;

/**
 * 信息发布DAO层
 * @author huangbj
 * 
 */
@Repository("wl.cm.WlCmInfoDao")
public class WlCmInfoDao extends BaseDao {

	public WlCmInfoDao() {
		modelClass = WlCmInfo.class;
		defaultOrder = "";
	}

	/**
	 * 获取信息对象
	 * @param key 发布信息的字段
	 * @param value key对应的字段的值
	 * @return 信息对象列表
	 */
	public List getWlCmInfoList(String key, String value) {
		try {
			String hql = "from WlCmInfo  where " + key + "='" + value + "'";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询信息发布管理，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param columnId 信息栏目id
	 * @param title 标题
	 * @param issueStateEk 发布状态
	 * @param dataState 数据状态
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param titleAndContent 标题和内容
	 * @return 结果
	 */
	public Page search(int pageSize, int currPage, String columnId, String title, String issueStateEk, String dataState, String auditNodeEk, String auditState,String titleAndContent) {
		try {
			String hql = "from WlCmInfo w where 1=1 and  dataState='" + dataState + "' ";
			if (!ValidateUtil.isEmpty(columnId)) {
				hql += " and columnId=" + FormatUtil.formatStrForDB(columnId);
			}
			if (!ValidateUtil.isEmpty(title)) {

				hql += " and title like " + FormatUtil.formatStrForDB("%" + title + "%");

			}
			if (!ValidateUtil.isEmpty(issueStateEk)) {
				hql += " and issueStateEk=" + FormatUtil.formatStrForDB(issueStateEk);
			}
			if (!ValidateUtil.isEmpty(auditNodeEk)) {
				hql += " and auditNodeEk=" + FormatUtil.formatStrForDB(auditNodeEk);
			}
			if (!ValidateUtil.isEmpty(auditState)) {
				hql += " and auditState=" + FormatUtil.formatStrForDB(auditState);
			}
			if (!ValidateUtil.isEmpty(titleAndContent)) {
				hql += " and (title like " + FormatUtil.formatStrForDB("%" + titleAndContent + "%")+" or content like "+ FormatUtil.formatStrForDB("%" + titleAndContent + "%")+") ";
			}
			hql += "order by sequ,issueTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除发布信息
	 * @param ids 信息ID数组
	 * @param dataState 数据状态
	 */
	public void updateWlCmInfosDataState(String[] ids, String dataState) {
		String hql = "update WlCmInfo set dataState ='" + dataState + "' where infoId in " + ConvertUtil.toDbString(ids);
		try {
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除发布信息
	 * @param ids 信息ID数组
	 */
	public void deletePhysicalWlCmInfos(String[] ids) {
		String hql = "delete WlCmInfo where infoId in " + ConvertUtil.toDbString(ids);
		try {
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找信息数量
	 * @param columnId 栏目数量
	 * @param auditNodeEk 审核环节代码
	 * @param auditState 审核标志
	 */
	public long getInfoCount(String columnId, String auditNodeEk, String auditState) {
		try {
			String hql = "select count(*) from WlCmInfo where 1=1 ";
			if (!ValidateUtil.isEmpty(columnId)) {
				hql += " and columnId=" + FormatUtil.formatStrForDB(columnId);
			}
			if (!ValidateUtil.isEmpty(auditNodeEk)) {
				hql += " and auditNodeEk=" + FormatUtil.formatStrForDB(auditNodeEk);

			}
			if (!ValidateUtil.isEmpty(auditState)) {
				hql += " and auditState=" + FormatUtil.formatStrForDB(auditState);
			}
			Long total = (Long) this.find(hql).get(0);
			return total;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 查询信息列表
	 * @param infoIds 信息ids，格式为('1','2','3')
	 * @param columnId 栏目Id号
	 * @param issueStateEk 发布状态
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核标志
	 * @param dataState 数据状态
	 * @param title 标题
	 * @return 信息列表
	 */
	public List findInfoList(String infoIds,String columnId, String issueStateEk, String auditNodeEk, String auditState,String dataState,String title) {
		try {
			String hql = "from WlCmInfo where 1=1 ";
			if (!ValidateUtil.isEmpty(infoIds)) {
				hql += " and infoId in" + infoIds;
			}
			if (!ValidateUtil.isEmpty(columnId)) {
				hql += " and columnId = " + FormatUtil.formatStrForDB(columnId);
			}
			if (!ValidateUtil.isEmpty(issueStateEk)) {
				hql += " and issueStateEk=" + FormatUtil.formatStrForDB(issueStateEk);

			}
			if (!ValidateUtil.isEmpty(auditNodeEk)) {
				hql += " and auditNodeEk=" + FormatUtil.formatStrForDB(auditNodeEk);

			}
			if (!ValidateUtil.isEmpty(auditState)) {
				hql += " and auditState=" + FormatUtil.formatStrForDB(auditState);
			}
			if (!ValidateUtil.isEmpty(dataState)) {
				hql += " and dataState=" + FormatUtil.formatStrForDB(dataState);
			}
			if (!ValidateUtil.isEmpty(title)) {
				hql += " and title like " + FormatUtil.formatStrForDB("%"+dataState+"%");
			}
			hql += "order by sequ,issueTime desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
