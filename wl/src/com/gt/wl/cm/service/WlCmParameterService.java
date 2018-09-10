package com.gt.wl.cm.service;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmParameterDao;
import com.gt.wl.cm.model.WlCmParameter;

/**
 * 参数Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmParameterService")
public class WlCmParameterService extends BaseService {
	private WlCmParameterDao wlCmParameterDao = (WlCmParameterDao) Sc.getBean("wl.cm.WlCmParameterDao");

	public WlCmParameterService() {
		baseDao = wlCmParameterDao;
	}

	/**
	 * 获得用户ID拥有操作权限的参数数据列表
	 * @param u 用户
	 * @param currPage 当前页码
	 * @param pageSize 每页行数
	 * @return 参数页对象
	 */
	public Page findPageByUser(User u, int currPage, int pageSize) {
		try {
			return wlCmParameterDao.findPageByUser(u, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过Code获得参数对象
	 * @param code 代码
	 * @return 参数对象
	 */
	public WlCmParameter findByCode(String code) {
		try {
			return wlCmParameterDao.findByCode(code);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找符合条件的记录集
	 * @param searchField 搜索字段
	 * @param searchValue 搜索值
	 * @param u 用户
	 * @param currPage 当前页码
	 * @param pageSize 每页行数
	 * @return 参数页对象
	 */
	public Page search(String searchField, String searchValue, User u, int currPage, int pageSize) {
		try {
			return wlCmParameterDao.search(searchField, searchValue, u, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}