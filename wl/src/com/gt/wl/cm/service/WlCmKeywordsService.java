package com.gt.wl.cm.service;



import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmKeywordsDao;

@Service("wl.cm.WlCmKeywordsService")
public class WlCmKeywordsService extends BaseService {
	private WlCmKeywordsDao wlCmKeywordsDao = (WlCmKeywordsDao) Sc.getBean("wl.cm.WlCmKeywordsDao");

	public WlCmKeywordsService() {
		baseDao = wlCmKeywordsDao;
	}
	
	/**
	 * 通过map查询记录
	 * @param pageSize 页记录数
	 * @param currPage 页码
	 * @return 数据页对象
	 */
	public Page search(int pageSize, int currPage) {
		try {

			return wlCmKeywordsDao.search(pageSize, currPage);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}
}