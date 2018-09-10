package com.gt.wl.cm.service;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmUserParameterDao;
import com.gt.wl.cm.model.WlCmUserParameter;

/**
 * 用户参数Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmUserParameterService")
public class WlCmUserParameterService extends BaseService {
	private WlCmUserParameterDao wlCmUserParameterDao = (WlCmUserParameterDao) Sc.getBean("wl.cm.WlCmUserParameterDao");

	public WlCmUserParameterService() {
		baseDao = wlCmUserParameterDao;
	}

	/**
	 * 通过用户ID与代码查找用户参数对象
	 * @param userId 用户ID
	 * @param code 代码
	 * @return 用户参数对象
	 */
	public WlCmUserParameter findByUserId(long userId, String code) {
		try {
			return wlCmUserParameterDao.findByUserId(userId, code);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}
}