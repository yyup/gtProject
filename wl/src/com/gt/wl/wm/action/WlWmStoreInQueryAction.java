package com.gt.wl.wm.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmOrgService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmStorageAreaService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.util.Type;
import com.gt.wl.wm.model.WlWmStoreIn;
import com.gt.wl.wm.model.WlWmStoreInDetl;
import com.gt.wl.wm.service.WlWmStoreInService;

@Controller
@RequestMapping("/wl/wm/wlWmStoreInQueryAction.do")
public class WlWmStoreInQueryAction extends BaseAction {
	private WlWmStoreInService wlWmStoreInService = (WlWmStoreInService) Sc.getBean("wl.wm.WlWmStoreInService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmStorageAreaService wlCmStorageAreaService = (WlCmStorageAreaService) Sc.getBean("wl.cm.WlCmStorageAreaService");
	private final static String MODULE_CODE = "0305009";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");

	// private final static String MODULE_NAME = Lang.getString("wl.wm.wlWmStoreInQueryAction.MODULE_NAME");

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
			mapResult.put("orgEnum", wlCmOrgService.getObjects());
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库明细列表
	 * @param storageId 仓库id
	 * @param wmInTypeEk 业务类型
	 * @param beginDate 制单开始时间
	 * @param endDate 制单结束时间
	 * @param itemCd 物料编码
	 * @param itemName 物料名称
	 * @param spec 规格型号
	 * @param storageAreaId 库区
	 * @param orgId 往来单位id
	 * @param response 响应
	 */
	@RequestMapping(params = "action=exportDataList")
	@ResponseBody
	public void exportDataList(String storageId, String wmInTypeEk, String beginDate, String endDate, String itemCd, String itemName, String spec, String storageAreaId, String orgId, HttpServletResponse response) {
		try {
			// 前端加码，后端解码
			if (!ValidateUtil.isEmpty(itemName)) {
				itemName = java.net.URLDecoder.decode(itemName, "utf-8");
			}
			if (!ValidateUtil.isEmpty(spec)) {
				spec = java.net.URLDecoder.decode(spec, "utf-8");
			}

			// 得到列表数据
			List<Map> list = wlWmStoreInService.findStoreInList("wlwmStoreInQuery", null, beginDate, endDate, storageId, null, wmInTypeEk, orgId, itemCd, itemName, spec, storageAreaId, null);
			// 得到业务类型枚举
			List<Type> wmInTypeEkList = wlCmTypeService.findByCode("WM_IN_TYPE_EK");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			int sequ = 1;
			// 循环列表，设置业务类型
			for (Map map : list) {
				Date inDate = (Date) map.get("inDate");
				map.put("inDate", sdf.format(inDate));
				map.put("sequ", sequ++);
				wmInTypeEk = map.get("wmInTypeEk").toString();
				for (Type type : wmInTypeEkList) {
					if (type.getId().equals(wmInTypeEk)) {
						map.put("wmInTypeEkName", type.getLable());
						break;
					}
				}
			}
			// 得到表头数据
			List fieldList = ExcelExportUtil.getHeadFieldList("export/WlWmStoreInQuery.xml");
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
	 * 查询入库单列表(分页)
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param paraMap 前端参数
	 * @return 入库单
	 */
	@RequestMapping(params = "action=findStoreInList")
	@ResponseBody
	public String findStoreInList(int pageSize, int currPage, ParaMap paraMap) {
		try {
			Map attrMap = paraMap.getMap();
			Page page = wlWmStoreInService.search("wlwmStoreInQuery", attrMap, pageSize, currPage);
			List<Type> wmInTypeEkList = wlCmTypeService.findByCode("WM_IN_TYPE_EK");
			int sequ = (currPage - 1) * pageSize + 1;
			List<Map> list = page.getItems();
			// 循环列表，设置业务类型
			for (Map map : list) {
				map.put("sequ", sequ++);
				String wmInTypeEk = map.get("wmInTypeEk").toString();
				for (Type type : wmInTypeEkList) {
					if (type.getId().equals(wmInTypeEk)) {
						map.put("wmInTypeEkName", type.getLable());
						break;
					}
				}
			}

			return this.getJson(true, page);
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
	 * 获取入库单和入库单对应的从表数据
	 * @param storeInId 入库单id
	 * @return 入库单和入库单对应的从表数据
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String storeInId) {
		try {
			WlWmStoreIn wlWmStoreIn = (WlWmStoreIn) wlWmStoreInService.getObject(storeInId);
			List<WlWmStoreInDetl> list = wlWmStoreInService.findWlWmStoreInDetlList(storeInId);
			List<WlWmStoreInDetl> resultList = new ArrayList();
			for (WlWmStoreInDetl wlWmStoreInDetl : list) {
				if (wlWmStoreInDetl.getBaseUnitQty() != 0) {
					resultList.add(wlWmStoreInDetl);
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("wlWmStoreIn", wlWmStoreIn);
			resultMap.put("wlWmStoreInDetlList", resultList);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过入库单id获取其下序列号列表
	 * @param storeInId 入库单ID
	 * @param storeInDetlId 入库单从表id
	 * @return 序列号列表
	 */
	@RequestMapping(params = "action=findSerialNoList")
	@ResponseBody
	public String findSerialNoList(String storeInId, String storeInDetlId) {
		try {
			return this.getJson(true, wlWmStoreInService.findSerialNoList(storeInId, storeInDetlId));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 入库总数量
	 * @param paraMap 条件
	 * @return 入库总数量
	 */
	@RequestMapping(params = "action=getInQty")
	@ResponseBody
	public String getInQty(ParaMap paraMap) {
		try {
			Map map = paraMap.getMap();
			return this.getJson(true, wlWmStoreInService.getInQty(map));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}