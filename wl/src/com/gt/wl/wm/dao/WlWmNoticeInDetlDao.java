package com.gt.wl.wm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmNoticeInDetl;

@Repository("wl.wm.WlWmNoticeInDetlDao")
public class WlWmNoticeInDetlDao extends BaseDao {

	public WlWmNoticeInDetlDao() {
		modelClass = WlWmNoticeInDetl.class;
		defaultOrder = "";
	}

	/**
	 * 查询库存表，将库存表相应字段查询到入库通知单从表
	 * @param inventoryIds 库存表ids 格式为('1','2','3')
	 * @return 结果
	 */
	public List getNoticeInDetlListByinventoryIds(String inventoryIds) {
		try {
			String hql = "select new map(w.storageId as storageId,w.storageName as storageName,w.itemId as itemId,w.itemCd as itemCd,w.itemName as itemName ,w.categoryId as categoryId";
			hql += ",w.spec as spec, w.baseUnitId as baseUnitId,w.baseUnitQty as baseUnitQty,w.baseUnitName as baseUnitName) from WlWmInventory w where w.inventoryId in " + inventoryIds;

			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库通知单从表
	 * @param noticeInId 入库通知单id
	 * @return 对象数组
	 */
	public List findNoticeInDetlList(String noticeInId) {
		try {
			String hql = "from WlWmNoticeInDetl where 1=1 ";
			if (!ValidateUtil.isEmpty(noticeInId)) {
				hql += " and noticeInId=" + FormatUtil.formatStrForDB(noticeInId);
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据通知单 ID 删除入库通知单从表数据
	 * @param noticeInIds 入库通知单Ids，格式为('1','2','3')
	 * @return 删除总数
	 */
	public int deleteByNoticeInId(String noticeInIds) {
		try {
			String hql = "delete WlWmNoticeInDetl o where  o.noticeInId in" + noticeInIds;
			return this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
