package com.gt.wl.wm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmNoticeCheckDetl;

/**
 * 盘点通知单明细DAO层
 * @author liuyj
 * 
 */
@Repository("wl.wm.WlWmNoticeCheckDetlDao")
public class WlWmNoticeCheckDetlDao extends BaseDao {

	public WlWmNoticeCheckDetlDao() {
		modelClass = WlWmNoticeCheckDetl.class;
		defaultOrder = "";
	}

	/**
	 * 通过盘点通知单ID获取盘点通知单明细列表
	 * @param noticeCheckId 盘点通知单ID
	 * @return 列表
	 */
	public List<WlWmNoticeCheckDetl> findNoticeCheckDtlList(String noticeCheckId) {
		try {
			String hql = "from WlWmNoticeCheckDetl where noticeCheckId=?";
			return this.find(hql, noticeCheckId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据通知单 ID 删除盘点通知单从表数据
	 * @param noticeCheckIds 盘点通知单通知单Ids，格式为('1','2','3')
	 * @return 删除总数
	 */
	public int deleteByNoticeCheckId(String noticeCheckIds) {
		try {
			String hql = "delete WlWmNoticeCheckDetl o where  o.noticeCheckId in" + noticeCheckIds;
			return this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取盘点单打印数据
	 * @param noticeCheckId 盘点单id
	 * @param storageId 仓库id
	 * @return 盘点单打印数据
	 */
	public List findDataList(String noticeCheckId, String storageId) {
		try {
			String sql = "select a.ITEM_CD as ITEM_CD,a.ITEM_NAME as ITEM_NAME,a.SPEC as SPEC,a.BASE_UNIT_NAME as BASE_UNIT_NAME,b.BASE_UNIT_QTY as BASE_UNIT_QTY ";
			sql += "from WL_WM_NOTICE_CHECK_DETL a left join  WL_WM_INVENTORY  b on a.ITEM_ID =b.ITEM_ID where 1=1 ";
			if (!ValidateUtil.isEmpty(noticeCheckId)) {
				sql += " and a.NOTICE_CHECK_ID=" + FormatUtil.formatStrForDB(noticeCheckId);
			}
			if (!ValidateUtil.isEmpty(storageId)) {
				sql += " and b.STORAGE_ID=" + FormatUtil.formatStrForDB(storageId);
			}
			return this.findByFreeSQL(sql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
