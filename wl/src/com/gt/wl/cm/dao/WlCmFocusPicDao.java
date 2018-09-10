package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmFocusPic;

/**
 * 焦点图片DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmFocusPicDao")
public class WlCmFocusPicDao extends BaseDao {

	public WlCmFocusPicDao() {
		modelClass = WlCmFocusPic.class;
		defaultOrder = "";
	}

	/**
	 * 获取焦点图片
	 * @param isEnableFlag 是否启用
	 * @return 焦点图片列表
	 */
	public List findFocusPic(String isEnableFlag, String auditNodeEk, String auditState) {
		try {
			String hql = "select new Map(w.url as url,w.path as path,w.description as description) from WlCmFocusPic w where 1=1 ";
			if (!ValidateUtil.isEmpty(isEnableFlag)) {
				hql += " and w.isEnableFlag=" + FormatUtil.formatStrForDB(isEnableFlag);

			}
			if (!ValidateUtil.isEmpty(auditNodeEk)) {
				hql += " and w.auditNodeEk=" + FormatUtil.formatStrForDB(auditNodeEk);
			}
			if (!ValidateUtil.isEmpty(auditState)) {
				hql += " and w.auditState=" + FormatUtil.formatStrForDB(auditState);
			}
			hql += " order by sequ";
			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取焦点图片列表
	 * @param picIds 图片ids
	 * @param isEnableFlag 是否启用
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @return 焦点图片列表
	 */
	public List findFocusPicList(String picIds, String isEnableFlag, String auditNodeEk, String auditState) {
		try {
			String hql = " from WlCmFocusPic where 1=1";
			if (!ValidateUtil.isEmpty(picIds)) {
				hql += " and picId in" + picIds;
			}
			if (!ValidateUtil.isEmpty(isEnableFlag)) {
				hql += " and isEnableFlag =" + FormatUtil.formatStrForDB(isEnableFlag);
			}
			if (!ValidateUtil.isEmpty(auditNodeEk)) {
				hql += " and auditNodeEk =" + FormatUtil.formatStrForDB(auditNodeEk);
			}
			if (!ValidateUtil.isEmpty(auditState)) {
				hql += " and auditState =" + FormatUtil.formatStrForDB(auditState);
			}
			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

}
