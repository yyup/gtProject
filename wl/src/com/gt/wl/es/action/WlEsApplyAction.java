package com.gt.wl.es.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;

import org.joyone.action.BaseAction;
import org.joyone.excel.ExcelExportUtil;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmCommonSetService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.model.WlEsApply;
import com.gt.wl.es.model.WlEsOrder;
import com.gt.wl.es.model.WlEsStoreOut;
import com.gt.wl.es.service.WlEsApplyService;
import com.gt.wl.es.service.WlEsOrderService;
import com.gt.wl.es.service.WlEsStoreOutService;
import com.gt.wl.es.service.WlEsTodoMsgService;
import com.gt.wl.util.Type;
import com.gt.wl.wm.service.WlWmStoreOutService;

/**
 * 配件申请管理Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/wl/es/wlEsApplyAction.do", "/wl/es/wlEsApplyAction.web" })
public class WlEsApplyAction extends BaseAction {
	private WlEsApplyService wlEsApplyService = (WlEsApplyService) Sc.getBean("wl.es.WlEsApplyService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) Sc.getBean("wl.es.WlEsTodoMsgService");
	private WlEsOrderService wlEsOrderService = (WlEsOrderService) Sc.getBean("wl.es.WlEsOrderService");
	private WlCmCommonSetService wlCmCommonSetService = (WlCmCommonSetService) Sc.getBean("wl.cm.WlCmCommonSetService");
	private WlWmStoreOutService wlWmStoreOutService = (WlWmStoreOutService) Sc.getBean("wl.wm.WlWmStoreOutService");
	private WlEsStoreOutService wlEsStoreOutService = (WlEsStoreOutService) Sc.getBean("wl.es.WlEsStoreOutService");
	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.wlEsApplyAction.MODULE_NAME");
	private final static String MODULE_CODE = "0302002";// 模块代码

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
			String postAge = wlCmCommonSetService.getWlCmCommonSetByKey("POSTAGE").getSetValue().trim();
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			mapResult.put("postAge", postAge);
			return this.getJson(true, mapResult);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询配件申请信息（分页）
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param keyId 字段名
	 * @param keyValue 字段值
	 * @param isDeliveryFlag 是否已发货
	 * @param applyTimeStart 申请时间开始
	 * @param applyTimeEnd 申请时间结束
	 * @param deliveryTimeStart 发货时间开始
	 * @param deliveryTimeEnd 发货时间结束
	 * @param paymentStatusEk 费用支付状态
	 * @return 配件申请信息
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int currPage, int pageSize, String keyId, String keyValue, String isDeliveryFlag, String applyTimeStart, String applyTimeEnd, String deliveryTimeStart, String deliveryTimeEnd, String paymentStatusEk, String lang) {
		try {
			if ("memberId".equals(keyId) && ValidateUtil.isNull(keyValue)) {
				keyValue = Session.getCurrUser().getId();
			}
			Page page = wlEsApplyService.search(currPage, pageSize, keyId, keyValue, isDeliveryFlag, applyTimeStart, applyTimeEnd, deliveryTimeStart, deliveryTimeEnd, paymentStatusEk);

			// 支付方式
			List<Type> payModeEkList = wlCmTypeService.findByCode("PAY_MODE_EK");
			// 发货状态
			// List<Type> processStatusList = wlCmTypeService.findByCode("PROCESS_STATUS_EK");
			List<Type> machineTypeEkList = wlCmTypeService.findByCode("MACHINE_TYPE_EK");
			List<Type> paymentStatusEkList = new ArrayList<Type>();
			// 物流公司
			List<Type> logiSticEkList = new ArrayList<Type>();
			if (!ValidateUtil.isEmpty(lang) && "EN".equals(lang)) {
				paymentStatusEkList = wlCmTypeService.findByCode("PAYMENT_STATUS_EK_EN");
				logiSticEkList = wlCmTypeService.findByCode("LOGISTIC_EK_EN");
			} else {
				paymentStatusEkList = wlCmTypeService.findByCode("PAYMENT_STATUS_EK");
				logiSticEkList = wlCmTypeService.findByCode("LOGISTIC_EK");
			}

			List<WlEsApply> list = page.getItems();

			for (WlEsApply wlEsApply : list) {

				String tempPayMode = wlEsApply.getPayModeEk();
				String tempLogiSticEk = wlEsApply.getLogisticEk();
				String tempProcessStatusEk = wlEsApply.getIsDeliveryFlag();
				String tempMachineTypeEk = wlEsApply.getMachineTypeEk();
				String tempPaymentStatusEk = wlEsApply.getPaymentStatusEk();

				for (Type type : payModeEkList) {
					if (type.getId().equals(tempPayMode)) {
						wlEsApply.setPayMoreEkValue(type.getLable());
						break;
					}
				}

				for (Type type : logiSticEkList) {
					if (type.getId().equals(tempLogiSticEk)) {
						wlEsApply.setLogiSticEkValue(type.getLable());
						break;
					}
				}

				for (Type type : paymentStatusEkList) {
					if (type.getId().equals(tempPaymentStatusEk)) {
						wlEsApply.setPaymentStatusEkValue(type.getLable());
						break;
					}
				}
				/*
				 * for (Type type : processStatusList) {
				 * if (type.getId().equals(tempProcessStatusEk)) {
				 * wlEsApply.setProcessStatusValue(type.getLable());
				 * break;
				 * }
				 * }
				 */
				for (Type type : machineTypeEkList) {
					if (type.getId().equals(tempMachineTypeEk)) {
						wlEsApply.setMachineTypeEkValue(type.getLable());
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
	 * @param wlEsApply 配件申请对象
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlEsApply wlEsApply, @RequestParam(defaultValue="CN")String langType) {
		try {

			wlEsApply.setMemberId(Session.getCurrUser().getId());
			/*
			 * WlCmArea wlCmArea = new WlCmArea();
			 * if (!ValidateUtil.isEmpty(wlEsApply.getProvinceId())) {
			 * wlCmArea = (WlCmArea) wlCmAreaService.getObject(wlEsApply.getProvinceId());
			 * wlEsApply.setProvince(wlCmArea.getName());
			 * }
			 * if (!ValidateUtil.isEmpty(wlEsApply.getCityId())) {
			 * wlCmArea = (WlCmArea) wlCmAreaService.getObject(wlEsApply.getCityId());
			 * wlEsApply.setCity(wlCmArea.getName());
			 * }
			 * if (!ValidateUtil.isEmpty(wlEsApply.getAreaId())) {
			 * wlCmArea = (WlCmArea) wlCmAreaService.getObject(wlEsApply.getAreaId());
			 * wlEsApply.setArea(wlCmArea.getName());
			 * }
			 */
			User user = Session.getCurrUser();
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlEsApply.getDeviceCd());

			// wlEsApplyService.saveObject(wlEsApply);
			Map<String, String> resultMap = new HashMap<String, String>();
			String orderId = wlEsApplyService.saveData(wlEsApply, user, langType);// 保存配件申请并生成订单
			resultMap.put("orderId", orderId);
			if (!ValidateUtil.isEmpty(orderId)) {
				WlEsOrder wlEsOrder = (WlEsOrder) wlEsOrderService.getObject(orderId);
				resultMap.put("orderNo", wlEsOrder.getOrderNo());
			}
			else {
				resultMap.put("orderNo", "");
			}
			wlEsTodoMsgService.saveToDoMsg(wlEsApply.getApplyId(), "APPLY");// 生成待发送信息
			return this.getJson(true, resultMap);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 修改发货信息
	 * @param applyId 申请ID
	 * @param deliveryNo 发货单号
	 * @param logisticEk 物流公司
	 * @return 结果
	 */
	@RequestMapping(params = "action=updateDeliveryData")
	@ResponseBody
	public String updateDeliveryData(String applyId, String deliveryNo, String logisticEk) {
		try {
			User user = Session.getCurrUser();
			WlEsApply wlEsApply = (WlEsApply) wlEsApplyService.getObject(applyId);
			wlEsApplyService.updateDeliveryData(wlEsApply, deliveryNo, logisticEk, user);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlEsApply.getApplyId() + Lang.getString("wl.es.wlEsApplyAction.updateDelivery"));
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取详细信息
	 * @param applyId 申请ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String applyId) {
		try {
			WlEsApply wlEsApply = (WlEsApply) wlEsApplyService.getObject(applyId);
			if (!ValidateUtil.isEmpty(wlEsApply.getDeviceCd())) {// 如果序列号不为空
				Map paraMap = new HashMap();
				paraMap.put("serialNo", wlEsApply.getDeviceCd());
				List<Map> list = wlWmStoreOutService.findStoreOutDetlList(paraMap);
				if (!ValidateUtil.isEmpty(list)) {// 出库单能查到到
					Map resultMap = list.get(0);
					Date outDate = (Date) resultMap.get("outDate");
					String spec = (String) resultMap.get("spec");
					String orgName = (String) resultMap.get("orgName");
					wlEsApply.setOutDate(outDate);
					wlEsApply.setSpec(spec);
					wlEsApply.setAgency(orgName);// 经销商
				}
				else {// 如果出库单找不到，到序列号登记表查找
					WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(wlEsApply.getDeviceCd());
					if (!ValidateUtil.isNull(wlEsStoreOut)) {// 如果序列号登记表不为空
						wlEsApply.setOutDate(wlEsStoreOut.getDeliveryDate());
						wlEsApply.setSpec(wlEsStoreOut.getProductName());
						wlEsApply.setAgency(wlEsStoreOut.getAgency());
					}
				}

			}
			return this.getJson(true, wlEsApply);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 导出配件申请列表
	 * @param keyId 字段名
	 * @param keyValue 字段值
	 * @param isDeliveryFlag 是否已发货
	 * @param applyTimeStart 申请时间开始
	 * @param applyTimeEnd 申请时间结束
	 * @param deliveryTimeStart 发货时间开始
	 * @param deliveryTimeEnd 发货时间结束
	 * @param paymentStatusEk 费用支付状态
	 * @param response 响应
	 * @return 空
	 * @throws IOException
	 * @throws WriteException
	 */
	@RequestMapping(params = "action=exportDataList")
	public void exportDataList(String keyId, String keyValue, String isDeliveryFlag, String applyTimeStart, String applyTimeEnd, String deliveryTimeStart, String deliveryTimeEnd, String paymentStatusEk, HttpServletResponse response)
			throws IOException, WriteException {
		try {
			// 前端加码，后端解码
			if (!ValidateUtil.isEmpty(keyValue)) {
				keyValue = java.net.URLDecoder.decode(keyValue, "utf-8");
			}
			// 得到列表数据
			List<WlEsApply> list = wlEsApplyService.findDataList(keyId, keyValue, isDeliveryFlag, applyTimeStart, applyTimeEnd, deliveryTimeStart, deliveryTimeEnd, paymentStatusEk);
			// 支付方式
			List<Type> payModeEkList = wlCmTypeService.findByCode("PAY_MODE_EK");
			// 物流公司
			List<Type> logiSticEkList = wlCmTypeService.findByCode("LOGISTIC_EK");
			List<Type> machineTypeEkList = wlCmTypeService.findByCode("MACHINE_TYPE_EK");
			List<Type> paymentStatusEkList = wlCmTypeService.findByCode("PAYMENT_STATUS_EK");
			List<Type> yesOrNoList = wlCmTypeService.findByCode("YESORNO");
			Map<String, String> payModeEkMap = new HashMap<String, String>();
			Map<String, String> logiSticEkMap = new HashMap<String, String>();
			Map<String, String> machineTypeEkMap = new HashMap<String, String>();
			Map<String, String> paymentStatusEkMap = new HashMap<String, String>();
			Map<String, String> yesOrNoMap = new HashMap<String, String>();

			for (Type type : payModeEkList) {
				payModeEkMap.put(type.getId(), type.getLable());
			}
			for (Type type : logiSticEkList) {
				logiSticEkMap.put(type.getId(), type.getLable());
			}
			for (Type type : machineTypeEkList) {
				machineTypeEkMap.put(type.getId(), type.getLable());
			}
			for (Type type : paymentStatusEkList) {
				paymentStatusEkMap.put(type.getId(), type.getLable());
			}
			for (Type type : yesOrNoList) {
				yesOrNoMap.put(type.getId(), type.getLable());
			}
			for (WlEsApply wlEsApply : list) {
				wlEsApply.setMachineTypeEk(machineTypeEkMap.get(wlEsApply.getMachineTypeEk()));
				if (!ValidateUtil.isEmpty(wlEsApply.getLogisticEk())) {
					wlEsApply.setLogisticEk(logiSticEkMap.get(wlEsApply.getLogisticEk()));
				}
				wlEsApply.setPaymentStatusEk(paymentStatusEkMap.get(wlEsApply.getPaymentStatusEk()));
				wlEsApply.setPayModeEk(payModeEkMap.get(wlEsApply.getPayModeEk()));
				wlEsApply.setAddr(wlEsApply.getProvince() + wlEsApply.getCity() + wlEsApply.getArea() + wlEsApply.getAddr());
				wlEsApply.setIsDeliveryFlag(yesOrNoMap.get(wlEsApply.getIsDeliveryFlag()));
				// 查找购买时间和型号和经销商
				if (!ValidateUtil.isEmpty(wlEsApply.getDeviceCd())) {// 如果序列号不为空
					Map paraMap = new HashMap();
					paraMap.put("serialNo", wlEsApply.getDeviceCd());
					List<Map> slist = wlWmStoreOutService.findStoreOutDetlList(paraMap);
					if (!ValidateUtil.isEmpty(slist)) {// 出库单能查到到
						Map resultMap = slist.get(0);
						Date outDate = (Date) resultMap.get("outDate");
						String spec = (String) resultMap.get("spec");
						String orgName = (String) resultMap.get("orgName");
						wlEsApply.setOutDateStr(DateUtil.dateToShortDateStr(outDate));
						wlEsApply.setSpec(spec);
						wlEsApply.setAgency(orgName);
					}
					else {// 如果出库单找不到，到序列号登记表查找
						WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(wlEsApply.getDeviceCd());
						if (!ValidateUtil.isNull(wlEsStoreOut)) {// 如果序列号登记表不为空
							wlEsApply.setOutDateStr(DateUtil.dateToShortDateStr(wlEsStoreOut.getDeliveryDate()));
							wlEsApply.setSpec(wlEsStoreOut.getProductName());
							wlEsApply.setAgency(wlEsStoreOut.getAgency());
						}
					}

				}
			}
			// 得到表头数据
			List fieldList = ExcelExportUtil.getHeadFieldList("export/WlEsApply.xml");
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
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}
	/**
	 * 删除净水片申请记录
	 * @param applyId
	 * @return
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String applyId) {
		try {
			wlEsApplyService.deleteObject(applyId);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 根据订单编号查询订单
	 * @param applyId
	 * @return
	 */
	@RequestMapping(params = "action=getorderByOrderNo")
	@ResponseBody
	public String getorderByOrderNo(String orderNo) {
		try {
			WlEsOrder order = wlEsOrderService.getWlEsOrderByOrderNo(orderNo);
			return this.getJson(true, order);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}