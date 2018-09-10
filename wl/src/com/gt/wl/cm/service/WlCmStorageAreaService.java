package com.gt.wl.cm.service;

import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmStorageAreaDao;

@Service("wl.cm.WlCmStorageAreaService")
public class WlCmStorageAreaService extends BaseService {
	private WlCmStorageAreaDao wlCmStorageAreaDao = (WlCmStorageAreaDao) Sc.getBean("wl.cm.WlCmStorageAreaDao");

	public WlCmStorageAreaService() {
		baseDao = wlCmStorageAreaDao;
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
			return wlCmStorageAreaDao.search(pageSize, currPage, storageId, storageAreaName);
		}
		catch (Exception e) {
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
			return wlCmStorageAreaDao.findWlCmStorageAreaList(storageId, isEnableFlag);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}