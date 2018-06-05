package cn.joyone.base.service;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.springframework.stereotype.Service;

import cn.joyone.base.dao.WlCmStorageDao;

@Service("wl.cm.WlCmStorageService")
public class WlCmStorageService extends BaseService {
	WlCmStorageDao wlCmStorageDao = (WlCmStorageDao) Sc.getBean("wl.cm.WlCmStorageDao");

	public WlCmStorageService() {
		baseDao = wlCmStorageDao;
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
			return wlCmStorageDao.search(currPage, pageSize, storageCd);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}