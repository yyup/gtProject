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

import com.gt.wl.wm.model.WlWmNoticeIn;

@Repository("wl.wm.WlWmNoticeInDao")
public class WlWmNoticeInDao extends BaseDao {

	public WlWmNoticeInDao() {
		modelClass = WlWmNoticeIn.class;
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
			String hql = "select count(*) from WlWmNoticeIn where billStateEk='ISSUE' and resultEk='NOT_EXECUTE'";
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
	 * 查询入库通知单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			String hql = " from WlWmNoticeIn where 1=1 ";
			// 通知单号
			if (paraMap.containsKey("noticeNo")) {
				String noticeNo = paraMap.get("noticeNo").toString();
				if (!ValidateUtil.isEmpty(noticeNo)) {
					hql += " and noticeNo like " + FormatUtil.formatStrForDB("%" + noticeNo + "%");
				}
			}
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and storageId =" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 库区
			if (paraMap.containsKey("storageAreaId")) {
				String storageAreaId = paraMap.get("storageAreaId").toString();
				if (!ValidateUtil.isEmpty(storageAreaId)) {
					hql += " and storageAreaId =" + FormatUtil.formatStrForDB(storageAreaId);
				}
			}
			// 入库业务类型
			if (paraMap.containsKey("wmInTypeEk")) {
				String wmInTypeEk = paraMap.get("wmInTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmInTypeEk)) {
					hql += " and wmInTypeEk =" + FormatUtil.formatStrForDB(wmInTypeEk);
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
					hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(beginDate));
				}
			}
			// 制单结束时间
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("createTime"));
					hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(endDate));
				}
			}
			hql += " order by createTime desc";

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
			String hqlWhere = " 1=1 ";
			String hql = "select max(noticeNo) from WlWmNoticeIn  where " + hqlWhere;
			Session session = getSession();
			Query query = session.createQuery(hql);
			return query.list();

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库通知单对象数组
	 * @param noticeInIds 入库通知单ids，格式为('1','2','3')
	 * @param billStateEk 单据状态
	 * @param resultEk 结果状态
	 * @return 通知单对象列表
	 */
	public List findNoticeInList(String noticeInIds, String billStateEk, String resultEk) {
		try {
			String hql = "from WlWmNoticeIn  where 1=1 ";
			if (!ValidateUtil.isEmpty(noticeInIds)) {
				hql += " and noticeInId in " + noticeInIds;
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
	 * 更改入库通知单的状态
	 * @param noticeInIds 入库通知单ids，格式为('1','2','3')
	 * @param billStateEk 单据状态
	 * @param issuer 发布人
	 * @param date 发布时间(格式yyyy-MM-dd HH:mm:ss)
	 */
	public void updateNoticeInState(String noticeInIds, String billStateEk, String issuer, String date) {
		try {
			String hql = "update WlWmNoticeIn set ";
			if (!ValidateUtil.isEmpty(billStateEk)) {
				hql += "  billStateEk =" + FormatUtil.formatStrForDB(billStateEk);

			}
			if (!ValidateUtil.isEmpty(issuer) && !ValidateUtil.isEmpty(date)) {
				hql += " ,issuer =" + FormatUtil.formatStrForDB(issuer);
				hql += " ,issueTime=" + FormatUtil.formatStrForDB(date);
			}
			if (!ValidateUtil.isEmpty(noticeInIds)) {
				hql += "  where  noticeInId in " + noticeInIds;
			}

			this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除入库通知单
	 * @param noticeInIds id数组转化成dbString，格式为('1','2','3')
	 */
	public void removeData(String noticeInIds) {
		try {
			String hqlWhere = "  a.noticeInId in " + noticeInIds;
			String hql = "delete from WlWmNoticeIn  a where " + hqlWhere;
			this.executeHql(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过通知单号获取入库通知单
	 * @param noticeNo 通知单号
	 * @return 入库通知单对象
	 */
	public WlWmNoticeIn getData(String noticeNo) {
		try {
			WlWmNoticeIn wlWmNoticeIn = null;
			String hql = "from WlWmNoticeIn where noticeNo=?";
			List list = this.find(hql, noticeNo);
			if (!ValidateUtil.isEmpty(list)) {
				wlWmNoticeIn = (WlWmNoticeIn) list.get(0);
			}
			return wlWmNoticeIn;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

}
