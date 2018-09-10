package com.gt.wl.wm.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmStoreCheck;

@Repository("wl.wm.WlWmStoreCheckDao")
public class WlWmStoreCheckDao extends BaseDao {

	public WlWmStoreCheckDao() {
		modelClass = WlWmStoreCheck.class;
		defaultOrder = "";
	}

	/**
	 * 查询盘点单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			String hql = " from WlWmStoreCheck where 1=1 ";
			// 单据编号
			if (paraMap.containsKey("billNo")) {
				String billNo = paraMap.get("billNo").toString();
				if (!ValidateUtil.isEmpty(billNo)) {
					hql += " and (billNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + " or noticeNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + ")";
				}
			}
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and storageId =" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 盘点开始时间
			if (paraMap.containsKey("beginDate")) {
				String beginDate = paraMap.get("beginDate").toString();
				if (!ValidateUtil.isEmpty(beginDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("checkDate"));
					hql = hql + "  >= " + this.sqlUtil.string2Date(FormatUtil.formatStrForDB(beginDate));
				}
			}
			// 盘点结束时间
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("checkDate"));
					hql = hql + "  <=" + this.sqlUtil.string2Date(FormatUtil.formatStrForDB(endDate));
				}
			}
			// 经办人
			if (paraMap.containsKey("createor")) {
				String createor = paraMap.get("createor").toString();
				if (!ValidateUtil.isEmpty(createor)) {
					hql += " and createor like " + FormatUtil.formatStrForDB("%" + createor + "%");
				}
			}
			// 盘点业务类型
			if (paraMap.containsKey("wmCheckTypeEk")) {
				String wmCheckTypeEk = paraMap.get("wmCheckTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmCheckTypeEk)) {
					hql += " and wmCheckTypeEk =" + FormatUtil.formatStrForDB(wmCheckTypeEk);
				}
			}
			// 单据状态
			if (paraMap.containsKey("lockFlagEk")) {
				String lockFlagEk = paraMap.get("lockFlagEk").toString();
				if (!ValidateUtil.isEmpty(lockFlagEk)) {
					hql += " and lockFlagEk = " + FormatUtil.formatStrForDB(lockFlagEk);
				}
			}
			// 审核状态
			if (paraMap.containsKey("auditState")) {
				String auditState = paraMap.get("auditState").toString();
				if (!ValidateUtil.isEmpty(auditState)) {
					if ("12".equals(auditState)) {// 12代表审核通过和驳回两种状态
						hql += "  and (auditState =" + FormatUtil.formatStrForDB("1") + " or auditState =" + FormatUtil.formatStrForDB("2") + ")";
					}
					else {
						hql += "  and auditState =" + FormatUtil.formatStrForDB(auditState);

					}

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
	 * 获取当前最新的单据编号
	 * @return 最新单据编号
	 */
	public String getNewBillNo() {
		try {
			String billNo = "";
			String hql = "select max(billNo) from WlWmStoreCheck";
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				billNo = (String) list.get(0);
			}
			return billNo;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取今日单据数
	 * @param today 今日日期
	 * @param storageId 仓库ID（为空或null则查询所有仓库）
	 * @return 单据数
	 */
	public long getBillCount(String today, String storageId) {
		try {
			String hql = "select count(*) from WlWmStoreCheck where 1=1";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and storageId=" + FormatUtil.formatStrForDB(storageId);
			}
			hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("checkDate"));
			hql += " =" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(today));
			Long total = (Long) this.find(hql).get(0);
			return total;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
