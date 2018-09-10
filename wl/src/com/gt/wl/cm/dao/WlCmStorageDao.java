package com.gt.wl.cm.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmStorage;

@Repository("wl.cm.WlCmStorageDao")
public class WlCmStorageDao extends BaseDao {

	public WlCmStorageDao() {
		modelClass = WlCmStorage.class;
		defaultOrder = "";
	}

	/**
	 * 查询仓库信息，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param content 要查询的内容编号或者名称
	 * @return 结果
	 */
	public Page search(int pageSize, int currPage, String content) {
		try {
			String hql = "from  WlCmStorage w where 1=1 ";

			if (!ValidateUtil.isEmpty(content)) {
				hql += " and (";
				hql += " w.storageCd like '%" + content + "%' or w.storageName like '%" + content + "%' ";
				hql = hql + ") ";
			}
			hql += " order by w.createTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询启用的仓库列表
	 * @return 仓库列表
	 */
	public List<Map> findStorageList() {
		try {
			String hql = "select new map(storageId as storageId,storageName as storageName) from WlCmStorage where isEnableFlag='1'";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 得到默认进货仓库
	 * @param storageId 仓库id
	 * @return 仓库对象（没有返回null）
	 */
	public WlCmStorage getDefaultInStorage(String storageId) {
		WlCmStorage wlCmStorage = null;
		try {
			String hql = "from WlCmStorage where isInFlag='1' and isEnableFlag='1' ";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += "  and storageId!='" + storageId + "' ";
			}
			List<WlCmStorage> list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				wlCmStorage = list.get(0);
			}
			return wlCmStorage;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 得到默认出库仓库
	 * @param storageId 仓库id
	 * @return 仓库对象（没有返回null）
	 */
	public WlCmStorage getDefaultOutStorage(String storageId) {
		WlCmStorage wlCmStorage = null;
		try {
			String hql = "from WlCmStorage where isOutFlag='1' and isEnableFlag='1'";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += "  and storageId!='" + storageId + "' ";
			}
			List<WlCmStorage> list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				wlCmStorage = list.get(0);
			}
			return wlCmStorage;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 得到指定编号仓库
	 * @param storageCd 仓库编号
	 * @param storageId 仓库id
	 * @return 仓库对象（没有返回null）
	 */
	public WlCmStorage getStorageByStorageCd(String storageCd, String storageId) {
		WlCmStorage wlCmStorage = null;
		try {
			String hql = "from WlCmStorage where storageCd='" + storageCd + "'";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += "  and storageId!='" + storageId + "' ";
			}
			List<WlCmStorage> list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				wlCmStorage = list.get(0);
			}
			return wlCmStorage;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
