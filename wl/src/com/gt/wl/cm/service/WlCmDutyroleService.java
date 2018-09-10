package com.gt.wl.cm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.util.ConvertUtil;
import org.joyone.util.TreeUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmDutyroleDao;
import com.gt.wl.cm.model.WlCmSecurity;
import com.gt.wl.cm.model.WlCmUserSecurity;
import com.gt.wl.model.Node;

/**
 * 角色职务Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmDutyroleService")
public class WlCmDutyroleService extends BaseService {
	private WlCmDutyroleDao wlCmDutyroleDao = (WlCmDutyroleDao) Sc.getBean("wl.cm.WlCmDutyroleDao");
	private WlCmUserSecurityService wlCmUserSecurityService = (WlCmUserSecurityService) Sc.getBean("wl.cm.WlCmUserSecurityService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");

	public WlCmDutyroleService() {
		baseDao = wlCmDutyroleDao;
	}

	/**
	 * 根据类型返回职务（角色）结果列表
	 * @param code 代码
	 * @param state 状态
	 * @return 职务（角色）结果列表
	 */
	public List findByCodeAndState(String code, int state) {
		try {
			return wlCmDutyroleDao.findByCodeAndState(code, state);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 根据名称查找
	 * @param name 名称
	 * @return 列表
	 */
	public List findByName(String name) {
		try {
			return wlCmDutyroleDao.findByName(name);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回翻页对象
	 * @param pageSize 页大小
	 * @param currPage 当前页
	 * @param state 状态
	 * @param code 类型 DUTY ROLE GROUP
	 * @param name 名称
	 * @return 翻页对象
	 */
	public Page search(int pageSize, int currPage, int state, String code, String name) {
		try {
			String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			Pattern p = Pattern.compile(regEx);
			if (name != null) {
				Matcher m = p.matcher(name);
				name = m.replaceAll("").trim();
			}
			Matcher c = p.matcher(code);
			code = c.replaceAll("").trim();

			return wlCmDutyroleDao.search(currPage, pageSize, state, code, name);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回角色所拥有的权限树
	 * @param id 角色
	 * @return 角色所拥有的权限树
	 */
	public Map getTree(String id) {
		try {
			List list = this.findRoleOwnTreeList(id);
			return TreeUtil.getPdTree(list, "0");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 
	 * @param id 角色id
	 * @return 角色拥有的权限列表
	 */
	public List findRoleOwnTreeList(String id) {
		try {
			List resultSecurityList = new ArrayList();
			List<WlCmUserSecurity> userSecurityList = wlCmUserSecurityService.findListByOwnerId(id);
			// 构造权限,用逗号隔开，用于查找权限
			String securityParentIds = "";
			for (WlCmUserSecurity wlCmUserSecurity : userSecurityList) {
				securityParentIds += "," + wlCmUserSecurity.getSecurityArray();
			}
			if (!ValidateUtil.isEmpty(securityParentIds)) {
				securityParentIds = securityParentIds.substring(1, securityParentIds.length());
				// 分别为查找权限（具体权限模块），菜单模块，系统模块，根节点
				for (int i = 0; i < 4; i++) {
					String array[] = securityParentIds.split(",");
					securityParentIds = this.getSecurityParentIdAndNodeList(resultSecurityList, array);
				}
			}

			return resultSecurityList;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 
	 * @param resultSecurityList 用于暂存权限列表
	 * @param array 权限数据
	 * @return 权限父节点id，逗号隔开
	 */
	public String getSecurityParentIdAndNodeList(List resultSecurityList, String[] array) {
		try {
			String securityParentIds = "";
			List<WlCmSecurity> securityList = wlCmSecurityService.findListByIds(ConvertUtil.toDbString(array));
			for (WlCmSecurity wlCmSecurity : securityList) {
				Node node = new Node();
				node.setLast(true);
				node.setName(wlCmSecurity.getName());
				node.setNodeId(wlCmSecurity.getId());
				node.setParentId(wlCmSecurity.getParentId());
				resultSecurityList.add(node);
				if (securityParentIds.indexOf(wlCmSecurity.getParentId()) == -1) {
					securityParentIds += "," + wlCmSecurity.getParentId();
				}
			}
			return securityParentIds.substring(1, securityParentIds.length());
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}