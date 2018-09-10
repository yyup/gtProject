package com.gt.wl.wm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmNoticeOutDetl;

/**
 * 出库通知单明细DAO层
 * @author liuyj
 * 
 */
@Repository("wl.wm.WlWmNoticeOutDetlDao")
public class WlWmNoticeOutDetlDao extends BaseDao {

	public WlWmNoticeOutDetlDao() {
		modelClass = WlWmNoticeOutDetl.class;
		defaultOrder = "";
	}

	/**
	 * 通过出库通知单ID获取明细
	 * @param noticeOutId 出库通知单ID
	 * @return 明细列表
	 */
	public List<WlWmNoticeOutDetl> findNoticeOutDetlList(String noticeOutId) {
		try {
			String hql = "from WlWmNoticeOutDetl where noticeOutId=?";
			return this.find(hql, noticeOutId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据通知单 ID 删除出库通知单从表数据
	 * @param noticeOutIds 出库通知单Ids,格式为('1','2','3')
	 * @return 删除总数
	 */
	public int deleteByNoticeInId(String noticeOutIds) {
		try {
			String hql = "delete WlWmNoticeOutDetl o where  o.noticeOutId in" + noticeOutIds;
			return this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
