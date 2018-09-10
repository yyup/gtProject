package com.gt.wl.cm.action;

import java.util.Date;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmStorageArea;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmStorageAreaService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;

@Controller
@RequestMapping("/wl/cm/wlCmStorageAreaAction.do")
public class WlCmStorageAreaAction extends BaseAction {
	private WlCmStorageAreaService wlCmStorageAreaService = (WlCmStorageAreaService) Sc.getBean("wl.cm.WlCmStorageAreaService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");

	private final static String MODULE_CODE = "0304005";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.wlCmStorageAreaAction.MODULE_NAME");

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型
	 * @param user 当前登录用户
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {
			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 根据模块代码获取权限
			WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			mapResult.put("storageEnum", wlCmStorageService.getObjects());
			mapResult.put("enAbleStorageEnum", wlCmStorageService.findStorageList());
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询仓库信息，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param storageId 仓库id
	 * @param storageAreaName 库区名字
	 * @return 结果
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int pageSize, int currPage, String storageId, String storageAreaName) {

		try {
			Page page = wlCmStorageAreaService.search(pageSize, currPage, storageId, storageAreaName);

			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 增加数据
	 * @param wlCmStorageArea 库区信息对象
	 * @param user 当前登录用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmStorageArea wlCmStorageArea, User user) {
		try {
			if (ValidateUtil.isEmpty(wlCmStorageArea.getStorageAreaId())) {// 新建
				wlCmStorageArea.setCreateTime(new Date());
				wlCmStorageArea.setCreator(user.getName());

				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmStorageArea.getStorageAreaName());
			}
			else {// 编辑
				wlCmStorageArea.setModifyTime(new Date());
				wlCmStorageArea.setModifier(user.getName());
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmStorageArea.getStorageAreaName());
			}

			wlCmStorageAreaService.saveObject(wlCmStorageArea);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改库区信息对象的状态
	 * @param storageAreaId 库区id
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateOrgState")
	@ResponseBody
	public String updateOrgState(String storageAreaId, User user) {
		try {

			WlCmStorageArea wlCmStorageArea = (WlCmStorageArea) wlCmStorageAreaService.getObject(storageAreaId);
			// 修改使用状态（0-禁用,1-启用)
			if ("0".equals(wlCmStorageArea.getIsEnableFlag())) {
				wlCmStorageArea.setIsEnableFlag("1");
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlCmStorageArea.getStorageAreaName() + Lang.getString("wl.cm.wlCmStorageAreaAction.USE"));
			}
			else if ("1".equals(wlCmStorageArea.getIsEnableFlag())) {
				wlCmStorageArea.setIsEnableFlag("0");
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlCmStorageArea.getStorageAreaName() + Lang.getString("wl.cm.wlCmStorageAreaAction.LOCK"));
			}
			wlCmStorageAreaService.saveObject(wlCmStorageArea);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据库区id获取库区对象
	 * @param storageAreaId 库区id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String storageAreaId) {
		try {
			// 获取仓库信息对象
			WlCmStorageArea wlCmStorageArea = (WlCmStorageArea) wlCmStorageAreaService.getObject(storageAreaId);
			return this.getJson(true, wlCmStorageArea);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}