package com.gt.wl.cm.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmUserDao;
import com.gt.wl.cm.model.WlCmUser;

/**
 * 用户管理Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmUserService")
public class WlCmUserService extends BaseService {
	private WlCmUserDao wlCmUserDao = (WlCmUserDao) Sc.getBean("wl.cm.WlCmUserDao");
	private WlCmUserGroupService wlCmUserGroupService = (WlCmUserGroupService) Sc.getBean("wl.cm.WlCmUserGroupService");

	public WlCmUserService() {
		baseDao = wlCmUserDao;
	}

	/**
	 * 通过登录名查找用户
	 * @param loginName 登录名
	 * @return 查找到的用户对象，没找到返回null
	 */
	public WlCmUser findByLoginName(String loginName) {
		try {
			WlCmUser user = wlCmUserDao.findByLoginName(loginName);
			return user;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 查找所有有效的用户
	 * @return 用户列表
	 */
	public List<WlCmUser> findAllUser() {
		try {
			return wlCmUserDao.findAllUser();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过复合条件进行查询
	 * @param currPage 当前页
	 * @param pageSize 页记录数
	 * @param name 用户名
	 * @param state 用户状态
	 * @return 数据集合
	 */
	public Page search(int currPage, int pageSize, String name, String state) {
		try {
			if (name == null) {
				name = "";
			}
			String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(name);
			name = m.replaceAll("").trim();
			int stateInt = 0;
			if (ValidateUtil.isEmpty(state)) {
				stateInt = 1;
			}
			else {
				stateInt = Integer.parseInt(state);
			}
			return wlCmUserDao.search(currPage, pageSize, name, stateInt);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取当前用户相同用户组的用户列表
	 * @param id 用户Id
	 * @return 用户列表
	 */
	public List<WlCmUser> findGroupUsers(String id) {
		try {

			return wlCmUserDao.findGroupUsers(wlCmUserGroupService.findByUserId(id));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}