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
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.util.Type;
import com.gt.wl.wm.model.WlWmStoreIn;
import com.gt.wl.wm.model.WlWmStoreInDetl;
import com.gt.wl.wm.service.WlWmStoreInService;

@Controller
@RequestMapping("/wl/wm/wlWmStoreInAction.do")
public class WlWmStoreInAction extends BaseAction {
	private WlWmStoreInService wlWmStoreInService = (WlWmStoreInService) Sc.getBean("wl.wm.WlWmStoreInService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private final static String MODULE_CODE = "0305002";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.wm.wlWmStoreInAction.MODULE_NAME");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");

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
	 * 查询入库单列表
	 * @param billNo 单据编号
	 * @param beginDate 制单开始时间
	 * @param endDate 制单结束时间
	 * @param storageId 仓库id
	 * @param createor 经办人
	 * @param wmInTypeEk 业务类型
	 * @param orgId 往来单位名字
	 * @param storageAreaId 库区
	 * @param lockFlagEk 单据状态
	 * @param response 响应
	 */
	@RequestMapping(params = "action=exportDataList")
	@ResponseBody
	public void exportDataList(String billNo, String beginDate, String endDate, String storageId, String createor, String wmInTypeEk, String orgId, String storageAreaId, String lockFlagEk, HttpServletResponse response) {
		try {
			// 前端加码，后端解码
			if (!ValidateUtil.isEmpty(createor)) {
				createor = java.net.URLDecoder.decode(createor, "utf-8");
			}

			// 得到列表数据
			List<Map> list = wlWmStoreInService.findStoreInList("wlWmStoreIn", billNo, beginDate, endDate, storageId, createor, wmInTypeEk, orgId, null, null, null, storageAreaId, lockFlagEk);
			// 得到业务类型枚举
			List<Type> wmInTypeEkList = wlCmTypeService.findByCode("WM_IN_TYPE_EK");
			// 得到单据性质枚举
			List<Type> lockFlagEkList = wlCmTypeService.findByCode("LOCK_FLAG_EK");
			// 循环列表，设置业务类型与单据性质
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (Map map : list) {
				Date inDate = (Date) map.get("inDate");
				map.put("inDate", sdf.format(inDate));
				wmInTypeEk = map.get("wmInTypeEk").toString();
				String lockFlagEkValue = map.get("lockFlagEk").toString();
				String storeInId = map.get("storeInId").toString();
				for (Type type : wmInTypeEkList) {
					if (type.getId().equals(wmInTypeEk)) {
						map.put("wmInTypeEkName", type.getLable());
						break;
					}
				}
				for (Type type : lockFlagEkList) {
					if (type.getId().equals(lockFlagEkValue)) {
						map.put("lockFlagEkName", type.getLable());
						break;
					}
				}
				map.put("itemName", wlWmStoreInService.getAllItemName(storeInId, 0));

			}
			// 得到表头数据
			List fieldList = ExcelExportUtil.getHeadFieldList("export/WlWmStoreIn.xml");
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
			Map map = paraMap.getMap();
			Page page = wlWmStoreInService.search("wlWmStoreIn", map, pageSize, currPage);
			List<WlWmStoreIn> list = page.getItems();
			for (WlWmStoreIn wlWmStoreIn : list) {
				wlWmStoreIn.setAllItemName(wlWmStoreInService.getAllItemName(wlWmStoreIn.getStoreInId(), 0));
			}
			return this.getJson(true, page);
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
	 * 更改入库单单据性质
	 * @param storeInId 入库单id
	 * @param user 当前登录用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateLockFlagState")
	@ResponseBody
	public String updateLockFlagState(String storeInId, User user) {
		try {
			// 获取入库单对象对象
			WlWmStoreIn wlWmStoreIn = (WlWmStoreIn) wlWmStoreInService.getObject(storeInId);
			// 修改使用状态（已锁-LOCK,未锁-NOLOCK)
			if ("LOCK".equals(wlWmStoreIn.getLockFlagEk())) {
				wlWmStoreIn.setLockFlagEk("NOLOCK");
				wlWmStoreInService.updateObject(wlWmStoreIn);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlWmStoreIn.getBillNo() + Lang.getString("wl.wm.wlWmStoreInAction.noLock"));
			}
			else if ("NOLOCK".equals(wlWmStoreIn.getLockFlagEk())) {
				wlWmStoreIn.setLockFlagEk("LOCK");
				wlWmStoreInService.updateObject(wlWmStoreIn);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlWmStoreIn.getBillNo() + Lang.getString("wl.wm.wlWmStoreInAction.lock"));
			}

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 入库单和入库商品列表
	 * @param storeInId 入库单id
	 * @return 入库单和入库商品列表
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(String storeInId) {
		try {
			WlWmStoreIn wlWmStoreIn = (WlWmStoreIn) wlWmStoreInService.getObject(storeInId);
			List list = wlWmStoreInService.findWlWmStoreInDetlAndInfoList(storeInId);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			double total = 0;
			List resultList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Object[] object = (Object[]) list.get(i);
				Map map = new HashMap();
				map.put("sequ", i + 1);
				map.put("itemCd", object[0]);
				map.put("itemName", object[1]);
				map.put("spec", object[2]);
				map.put("baseUnitQty", object[3]);
				map.put("baseUnitName", object[4]);
				map.put("memo", object[5]);
				map.put("serialNo", object[6]);
				total += Double.parseDouble(object[3].toString());
				resultList.add(map);
			}
			resultMap.put("total", total);
			resultMap.put("wlWmStoreIn", wlWmStoreIn);
			resultMap.put("wlWmStoreInDetlAndInfo", resultList);
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

}