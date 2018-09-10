package com.gt.wl.cm.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmDutyrole;
import com.gt.wl.cm.service.WlCmDutyroleService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.cm.service.WlCmUserSecurityService;

/**
 * 角色Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/html/sys/wlCmDutyroleAction.do")
public class WlCmDutyroleAction extends BaseAction {
	private WlCmDutyroleService wlCmDutyroleService = (WlCmDutyroleService) Sc.getBean("wl.cm.WlCmDutyroleService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmUserSecurityService wlCmUserSecurityService = (WlCmUserSecurityService) Sc.getBean("wl.cm.WlCmUserSecurityService");
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmDutyroleAction.MODULE_NAME");
	private final static String MODULE_CODE = "0303002";// 模块代码

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型，多个枚举项以“,”隔开，如“SEX,PARTY”
	 * @param user 当前登录用户
	 * @param code 类型
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user, String code) {
		try {

			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 根据模块代码获取权限
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据

			mapResult.put("state", 1);
			mapResult.put("code", code);
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回翻页对象
	 * @param user 当前用户
	 * @param pageSize 页大小
	 * @param currPage 当前页
	 * @param state 状态
	 * @param code 类型 DUTY ROLE GROUP
	 * @param name 名称
	 * @return 翻页对象
	 */
	@RequestMapping(params = "action=findPage")
	@ResponseBody
	public String findPage(User user, int pageSize, int currPage, int state, String code, String name) {
		try {

			Page page = wlCmDutyroleService.search(currPage, pageSize, state, code, name);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回GROUP对象
	 * @param user 当前用户
	 * @return GROUP对象
	 */
	@RequestMapping(params = "action=findGroup")
	@ResponseBody
	public String findGroup(User user) {
		try {

			List result = wlCmDutyroleService.findByCodeAndState("GROUP", 1);
			return this.getJson(true, result);
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
			return this.getJson(true, wlCmDutyroleService.getObject(id));
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
	public WlCmDutyrole initAttribute(String action, String id) {
		try {
			WlCmDutyrole wlCmDutyrole = null;
			if ("saveData".equals(action)) {
				if (ValidateUtil.isEmpty(id))
					wlCmDutyrole = new WlCmDutyrole();
				else
					wlCmDutyrole = (WlCmDutyrole) wlCmDutyroleService.getObject(id);
			}
			return wlCmDutyrole;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 添加/修改角色职务
	 * @param wlCmDutyrole 对象
	 * @param user 当前登录用户
	 * @return 成功/失败
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String addData(@ModelAttribute("saveModel") WlCmDutyrole wlCmDutyrole, User user) {
		try {

			List list = wlCmDutyroleService.findByName(wlCmDutyrole.getName());
			if (ValidateUtil.isEmpty(wlCmDutyrole.getId())) {
				if (list.size() > 0) {
					return this.getJson(false, Lang.getString("wl.cm.WlCmDutyroleAction.nameuplication"));
				}
			}
			else {
				WlCmDutyrole oldDutyrole = (WlCmDutyrole) wlCmDutyroleService.getObject(wlCmDutyrole.getId());
				if (!oldDutyrole.getName().equals(wlCmDutyrole.getName())) {
					if (list.size() > 0) {
						return this.getJson(false, Lang.getString("wl.cm.WlCmDutyroleAction.nameuplication"));
					}
				}

			}
			if (ValidateUtil.isEmpty(wlCmDutyrole.getId())) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmDutyrole.getCode());

			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmDutyrole.getCode());

			}
			wlCmDutyroleService.saveObject(wlCmDutyrole);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 改变状态
	 * @param ids id
	 * @param state 状态
	 * @param request 请求
	 * @param user 当前登录用户
	 * @return true or false
	 */
	@RequestMapping(params = "action=updateState")
	@ResponseBody
	public String updateState(String ids[], int state, HttpServletRequest request, User user) throws BaseException {
		try {
			String logs = "";
			for (String id : ids) {
				WlCmDutyrole wlCmDutyrole = (WlCmDutyrole) wlCmDutyroleService.getObject(id);

				wlCmDutyrole.setState(state);
				wlCmDutyroleService.updateObject(wlCmDutyrole);
				logs += wlCmDutyrole.getName() + ";";
			}
			if (state == 0) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmDutyroleAction.delete"), "[" + logs + "]");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmDutyroleAction.recover"), "[" + logs + "]");

			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 角色所拥有的权限树
	 * @param id 角色id
	 * @return 角色所拥有的权限树
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(String id) {
		try {
			return this.getJson(true, wlCmDutyroleService.getTree(id));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取权限树
	 * @param state 状态
	 * @return 权限树
	 */
	@RequestMapping(params = "action=getSecurityTree")
	@ResponseBody
	public String getTree(int state) {
		try {
			Map map = wlCmSecurityService.getTree(state, "0");
			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询角色拥有的权限列表
	 * @param ownerId 权限id
	 * @return 角色拥有的权限列表
	 */
	@RequestMapping(params = "action=findListByOwnerId")
	@ResponseBody
	public String findListByOwnerId(String ownerId) {
		try {
			return this.getJson(true, wlCmUserSecurityService.findListByOwnerId(ownerId));

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存权限
	 * @param securitys 权限,用逗号隔开
	 * @param ownerId 拥有者
	 * @param departmentId 组织机构ID
	 * @param type 类型
	 * @param user 当前用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveUserSecuriy")
	@ResponseBody
	public String saveUserSecuriy(String securitys, String ownerId, String departmentId, int type) {
		try {
			wlCmUserSecurityService.saveUserSecuriy(securitys, ownerId, departmentId, type);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}