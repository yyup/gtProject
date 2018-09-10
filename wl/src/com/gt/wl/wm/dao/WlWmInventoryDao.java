package com.gt.wl.wm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmInventory;

/**
 * 库存DAO层
 * @author liuyj
 * 
 */
@Repository("wl.wm.WlWmInventoryDao")
public class WlWmInventoryDao extends BaseDao {

	public WlWmInventoryDao() {
		modelClass = WlWmInventory.class;
		defaultOrder = "";
	}

	/**
	 * 通过仓库ID获取其下物料总数
	 * @param storageId 仓库ID
	 * @return 物料总数
	 */
	public long getItemTotalByStorageId(String storageId) {
		try {
			String hql = "select count(*) from WlWmInventory where isEnableFlag='1' and storageId=?";
			Long total = (Long) this.find(hql, storageId).get(0);
			return total;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过仓库ID获取其下物料库存过多的物料数
	 * @param storageId 仓库ID
	 * @return 库存过多的物料数
	 */
	public long getMuchTotalByStorageId(String storageId) {
		try {
			String hql = "select count(*) from WlWmInventory m,WlCmItem s where m.itemId=s.itemId and m.isEnableFlag='1' and " + "s.upperLimit<>0" + " and (m.baseUnitQty-s.upperLimit>0) and m.storageId=?";
			Long total = (Long) this.find(hql, storageId).get(0);
			return total;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过仓库ID获取其下物料库存不足的物料数
	 * @param storageId 仓库ID
	 * @return 库存不足的物料数
	 */
	public long getLowTotalByStorageId(String storageId) {
		try {
			String hql = "select count(*) from WlWmInventory m,WlCmItem s where m.itemId=s.itemId and m.isEnableFlag='1' and " + "s.lowerLimit<>0" + " and (m.baseUnitQty-s.lowerLimit<0) and m.storageId=?";
			Long total = (Long) this.find(hql, storageId).get(0);
			return total;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询库存物料分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param storageId 仓库ID
	 * @param content 查询内容
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, String storageId, String content) {
		try {
			String hql = "select new map(inventoryId as inventoryId,itemId as itemId,itemCd as itemCd,itemName as itemName,spec as spec,baseUnitQty as baseUnitQty) from WlWmInventory where storageId=" + FormatUtil.formatStrForDB(storageId);
			if (!ValidateUtil.isEmpty(content)) {
				hql += " and (itemCd like '%" + content + "%' or itemName like '%" + content + "%' or spec like '%" + content + "%')";
			}
			hql += "  order by itemName";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询库存物料分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page searchInventory(int currPage, int pageSize, Map paraMap) {
		try {
			String hql = "select new map(inv.itemCd as itemCd,inv.itemName as itemName,inv.spec as spec,inv.baseUnitQty as baseUnitQty,inv.storageName as storageName,inv.baseUnitQty as baseUnitQty,inv.baseUnitName as baseUnitName,inv.storageAreaName as storageAreaName";
			hql += ",inv.inventoryId as inventoryId ,cat.name as name ";
			hql += ") from WlWmInventory inv,WlCmItem item,WlCmCategory cat where inv.itemId=item.itemId and item.categoryId=cat.categoryId ";
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = (String) paraMap.get("storageId");
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and inv.storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 库区
			if (paraMap.containsKey("storageAreaId")) {
				String storageAreaId = (String) paraMap.get("storageAreaId");
				if (!ValidateUtil.isEmpty(storageAreaId)) {
					hql += " and inv.storageAreaId=" + FormatUtil.formatStrForDB(storageAreaId);
				}
			}
			// 物料编码或物料名称搜索
			if (paraMap.containsKey("content")) {
				String content = (String) paraMap.get("content");
				if (!ValidateUtil.isEmpty(content)) {
					hql += " and (inv.itemCd like '%" + content + "%' or inv.itemName like '%" + content + "%')";
				}
			}
			// 是否显示给经销商
			if (paraMap.containsKey("isShowAgency")) {
				String isShowAgency = (String) paraMap.get("isShowAgency");
				if (!ValidateUtil.isEmpty(isShowAgency)) {
					hql += " and item.isShowAgency=" + FormatUtil.formatStrForDB(isShowAgency);
					hql += " and cat.isShowAgency=" + FormatUtil.formatStrForDB(isShowAgency);
				}
			}
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 统计库存数量
	 * @param paraMap 条件
	 * @return 库存数量
	 */
	public double getTotalQty(Map paraMap) {
		try {
			double sumQty = 0;
			String hql = "select sum(baseUnitQty) from WlWmInventory where 1=1 ";
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = (String) paraMap.get("storageId");
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 库区
			if (paraMap.containsKey("storageAreaId")) {
				String storageAreaId = (String) paraMap.get("storageAreaId");
				if (!ValidateUtil.isEmpty(storageAreaId)) {
					hql += " and storageAreaId=" + FormatUtil.formatStrForDB(storageAreaId);
				}
			}
			// 物料编码或物料名称搜索
			if (paraMap.containsKey("content")) {
				String content = (String) paraMap.get("content");
				if (!ValidateUtil.isEmpty(content)) {
					hql += " and (itemCd like '%" + content + "%' or itemName like '%" + content + "%')";
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

	/**
	 * 查询库存物料数据
	 * @param storageId 仓库ID
	 * @param storageAreaId 库区ID
	 * @param content 查询内容
	 * @return 结果
	 */
	public List findInventoryList(String storageId, String storageAreaId, String content) {
		try {
			String hql = "select new map(itemCd as itemCd,itemName as itemName,spec as spec,baseUnitQty as baseUnitQty,storageName as storageName,baseUnitName as baseUnitName,storageAreaName as storageAreaName) from WlWmInventory where 1=1 ";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and storageId=" + FormatUtil.formatStrForDB(storageId);
			}
			if (!ValidateUtil.isEmpty(storageAreaId)) {
				hql += " and storageAreaId=" + FormatUtil.formatStrForDB(storageAreaId);
			}
			if (!ValidateUtil.isEmpty(content)) {
				hql += " and (itemCd like '%" + content + "%' or itemName like '%" + content + "%')";
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询库存过多物料分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param storageId 仓库ID
	 * @param content 查询内容
	 * @return 分页结果
	 */
	public Page searchMuchStore(int currPage, int pageSize, String storageId, String content) {
		try {
			String hql = "select new map(m.inventoryId as inventoryId,m.itemId as itemId,m.itemCd as itemCd,m.itemName as itemName,m.spec as spec,m.baseUnitQty as baseUnitQty) ";
			hql += " from WlWmInventory m,WlCmItem s where m.itemId=s.itemId and m.isEnableFlag='1' and m.storageId=" + FormatUtil.formatStrForDB(storageId);
			hql += " and " + "s.upperLimit<>0" + " and (m.baseUnitQty-s.upperLimit>0)";
			if (!ValidateUtil.isEmpty(content)) {
				hql += " and (m.itemCd like '%" + content + "%' or m.itemName like '%" + content + "%' or m.spec like '%" + content + "%')";
			}
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询库存不足物料分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param storageId 仓库ID
	 * @param content 查询内容
	 * @return 分页结果
	 */
	public Page searchLowerStore(int currPage, int pageSize, String storageId, String content) {
		try {
			String hql = "select new map(m.inventoryId as inventoryId,m.itemId as itemId,m.itemCd as itemCd,m.itemName as itemName,m.spec as spec,m.baseUnitQty as baseUnitQty) ";
			hql += " from WlWmInventory m,WlCmItem s where m.itemId=s.itemId and m.isEnableFlag='1' and m.storageId=" + FormatUtil.formatStrForDB(storageId);
			hql += " and " + "s.lowerLimit<>0" + " and (m.baseUnitQty-s.lowerLimit<0)";
			if (!ValidateUtil.isEmpty(content)) {
				hql += " and (m.itemCd like '%" + content + "%' or m.itemName like '%" + content + "%' or m.spec like '%" + content + "%')";
			}
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取库存货品详情(通过仓库ID及物料ID)
	 * @param storageId 仓库ID
	 * @param itemId 物料ID
	 * @return 库存对象，无则返回null
	 */
	public WlWmInventory getData(String storageId, String itemId) {
		WlWmInventory wlWmInventory = null;
		try {
			String hql = "from WlWmInventory where storageId=? and itemId=?";
			List list = this.find(hql, new Object[] { storageId, itemId });
			if (!ValidateUtil.isEmpty(list)) {
				wlWmInventory = (WlWmInventory) list.get(0);
			}
			return wlWmInventory;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 查找库存列表
	 * @param storageId 仓库ID
	 * @param itemId 物料id
	 * @return 库存列表
	 */
	public List<WlWmInventory> findDataList(String storageId, String itemId) {
		try {
			String hql = "from WlWmInventory where 1=1";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and storageId=" + FormatUtil.formatStrForDB(storageId);
			}
			if (!ValidateUtil.isEmpty(itemId)) {
				hql += " and itemId=" + FormatUtil.formatStrForDB(itemId);
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据序列号查询库存信息
	 * @param serialNo
	 * @return
	 */
	public Map getInventoryBySerialNo(String serialNo) {
		try {
			Map map = new HashMap();
			String hql = "select new map(i.itemId as itemId, i.itemCd as itemCd, i.itemName as itemName) from WlWmInventory i, WlWmInventoryDtl ind where 1=1 and i.inventoryId = ind.inventoryId ";
			if (!ValidateUtil.isEmpty(serialNo)) {
				hql += " and ind.serialNo=" + FormatUtil.formatStrForDB(serialNo);
			}
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				map = (Map) list.get(0);
			}
			return map;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
