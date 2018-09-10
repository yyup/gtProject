package com.gt.wl.cm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.User;
import org.joyone.util.TreeUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmSecurityDao;
import com.gt.wl.cm.model.WlCmSecurity;
import com.gt.wl.cm.model.WlCmUserSecurity;

/**
 * 权限功能Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmSecurityService")
public class WlCmSecurityService extends BaseService {
	private WlCmUserSecurityService wlCmUserSecurityService = (WlCmUserSecurityService) Sc.getBean("wl.cm.WlCmUserSecurityService");
	private WlCmUserGroupService wlCmUserGroupService = (WlCmUserGroupService) Sc.getBean("wl.cm.WlCmUserGroupService");
	private WlCmSecurityDao wlCmSecurityDao = (WlCmSecurityDao) Sc.getBean("wl.cm.WlCmSecurityDao");

	public WlCmSecurityService() {
		baseDao = wlCmSecurityDao;
	}

	/**
	 * 通过模块名称返回用户此模块拥有权限
	 * @param user 用户对象
	 * @param moduleCode 模块代码
	 * @return Map 用户拥有的权限
	 */
	public Map getSmcMap(User user, String moduleCode) {
		try {
			// 获取该模块定义的全部权限（如管理、访问等）
			List<WlCmSecurity> securitylist = wlCmSecurityDao.findNodeByCode(moduleCode);
			String[] idArray = new String[securitylist.size()];
			Iterator it = securitylist.iterator();
			int i = 0;
			String parnetId = "";
			// 循环，得到全部权限id数组
			while (it.hasNext()) {
				WlCmSecurity wlCmSecurity = (WlCmSecurity) it.next();
				parnetId = wlCmSecurity.getParentId();
				idArray[i] = wlCmSecurity.getId();
				i++;
			}
			//
			List userSecuritylist = wlCmUserSecurityService.findAllNode(parnetId, idArray);
			List<String> securityIdList = getSmcIdArray(user, userSecuritylist);
			Map smcmap = new HashMap();
			for (WlCmSecurity security : securitylist) {
				boolean hasSecurity = false;
				for (String securityId : securityIdList) {
					if (security.getId().equals(securityId)) { // 判断权限ID是否在权限ID列表存在
						hasSecurity = true; // 如果存在表明此权限有效
						break;// 跳出
					}
				}
				if (user.isAdmin() || user.isProgrammer()) {// 如果是系统管理员 直接判断为有效
					hasSecurity = true; // 如果存在表明此权限有效

				}
				if (hasSecurity) {// 如果权限有效,设置对应权限码在Smc中为true
					smcmap.put(security.getValue() + "Smc", true);
				}
				else {
					smcmap.put(security.getValue() + "Smc", false);
				}

			}

			return smcmap;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过模块名称和功能名称进行权限检测--用于菜单权限检测
	 * @param user 用户对象
	 * @param securityId 模块或功能ID号
	 * @return boolean
	 */
	public boolean checkFunction(User user, String securityId) {
		try {
			if (ValidateUtil.isEmpty(securityId)) {
				return true;
			}
			List list = wlCmUserSecurityService.findAllNode(securityId);
			return check(user, list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据ID返回名称
	 * @param id id
	 * @return 名称
	 */
	public String getName(String id) {
		try {
			if (ValidateUtil.isEmpty(id)) {
				return "";
			}
			WlCmSecurity object = (WlCmSecurity) this.getObject(id);
			return object == null ? "" : object.getName();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过parentId查找子权限
	 * @param id 节点ID
	 * @return 列表
	 */
	public List findChildByParentId(String id) {
		try {
			return wlCmSecurityDao.findChildByParentId(id);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 通过网址和请求字符串进行权限检测
	 * @param user 用户对象
	 * @param url 网址
	 * @param query 请求字符
	 * @return boolean
	 */
	public boolean checkUrl(User user, String url, String query) {
		try {
			List list = wlCmSecurityDao.findNodeByUrl(url, query);
			String[] ids = new String[list.size()];
			Iterator it = list.iterator();
			int i = 0;
			while (it.hasNext()) {
				WlCmSecurity wlCmSecurity = (WlCmSecurity) it.next();
				ids[i] = wlCmSecurity.getId();
				i++;
			}
			list = wlCmUserSecurityService.findAllNode(ids);
			return check(user, list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 检测用户权限
	 * @param user 用户对象
	 * @param list BaseUserSecurity列表
	 * @return boolean
	 */
	private boolean check(User user, List list) {
		try {
			if (user == null) {
				return false;
			}
			if (user.isProgrammer()) {
				return true;
			}
			Iterator it = list.iterator();
			while (it.hasNext()) {
				WlCmUserSecurity wlCmUserSecurity = (WlCmUserSecurity) it.next();
				if ("-1".equals(wlCmUserSecurity.getOwnerId()) && "-1".equals(wlCmUserSecurity.getDepartmentId())) {
					return true; // 无限制
				}
				if (wlCmUserSecurity.getType() == 1) // 用户
				{
					if ("-1".equals(wlCmUserSecurity.getOwnerId())) {
						if (wlCmUserSecurity.getDepartmentId().equals(user.getDepartmentId())) {
							return true;
						}
					}
					else if (wlCmUserSecurity.getOwnerId().equals(user.getId())) {
						return true;
					}
				}
				else if (wlCmUserSecurity.getType() == 2) // 职务
				{
					if ("-1".equals(wlCmUserSecurity.getOwnerId())) {
						if (wlCmUserGroupService.checkIsBelongToDepartment(user.getId(), wlCmUserSecurity.getDepartmentId(), 1)) {
							return true;
						}
					}
					else {
						if ("-1".equals(wlCmUserSecurity.getDepartmentId())) {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getOwnerId(), 1)) {
								return true;
							}
						}
						else {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getDepartmentId(), wlCmUserSecurity.getOwnerId(), 1)) {
								return true;
							}
						}
					}

				}
				else if ((wlCmUserSecurity.getType() == 3)) // 角色
				{
					if ("-1".equals(wlCmUserSecurity.getOwnerId())) {
						if (wlCmUserGroupService.checkIsBelongToDepartment(user.getId(), wlCmUserSecurity.getDepartmentId(), 2)) {
							return true;
						}
					}
					else {
						if ("-1".equals(wlCmUserSecurity.getDepartmentId())) {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getOwnerId(), 2)) {
								return true;
							}
						}
						else {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getDepartmentId(), wlCmUserSecurity.getOwnerId(), 2)) {
								return true;
							}
						}
					}
				}
				else if ((wlCmUserSecurity.getType() == 4)) // 用户组
				{
					if ("-1".equals(wlCmUserSecurity.getOwnerId())) {
						if (wlCmUserGroupService.checkIsBelongToDepartment(user.getId(), wlCmUserSecurity.getDepartmentId(), 3)) {
							return true;
						}
					}
					else {
						if ("-1".equals(wlCmUserSecurity.getDepartmentId())) {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getOwnerId(), 3)) {
								return true;
							}
						}
						else {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getDepartmentId(), wlCmUserSecurity.getOwnerId(), 3)) {
								return true;
							}
						}
					}
				}
			}
			return false;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回用户权限ID数组
	 * @param user 用户对象
	 * @param list BaseUserSecurity列表
	 * @return List<String> 拥有权限的权限ID列表（可能重复）
	 */
	private List<String> getSmcIdArray(User user, List list) {
		List<String> smcIdList = new ArrayList<String>();
		try {
			if (user == null) {
				return smcIdList;
			}
			Iterator it = list.iterator();
			while (it.hasNext()) {
				WlCmUserSecurity wlCmUserSecurity = (WlCmUserSecurity) it.next();

				if (user.isProgrammer()) {
					smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
					continue;
				}

				if ("-1".equals(wlCmUserSecurity.getOwnerId()) && "-1".equals(wlCmUserSecurity.getDepartmentId())) {
					smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
					continue; // 无限制
				}
				if (wlCmUserSecurity.getType() == 1) // 用户
				{
					if ("-1".equals(wlCmUserSecurity.getOwnerId())) {
						if (wlCmUserSecurity.getDepartmentId().equals(user.getDepartmentId())) {
							smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
							continue;
						}
					}
					else if (wlCmUserSecurity.getOwnerId().equals(user.getId())) {
						smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
						continue;
					}
				}
				else if (wlCmUserSecurity.getType() == 2) // 职务
				{
					if ("-1".equals(wlCmUserSecurity.getOwnerId())) {
						if (wlCmUserGroupService.checkIsBelongToDepartment(user.getId(), wlCmUserSecurity.getDepartmentId(), 1)) {
							smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
							continue;
						}
					}
					else {
						if ("-1".equals(wlCmUserSecurity.getDepartmentId())) {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getOwnerId(), 1)) {
								smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
								continue;
							}
						}
						else {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getDepartmentId(), wlCmUserSecurity.getOwnerId(), 1)) {
								smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
								continue;
							}
						}
					}

				}
				else if ((wlCmUserSecurity.getType() == 3)) // 角色
				{
					if ("-1".equals(wlCmUserSecurity.getOwnerId())) {
						if (wlCmUserGroupService.checkIsBelongToDepartment(user.getId(), wlCmUserSecurity.getDepartmentId(), 2)) {
							smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
							continue;
						}
					}
					else {
						if ("-1".equals(wlCmUserSecurity.getDepartmentId())) {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getOwnerId(), 2)) {
								smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
								continue;
							}
						}
						else {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getDepartmentId(), wlCmUserSecurity.getOwnerId(), 2)) {
								smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
								continue;
							}
						}
					}
				}
				else if ((wlCmUserSecurity.getType() == 4)) // 用户组
				{
					if ("-1".equals(wlCmUserSecurity.getOwnerId())) {
						if (wlCmUserGroupService.checkIsBelongToDepartment(user.getId(), wlCmUserSecurity.getDepartmentId(), 3)) {
							smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
							continue;
						}
					}
					else {
						if ("-1".equals(wlCmUserSecurity.getDepartmentId())) {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getOwnerId(), 3)) {
								smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
								continue;
							}
						}
						else {
							if (wlCmUserGroupService.checkIsBelongToGroup(user.getId(), wlCmUserSecurity.getDepartmentId(), wlCmUserSecurity.getOwnerId(), 3)) {
								smcIdList = setSmcArray(wlCmUserSecurity, smcIdList);
								continue;
							}
						}
					}
				}
			}
			return smcIdList;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据用户权限对象，向权限ID数组添加权限ID
	 * @param wlCmUserSecurity 用户权限对象
	 * @param smcIdList 权限ID数组
	 * @return 添加完的权限ID数组
	 */
	public List<String> setSmcArray(WlCmUserSecurity wlCmUserSecurity, List<String> smcIdList) {
		try {
			String arr[] = wlCmUserSecurity.getSecurityArray().split(",");
			if (arr.length > 1) {
				for (String b : arr) {
					smcIdList.add(b);
				}
			}
			else {
				smcIdList.add(wlCmUserSecurity.getSecurityArray());
			}

			return smcIdList;

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过类型 状态获取权限ID列表
	 * @param state 状态
	 * @param type 类型
	 * @return 权限ID列表
	 */
	public List findByStateAndType(int state, int type) {
		try {
			return wlCmSecurityDao.findChild(state, type);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 通过状态获取权限ID列表
	 * @param state 状态
	 * @return 权限ID列表
	 */
	public List findByState(int state) {
		try {
			return wlCmSecurityDao.findChild(state);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 返回Tree 对象
	 * @param state 状态
	 * @param rootId 根节点ID
	 * @return map树对象
	 */
	public Map getTree(int state, String rootId) {
		try {
			return TreeUtil.getPdTree(this.findByState(state), rootId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 返回Tree 对象
	 * @param state 状态
	 * @param id 过滤Id
	 * @param rootId 根节点ID
	 * @return map树对象
	 */
	public Map getParentTree(int state, String id, String rootId) {
		try {
			return TreeUtil.getPdTree(this.findByState(state), rootId, id);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 检测代码是否已存在--存在返回true
	 * @param code 代码
	 * @param parentId ID号
	 * @param type 类型
	 * @return boolean 是否存在
	 */
	public boolean checkHadCode(String code, String parentId, int type) {
		try {
			return wlCmSecurityDao.checkHadCode(code, parentId, type);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 
	 * @param ids 权限id，格式为('1','2','3')
	 * @return 权限列表
	 */
	public List findListByIds(String ids) {
		try {
			return wlCmSecurityDao.findListByIds(ids);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
