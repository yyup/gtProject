package com.gt.wl.cm.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.model.EnumModel;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.Session;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmInfo;
import com.gt.wl.cm.service.WlCmColumnService;
import com.gt.wl.cm.service.WlCmInfoService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 回收站action层
 * @author yanges
 * 
 */
@Controller
@RequestMapping("/wl/cm/wlCmRecycleBinAction.do")
public class WlCmRecycleBinAction extends BaseAction {
	private final static String MODULE_CODE = "0301006";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmRecycleBinAction.MODULE_NAME");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmInfoService wlCmInfoService = (WlCmInfoService) Sc.getBean("wl.cm.WlCmInfoService");// 信息发布
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");// 数据字典
	private WlCmColumnService wlCmColumnService = (WlCmColumnService) Sc.getBean("wl.cm.WlCmColumnService");

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes) {
		try {
			// 根据模块代码获取权限
			Map mapResult = wlCmSecurityService.getSmcMap(Session.getCurrUser(), MODULE_CODE);
			// 到字典表获取枚举数据
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));

			List<EnumModel> enumList = new ArrayList();
			enumList = wlCmColumnService.getObjects();
			mapResult.put("columnNameEnum", enumList);
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询信息发布管理
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param columnId 信息栏目id
	 * @param title 标题
	 * @param issueStateEk 发布状态
	 * @return 结果
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int pageSize, int currPage, String columnId, String title, String issueStateEk) {
		try {
			Page page = wlCmInfoService.search(pageSize, currPage, columnId, title, issueStateEk, "0", "", "","");
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 根据ID获取对象
	 * @param infoId 发布信息编号
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String infoId) {
		try {
			WlCmInfo cmInfo = (WlCmInfo) wlCmInfoService.getObject(infoId);
			return this.getJson(true, cmInfo);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 删除选中数据
	 * @param ids 选中id数组
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String[] ids) {
		String wlCmInfoNames = "";
		try {
			for (int i = 0; i < ids.length; i++) {
				// 获取类型的名称
				WlCmInfo wlCmInfo = (WlCmInfo) wlCmInfoService.getObject(ids[i]);
				wlCmInfoNames += wlCmInfo.getTitle() + ",";
			}
			// 删除类型
			wlCmInfoService.deletePhysicalWlCmInfos(ids);
			// 设置日志
			if (!ValidateUtil.isEmpty(wlCmInfoNames)) {
				wlCmInfoNames = wlCmInfoNames.substring(0, wlCmInfoNames.length() - 1);
				this.addLog(Session.getCurrUser(), SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), wlCmInfoNames);
			}

			return this.getJson(true, "");

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 恢复信息发布的使用状态
	 * @param ids 信息的id数组
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=recoverData")
	@ResponseBody
	public String recoverData(String[] ids) {
		try {
			wlCmInfoService.updateWlCmInfosDataState(ids, "1");
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}