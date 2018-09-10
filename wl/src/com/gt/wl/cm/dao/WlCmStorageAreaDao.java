package com.gt.wl.cm.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmStorageArea;

@Repository("wl.cm.WlCmStorageAreaDao")
public class WlCmStorageAreaDao extends BaseDao {

	public WlCmStorageAreaDao() {
		modelClass = WlCmStorageArea.class;
		defaultOrder = "";
	}

	/**
	 * 查询仓库信息，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param storageId 仓库id
	 * @param storageAreaName 库区名字
	 * @return 结果
	 */
	public Page search(int pageSize, int currPage, String storageId, String storageAreaName) {
		try {
			String hql = "from WlCmStorageArea  where 1=1 ";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and storageId=" + FormatUtil.formatStrForDB(storageId);
			}
			if (!ValidateUtil.isEmpty(storageAreaName)) {

				hql += " and storageAreaName like " + FormatUtil.formatStrForDB("%" + storageAreaName + "%");

			}
			hql += "order by createTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询启用的库区列表
	 * @param storageId 仓库id
	 * @param isEnableFlag 是否启用
	 * @return 库区列表
	 */
	public List<Map> findWlCmStorageAreaList(String storageId, String isEnableFlag) {
		try {
			String hql = "select new map(storageAreaId as storageAreaId,storageAreaName as storageAreaName) from WlCmStorageArea where 1=1 ";
			if (!ValidateUtil.isEmpty(isEnableFlag)) {
				hql += " and isEnableFlag = " + FormatUtil.formatStrForDB(isEnableFlag);

			}
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and storageId = " + FormatUtil.formatStrForDB(storageId);
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
