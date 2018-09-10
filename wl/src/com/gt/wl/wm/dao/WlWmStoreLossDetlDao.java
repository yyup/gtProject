package com.gt.wl.wm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmStoreLossDetl;

@Repository("wl.wm.WlWmStoreLossDetlDao")
public class WlWmStoreLossDetlDao extends BaseDao {

	public WlWmStoreLossDetlDao() {
		modelClass = WlWmStoreLossDetl.class;
		defaultOrder = "";
	}

	/**
	 * 根据损益ID删除损益单从表数据
	 * @param storeLossId 损益ID
	 * @return 删除总数
	 */
	public int deleteByStoreLossId(String storeLossId) {
		try {
			String hql = "delete WlWmStoreLossDetl o where  o.storeLossId =" + FormatUtil.formatStrForDB(storeLossId);
			return this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询损益单从表对象数组
	 * @param storeLossId 损益ID
	 * @return 损益单从表对象数组
	 */
	public List findStoreLossDetlList(String storeLossId) {
		try {
			String hql = "from WlWmStoreLossDetl  where 1=1 ";
			if (!ValidateUtil.isEmpty(storeLossId)) {
				hql += " and storeLossId=" + FormatUtil.formatStrForDB(storeLossId);
			}
			return this.find(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
