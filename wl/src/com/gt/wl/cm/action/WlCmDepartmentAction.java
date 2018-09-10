package com.gt.wl.cm.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmDepartment;
import com.gt.wl.cm.service.WlCmDepartmentService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

@Controller
@RequestMapping("/html/sys/wlCmDepartmentAction.do")
public class WlCmDepartmentAction extends BaseAction {
	private WlCmDepartmentService wlCmDepartmentService = (WlCmDepartmentService) Sc.getBean("wl.cm.WlCmDepartmentService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmDepartmentAction.MODULE_NAME");
	private final static String MODULE_CODE = "DEPARTMENT";// 模块代码

	/**
	 * 获取所有的部门
	 * @param user 当前用户
	 * @param state 状态
	 * @return 部门的树形json
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(int state, User user) {
		try {
			Map map = wlCmDepartmentService.getTree(state, "0");

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取除了当前节点及子节点的树
	 * @param user 当前用户
	 * @param id 当前节点ID
	 * @param state 状态
	 * @return 部门的树形json
	 */
	@RequestMapping(params = "action=getParentTree")
	@ResponseBody
	public String getParentTree(int state, String id, User user) {
		try {

			Map map = wlCmDepartmentService.getParentTree(state, id, "0");

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型，多个枚举项以“,”隔开，如“SEX,PARTY”
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
	 * 根据ID获取对象
	 * @param id id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String id) {
		try {
			WlCmDepartment dep = (WlCmDepartment) wlCmDepartmentService.getObject(id);
			dep.setParentName(wlCmDepartmentService.getName(dep.getParentId()));
			return this.getJson(true, dep);
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
	public WlCmDepartment initAttribute(String action, String id) {
		try {
			WlCmDepartment wlCmDepartment = null;
			if ("saveData".equals(action)) {
				if (ValidateUtil.isEmpty(id))
					wlCmDepartment = new WlCmDepartment();
				else
					wlCmDepartment = (WlCmDepartment) wlCmDepartmentService.getObject(id);
			}
			return wlCmDepartment;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 编辑部门
	 * @param user 当前用户
	 * @param wlCmDepartment 部门
	 * @return 成功或失败
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(@ModelAttribute("saveModel") WlCmDepartment wlCmDepartment, User user) {
		try {
			wlCmDepartment.setState(1);
			if (ValidateUtil.isEmpty(wlCmDepartment.getId())) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), Lang.getString("wl.cm.WlCmDepartmentAction.name") + "[" + wlCmDepartment.getName() + "]");

			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), Lang.getString("wl.cm.WlCmDepartmentAction.name") + "[" + wlCmDepartment.getName() + "]");

			}
			wlCmDepartmentService.saveObject(wlCmDepartment);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 改变状态
	 * @param ids ids数组
	 * @param state 状态
	 * @param request 请求
	 * @param user 当前登录用户
	 * @return 成功或失败
	 */
	@RequestMapping(params = "action=updateState")
	@ResponseBody
	public String updateState(String ids[], int state, HttpServletRequest request, User user) {
		try {
			String logs = "";
			for (String id : ids) {
				WlCmDepartment wlCmDepartment = (WlCmDepartment) wlCmDepartmentService.getObject(id);
				wlCmDepartment.setState(state);
				wlCmDepartmentService.updateObject(wlCmDepartment);
				logs += wlCmDepartment.getName() + ";";
			}
			if (state == 0) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmDepartmentAction.delete"), "[" + logs + "]");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmDepartmentAction.recover"), "[" + logs + "]");

			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}