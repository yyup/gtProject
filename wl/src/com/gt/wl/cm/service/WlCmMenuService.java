package com.gt.wl.cm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.User;
import org.joyone.util.TreeUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmMenuDao;
import com.gt.wl.cm.model.WlCmMenu;

/**
 * 菜单Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmMenuService")
public class WlCmMenuService extends BaseService {
	private WlCmMenuDao wlCmMenuDao = (WlCmMenuDao) Sc.getBean("wl.cm.WlCmMenuDao");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");

	public WlCmMenuService() {
		baseDao = wlCmMenuDao;
	}

	/**
	 * 返回Tree 对象
	 * @param state 状态
	 * @param rootId 根节点ID
	 * @return map树对象
	 */
	public Map getTree(int state, String rootId) {
		try {
			return TreeUtil.getPdTree(this.findAllByState(state), rootId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 返回过滤Tree 对象
	 * @param state 状态
	 * @param id 过滤Id
	 * @param rootId 根节点ID
	 * @return map树对象
	 */
	public Map getParentTree(int state, String id, String rootId) {
		try {
			return TreeUtil.getPdTree(this.findAllByState(state), rootId, id);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 获取菜单名称
	 * @param id 菜单ID
	 * @return 菜单名称
	 */
	public String getName(String id) {
		try {
			if (id == null) {
				return "";
			}
			WlCmMenu object = (WlCmMenu) this.getObject(id);

			return object == null ? "" : object.getName();

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据状态返回对象列表
	 * @param state 状态
	 * @return 返回菜单列表
	 */
	public List findAllByState(int state) {
		try {
			return wlCmMenuDao.findAllByState(state);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 生成树型菜单
	 * @param user 用户对象
	 * @param target 目标代码
	 * @return 树状菜单
	 */
	public Map createMenu(User user, String target) {
		try {
			Map baseMenuMap = TreeUtil.getPdTree(wlCmMenuDao.findAllByStateAndTarget(1, target), "0");
			Map map = getChildMenu(baseMenuMap, user);
			return map;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 生成企业用户菜单
	 * @param user 用户对象
	 * @param target 目标代码
	 * @return 菜单map对象
	 */
	public Map createCorpMenu(User user, String target) {
		try {
			Map baseMenuMap = TreeUtil.getPdTree(wlCmMenuDao.findAllByStateAndTarget(1, target), "0");
			Map map = getCorpChildMenu(baseMenuMap, user);
			return map;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取企业用户子节点的菜单Map对象
	 * @param baseMenuMap 节点Map对象
	 * @return Map 对象
	 */
	private Map getCorpChildMenu(Map baseMenuMap, User user) {
		try {
			Map resultMap = new HashMap();//
			WlCmMenu wlCmMenu = (WlCmMenu) baseMenuMap.get("main");
			if (wlCmMenu == null) {
				return null;
			}

			if ("root".equals(wlCmMenu.getTarget())) {
				List<Map> childbaseMenuList = (List<Map>) baseMenuMap.get("items");
				if (childbaseMenuList != null) {

					List items = new ArrayList();
					for (Map childMenuMap : childbaseMenuList) {
						Map map = getCorpChildMenu(childMenuMap, user);
						if (map != null) {
							items.add(map);
						}
					}
					resultMap.put("items", items);
					return resultMap;
				}
			}
			else {
				resultMap.put("main", wlCmMenu);
				List<Map> childbaseMenuList = (List<Map>) baseMenuMap.get("items");
				if (childbaseMenuList != null) {

					List items = new ArrayList();
					for (Map childMenuMap : childbaseMenuList) {
						Map map = getCorpChildMenu(childMenuMap, user);
						if (map != null) {
							items.add(map);
						}
					}
					resultMap.put("items", items);
				}
				return resultMap;
			}
			return null;

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取子节点的菜单Map对象
	 * @param baseMenuMap 节点Map对象
	 * @return Map 对象
	 */
	private Map getChildMenu(Map baseMenuMap, User user) {
		try {
			Map resultMap = new HashMap();

			WlCmMenu wlCmMenu = (WlCmMenu) baseMenuMap.get("main");
			if (wlCmMenu == null) {
				return null;
			}
			if ("root".equals(wlCmMenu.getTarget())) {
				List<Map> childbaseMenuList = (List<Map>) baseMenuMap.get("items");
				if (childbaseMenuList != null) {
					List items = new ArrayList();
					for (Map childMenuMap : childbaseMenuList) {
						Map map = getChildMenu(childMenuMap, user);
						if (!ValidateUtil.isEmpty(map)) {
							items.add(map);
						}
					}
					resultMap.put("items", items);
					return resultMap;
				}
			}
			else {

				// 权限检测
				boolean isAccess = wlCmSecurityService.checkFunction(user, wlCmMenu.getSecurityId());
				if (isAccess || user.isAdmin() || user.isProgrammer()) {

					List<Map> childbaseMenuList = (List<Map>) baseMenuMap.get("items");
					if (!ValidateUtil.isEmpty(childbaseMenuList)) {

						List items = new ArrayList();
						for (Map childMenuMap : childbaseMenuList) {
							Map map = getChildMenu(childMenuMap, user);
							if (!ValidateUtil.isEmpty(map)) {
								items.add(map);
							}
						}
						if (items.size() > 0) {// 菜单项的子菜单不为空时才显示
							resultMap.put("items", items);
							resultMap.put("main", wlCmMenu);
						}
						else {
							if (!"#".equals(wlCmMenu.getValue())) {
								resultMap.put("main", wlCmMenu);
							}
						}
					}
					else {
						resultMap.put("main", wlCmMenu);
					}
					return resultMap;
				}
			}
			return null;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
