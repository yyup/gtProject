package com.gt.wl.cm.service;

import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmOrgUserDao;
import com.gt.wl.cm.model.WlCmOrgUser;

/**
 * 往来单位用户Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmOrgUserService")
public class WlCmOrgUserService extends BaseService {
	private WlCmOrgUserDao wlCmOrgUserDao = (WlCmOrgUserDao) Sc.getBean("wl.cm.WlCmOrgUserDao");

	public WlCmOrgUserService() {
		baseDao = wlCmOrgUserDao;
	}

	/**
	 * 通过经销商用户信息
	 * @param account 账号
	 * @return 经销商用户信息
	 */
	public WlCmOrgUser getWlCmOrgUser(String account) {
		try {
			return wlCmOrgUserDao.getWlCmOrgUser(account);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询经销商用户分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page findDataList(int pageSize, int currPage, Map paraMap) {
		try {
			return wlCmOrgUserDao.findDataList(pageSize, currPage, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}
}
