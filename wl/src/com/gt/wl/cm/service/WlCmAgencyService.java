package com.gt.wl.cm.service;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmAgencyDao;

/**
 * 经销商管理service层
 * @author chench
 * 
 */
@Service("wl.cm.WlCmAgencyService")
public class WlCmAgencyService extends BaseService {
	private WlCmAgencyDao wlCmAgencyDao = (WlCmAgencyDao) Sc.getBean("wl.cm.WlCmAgencyDao");

	public WlCmAgencyService() {
		baseDao = wlCmAgencyDao;
	}

	/**
	 * 查询经销商列表数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param agencyName 经销商名称
	 * @return 分页结果
	 */

	public Page search(int currPage, int pageSize, String agencyName) {
		try {
			return wlCmAgencyDao.search(currPage, pageSize, agencyName);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}