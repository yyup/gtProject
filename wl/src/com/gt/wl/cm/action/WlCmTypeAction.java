package com.gt.wl.cm.action;

import java.util.Map;

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

import com.gt.wl.cm.model.WlCmType;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 枚举Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/html/sys/wlCmTypeAction.do")
public class WlCmTypeAction extends BaseAction {

	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmTypeAction.MODULE_NAME");
	private final static String MODULE_CODE = "0303004";// 模块代码
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型字符串数组['TREETYPE','YESORNO']
	 * @param user 当前登录用户
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {
			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 获取权限
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			mapResult.put("state", "1");
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取所有的字典
	 * @param user 当前用户
	 * @param state 状态
	 * @return 树形json
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(int state, User user) {
		try {
			Map map = wlCmTypeService.getTree(state, "0");

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

			Map map = wlCmTypeService.getParentTree(state, id, "0");

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 改变状态
	 * @param ids id
	 * @param state 状态
	 * @param user 用户
	 * @return true false
	 */
	@RequestMapping(params = "action=updateState")
	@ResponseBody
	public String updateState(String ids[], int state, User user) throws BaseException {
		try {
			String logs = "";
			for (String id : ids) {
				WlCmType wlCmType = (WlCmType) wlCmTypeService.getObject(id);

				wlCmType.setState(state);
				wlCmTypeService.updateObject(wlCmType);
				logs += wlCmType.getName() + ";";
			}
			if (state == 0) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmTypeAction.delete"), "[" + logs + "]");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmTypeAction.recover"), "[" + logs + "]");
			}
			return this.getJson(true, "");
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
			WlCmType wlCmType = (WlCmType) wlCmTypeService.getObject(id);
			wlCmType.setParentName(wlCmTypeService.getName(wlCmType.getParentId()));
			return this.getJson(true, wlCmType);
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
	public WlCmType initAttribute(String action, String id) {
		try {
			WlCmType wlCmType = null;
			if ("saveData".equals(action)) {
				if (ValidateUtil.isEmpty(id))
					wlCmType = new WlCmType();
				else
					wlCmType = (WlCmType) wlCmTypeService.getObject(id);
			}
			return wlCmType;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 编辑菜单
	 * @param wlCmType 对象
	 * @param user 当前用户
	 * @return json
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(@ModelAttribute("saveModel") WlCmType wlCmType, User user) {
		try {
			wlCmType.setState(1);
			if (ValidateUtil.isEmpty(wlCmType.getId())) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlCmType.getName() + "]");

			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), "[" + wlCmType.getName() + "]");

			}
			wlCmTypeService.saveObject(wlCmType);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}