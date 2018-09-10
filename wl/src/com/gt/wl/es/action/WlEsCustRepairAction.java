package com.gt.wl.es.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmArea;
import com.gt.wl.cm.model.WlCmMember;
import com.gt.wl.cm.service.WlCmAreaService;
import com.gt.wl.cm.service.WlCmDocumentService;
import com.gt.wl.cm.service.WlCmMemberService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.model.WlEsCustRepair;
import com.gt.wl.es.model.WlEsCustRepairSug;
import com.gt.wl.es.model.WlEsStoreOut;
import com.gt.wl.es.service.WlEsCustRepairService;
import com.gt.wl.es.service.WlEsStoreOutService;
import com.gt.wl.es.service.WlEsTodoMsgService;
import com.gt.wl.util.Type;
import com.gt.wl.wm.service.WlWmStoreOutService;

/**
 * 售后维修Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/wl/es/wlEsCustRepairAction.do", "/wl/es/wlEsCustRepairAction.web" })
public class WlEsCustRepairAction extends BaseAction {
	private WlEsCustRepairService wlEsCustRepairService = (WlEsCustRepairService) Sc.getBean("wl.es.WlEsCustRepairService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmAreaService wlCmAreaService = (WlCmAreaService) Sc.getBean("wl.cm.WlCmAreaService");
	private WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) Sc.getBean("wl.es.WlEsTodoMsgService");
	private WlCmMemberService wlCmMemberService = (WlCmMemberService) Sc.getBean("wl.cm.WlCmMemberService");
	private WlWmStoreOutService wlWmStoreOutService = (WlWmStoreOutService) Sc.getBean("wl.wm.WlWmStoreOutService");
	private WlEsStoreOutService wlEsStoreOutService = (WlEsStoreOutService) Sc.getBean("wl.es.WlEsStoreOutService");
	private WlCmDocumentService wlCmDocumentService = (WlCmDocumentService) Sc.getBean("wl.cm.WlCmDocumentService");
	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.wlEsCustRepairAction.MODULE_NAME");
	private final static String MODULE_CODE = "0302005";// 模块代码

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
	public String findDataList(ParaMap paraMap, int currPage, int pageSize, @RequestParam(defaultValue="CN")String langType) {
		try {

			Map pMap = paraMap.getMap();
			if (pMap.containsKey("keyId")) {
				String keyId = (String) pMap.get("keyId");
				if ("memberId".equals(keyId)) {
					pMap.put("keyValue", Session.getCurrUser().getId());
				}
			}
			Page page = wlEsCustRepairService.search(pMap, currPage, pageSize);

			List<WlEsCustRepair> list = page.getItems();
			List<Type> moduleEkList = new ArrayList<Type>();
			List<Type> paymentStatusEkList = new ArrayList<Type>();
			if ("EN".equals(langType)) {
				moduleEkList = wlCmTypeService.findByCode("MODULE_EK_EN");
				paymentStatusEkList = wlCmTypeService.findByCode("PAYMENT_STATUS_EK_EN");
			} else {
				moduleEkList = wlCmTypeService.findByCode("MODULE_EK");
				paymentStatusEkList = wlCmTypeService.findByCode("PAYMENT_STATUS_EK");
			}

			for (WlEsCustRepair wlEsCustRepair : list) {
				String moduleEk = wlEsCustRepair.getModuleEk();
				// String serialVerfEk = wlEsCustRepair.getSerialVerfEk();
				String paymentStatusEk = wlEsCustRepair.getPaymentStatusEk();
				for (Type type : moduleEkList) {
					if (type.getId().equals(moduleEk)) {
						wlEsCustRepair.setModuleEkValue(type.getLable());
						break;
					}
				}

				for (Type type : paymentStatusEkList) {
					if (type.getId().equals(paymentStatusEk)) {
						wlEsCustRepair.setPaymentStatusEkValue(type.getLable());
						break;
					}
				}

			}

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
			WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(user.getId());
			if (ValidateUtil.isEmpty(wlEsCustRepair.getRepairId())) {// 新增
				wlEsCustRepair.setAccount(user.getLoginName());
				wlEsCustRepair.setMemberId(user.getId());
				wlEsCustRepair.setName(user.getName());
				wlEsCustRepair.setCreator(user.getName());
				wlEsCustRepair.setContact(wlCmMember.getMobile());
				wlEsCustRepair.setProcessStatusEk("0");
				wlEsCustRepair.setSerialVerfEk("3");
				wlEsCustRepair.setPaymentStatusEk("0");
				wlEsCustRepair.setRepairNo(wlEsCustRepairService.getNewCustRepairNO());
				wlEsCustRepairService.saveCustRepair(wlEsCustRepair, picPathList, videoPathList);
				wlEsTodoMsgService.saveToDoMsg(wlEsCustRepair.getRepairId(), "REPAIR");
			}
			else {// 更新
				WlCmArea wlCmArea = new WlCmArea();
				if (!ValidateUtil.isEmpty(wlEsCustRepair.getProvinceId())) {
					wlCmArea = (WlCmArea) wlCmAreaService.getObject(wlEsCustRepair.getProvinceId());
					wlEsCustRepair.setProvince(wlCmArea.getName());
				}
				if (!ValidateUtil.isEmpty(wlEsCustRepair.getCityId())) {
					wlCmArea = (WlCmArea) wlCmAreaService.getObject(wlEsCustRepair.getCityId());
					wlEsCustRepair.setCity(wlCmArea.getName());
				}
				if (!ValidateUtil.isEmpty(wlEsCustRepair.getAreaId())) {
					wlCmArea = (WlCmArea) wlCmAreaService.getObject(wlEsCustRepair.getAreaId());
					wlEsCustRepair.setArea(wlCmArea.getName());
				}
				wlEsCustRepair.setProcessStatusEk("2");// 用户已发货
				wlEsCustRepairService.saveObject(wlEsCustRepair);
			}

			if (!ValidateUtil.isNull(user)) {
				if (ValidateUtil.isEmpty(wlEsCustRepair.getRepairId())) {
					this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlEsCustRepair.getName() + "]");
				}
				else {
					this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), "[" + wlEsCustRepair.getName() + "]");
				}
			}

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存新增或编辑客户维修数据
	 * @param wlEsCustRepair 售后维修对象
	 * @param picPathList 商品的图片path对象数组
	 * @param videoPathList 商品的视频path对象数组
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveCustRepairData")
	@ResponseBody
	public String saveCustRepairData(WlEsCustRepair wlEsCustRepair, String[] picPathList, String[] videoPathList) {
		try {
			User user = Session.getCurrUser();
			WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(user.getId());
			if (ValidateUtil.isEmpty(wlEsCustRepair.getRepairId())) {// 新增
				wlEsCustRepair.setAccount(user.getLoginName());
				wlEsCustRepair.setMemberId(user.getId());
				wlEsCustRepair.setName(user.getName());
				wlEsCustRepair.setCreator(user.getName());
				wlEsCustRepair.setContact(wlCmMember.getMobile());
				wlEsCustRepair.setProcessStatusEk("0");
				wlEsCustRepair.setSerialVerfEk("3");
				wlEsCustRepair.setPaymentStatusEk("0");
				wlEsCustRepair.setRepairNo(wlEsCustRepairService.getNewCustRepairNO());
				wlEsCustRepairService.saveCustRepair(wlEsCustRepair, picPathList, videoPathList);
				wlEsTodoMsgService.saveToDoMsg(wlEsCustRepair.getRepairId(), "REPAIR");
				if (!ValidateUtil.isNull(user)) {
					this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlEsCustRepair.getName() + "]");
				}
				wlEsCustRepairService.saveCustRepair(wlEsCustRepair, picPathList, videoPathList);
				wlEsTodoMsgService.saveToDoMsg(wlEsCustRepair.getRepairId(), "REPAIR");
			}
			else {// 更新
				wlEsCustRepair.setProcessStatusEk("0");
				wlEsCustRepair.setSerialVerfEk("3");
				wlEsCustRepair.setPaymentStatusEk("0");
				wlEsCustRepairService.saveCustRepair(wlEsCustRepair, picPathList, videoPathList);
				if (!ValidateUtil.isNull(user)) {
					this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), "[" + wlEsCustRepair.getName() + "]");
				}
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改处理信息
	 * @param repairId 维修ID
	 * @param isReturnFlag 是否退回维修配件
	 * @param upkeep 维修费
	 * @param freight 运费
	 * @param reply 回复意见
	 * @param isDelivery 是否发货给客户
	 * @return 结果
	 */
	@RequestMapping(params = "action=updateHandleData")
	@ResponseBody
	public String updateHandleData(String repairId, String isReturnFlag, String upkeep, String freight, String reply, String isDelivery, @RequestParam(defaultValue="CN")String langType) {
		try {
			User user = Session.getCurrUser();
			WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) wlEsCustRepairService.getObject(repairId);
			wlEsCustRepairService.updateHandleData(repairId, isReturnFlag, upkeep, freight, user.getId(), user.getName(), reply, isDelivery, langType);
			// 增加日志
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlEsCustRepair.getRepairNo() + Lang.getString("wl.es.wlEsCustRepairAction.updateHandleData"));
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改确认收货及发货信息
	 * @param repairId 维修ID
	 * @param processStatusEk 处理状态
	 * @param logisticEk 物流公司
	 * @param deliveryNo 发货单号
	 * @return 结果
	 */
	@RequestMapping(params = "action=updateData")
	@ResponseBody
	public String updateData(String repairId, String processStatusEk, String logisticEk, String deliveryNo) {
		try {
			User user = Session.getCurrUser();
			WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) wlEsCustRepairService.getObject(repairId);
			wlEsCustRepair.setProcessStatusEk(processStatusEk);// 处理状态
			wlEsCustRepair.setLogisticEk(logisticEk);// 物流公司
			if (!ValidateUtil.isEmpty(deliveryNo)) {
				deliveryNo = deliveryNo.replaceAll("\\t", "");
			}
			wlEsCustRepair.setDeliveryNo(deliveryNo);// 发货单号
			wlEsCustRepair.setDeliveryTime(new Date());// 发货时间
			wlEsCustRepair.setCorpUserId(user.getId());// 发货人ID
			wlEsCustRepair.setCorpUser(user.getName());// 发货人
			if ("4".equals(processStatusEk)) {// 确认发货
				wlEsCustRepairService.updateData(wlEsCustRepair, user);// 售后维修发货并将订单更改为已发货
			}
			else if ("3".equals(processStatusEk)) {// 确认收货
				wlEsCustRepairService.updateObject(wlEsCustRepair);
			}
			// 增加日志
			if ("3".equals(processStatusEk)) {// 确认收货
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlEsCustRepair.getRepairNo() + Lang.getString("wl.es.wlEsCustRepairAction.confirmReceiverData"));
			}
			else if ("4".equals(processStatusEk)) {// 确认发货
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlEsCustRepair.getRepairNo() + Lang.getString("wl.es.wlEsCustRepairAction.confirmSendData"));
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更新为驳回
	 * @param repairId 售后维修id
	 * @return 空
	 */
	@RequestMapping(params = "action=updateReject")
	@ResponseBody
	public String updateReject(String repairId) {
		try {
			WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) wlEsCustRepairService.getObject(repairId);
			if ("WL".equals(wlEsCustRepair.getBillSourceEk())) {
				wlEsCustRepair.setProcessStatusEk("");
			}
			else {
				wlEsCustRepair.setProcessStatusEk("REJECT");// 处理状态更改为驳回
			}
			wlEsCustRepairService.updateObject(wlEsCustRepair);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 售后维修详情
	 * @param repairId 维修ID
	 * @return 售后维修对象
	 */
	@RequestMapping(params = "action=getDetailData")
	@ResponseBody
	public String getDetailData(String repairId) {
		try {
			WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) wlEsCustRepairService.getObject(repairId);
			return this.getJson(true, wlEsCustRepair);
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
			if (!ValidateUtil.isEmpty(wlEsCustRepair.getDeviceCd())) {// 如果序列号不为空
				Map paraMap = new HashMap();
				paraMap.put("serialNo", wlEsCustRepair.getDeviceCd());
				List<Map> list = wlWmStoreOutService.findStoreOutDetlList(paraMap);
				if (!ValidateUtil.isEmpty(list)) {// 出库单能查到到
					Map resultMap = list.get(0);
					Date outDate = (Date) resultMap.get("outDate");
					String spec = (String) resultMap.get("spec");
					String orgName = (String) resultMap.get("orgName");
					wlEsCustRepair.setOutDate(outDate);
					wlEsCustRepair.setSpec(spec);
					wlEsCustRepair.setAgency(orgName);
				}
				else {// 如果出库单找不到，到序列号登记表查找
					WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(wlEsCustRepair.getDeviceCd());
					if (!ValidateUtil.isNull(wlEsStoreOut)) {// 如果序列号登记表不为空
						wlEsCustRepair.setOutDate(wlEsStoreOut.getDeliveryDate());
						wlEsCustRepair.setSpec(wlEsStoreOut.getProductName());
						wlEsCustRepair.setAgency(wlEsStoreOut.getAgency());
					}
				}

			}
			return this.getJson(true, wlEsCustRepair);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取打印数据
	 * @param repairId 维修id
	 * @param request 请求
	 * @return 打印数据
	 */
	@RequestMapping(params = "action=getPrintData")
	@ResponseBody
	public String getPrintData(String repairId, HttpServletRequest request) {
		try {
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
			WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) wlEsCustRepairService.getCustRepair(repairId);
			Map resultMap = new HashMap();
			resultMap.put("deviceCd", wlEsCustRepair.getDeviceCd());
			resultMap.put("receiver", "");
			resultMap.put("contact", "");
			resultMap.put("addr", "");
			resultMap.put("agency", "");
			resultMap.put("spec", "");
			resultMap.put("deliveryDate", "");
			resultMap.put("basePath", basePath);
			if (!ValidateUtil.isEmpty(wlEsCustRepair.getDeviceCd())) {// 如果有序列号
				WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(wlEsCustRepair.getDeviceCd());
				resultMap.put("receiver", wlEsStoreOut.getReceiver());
				resultMap.put("contact", wlEsStoreOut.getContact());
				resultMap.put("addr", wlEsStoreOut.getAddr());
				resultMap.put("agency", wlEsStoreOut.getAgency());
				resultMap.put("spec", wlEsStoreOut.getProductName());
				resultMap.put("deliveryDate", wlEsStoreOut.getDeliveryDate());
			}

			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取售后维修对象(含图片、视频、处理意见)
	 * @param repairId 售后维修ID
	 * @return 售后维修对象
	 */
	@RequestMapping(params = "action=getCustRepair")
	@ResponseBody
	public String getCustRepair(String repairId) {
		try {
			WlEsCustRepair wlEsCustRepair = wlEsCustRepairService.getCustRepair(repairId);
			Map map = new HashMap();
			map.put("picList", wlEsCustRepair.getPicList());
			map.put("videoList", wlEsCustRepair.getVideoList());
			map.put("custRepair", wlEsCustRepairService.getObject(wlEsCustRepair.getRepairId()));
			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除文件和维修列表文件记录
	 * @param path 文件路径
	 * @return 空
	 */
	@RequestMapping(params = "action=deleteFileAndRecord")
	@ResponseBody
	public String deleteFileAndRecord(String path) {
		try {
			wlCmDocumentService.deleteFile(path);
			wlEsCustRepairService.deleteData("", path);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除文件和维修列表文件记录
	 * @param path 文件路径
	 * @return 空
	 */
	@RequestMapping(params = "action=deleteVideoFileAndRecord")
	@ResponseBody
	public String deleteVideoFileAndRecord(HttpServletRequest request, String path) {
		try {
			String realPath = request.getSession().getServletContext().getRealPath("/upload/");
			wlCmDocumentService.deleteVideoFile(realPath, path);
			wlEsCustRepairService.deleteData("", path);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存处理结果
	 * @param repairId 维修id
	 * @param reason 原因
	 * @param dealResult 处理结果
	 * @return 空
	 */
	@RequestMapping(params = "action=savaDealData")
	@ResponseBody
	public String savaDealData(String repairId, String reason, String dealResult) {
		try {
			WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) wlEsCustRepairService.getObject(repairId);
			wlEsCustRepair.setReason(reason);
			wlEsCustRepair.setDealResult(dealResult);
			wlEsCustRepairService.saveObject(wlEsCustRepair);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

}