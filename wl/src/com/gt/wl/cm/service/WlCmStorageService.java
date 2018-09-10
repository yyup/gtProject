package com.gt.wl.cm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmStorageDao;
import com.gt.wl.cm.model.WlCmStorage;

@Service("wl.cm.WlCmStorageService")
public class WlCmStorageService extends BaseService {
	private WlCmStorageDao wlCmStorageDao = (WlCmStorageDao) Sc.getBean("wl.cm.WlCmStorageDao");

	public WlCmStorageService() {
		baseDao = wlCmStorageDao;
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
			return wlCmStorageDao.search(pageSize, currPage, content);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询启用的仓库列表
	 * @return 仓库列表
	 */
	public List<Map> findStorageList() {
		try {
			return wlCmStorageDao.findStorageList();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 得到默认进货仓库或出库仓库
	 * 先取默认进货仓库，没有则取默认出库仓库，都没有返回null
	 * @return 仓库对象(没有返回null)
	 */
	public WlCmStorage getDefaultStorage() {
		try {
			WlCmStorage wlCmStorage = null;
			// 取默认进货仓库
			wlCmStorage = wlCmStorageDao.getDefaultInStorage(null);
			if (ValidateUtil.isNull(wlCmStorage)) {// 为null则取默认出库仓库
				wlCmStorage = wlCmStorageDao.getDefaultOutStorage(null);
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
		try {
			return wlCmStorageDao.getStorageByStorageCd(storageCd, storageId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取进货仓库
	 * @param storageId 仓库id
	 * @return 仓库对象（没有返回null）
	 */
	public WlCmStorage getDefaultInStorage(String storageId) {
		try {
			return wlCmStorageDao.getDefaultInStorage(storageId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取出库仓库
	 * @param storageId 仓库id
	 * @return 仓库对象（没有返回null）
	 */
	public WlCmStorage getDefaultOutStorage(String storageId) {
		try {
			return wlCmStorageDao.getDefaultOutStorage(storageId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 增加数据
	 * @param wlCmStorage 仓库信息对象
	 * @param user 当前登录用户
	 */
	public void saveData(WlCmStorage wlCmStorage, User user) {
		try {
			WlCmStorage inWlCmStorage = null;
			WlCmStorage outWlCmStorage = null;

			if (ValidateUtil.isEmpty(wlCmStorage.getStorageId())) {// 新建
				wlCmStorage.setCreator(user.getName());
				wlCmStorage.setCreateTime(new Date());
				if ("1".equals(wlCmStorage.getIsInFlag())) {
					inWlCmStorage = (WlCmStorage) this.getDefaultInStorage(null);
					// 如果存在默认入货仓库，将其改为不默认
					if (!ValidateUtil.isNull(inWlCmStorage)) {
						inWlCmStorage.setIsInFlag("0");
					}
				}
				if ("1".equals(wlCmStorage.getIsOutFlag())) {
					outWlCmStorage = (WlCmStorage) this.getDefaultOutStorage(null);
					// 如果存在默认出库仓库，将其改为不默认
					if (!ValidateUtil.isNull(outWlCmStorage)) {
						outWlCmStorage.setIsOutFlag("0");
					}
				}

			}
			else {// 编辑
				wlCmStorage.setModifier(user.getName());
				wlCmStorage.setModifyTime(new Date());
				if ("1".equals(wlCmStorage.getIsInFlag())) {
					inWlCmStorage = (WlCmStorage) this.getDefaultInStorage(wlCmStorage.getStorageId());
					// 如果存在默认入货仓库，将其改为不默认
					if (!ValidateUtil.isNull(inWlCmStorage)) {
						inWlCmStorage.setIsInFlag("0");
					}
				}
				if ("1".equals(wlCmStorage.getIsOutFlag())) {
					outWlCmStorage = (WlCmStorage) this.getDefaultOutStorage(wlCmStorage.getStorageId());
					// 如果存在默认出库仓库，将其改为不默认
					if (!ValidateUtil.isNull(outWlCmStorage)) {
						outWlCmStorage.setIsOutFlag("0");
					}
				}

			}
			if (!ValidateUtil.isNull(inWlCmStorage)) {
				this.saveObject(inWlCmStorage);
			}
			if (!ValidateUtil.isNull(outWlCmStorage)) {
				this.saveObject(outWlCmStorage);
			}
			this.saveObject(wlCmStorage);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改仓库状态
	 * @param wlCmStorage 要更改的仓库对象
	 */
	public void updateOrgState(WlCmStorage wlCmStorage) {
		try {
			WlCmStorage inWlCmStorage = null;
			WlCmStorage outWlCmStorage = null;
			// 获取仓库信息对象

			// 修改使用状态（0-禁用,1-启用)
			if ("0".equals(wlCmStorage.getIsEnableFlag())) {
				if ("1".equals(wlCmStorage.getIsInFlag())) {
					inWlCmStorage = (WlCmStorage) this.getDefaultInStorage(null);
					// 如果存在默认入货仓库，将其改为不默认
					if (!ValidateUtil.isNull(inWlCmStorage)) {
						inWlCmStorage.setIsInFlag("0");
					}
				}
				if ("1".equals(wlCmStorage.getIsOutFlag())) {
					outWlCmStorage = (WlCmStorage) this.getDefaultOutStorage(null);
					// 如果存在默认出库仓库，将其改为不默认
					if (!ValidateUtil.isNull(outWlCmStorage)) {
						outWlCmStorage.setIsOutFlag("0");
					}
				}
				wlCmStorage.setIsEnableFlag("1");
				if (!ValidateUtil.isNull(inWlCmStorage)) {
					this.saveObject(inWlCmStorage);
				}
				if (!ValidateUtil.isNull(outWlCmStorage)) {
					this.saveObject(outWlCmStorage);
				}
				this.updateObject(wlCmStorage);
			}
			else if ("1".equals(wlCmStorage.getIsEnableFlag())) {
				wlCmStorage.setIsEnableFlag("0");
				this.updateObject(wlCmStorage);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}