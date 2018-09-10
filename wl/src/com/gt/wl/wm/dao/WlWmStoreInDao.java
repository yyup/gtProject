package com.gt.wl.wm.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmStoreIn;

/**
 * 入库单明细DAO层
 * @author liuyj
 * 
 */
@Repository("wl.wm.WlWmStoreInDao")
public class WlWmStoreInDao extends BaseDao {

	public WlWmStoreInDao() {
		modelClass = WlWmStoreIn.class;
		defaultOrder = "";
	}

	/**
	 * 查询入库单列表
	 * @param flag 调用标志(入库单据管理为wlWmStoreIn，入库明细查为wlwmStoreInQuery）
	 * @param billNo 单据编号
	 * @param beginDate 制单开始时间
	 * @param endDate 制单结束时间
	 * @param storageId 仓库id
	 * @param createor 经办人
	 * @param wmInTypeEk 业务类型
	 * @param orgId 往来单位名字
	 * @param itemCd 物料编码
	 * @param itemName 物料名称
	 * @param spec 规格型号
	 * @param storageAreaId 库区id
	 * @param lockFlagEk 单据状态
	 * @return 入库单列表或入库明细列表
	 */
	public List<Map> findStoreInList(String flag, String billNo, String beginDate, String endDate, String storageId, String createor, String wmInTypeEk, String orgId, String itemCd, String itemName, String spec, String storageAreaId,
			String lockFlagEk) {
		try {
			String hql = "";
			if ("wlWmStoreIn".equals(flag)) {
				hql = " select new map(a.storeInId as storeInId,a.billNo as billNo,a.totalQty as totalQty,a.wmInTypeEk as wmInTypeEk,a.storageName as storageName,a.storageAreaName as storageAreaName,a.inDate as inDate ";
				hql += ",a.createor as createor,a.lockFlagEk as lockFlagEk)";
				hql += "  from WlWmStoreIn a where 1=1";
			}
			else if ("wlwmStoreInQuery".equals(flag)) {
				hql = " select new map(";
				hql += "a.storageName as storageName,a.storageAreaName as storageAreaName,a.wmInTypeEk as wmInTypeEk,a.inDate as inDate ,a.billNo as billNo ,a.orgName as orgName,";
				hql += "b.itemCd as itemCd,b.itemName as itemName,b.spec as spec,";
				hql += "c.manuSpec as manuSpec,c.manuItemName as manuItemName,c.baseUnitName as baseUnitName,b.baseUnitQty as baseUnitQty";
				hql += ")";
				hql += "  from WlWmStoreIn a,WlWmStoreInDetl b,WlCmItem c  where a.storeInId=b.storeInId and b.itemId=c.itemId and b.baseUnitQty!=0 ";
			}
			// 单据编号

			if (!ValidateUtil.isEmpty(billNo)) {
				hql += " and (a.billNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + " or a.noticeNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + ")";
			}

			// 制单开始时间
			if (!ValidateUtil.isEmpty(beginDate)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.inDate"));
				hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(beginDate));
			}

			// 制单结束时间

			if (!ValidateUtil.isEmpty(endDate)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.inDate"));
				hql += " <=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(endDate));
			}
			// 仓库
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and a.storageId=" + FormatUtil.formatStrForDB(storageId);
			}
			// 库区
			if (!ValidateUtil.isEmpty(storageAreaId)) {
				hql += " and a.storageAreaId=" + FormatUtil.formatStrForDB(storageAreaId);
			}
			// 经办人
			if (!ValidateUtil.isEmpty(createor)) {
				hql += " and a.createor like " + FormatUtil.formatStrForDB("%" + createor + "%");

			}
			// 业务类型
			if (!ValidateUtil.isEmpty(wmInTypeEk)) {
				hql += " and a.wmInTypeEk=" + FormatUtil.formatStrForDB(wmInTypeEk);
			}
			// 往来单位id
			if (!ValidateUtil.isEmpty(orgId)) {
				hql += " and a.orgId = " + FormatUtil.formatStrForDB(orgId);
			}
			// 物料编码
			if (!ValidateUtil.isEmpty(itemCd)) {
				hql += " and b.itemCd like " + FormatUtil.formatStrForDB("%" + itemCd + "%");
			}
			// 物料名称
			if (!ValidateUtil.isEmpty(itemName)) {
				hql += " and b.itemName like " + FormatUtil.formatStrForDB("%" + itemName + "%");
			}
			// 规格型号
			if (!ValidateUtil.isEmpty(spec)) {
				hql += " and b.spec like " + FormatUtil.formatStrForDB("%" + spec + "%");
			}
			// 单据状态
			if (!ValidateUtil.isEmpty(lockFlagEk)) {
				hql += " and a.lockFlagEk = " + FormatUtil.formatStrForDB(lockFlagEk);
			}

