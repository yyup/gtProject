package com.gt.wl.wm.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmNoticeCheck;

/**
 * 盘点通知DAO层
 * @author liuyj
 * 
 */
@Repository("wl.wm.WlWmNoticeCheckDao")
public class WlWmNoticeCheckDao extends BaseDao {

	public WlWmNoticeCheckDao() {
		modelClass = WlWmNoticeCheck.class;
		defaultOrder = "";
	}

	/**
	 * 获取待办单数
	 * @param storageId 仓库ID（为空或null则查询所有仓库）
	 * @return 待办单数
	 */
	public long getTodoCount(String storageId) {
		try {
			// 单据状态为“已下发”并且结果为“未执行”
			String hql = "select count(*) from WlWmNoticeCheck where billStateEk='ISSUE' and resultEk='NOT_EXECUTE'";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and storageId=" + FormatUtil.formatStrForDB(storageId);
			}
			Long total = (Long) this.find(hql).get(0);
			return total;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询盘点通知单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			String hql = " from WlWmNoticeCheck where 1=1 ";
			// 通知单号
			if (paraMap.containsKey("noticeNo")) {
				String noticeNo = paraMap.get("noticeNo").toString();
				if (!ValidateUtil.isEmpty(noticeNo)) {
					hql += " and noticeNo like " + FormatUtil.formatStrForDB("%" + noticeNo + "%");
				}
			}
			// 盘点仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and storageId =" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 盘点库区
			if (paraMap.containsKey("storageAreaId")) {
				String storageAreaId = paraMap.get("storageAreaId").toString();
				if (!ValidateUtil.isEmpty(storageAreaId)) {
					hql += " and storageAreaId =" + FormatUtil.formatStrForDB(storageAreaId);
				}
			}
			// 业务类型
			if (paraMap.containsKey("wmCheckTypeEk")) {
				String wmCheckTypeEk = paraMap.get("wmCheckTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmCheckTypeEk)) {
					hql += " and wmCheckTypeEk =" + FormatUtil.formatStrForDB(wmCheckTypeEk);
				}
			}
			// 单据状态
			if (paraMap.containsKey("billStateEk")) {
				String billStateEk = paraMap.get("billStateEk").toString();
				if (!ValidateUtil.isEmpty(billStateEk)) {
					hql += " and billStateEk =" + FormatUtil.formatStrForDB(billStateEk);
				}
			}
			// 结果状态
			if (paraMap.containsKey("resultEk")) {
				String resultEk = paraMap.get("resultEk").toString();
				if (!ValidateUtil.isEmpty(resultEk)) {
					hql += " and resultEk =" + FormatUtil.formatStrForDB(resultEk);
				}
			}
			// 制单开始时间
			if (paraMap.containsKey("beginDate")) {
				String beginDate = paraMap.get("beginDate").toString();
				if (!ValidateUtil.isEmpty(beginDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("createTime"));
					hql = hql + "  >= " + this.sqlUtil.string2Date("'" + beginDate + "'");
				}
			}
			// 制单结束时间
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("createTime"));
					hql = hql + " <=" + sqlUtil.string2Date("'" + endDate + "'");
				}
			}
			hql += "  order by createTime desc ";
			return this.findPage(hql, currPage, pageSize);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取新的通知单流水编号
	 * @return 新的通知单号
	 * @author
	 */
	public List getNewNoticeNoCode() {
		try {
			String hql = "select max(noticeNo) from WlWmNoticeCheck";
			Session session = getSession();
			Query query = session.createQuery(hql);
			return query.list();

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询盘点通知单对象数组
	 * @param noticeCheckIds 盘点通知单ids，格式为('1','2','3')
	 * @param billStateEk 单据状态
	 * @param resultEk 结果状态
	 * @return 盘点通知单对象列表
	 */
	public List findNoticeCheckList(String noticeCheckIds, String billStateEk, String resultEk) {
		try {
			String hql = "from WlWmNoticeCheck  where 1=1 ";
			if (!ValidateUtil.isEmpty(noticeCheckIds)) {
				hql += " and noticeCheckId in " + noticeCheckIds;
			}
			if (!ValidateUtil.isEmpty(billStateEk)) {
				hql += " and billStateEk !=" + FormatUtil.formatStrForDB(billStateEk);

			}
			if (!ValidateUtil.isEmpty(resultEk)) {
				hql += " and resultEk !=" + FormatUtil.formatStrForDB(resultEk);

			}
			return this.find(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改盘点通知单的状态
	 * @param noticeCheckIds 盘点通知单ids，格式为('1','2','3')
	 * @param billStateEk 单据状态
	 * @param issuer 发布人
	 * @param date 发布时间(格式yyyy-MM-dd HH:mm:ss)
	 */
	public void updateNoticeCheckState(String noticeCheckIds, String billStateEk, String issuer, String date) {
		try {
			String hql = "update WlWmNoticeCheck  set ";
			if (!ValidateUtil.isEmpty(billStateEk)) {
				hql += "  billStateEk =" + FormatUtil.formatStrForDB(billStateEk);
			}
			if (!ValidateUtil.isEmpty(issuer) && !ValidateUtil.isEmpty(date)) {
				hql += " ,issuer =" + FormatUtil.formatStrForDB(issuer);
				hql += " ,issueTime=" + FormatUtil.formatStrForDB(date);
			}
			if (!ValidateUtil.isEmpty(noticeCheckIds)) {
				hql += "  where  noticeCheckId in " + noticeCheckIds;
			}

			this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除出库通知单
	 * @param noticeCheckIds 盘点通知单ids，格式为('1','2','3')
	 */
	public void removeData(String noticeCheckIds) {
		try {
			String hqlWhere = "  a.noticeCheckId in " + noticeCheckIds;
			String hql = "delete from WlWmNoticeCheck  a where " + hqlWhere;
			this.executeHql(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过通知单号获取盘点通知单
	 * @param noticeNo 通知单号
	 * @return 盘点通知单对象
	 */
	public WlWmNoticeCheck getData(String noticeNo) {
		try {
			WlWmNoticeCheck wlWmNoticeCheck = null;
			String hql = "from WlWmNoticeCheck where noticeNo=?";
			List list = this.find(hql, noticeNo);
			if (!ValidateUtil.isEmpty(list)) {
				wlWmNoticeCheck = (WlWmNoticeCheck) list.get(0);
			}
			return wlWmNoticeCheck;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
