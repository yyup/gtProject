package com.gt.wl.es.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.action.ParaMap;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.model.WlEsCustRepair;
import com.gt.wl.es.model.WlEsStoreOut;
import com.gt.wl.es.service.WlEsCustRepairService;
import com.gt.wl.es.service.WlEsStoreOutService;

/**
 * 售后维修申请Action层
 * @author huangbj
 * 
 */
@Controller
@RequestMapping("/wl/es/wlEsRepairApplyAction.do")
public class WlEsRepairApplyAction extends BaseAction {
	private WlEsCustRepairService wlEsCustRepairService = (WlEsCustRepairService) Sc.getBean("wl.es.WlEsCustRepairService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlEsStoreOutService wlEsStoreOutService = (WlEsStoreOutService) Sc.getBean("wl.es.WlEsStoreOutService");
	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.wlEsCustRepairAction.MODULE_NAME");
	private final static String MODULE_CODE = "0302009";// 模块代码

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
	 * 查询售后维修信息(分页)
	 * @param paraMap 前端参数
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return 售后维修信息
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(ParaMap paraMap, int currPage, int pageSize) {
		try {
			Map pMap = paraMap.getMap();
			Page page = wlEsCustRepairService.search(pMap, currPage, pageSize);
			List<WlEsCustRepair> list = page.getItems();
			for (WlEsCustRepair wlEsCustRepair : list) {
				WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(wlEsCustRepair.getDeviceCd());
				if (!ValidateUtil.isNull(wlEsStoreOut)) {
					wlEsCustRepair.setSpec(wlEsStoreOut.getProductName());
					wlEsCustRepair.setOutDate(wlEsStoreOut.getDeliveryDate());
					wlEsCustRepair.setAgency(wlEsStoreOut.getAgency());
				}
			}
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存售后微信
	 * @param wlEsCustRepair 售后维修申请对象
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlEsCustRepair wlEsCustRepair, String[] picPathList, String[] videoPathList, User user) {
		try {
			WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(wlEsCustRepair.getDeviceCd());
			if (ValidateUtil.isNull(wlEsStoreOut)) {// 序列号登记表找不到记录
				return this.getJson(true, Lang.getString("wl.es.wlEsRepairApplyAction.noDeviceCd"));
			}
			if (ValidateUtil.isEmpty(wlEsCustRepair.getRepairId())) {// 新增
				wlEsCustRepair.setCreator(user.getName());
				wlEsCustRepair.setCreateTime(new Date());
				wlEsCustRepair.setSerialVerfEk("3");
				wlEsCustRepair.setBillSourceEk("WL");
				wlEsCustRepair.setName(user.getName());
				wlEsCustRepair.setRepairNo(wlEsCustRepairService.getNewCustRepairNO());
			}
			else {// 编辑
				wlEsCustRepair.setModifier(user.getName());
				wlEsCustRepair.setModifyTime(new Date());
			}
			if (!ValidateUtil.isEmpty(wlEsCustRepair.getAddr())) {
				String addr = wlEsCustRepair.getAddr();
				addr = addr.replaceAll("\\t", "");// 去掉tab键
				wlEsCustRepair.setAddr(addr);
			}
			wlEsCustRepairService.saveCustRepair(wlEsCustRepair, picPathList, videoPathList);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存处理结果
	 * @param wlEsCustRepair
	 * @return 空
	 */
	@RequestMapping(params = "action=savaDealData")
	@ResponseBody
	public String savaDealData(WlEsCustRepair wlEsCustRepair) {
		try {
			wlEsCustRepairService.saveObject(wlEsCustRepair);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取售后维修申请对象
	 * @param repairId 维修申请id
	 * @return 售后维修对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String repairId) {
		try {
			WlEsCustRepair wlEsCustRepair = wlEsCustRepairService.getCustRepair(repairId);
			WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(wlEsCustRepair.getDeviceCd());
			if (!ValidateUtil.isNull(wlEsStoreOut)) {
				wlEsCustRepair.setSpec(wlEsStoreOut.getProductName());
				wlEsCustRepair.setOutDate(wlEsStoreOut.getDeliveryDate());
				wlEsCustRepair.setAgency(wlEsStoreOut.getAgency());
			}
			return this.getJson(true, wlEsCustRepair);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 序列号登记信息
	 * @param deviceCd 设备编码即序列号
	 * @return 序列号登记信息
	 */
	@RequestMapping(params = "action=getStoreOut")
	@ResponseBody
	public String getStoreOut(String deviceCd) {
		try {
			WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(deviceCd);
			if (!ValidateUtil.isNull(wlEsStoreOut)) {
				Map map = new HashMap();
				map.put("addr", wlEsStoreOut.getAddr());
				map.put("spec", wlEsStoreOut.getProductName());
				map.put("mobile", wlEsStoreOut.getContact());
				map.put("receiver", wlEsStoreOut.getReceiver());
				return this.getJson(true, map);
			}
			else {
				return this.getJson(true, "");
			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 提交到未处理状态
	 * @param repairIds 维修申请id
	 * @return 空
	 */
	@RequestMapping(params = "action=updateSubmit")
	@ResponseBody
	public String updateSubmit(String[] repairIds) {
		try {
			wlEsCustRepairService.updateSubmit(repairIds);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 撤回到未提交状态
	 * @param repairIds 维修申请id
	 * @return 空
	 */
	@RequestMapping(params = "action=updateRetract")
	@ResponseBody
	public String updateRetract(String[] repairIds) {
		try {
			wlEsCustRepairService.updateRetract(repairIds);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}