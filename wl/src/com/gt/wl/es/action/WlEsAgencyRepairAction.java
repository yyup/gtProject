package com.gt.wl.es.action;

import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.action.ParaMap;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmOrg;
import com.gt.wl.cm.model.WlCmOrgUser;
import com.gt.wl.cm.service.WlCmAreaService;
import com.gt.wl.cm.service.WlCmOrgService;
import com.gt.wl.cm.service.WlCmOrgUserService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.model.WlEsCustRepair;
import com.gt.wl.es.model.WlEsCustRepairSug;
import com.gt.wl.es.model.WlEsStoreOut;
import com.gt.wl.es.service.WlEsCustRepairService;
import com.gt.wl.es.service.WlEsStoreOutService;
import com.gt.wl.es.service.WlEsTodoMsgService;
import com.gt.wl.util.Type;

/**
 * 售后维修Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/wl/es/WlEsAgencyRepairAction.agency")
public class WlEsAgencyRepairAction extends BaseAction {
	private WlEsCustRepairService wlEsCustRepairService = (WlEsCustRepairService) Sc.getBean("wl.es.WlEsCustRepairService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmAreaService wlCmAreaService = (WlCmAreaService) Sc.getBean("wl.cm.WlCmAreaService");
	private WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) Sc.getBean("wl.es.WlEsTodoMsgService");
	private WlCmOrgUserService wlCmOrgUserService = (WlCmOrgUserService) Sc.getBean("wl.cm.WlCmOrgUserService");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");
	private WlEsStoreOutService wlEsStoreOutService = (WlEsStoreOutService) Sc.getBean("wl.es.WlEsStoreOutService");

	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.WlEsAgencyRepairAction.MODULE_NAME");
	private final static String MODULE_CODE = "0306001";// 模块代码

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
			pMap.put("memberId", Session.getCurrUser().getId());
			Page page = wlEsCustRepairService.search(pMap, currPage, pageSize);
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 保存数据
	 * @param wlEsCustRepair 售后维修对象
	 * @param picPathList 商品的图片path对象数组
	 * @param videoPathList 商品的视频path对象数组
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlEsCustRepair wlEsCustRepair, String[] picPathList, String[] videoPathList) {
		try {
			User user = Session.getCurrUser();
			WlCmOrgUser wlCmOrgUser = (WlCmOrgUser) wlCmOrgUserService.getObject(user.getId());
			WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(wlCmOrgUser.getOrgId());
			if (ValidateUtil.isEmpty(wlEsCustRepair.getRepairId())) {// 新增
				wlEsCustRepair.setAccount(user.getLoginName());
				wlEsCustRepair.setMemberId(user.getId());
				if (!ValidateUtil.isEmpty(wlCmOrg.getMobile())) {
					wlEsCustRepair.setContact(wlCmOrg.getMobile());
				}
				else {
					wlEsCustRepair.setContact(wlCmOrg.getTel());
				}
				wlEsCustRepair.setProcessStatusEk("0");
				// 校验序列号是否有效
				if (!ValidateUtil.isEmpty(wlEsCustRepair.getDeviceCd())) {
					WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(wlEsCustRepair.getDeviceCd());
					if (!ValidateUtil.isNull(wlEsStoreOut)) {// 查找到序列号，设置为有效序列号
						wlEsCustRepair.setSerialVerfEk("3");
					}
					else {// 查找不到序列号，设置为无效序列号
						wlEsCustRepair.setSerialVerfEk("0");
					}

				}
				/*
				 * else {// 序列号没有输入设置为无效序列号
				 * wlEsCustRepair.setSerialVerfEk("0");
				 * 
				 * }
				 */
				wlEsCustRepair.setName(user.getName());
				wlEsCustRepair.setBillSourceEk("AGENCY");
				wlEsCustRepair.setRepairNo(wlEsCustRepairService.getNewCustRepairNO());
				// 添加日志
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlEsCustRepair.getName() + "]");
				wlEsCustRepairService.saveCustRepair(wlEsCustRepair, picPathList, videoPathList);
				wlEsTodoMsgService.saveToDoMsg(wlEsCustRepair.getRepairId(), "REPAIR");
			}

			/*
			 * if (!ValidateUtil.isNull(user)) {
			 * if (ValidateUtil.isEmpty(wlEsCustRepair.getRepairId())) {
			 * this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlEsCustRepair.getName() + "]");
			 * }
			 * }
			 */

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 售后维修详情(含图片、视频、处理意见)
	 * @param repairId 维修ID
	 * @return 售后维修对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String repairId) {
		try {
			WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) wlEsCustRepairService.getCustRepair(repairId);
			List<Type> processStatusEkList = wlCmTypeService.findByCode("PROCESS_STATUS_EK");
			List<WlEsCustRepairSug> sugList = wlEsCustRepair.getSugList();
			for (WlEsCustRepairSug wlEsCustRepairSug : sugList) {
				String processStatusEk = wlEsCustRepairSug.getProcessStatusEk();
				for (Type type : processStatusEkList) {
					if (type.getId().equals(processStatusEk)) {
						wlEsCustRepairSug.setProcessStatusEkValue(type.getLable());
					}
				}

			}
			return this.getJson(true, wlEsCustRepair);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}