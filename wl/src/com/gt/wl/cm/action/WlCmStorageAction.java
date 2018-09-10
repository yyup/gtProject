package com.gt.wl.cm.action;

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

import com.gt.wl.cm.model.WlCmStorage;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;

@Controller
@RequestMapping("/wl/cm/wlCmStorageAction.do")
public class WlCmStorageAction extends BaseAction {
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");

	private final static String MODULE_CODE = "0304002";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.wlCmStorageAction.MODULE_NAME");

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
	 * @param content 要查询的内容编号或者名称
	 * @return 结果
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int pageSize, int currPage, String content) {

		try {
			Page page = wlCmStorageService.search(pageSize, currPage, content);
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 增加数据
	 * @param wlCmStorage 仓库信息对象
	 * @param user 当前登录用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmStorage wlCmStorage, User user) {
		try {
			if (ValidateUtil.isEmpty(wlCmStorage.getStorageId())) {// 新建

				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmStorage.getStorageName());
			}
			else {// 编辑
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmStorage.getStorageName());
			}

			wlCmStorageService.saveData(wlCmStorage, user);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改仓库信息对象的状态
	 * @param storageId 仓库信息id
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateOrgState")
	@ResponseBody
	public String updateOrgState(String storageId, User user) {
		try {

			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageService.getObject(storageId);
			wlCmStorageService.updateOrgState(wlCmStorage);
			// 修改使用状态（0-禁用,1-启用)
			if ("0".equals(wlCmStorage.getIsEnableFlag())) {

				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlCmStorage.getStorageCd() + Lang.getString("wl.cm.wlCmStorageAction.USE"));
			}
			else if ("1".equals(wlCmStorage.getIsEnableFlag())) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlCmStorage.getStorageCd() + Lang.getString("wl.cm.wlCmStorageAction.LOCK"));
			}

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据单位id获取仓库对象
	 * @param storageId 仓库id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String storageId) {
		try {
			// 获取仓库信息对象
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageService.getObject(storageId);
			return this.getJson(true, wlCmStorage);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 得到指定编号仓库
	 * @param storageCd 仓库编号
	 * @param storageId 仓库id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getStorageByStorageCd")
	@ResponseBody
	public String getStorageByStorageCd(String storageCd, String storageId) {
		try {
			// 获取仓库信息对象
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageService.getStorageByStorageCd(storageCd, storageId);
			return this.getJson(true, wlCmStorage);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取进货仓库
	 * @param storageId 仓库id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getDefaultInStorage")
	@ResponseBody
	public String getDefaultInStorage(String storageId) {
		try {
			// 获取进货仓库对象
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageService.getDefaultInStorage(storageId);
			return this.getJson(true, wlCmStorage);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取出库仓库
	 * @param storageId 仓库id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getDefaultOutStorage")
	@ResponseBody
	public String getDefaultOutStorage(String storageId) {
		try {
			// 获取出库仓库对象
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageService.getDefaultOutStorage(storageId);
			return this.getJson(true, wlCmStorage);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}