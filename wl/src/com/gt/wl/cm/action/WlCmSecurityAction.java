package com.gt.wl.cm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.gt.wl.cm.model.WlCmSecurity;
import com.gt.wl.cm.model.WlCmSecurityUrl;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmSecurityUrlService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 权限Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/html/sys/wlCmSecurityAction.do")
public class WlCmSecurityAction extends BaseAction {
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityUrlService wlCmSecurityUrlService = (WlCmSecurityUrlService) Sc.getBean("wl.cm.WlCmSecurityUrlService");
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmSecurityAction.MODULE_NAME");
	private final static String MODULE_CODE = "0303003";// "SECURITY_SETUP";// 模块代码

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型，多个枚举项以“,”隔开，如“SEX,PARTY”
	 * @param user 当前用户
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
	 * @param state 状态
	 * @return 获取树
	 */
	@RequestMapping(params = "action=getTree")
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

			Map map = wlCmSecurityService.getParentTree(state, id, "0");

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
			WlCmSecurity security = (WlCmSecurity) wlCmSecurityService.getObject(id);
			security.setParentName(wlCmSecurityService.getName(security.getParentId()));
			// 获取权限对应的URL

			List list = wlCmSecurityUrlService.findBySecurityId(id);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("security", security);
			resultMap.put("securityUrl", list);
			return this.getJson(true, resultMap);
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
	public WlCmSecurity initAttribute(String action, String id) {
		try {
			WlCmSecurity wlCmSecurity = null;
			if ("saveData".equals(action)) {
				if (ValidateUtil.isEmpty(id))
					wlCmSecurity = new WlCmSecurity();
				else
					wlCmSecurity = (WlCmSecurity) wlCmSecurityService.getObject(id);
			}
			return wlCmSecurity;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 新增/修改数据
	 * @param wlCmSecurity 权限对象
	 * @param securityUrl url JSON集
	 * @param user 当前登录用户
	 * @return 是否成功
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(@ModelAttribute("saveModel") WlCmSecurity wlCmSecurity, String securityUrl, User user) {
		try {
			if (ValidateUtil.isEmpty(wlCmSecurity.getId())) {
				if (wlCmSecurityService.checkHadCode(wlCmSecurity.getValue(), wlCmSecurity.getParentId(), wlCmSecurity.getType())) {
					return this.getJson(false, Lang.getString("wl.cm.WlCmSecurityAction.codeDuplication"));
				}
			}
			wlCmSecurity.setState(1);
			if (ValidateUtil.isEmpty(wlCmSecurity.getId())) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlCmSecurity.getName() + "]");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), "[" + wlCmSecurity.getName() + "]");

			}
			wlCmSecurityService.saveObject(wlCmSecurity);
			List<WlCmSecurityUrl> wlCmSecurityUrls = JsonUtil.toList(securityUrl, WlCmSecurityUrl.class);

			wlCmSecurityUrlService.saveSecurityUrlList(wlCmSecurity.getId(), wlCmSecurityUrls);

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改状态
	 * @param ids id
	 * @param state 状态
	 * @param user 当前用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=updateState")
	@ResponseBody
	public String updateState(String[] ids, int state, User user) {
		try {

			for (String id : ids) {
				WlCmSecurity wlCmSecurity = (WlCmSecurity) wlCmSecurityService.getObject(id);
				wlCmSecurity.setState(state);
				wlCmSecurityService.updateObject(wlCmSecurity);
			}
			if (state == 0) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmSecurityAction.delete"), "ID[" + JsonUtil.toString(ids) + "]");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmSecurityAction.recover"), "ID[" + JsonUtil.toString(ids) + "]");
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}