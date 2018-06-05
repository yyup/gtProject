package cn.joyone.base.dao;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import cn.joyone.base.model.WlCmStorage;

@Repository("wl.cm.WlCmStorageDao")
public class WlCmStorageDao extends BaseDao {

	public WlCmStorageDao() {
		modelClass = WlCmStorage.class;
		defaultOrder = "";
	}

	/**
	 * 根据编号分页查询参考信息
	 * @param currPage
	 * @param pageSize
	 * @param storageCd
	 * @return
	 */
	public Page search(int currPage, int pageSize, String storageCd) {
		try {
			String where = " where  1 = 1 ";
			if (!ValidateUtil.isEmpty(storageCd)) {
				where += " and t.storageCd like '%" + storageCd + "%' ";
			}
			String hql = " from WlCmStorage as t " + where + " order by t.id desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
