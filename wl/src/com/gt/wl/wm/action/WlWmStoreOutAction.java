package com.gt.wl.wm.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
import com.gt.wl.wm.model.WlWmStoreOut;
import com.gt.wl.wm.model.WlWmStoreOutDetl;
import com.gt.wl.wm.service.WlWmStoreOutService;

@Controller
@RequestMapping("/wl/wm/wlWmStoreOutAction.do")
public class WlWmStoreOutAction extends BaseAction {
	private WlWmStoreOutService wlWmStoreOutService = (WlWmStoreOutService) Sc.getBean("wl.wm.WlWmStoreOutService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");

	private final static String MODULE_CODE = "0305004";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.wm.wlWmStoreOutAction.MODULE_NAME");

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
	 * 查询出库单列表
	 * @param billNo 单据编号
	 * @param beginDate 制单开始时间
	 * @param endDate 制单结束时间
	 * @param storageId 仓库id
	 * @param createor 经办人
	 * @param wmOutTypeEk 业务类型
	 * @param orgId 往来单位id
	 * @param lockFlagEk 单据状态
	 * @param response 响应
	 */
	@RequestMapping(params = "action=exportDataList")
	@ResponseBody
	public void exportDataList(String billNo, String beginDate, String endDate, String storageId, String createor, String wmOutTypeEk, String orgId, String lockFlagEk, HttpServletResponse response) {
		try {
			// 前端加码，后端解码
			if (!ValidateUtil.isEmpty(createor)) {
				createor = java.net.URLDecoder.decode(createor, "utf-8");
			}

			// 得到列表数据
			List<Map> list = wlWmStoreOutService.findStoreOutList("wlWmStoreOut", billNo, beginDate, endDate, storageId, createor, wmOutTypeEk, orgId, null, null, null, lockFlagEk, null);
			// 得到业务类型枚举
			List<Type> wmOutTypeEkList = wlCmTypeService.findByCode("WM_OUT_TYPE_EK");
			// 得到单据性质枚举
			List<Type> lockFlagEkList = wlCmTypeService.findByCode("LOCK_FLAG_EK");
			// 循环列表，设置业务类型与单据性质
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (Map map : list) {
				Date outDate = (Date) map.get("outDate");
				map.put("outDate", sdf.format(outDate));
				wmOutTypeEk = map.get("wmOutTypeEk").toString();
				String lockFlagEkValue = map.get("lockFlagEk").toString();
				String storeOutId = map.get("storeOutId").toString();
				for (Type type : wmOutTypeEkList) {
					if (type.getId().equals(wmOutTypeEk)) {
						map.put("wmOutTypeEkName", type.getLable());
						break;
					}
				}
				for (Type type : lockFlagEkList) {
					if (type.getId().equals(lockFlagEkValue)) {
						map.put("lockFlagEkName", type.getLable());
						break;
					}
				}
				map.put("itemName", wlWmStoreOutService.getAllItemName(storeOutId, 0));

			}
			// 得到表头数据
			List fieldList = ExcelExportUtil.getHeadFieldList("export/WlWmStoreOut.xml");
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
	 * 查询出库单列表(分页)
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param paraMap 前端参数
	 * @return 出库单
	 */
	@RequestMapping(params = "action=findStoreOutList")
	@ResponseBody
	public String findStoreOutList(int pageSize, int currPage, ParaMap paraMap) {
		try {
			Map map = paraMap.getMap();
			Page page = wlWmStoreOutService.search("wlWmStoreOut", map, pageSize, currPage);
			List<WlWmStoreOut> list = page.getItems();
			for (WlWmStoreOut wlWmStoreOut : list) {
				wlWmStoreOut.setAllItemName(wlWmStoreOutService.getAllItemName(wlWmStoreOut.getStoreOutId(), 0));
			}
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取出库单和入库单对应的从表数据
	 * @param storeOutId 出库单id
	 * @return 出库单和出库单对应的从表数据
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String storeOutId) {
		try {
			WlWmStoreOut wlWmStoreOut = (WlWmStoreOut) wlWmStoreOutService.getObject(storeOutId);
			List<WlWmStoreOutDetl> list = wlWmStoreOutService.findWlWmStoreOutDetlList(storeOutId, "", "");
			List<WlWmStoreOutDetl> resultList = new ArrayList();
			for (WlWmStoreOutDetl wlWmStoreOutDetl : list) {
				if (wlWmStoreOutDetl.getBaseUnitQty() != 0) {
					resultList.add(wlWmStoreOutDetl);
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("wlWmStoreOut", wlWmStoreOut);
			resultMap.put("wlWmStoreOutDetlList", resultList);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改出库单单据性质
	 * @param storeOutId 出库单id
	 * @param user 当前登录用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateLockFlagState")
	@ResponseBody
	public String updateLockFlagState(String storeOutId, User user) {
		try {
			// 获取入库单对象对象
			WlWmStoreOut wlWmStoreOut = (WlWmStoreOut) wlWmStoreOutService.getObject(storeOutId);
			// 修改使用状态（已锁-LOCK,未锁-NOLOCK)
			if ("LOCK".equals(wlWmStoreOut.getLockFlagEk())) {
				wlWmStoreOut.setLockFlagEk("NOLOCK");
				wlWmStoreOutService.updateObject(wlWmStoreOut);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlWmStoreOut.getBillNo() + Lang.getString("wl.wm.wlWmStoreOutAction.noLock"));
			}
			else if ("NOLOCK".equals(wlWmStoreOut.getLockFlagEk())) {
				wlWmStoreOut.setLockFlagEk("LOCK");
				wlWmStoreOutService.updateObject(wlWmStoreOut);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlWmStoreOut.getBillNo() + Lang.getString("wl.wm.wlWmStoreOutAction.lock"));
			}

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库单以及出库单从表和从表明细列表对象数组
	 * @param storeOutId 出库单id
	 * @return 出库单以及出库单从表和从表明细列表对象数组
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(String storeOutId) {
		try {
			WlWmStoreOut wlWmStoreOut = (WlWmStoreOut) wlWmStoreOutService.getObject(storeOutId);
			List list = wlWmStoreOutService.findWlWmStoreOutDetlAndInfoList(storeOutId);
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
			resultMap.put("wlWmStoreOut", wlWmStoreOut);
			resultMap.put("wlWmStoreOutDetlAndInfo", resultList);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 出库发货的相关信息
	 * @param storeOutIds 出库单id数组
	 * @param request 请求
	 * @return 出库发货的相关信息
	 */
	@RequestMapping(params = "action=findStoreOutAndDetlList")
	@ResponseBody
	public String findStoreOutAndDetlList(String[] storeOutIds, HttpServletRequest request) {
		try {
			String isCountDec = "0";// 是否含有盘亏出库
			List<Map> list = wlWmStoreOutService.findStoreOutAndDetlList(storeOutIds);
			for (Map map : list) {
				String wmOutTypeEk = map.get("wmOutTypeEk").toString();
				if ("COUNT_DEC".equals(wmOutTypeEk)) {
					isCountDec = "1";
					break;
				}
			}
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("wlWmStoreOutDetl", list);// 要打印的列表
			resultMap.put("isCountDec", isCountDec);// 是否含有盘亏出库单
			resultMap.put("basePath", basePath);// 地址

			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过出库单ID获取其下序号列表
	 * @param storeOutId 出库单ID
	 * @param storeOutDetlId 出库单从表ID
	 * @return 序列号列表
	 */
	@RequestMapping(params = "action=findSerialNoList")
	@ResponseBody
	public String findSerialNoList(String storeOutId, String storeOutDetlId) {
		try {
			return this.getJson(true, wlWmStoreOutService.findSerialNoList(storeOutId, storeOutDetlId));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}