			hql += "  order by a.createTime desc";

			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库单分页数据
	 * @param flag 调用标志(入库单据管理为wlWmStoreIn，入库明细查为wlwmStoreInQuery）
	 * @param paraMap 条件
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @return 入库单分页数据
	 */
	public Page search(String flag, Map paraMap, int pageSize, int currPage) {
		try {
			String hql = "";
			if ("wlWmStoreIn".equals(flag)) {
				hql = "  from WlWmStoreIn a where 1=1 ";
			}
			else if ("wlwmStoreInQuery".equals(flag)) {
				hql = " select new map(";
				hql += "a.storageName as storageName,a.wmInTypeEk as wmInTypeEk,a.inDate as inDate ,a.billNo as billNo ,a.orgName as orgName,";
				hql += "b.itemCd as itemCd,b.itemName as itemName,b.spec as spec,";
				hql += "c.manuSpec as manuSpec,c.manuItemName as manuItemName,c.baseUnitName as baseUnitName,b.baseUnitQty as baseUnitQty";
				hql += ",a.storeInId as storeInId";
				hql += ")";
				hql += "  from WlWmStoreIn a,WlWmStoreInDetl b,WlCmItem c  where a.storeInId=b.storeInId and b.itemId=c.itemId and b.baseUnitQty!=0 ";
			}

			// 单据编号
			if (paraMap.containsKey("billNo")) {
				String billNo = paraMap.get("billNo").toString();
				if (!ValidateUtil.isEmpty(billNo)) {
					hql += " and (a.billNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + " or a.noticeNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + ")";
				}
			}
			// 制单开始时间
			if (paraMap.containsKey("beginDate")) {
				String beginDate = paraMap.get("beginDate").toString();
				if (!ValidateUtil.isEmpty(beginDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.inDate"));
					hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(beginDate));
				}
			}
			// 制单结束时间
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.inDate"));
					hql += " <=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(endDate));
				}
			}
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and a.storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 库区
			if (paraMap.containsKey("storageAreaId")) {
				String storageAreaId = paraMap.get("storageAreaId").toString();
				if (!ValidateUtil.isEmpty(storageAreaId)) {
					hql += " and a.storageAreaId=" + FormatUtil.formatStrForDB(storageAreaId);
				}
			}
			// 经办人
			if (paraMap.containsKey("createor")) {
				String createor = paraMap.get("createor").toString();
				if (!ValidateUtil.isEmpty(createor)) {
					hql += " and a.createor like " + FormatUtil.formatStrForDB("%" + createor + "%");
				}
			}
			// 业务类型
			if (paraMap.containsKey("wmInTypeEk")) {
				String wmInTypeEk = paraMap.get("wmInTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmInTypeEk)) {
					hql += " and a.wmInTypeEk=" + FormatUtil.formatStrForDB(wmInTypeEk);
				}
			}
			// 往来单位名字
			if (paraMap.containsKey("orgName")) {
				String orgName = paraMap.get("orgName").toString();
				if (!ValidateUtil.isEmpty(orgName)) {
					hql += " and a.orgName like " + FormatUtil.formatStrForDB("%" + orgName + "%");
				}
			}
			// 往来单位id
			if (paraMap.containsKey("orgId")) {
				String orgId = paraMap.get("orgId").toString();
				if (!ValidateUtil.isEmpty(orgId)) {
					hql += " and a.orgId = " + FormatUtil.formatStrForDB(orgId);
				}
			}
			// 物料编码
			if (paraMap.containsKey("itemCd")) {
				String itemCd = paraMap.get("itemCd").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like " + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			// 物料编码(入库明细查询用)
			if (paraMap.containsKey("itemCdAsLike")) {
				String itemCd = paraMap.get("itemCdAsLike").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like " + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			// 物料名称
			if (paraMap.containsKey("itemName")) {
				String itemName = paraMap.get("itemName").toString();
				if (!ValidateUtil.isEmpty(itemName)) {
					hql += " and b.itemName like " + FormatUtil.formatStrForDB("%" + itemName + "%");
				}
			}
			// 规格型号
			if (paraMap.containsKey("spec")) {
				String spec = paraMap.get("spec").toString();
				if (!ValidateUtil.isEmpty(spec)) {
					hql += " and b.spec like " + FormatUtil.formatStrForDB("%" + spec + "%");
				}
			}
			// 通知单号
			if (paraMap.containsKey("noticeNo")) {
				String noticeNo = paraMap.get("noticeNo").toString();
				if (!ValidateUtil.isEmpty(noticeNo)) {
					hql += " and a.noticeNo = " + FormatUtil.formatStrForDB(noticeNo);
				}
			}
			// 单据状态
			if (paraMap.containsKey("lockFlagEk")) {
				String lockFlagEk = paraMap.get("lockFlagEk").toString();
				if (!ValidateUtil.isEmpty(lockFlagEk)) {
					hql += " and a.lockFlagEk = " + FormatUtil.formatStrForDB(lockFlagEk);
				}
			}
			hql += "  order by a.createTime desc";
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
			String hql = "select max(billNo) from WlWmStoreIn";
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
			String hql = "select count(*) from WlWmStoreIn where 1=1";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and storageId=" + FormatUtil.formatStrForDB(storageId);
			}
			hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("inDate"));
			hql += " =" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(today));
			Long total = (Long) this.find(hql).get(0);
			return total;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取入库日期
	 * @param paraMap 条件
	 * @return 入库日期列表
	 */
	public List<Map> findInDate(Map paraMap) {
		try {
			String hql = "select new map(a.inDate as inDate";
			hql += ") from WlWmStoreIn a ,WlWmStoreInDetl b where a.storeInId=b.storeInId";
			// 日期
			if (paraMap.containsKey("date")) {
				String date = paraMap.get("date").toString();
				if (!ValidateUtil.isEmpty(date)) {
					hql += " and " + sqlUtil.string2Date(sqlUtil.date2String("a.inDate"), "yyyy-MM");
					hql += " =" + sqlUtil.string2Date(FormatUtil.formatStrForDB(date), "yyyy-MM");
					// sqlUtil.string2Date(arg0, arg1)
				}
			}
			// 物料编码
			if (paraMap.containsKey("itemCd")) {
				String itemCd = paraMap.get("itemCd").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd=" + FormatUtil.formatStrForDB(itemCd);
				}
			}
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and b.storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			hql += "  order by a.inDate";

			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取入库总数量
	 * @param paraMap 条件
	 * @return 人库总数量
	 */
	public double getInQty(Map paraMap) {
		try {
			double sumQty = 0;
			String hql = "select sum(b.baseUnitQty)";
			hql += " from WlWmStoreIn a ,WlWmStoreInDetl b where a.storeInId=b.storeInId";
			// 日期(年月)
			if (paraMap.containsKey("date")) {
				String date = paraMap.get("date").toString();
				if (!ValidateUtil.isEmpty(date)) {
					hql += " and " + sqlUtil.string2Date(sqlUtil.date2String("a.inDate"), "yyyy-MM");
					hql += " <" + sqlUtil.string2Date(FormatUtil.formatStrForDB(date), "yyyy-MM");
				}
			}
			// 日期(天)
			if (paraMap.containsKey("day")) {
				String day = paraMap.get("day").toString();
				if (!ValidateUtil.isEmpty(day)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.inDate"));
					hql += " =" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(day));
				}
			}

			// 物料编码(不模糊查询)
			if (paraMap.containsKey("itemCd")) {
				String itemCd = paraMap.get("itemCd").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd=" + FormatUtil.formatStrForDB(itemCd);
				}
			}
			// 物料编码(模糊查询)
			if (paraMap.containsKey("itemCdAsLike")) {
				String itemCd = paraMap.get("itemCdAsLike").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like " + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and b.storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 入库类型
			if (paraMap.containsKey("wmInTypeEk")) {
				String wmInTypeEk = paraMap.get("wmInTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmInTypeEk)) {
					hql += " and a.wmInTypeEk=" + FormatUtil.formatStrForDB(wmInTypeEk);
				}
			}
			// 开始日期
			if (paraMap.containsKey("beginDate")) {
				String beginDate = paraMap.get("beginDate").toString();
				if (!ValidateUtil.isEmpty(beginDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.inDate"));
					hql += " >=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(beginDate));
				}
			}
			// 结束日期
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.inDate"));
					hql += " <=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(endDate));
					// sqlUtil.string2Date(arg0, arg1)
				}
			}
			// 物料名称
			if (paraMap.containsKey("itemName")) {
				String itemName = paraMap.get("itemName").toString();
				if (!ValidateUtil.isEmpty(itemName)) {
					hql += " and b.itemName like " + FormatUtil.formatStrForDB("%" + itemName + "%");
				}
			}
			// 规格型号
			if (paraMap.containsKey("spec")) {
				String spec = paraMap.get("spec").toString();
				if (!ValidateUtil.isEmpty(spec)) {
					hql += " and b.spec like " + FormatUtil.formatStrForDB("%" + spec + "%");
				}
			}
			// 经销商id
			if (paraMap.containsKey("orgId")) {
				String orgId = paraMap.get("orgId").toString();
				if (!ValidateUtil.isEmpty(orgId)) {
					hql += " and a.orgId= " + FormatUtil.formatStrForDB(orgId);
				}
			}
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				sumQty = Double.parseDouble(list.get(0).toString());
			}
			return sumQty;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取入库统计列表
	 * @param paraMap 条件
	 * @return 入库统计列表
	 */
	public List<Map> findInDetailTotalList(Map paraMap) {
		try {
			String hql = "select new map(b.storageName as storageName,a.wmInTypeEk as wmInTypeEk";
			hql += ",b.itemCd as itemCd,b.itemName as itemName,b.spec as spec,sum(b.baseUnitQty) as inTotal";
			hql += ") from WlWmStoreIn a ,WlWmStoreInDetl b where a.storeInId=b.storeInId";
			// 入库类型
			if (paraMap.containsKey("wmInTypeEk")) {
				String wmInTypeEk = paraMap.get("wmInTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmInTypeEk)) {
					hql += " and a.wmInTypeEk=" + FormatUtil.formatStrForDB(wmInTypeEk);
				}
			}
			// 开始日期
			if (paraMap.containsKey("beginDate")) {
				String beginDate = paraMap.get("beginDate").toString();
				if (!ValidateUtil.isEmpty(beginDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.inDate"));
					hql += " >=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(beginDate));
					// sqlUtil.string2Date(arg0, arg1)
				}
			}
			// 结束日期
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.inDate"));
					hql += " <=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(endDate));
					// sqlUtil.string2Date(arg0, arg1)
				}
			}

			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and b.storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 物料编码
			if (paraMap.containsKey("itemCd")) {
				String itemCd = paraMap.get("itemCd").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like " + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			// 物料名称
			if (paraMap.containsKey("itemName")) {
				String itemName = paraMap.get("itemName").toString();
				if (!ValidateUtil.isEmpty(itemName)) {
					hql += " and b.itemName like " + FormatUtil.formatStrForDB("%" + itemName + "%");
				}
			}
			// 物料编码(模糊查询)
			if (paraMap.containsKey("itemCdAsLike")) {
				String itemCd = paraMap.get("itemCdAsLike").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like " + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			// 规格型号
			if (paraMap.containsKey("spec")) {
				String spec = paraMap.get("spec").toString();
				if (!ValidateUtil.isEmpty(spec)) {
					hql += " and b.spec like " + FormatUtil.formatStrForDB("%" + spec + "%");
				}
			}
			// 经销商
			if (paraMap.containsKey("orgName")) {
				String orgName = paraMap.get("orgName").toString();
				if (!ValidateUtil.isEmpty(orgName)) {
					hql += " and a.orgName like " + FormatUtil.formatStrForDB("%" + orgName + "%");
				}
			}
			// 经销商id
			if (paraMap.containsKey("orgId")) {
				String orgId = paraMap.get("orgId").toString();
				if (!ValidateUtil.isEmpty(orgId)) {
					hql += " and a.orgId= " + FormatUtil.formatStrForDB(orgId);
				}
			}
			hql += " group by a.wmInTypeEk,b.storageId,b.itemCd  order by b.itemCd";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
