package com.gt.wl.cm.dao;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmLog;

/**
 * 日志DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmLogDao")
public class WlCmLogDao extends BaseDao {

	public WlCmLogDao() {
		modelClass = WlCmLog.class;
		defaultOrder = "";
	}

	/**
	 * 查询日志
	 * @param wlCmLog 日志对象
	 * @param fromDate 开始日期
	 * @param toDate 结束日期
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return page对象
	 */
	public Page search(WlCmLog wlCmLog, String fromDate, String toDate, int currPage, int pageSize) {
		try {
			String hql = "from WlCmLog where 1=1 ";
			if (!ValidateUtil.isEmpty(wlCmLog.getContent())) {
				hql += " and content like '%" + wlCmLog.getContent() + "%'";
			}
			// 开始日期
			if (!ValidateUtil.isEmpty(fromDate)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("nodeTime"));
				hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(fromDate));
			}

			// 结束日期
			if (!ValidateUtil.isEmpty(toDate)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("nodeTime"));
				hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(toDate));
			}
			hql += " order by nodeTime desc";

			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
