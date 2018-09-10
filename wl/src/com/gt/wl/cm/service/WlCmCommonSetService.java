package com.gt.wl.cm.service;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmCommonSetDao;
import com.gt.wl.cm.model.WlCmCommonSet;

/**
 * 参数设置Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmCommonSetService")
public class WlCmCommonSetService extends BaseService {
	private WlCmCommonSetDao wlCmCommonSetDao = (WlCmCommonSetDao) Sc.getBean("wl.cm.WlCmCommonSetDao");

	public WlCmCommonSetService() {
		baseDao = wlCmCommonSetDao;
	}

	/**
	 * 返回参数设置对象
	 * @param setKey 设置KEY
	 * @return 参数设置对象
	 */
	public WlCmCommonSet getWlCmCommonSetByKey(String setKey) {
		try {
			return wlCmCommonSetDao.getWlCmCommonSetByKey(setKey);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}