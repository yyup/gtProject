package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmItem;

@Repository("wl.cm.WlCmItemDao")
public class WlCmItemDao extends BaseDao {

	public WlCmItemDao() {
		modelClass = WlCmItem.class;
		defaultOrder = "";
	}

	/**
	 * 查询物料管理，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param categoryId 物料类别id
	 * @param isShowAgency 是否显示给经销商
	 * @param filterItemId 用于过滤自身（物料）Id
	 * @param itemCd 物料cd
	 * @param itemName 物料名称
	 * @param spec 规格型号
	 * @param categoryIds 物料类别ids
	 * @return 结果
	 */
	public Page search(int pageSize, int currPage, String categoryId, String isShowAgency,String filterItemId,String itemCd,String itemName,String spec,String categoryIds) {
		try {
			String hql = "from  WlCmItem w where 1=1 ";
			if (!ValidateUtil.isEmpty(categoryId)) {
				hql += " and categoryId=" + FormatUtil.formatStrForDB(categoryId);
			}
			if (!ValidateUtil.isEmpty(isShowAgency)) {
				hql += " and isShowAgency=" + FormatUtil.formatStrForDB(isShowAgency);
			}
			//用于过滤自身（物料）Id
			if (!ValidateUtil.isEmpty(filterItemId)) {
				hql += " and itemId !=" + FormatUtil.formatStrForDB(filterItemId);
			}
			if (!ValidateUtil.isEmpty(itemCd)) {
				hql += " and itemCd like" + FormatUtil.formatStrForDB("%" +itemCd+ "%");
			}
			if (!ValidateUtil.isEmpty(itemName)) {
				hql += " and itemName like" + FormatUtil.formatStrForDB("%" +itemName+ "%");
			}
			if (!ValidateUtil.isEmpty(spec)) {
				hql += " and spec like" + FormatUtil.formatStrForDB("%" +spec+ "%");
			}
			if (!ValidateUtil.isEmpty(categoryIds)) {
				hql += " and categoryId in" + ConvertUtil.toDbString(categoryIds.split(","));
			}
			hql += " order by w.createTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询物料管理，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param storageId 仓库id
	 * @param itemIds 物料ids格式为('1','2','3')
	 * @param categoryId 物料类别id
	 * @param inventoryIsNull 库存是否为null(1允许为null ，0不为null)
	 * @param itemName 物料名称
	 * @param itemCd 物料编码
	 * @return 结果
	 */
	public Page search2(int pageSize, int currPage, String storageId, String itemIds, String categoryId, String inventoryIsNull,String itemName,String itemCd) {
		try {
			// String sql = "select '' as NOTICE_IN_DETL_ID,'' as STORAGE_NAME,'' as NOTICE_IN_ID,'' as STORAGE_ID,'' as CATEGORY_ID,'' as BASE_UNIT_ID ,'' as SEQU,'' as MEMO,'' as MODIFIER ,null as MODIFY_TIME";
			String sql = "select a.ITEM_ID as ITEM_ID,a.ITEM_NAME as ITEM_NAME,a.ITEM_CD as ITEM_CD,a.SPEC as SPEC,a.BASE_UNIT_NAME as BASE_UNIT_NAME,b.BASE_UNIT_QTY as BASE_UNIT_QTY";
			sql += "  from WL_CM_ITEM  a left join  (select * from WL_WM_INVENTORY c where c.IS_ENABLE_FLAG='1' ";
			if (!ValidateUtil.isEmpty(storageId)) {
				sql += " and c.STORAGE_ID=" + FormatUtil.formatStrForDB(storageId);
			}
			sql += " ) b on a.ITEM_ID =b.ITEM_ID  where 1=1 and a.IS_ENABLE_FLAG='1'";
			if ("0".equals(inventoryIsNull)) {// 过滤无库存
				if (!ValidateUtil.isEmpty(storageId)) {
					sql += " and b.STORAGE_ID='" + storageId + "'";
				}
			}
			/*
			 * if ("0".equals(inventoryIsNull)) {
			 * sql += "  and b.IS_ENABLE_FLAG='1'";
			 * if (!ValidateUtil.isEmpty(storageId)) {
			 * sql += " and b.STORAGE_ID='" + storageId + "'";
			 * }
			 * 
			 * }
			 * else if ("1".equals(inventoryIsNull)) {
			 * sql += " and a.IS_ENABLE_FLAG='1' and (b.IS_ENABLE_FLAG='1' or b.IS_ENABLE_FLAG is null)";
			 * if (!ValidateUtil.isEmpty(storageId)) {
			 * sql += " and (b.STORAGE_ID='" + storageId + "' or b.STORAGE_ID is null )";
			 * }
			 * 
			 * }
			 */

			if (!ValidateUtil.isEmpty(categoryId)) {
				sql += " and (a.CATEGORY_ID='" + categoryId + "')";
			}
			if (!ValidateUtil.isEmpty(itemIds)) {
				sql += " and a.ITEM_ID not in " + itemIds;
			}
			if (!ValidateUtil.isEmpty(itemName)) {
				sql += " and a.ITEM_NAME like" + FormatUtil.formatStrForDB("%" +itemName+ "%");
			}
			if (!ValidateUtil.isEmpty(itemCd)) {
				sql += " and a.ITEM_CD like" + FormatUtil.formatStrForDB("%" +itemCd+ "%");
			}
			sql += "  order by a.ITEM_CD";
			return this.findPageByFreeSQL(sql, currPage, pageSize);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 获取物料管理对象
	 * @param key 物料管理的字段
	 * @param value key对应的字段的值
	 * @param itemId 物料管理id
	 * @return 物料管理对象列表
	 */
	public List findWlCmItemList(String key, String value, String itemId) {
		try {
			String hql = "from WlCmItem  where 1=1  ";
			if (!ValidateUtil.isEmpty(key) && !ValidateUtil.isEmpty(value)) {
				hql += " and " + key + "='" + value + "'";
			}
			if (!ValidateUtil.isEmpty(itemId)) {
				hql += "  and itemId!=" + FormatUtil.formatStrForDB(itemId);
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除物料管理
	 * @param itemIds id数组转化成dbString，格式为('1','2','3')
	 */
	public void removeData(String itemIds) {
		try {
			String hqlWhere = "  a.itemId in " + itemIds;
			String hql = "delete from WlCmItem  a where " + hqlWhere;
			this.executeHql(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取物料管理对象
	 * @param itemIds 物料管理ids 格式为('0','1','2')
	 * @param isEnableFlag 启用标志
	 * @return 物料管理对象列表
	 */
	public List findItemList(String itemIds, String isEnableFlag) {
		try {
			String hql = "select new map(itemId as itemId,itemCd as itemCd,itemName as itemName,spec as spec ";
			hql += ",baseUnitId as baseUnitId,baseUnitName as baseUnitName,'0' as baseUnitQty,categoryId as categoryId) from WlCmItem  where 1=1  ";
			if (!ValidateUtil.isEmpty(itemIds)) {
				hql += " and itemId in " + itemIds;
			}
			if (!ValidateUtil.isEmpty(isEnableFlag)) {
				hql += " and isEnableFlag=" + FormatUtil.formatStrForDB(isEnableFlag);
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过条形码获取物料对象
	 * @param barCd 条形码
	 * @return 物料对象（没有返回null）
	 */
	public WlCmItem getItemByBarCd(String barCd) {
		WlCmItem wlCmItem = null;
		try {
			String hql = "from WlCmItem where barCd=?";
			List list = this.find(hql, barCd);
			if (!ValidateUtil.isEmpty(list)) {
				wlCmItem = (WlCmItem) list.get(0);
			}
			return wlCmItem;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过物料编码获取物料对象
	 * @param itemCd 编码
	 * @return 物料对象（没有返回null）
	 */
	public WlCmItem getItemByItemCd(String itemCd) {
		WlCmItem wlCmItem = null;
		try {
			String hql = "from WlCmItem where itemCd=?";
			List list = this.find(hql, itemCd);
			if (!ValidateUtil.isEmpty(list)) {
				wlCmItem = (WlCmItem) list.get(0);
			}
			return wlCmItem;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
