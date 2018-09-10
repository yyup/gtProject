package com.gt.wl.cm.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.User;
import org.joyone.util.JsonUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmMenu;
import com.gt.wl.cm.service.WlCmMenuService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 菜单Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/html/sys/wlCmMenuAction.do", "/html/sys/wlCmMenuAction.agency" })
public class WlCmMenuAction extends BaseAction {

	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmMenuAction.MODULE_NAME");
	private final static String MODULE_CODE = "0303008";// 模块代码

	private WlCmMenuService wlCmMenuService = (WlCmMenuService) Sc.getBean("wl.cm.WlCmMenuService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型数组
	 * @param user 当前登录用户
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {

			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 根据模块代码获取权限
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取所有的部门
	 * @param state 状态
	 * @param user 当前用户
	 * @return 部门的树形json
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(int state, User user) {
		try {
			Map map = wlCmMenuService.getTree(state, "0");

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取除了当前节点及子节点的树
	 * @param state 状态
	 * @param id id
	 * @param user 当前用户
	 * @return 部门的树形json
	 */
	@RequestMapping(params = "action=getParentTree")
	@ResponseBody
	public String getParentTree(int state, String id, User user) {
		try {

			Map map = wlCmMenuService.getParentTree(state, id, "0");

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据ID获取对象
	 * @param id id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String id) {
		try {
			WlCmMenu menu = (WlCmMenu) wlCmMenuService.getObject(id);
			menu.setParentName(wlCmMenuService.getName(menu.getParentId()));
			menu.setSecurityName(wlCmSecurityService.getName(menu.getSecurityId()));
			return this.getJson(true, menu);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 初始化对象属性
	 * @param action 操作名称
	 * @param id ID号
	 * @return 对易用
	 */
	@ModelAttribute("saveModel")
	public WlCmMenu initAttribute(String action, String id) {
		try {
			WlCmMenu wlCmMenu = null;
			if ("saveData".equals(action)) {
				if (ValidateUtil.isEmpty(id))
					wlCmMenu = new WlCmMenu();
				else
					wlCmMenu = (WlCmMenu) wlCmMenuService.getObject(id);
			}
			return wlCmMenu;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 编辑菜单
	 * @param wlCmMenu 对象
	 * @param user 当前登录用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(@ModelAttribute("saveModel") WlCmMenu wlCmMenu, User user) {
		try {
			wlCmMenu.setState(1);
			if (ValidateUtil.isEmpty(wlCmMenu.getId())) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), Lang.getString("wl.cm.WlCmMenuAction.name") + "[" + wlCmMenu.getName() + "]");

			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), Lang.getString("wl.cm.WlCmMenuAction.name") + "[" + wlCmMenu.getName() + "]");

			}
			wlCmMenuService.saveObject(wlCmMenu);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据系统用户创建菜单
	 * @param request 请求
	 * @param user 当前用户
	 * @param response 响应
	 * @param target 目标代码
	 * @return 树形json
	 */
	@RequestMapping(params = "action=createMenu")
	@ResponseBody
	public String createMenu(HttpServletRequest request, User user, HttpServletResponse response, String target) {
		try {

			return this.getJson(true, wlCmMenuService.createMenu(user, target));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据经销商用户创建菜单
	 * @param request 请求
	 * @param user 当前用户
	 * @param response 响应
	 * @param target 目标代码
	 * @return 树形json
	 */
	@RequestMapping(params = "action=createCorpMenu")
	@ResponseBody
	public String createCorpMenu(HttpServletRequest request, User user, HttpServletResponse response, String target) {
		try {

			return this.getJson(true, wlCmMenuService.createCorpMenu(user, target));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改状态
	 * @param request 请求
	 * @param response 响应
	 * @param ids id
	 * @param state 状态
	 * @param user 当前登录
	 * @return 结果
	 */
	@RequestMapping(params = "action=updateState")
	@ResponseBody
	public String updateState(HttpServletRequest request, HttpServletResponse response, String[] ids, int state, User user) {
		try {
			for (String id : ids) {
				WlCmMenu basemenu = (WlCmMenu) wlCmMenuService.getObject(id);
				basemenu.setState(state);
				wlCmMenuService.updateObject(basemenu);
			}
			if (state == 0) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmMenuAction.delete"), "ID[" + JsonUtil.toString(ids) + "]");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmMenuAction.recover"), "ID[" + JsonUtil.toString(ids) + "]");
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}