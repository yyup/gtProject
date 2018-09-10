package com.gt.wl.cm.action;

import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmCommonSet;
import com.gt.wl.cm.service.WlCmCommonSetService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 参数设置Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/wl/cm/wlCmCommonSetAction.do")
public class WlCmCommonSetAction extends BaseAction {
	private WlCmCommonSetService wlCmCommonSetService = (WlCmCommonSetService) Sc.getBean("wl.cm.WlCmCommonSetService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.wlCmCommonSetAction.MODULE_NAME");
	private final static String MODULE_CODE = "0303007";// 模块代码

	/**
	 * 界面初始化
	 * @param enumTypes 枚举
	 * @param user 用户
	 * @return 枚举数据
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {
			Map<String, Object> mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			return this.getJson(true, mapResult);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询参数设置信息（分页）
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return 参数信息
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int currPage, int pageSize) {
		try {
			Page page = wlCmCommonSetService.findPage(currPage, pageSize);
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 保存数据
	 * @param wlCmCommonSet 参数设置对象
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmCommonSet wlCmCommonSet) {
		try {
			wlCmCommonSetService.updateObject(wlCmCommonSet);
			this.addLog(Session.getCurrUser(), SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmCommonSet.getName() + Lang.getString("wl.cm.wlCmCommonSetAction.updateData"));
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取详细信息
	 * @param commonSetId 参数设置ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String commonSetId) {
		try {
			WlCmCommonSet wlCmCommonSet = (WlCmCommonSet) wlCmCommonSetService.getObject(commonSetId);
			return this.getJson(true, wlCmCommonSet);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

}