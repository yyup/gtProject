package com.gt.wl.cm.service;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmHydrantDao;
import com.gt.wl.cm.model.WlCmHydrant;

@Service("wl.cm.WlCmHydrantService")
public class WlCmHydrantService extends BaseService {
	private WlCmHydrantDao wlCmHydrantDao = (WlCmHydrantDao) Sc.getBean("wl.cm.WlCmHydrantDao");

	public WlCmHydrantService() {
		baseDao = wlCmHydrantDao;
	}

	/**
	 * 通过标签号查找消防栓信息
	 * @param labelNo 标签号
	 * @return 消防栓信息对象
	 */
	public WlCmHydrant getWlCmHydrant(String labelNo) {
		try {
			return wlCmHydrantDao.getWlCmHydrant(labelNo);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}