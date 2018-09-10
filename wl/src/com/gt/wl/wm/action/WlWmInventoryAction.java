package com.gt.wl.wm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.joyone.action.BaseAction;
import org.joyone.action.ParaMap;
import org.joyone.excel.ExcelExportUtil;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmStorageAreaService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.model.WlWmInventoryDtl;
import com.gt.wl.wm.service.WlWmInventoryService;

@Controller
@RequestMapping("/wl/wm/wlWmInventoryAction.do")
public class WlWmInventoryAction extends BaseAction {
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmStorageAreaService wlCmStorageAreaService = (WlCmStorageAreaService) Sc.getBean("wl.cm.WlCmStorageAreaService");

	private final static String MODULE_CODE = "0305008";// 模块代码

	// private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");

	// private final static String MODULE_NAME = Lang.getString("wl.cm.wlCmStorageAction.MODULE_NAME");

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
			mapResult.put("allStorageEnum", wlCmStorageService.getObjects());
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 搜索库存物料
	 * @param pageSize 页大小
	 * @param currPage 当前页数
	 * @param paraMap 条件
	 * @return 物料列表
	 */
	@RequestMapping(params = "action=search")
	@ResponseBody
	public String search(int pageSize, int currPage, ParaMap paraMap) {
		try {
			Map map = paraMap.getMap();
			Page page = wlWmInventoryService.searchInventory(currPage, pageSize, map);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 统计库存数量
	 * @param paraMap 条件
	 * @return 库存数量
	 */
	@RequestMapping(params = "action=getTotalQty")
	@ResponseBody
	public String getTotalQty(ParaMap paraMap) {
		try {
			Map map = paraMap.getMap();
			return this.getJson(true, wlWmInventoryService.getTotalQty(map));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询库存物料数据
	 * @param storageId 仓库ID
	 * @param storageAreaId 库区ID
	 * @param content 查询内容
	 * @param response 响应
	 * @return
	 */
	@RequestMapping(params = "action=exportDataList")
	@ResponseBody
	public void exportDataList(String storageId, String storageAreaId, String content, HttpServletResponse response) {
		try {
			// 前端加码，后端解码
			if (!ValidateUtil.isEmpty(content)) {
				content = java.net.URLDecoder.decode(content, "utf-8");
			}
			// 得到列表数据
			List<Map> list = wlWmInventoryService.findInventoryList(storageId, content, storageAreaId);
			// 得到表头数据
			List fieldList = ExcelExportUtil.getHeadFieldList("export/WlWmInventoryQuery.xml");
			// 清空response
			response.reset();
			// 设置response的Header
			// 相应消息不缓存
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("application/x-msdownload");
			// 下载格式设置
			response.setHeader("Content-disposition", "attachment;filename=data.xls"); // 文件名

			ServletOutputStream out = response.getOutputStream();
			ExcelExportUtil.export(out, fieldList, list);
			out.flush();
			out.close();
			response.flushBuffer();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 导出序列号列表
	 * @param inventoryId 库存id
	 * @param serialNo 序列号
	 * @return 导出序列号列表
	 */
	@RequestMapping(params = "action=exportSerialNoList")
	@ResponseBody
	public void exportSerialNoList(String inventoryId, String serialNo, HttpServletResponse response) {
		try {
			WlWmInventory wlWmInventory = (WlWmInventory) wlWmInventoryService.getObject(inventoryId);
			// 得到列表数据
			List<WlWmInventoryDtl> list = wlWmInventoryService.findSerialNoList(inventoryId, serialNo, "1");
			// 得到表头数据
			List fieldList = ExcelExportUtil.getHeadFieldList("export/WlWmInventory.xml");
			// 清空response
			response.reset();
			// 设置response的Header
			// 相应消息不缓存
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("application/x-msdownload");
			// 下载格式设置
			response.setHeader("Content-disposition", "attachment;filename=" + wlWmInventory.getItemCd() + ".xls"); // 文件名

			ServletOutputStream out = response.getOutputStream();
			ExcelExportUtil.export(out, fieldList, list);
			out.flush();
			out.close();
			response.flushBuffer();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据仓库storageId获取仓库下库区对象
	 * @param storageId 仓库id
	 * @param isEnableFlag 是否启用
	 * @return 对象
	 */
	@RequestMapping(params = "action=findWlCmStorageAreaList")
	@ResponseBody
	public String findWlCmStorageAreaList(String storageId, String isEnableFlag) {
		try {
			Map mapResult = new HashMap();
			// 获取仓库信息对象
			mapResult.put("storageAreaEnum", wlCmStorageAreaService.findWlCmStorageAreaList(storageId, isEnableFlag));
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找序列号列表
	 * @param inventoryId 库存id
	 * @param serialNo 序列号
	 * @return 序列号列表
	 */
	@RequestMapping(params = "action=findSerialNoList")
	@ResponseBody
	public String findSerialNoList(String inventoryId, String serialNo) {
		try {
			return this.getJson(true, wlWmInventoryService.findSerialNoList(inventoryId, serialNo, "1"));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}