package com.gt.wl.es.action;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.joyone.util.ConvertUtil;
import org.joyone.util.JsonUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmMember;
import com.gt.wl.cm.model.WlCmSellPic;
import com.gt.wl.cm.service.WlCmMemberService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmSellService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.model.WlEsOrder;
import com.gt.wl.es.model.WlEsOrderGood;
import com.gt.wl.es.service.WlEsOrderService;
import com.gt.wl.util.Type;

/**
 * 订单管理Action层
 * @author liuyj
 */

@Controller
@RequestMapping({ "/wl/es/wlEsOrderAction.do", "/wl/es/wlEsOrderAction.web" })
public class WlEsOrderAction extends BaseAction {
	private WlEsOrderService wlEsOrderService = (WlEsOrderService) Sc.getBean("wl.es.WlEsOrderService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmMemberService wlCmMemberService = (WlCmMemberService) Sc.getBean("wl.cm.WlCmMemberService");
	private WlCmSellService wlCmSellService = (WlCmSellService) Sc.getBean("wl.cm.WlCmSellService");
	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.wlEsOrderAction.MODULE_NAME");
	private final static String MODULE_CODE = "0302001";// 模块代码

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
	 * 查询订单信息（旧网站使用）
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param orderStateEk 订单状态
	 * @param values 字段名
	 * @param namesValue 字段值
	 * @return 订单信息
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int currPage, int pageSize, String orderStateEk, String values, String namesValue) {
		try {
			if ("s.memberId".equals(values) && ValidateUtil.isNull(namesValue)) {
				namesValue = Session.getCurrUser().getId();
			}
			Page page = wlEsOrderService.search(pageSize, currPage, orderStateEk, values, namesValue, "","");
			List<Map> list = page.getItems();
			List<Type> payModeEkList = wlCmTypeService.findByCode("PAY_MODE_EK");
			List<Type> orderStateList = wlCmTypeService.findByCode("ORDER_STATE_EK");

			for (Map map : list) {
				String payModeEk = (String) map.get("payModeEk");
				String orderStateEkTemp = (String) map.get("orderStateEk");
				if (ValidateUtil.isNull(map.get("deliveryTime"))) {
					map.put("deliveryTime", "");
				}
				map.put("payModeEkValue", "");

				for (Type type : payModeEkList) {
					if (type.getId().equals(payModeEk)) {
						map.put("payModeEkValue", type.getLable());
						break;
					}
				}
				for (Type type : orderStateList) {
					if (type.getId().equals(orderStateEkTemp)) {
						map.put("orderStateEkValue", type.getLable());
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
	 * 查询订单信息列表（旧网站使用）
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param orderStateEk 订单状态
	 * @param values 字段名
	 * @param namesValue 字段值
	 * @param goodName 字段值
	 * @return 订单信息
	 */
	@RequestMapping(params = "action=findOrderDataPage")
	@ResponseBody
	public String findOrderDataPage(int currPage, int pageSize, String orderStateEk, String values, String namesValue,String goodName) {
		try {
			if ("s.memberId".equals(values) && ValidateUtil.isNull(namesValue)) {
				namesValue = Session.getCurrUser().getId();
			}
			Page page = wlEsOrderService.search(pageSize, currPage, orderStateEk, values, namesValue, "",goodName);
			List<Map> list = page.getItems();
			List<Type> payModeEkList = wlCmTypeService.findByCode("PAY_MODE_EK");
			List<Type> orderStateList = wlCmTypeService.findByCode("ORDER_STATE_EK");
			List<Type> orderStateEnList = wlCmTypeService.findByCode("ORDER_STATE_EK_EN");
			Map payModeEkMap = new HashMap();
			Map orderStateEkMap = new HashMap();
			Map orderStateEkEnMap = new HashMap();
			for (Type type : payModeEkList) {
				payModeEkMap.put(type.getId(), type.getLable());
			}
			for (Type type : orderStateList) {
				orderStateEkMap.put(type.getId(), type.getLable());
			}
			for (Type type : orderStateEnList) {
				orderStateEkEnMap.put(type.getId(), type.getLable());
			}
			for (Map map : list) {
				String payModeEk = (String) map.get("payModeEk");
				String orderId = (String) map.get("orderId");
				WlEsOrder wlEsOrder = (WlEsOrder) wlEsOrderService.getObject(orderId);
				//查询订单信息
				List<WlEsOrderGood> wlpEsOrderGoodList = wlEsOrderService.findOrderGoodList(orderId);
				for(WlEsOrderGood wlEsOrderGood : wlpEsOrderGoodList){
					if(!ValidateUtil.isEmpty(wlEsOrderGood.getSellId())){
						WlCmSellPic wlCmSellPic = wlCmSellService.getSellPicByIsThumFlag(wlEsOrderGood.getSellId(),"1");
						if(!ValidateUtil.isNull(wlCmSellPic)){//防止空指针
							wlEsOrderGood.setPath(wlCmSellPic.getPath());//设置图片
						}
					}
				}
				wlEsOrder.setGoodList(wlpEsOrderGoodList);
				map.put("wlEsOrder", wlpEsOrderGoodList);//每一个订单详情
				if (ValidateUtil.isNull(map.get("deliveryTime"))) {
					map.put("deliveryTime", "");
				}
				map.put("payModeEkValue", payModeEkMap.get((String)map.get("payModeEk")));//订单状态
				map.put("orderStateEkValue", orderStateEkMap.get((String)map.get("orderStateEk")));//订单状态
				map.put("orderStateEkEnValue", orderStateEkEnMap.get((String)map.get("orderStateEk")));//订单状态
			
			}
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 初始化对象属性
	 * @param action 操作名称
	 * @param orderId ID号
	 * @param user 企业用户
	 * @return 结果
	 */
	@ModelAttribute("saveModel")
	public WlEsOrder initAttribute(String action, String orderId, User user) {
		try {
			WlEsOrder wlEsOrder = null;
			if ("saveData".equals(action)) {
				if (ValidateUtil.isEmpty(orderId)) {
					wlEsOrder = new WlEsOrder();
					wlEsOrder.setCreator(user.getName());
					wlEsOrder.setCreateTime(new Date());
				}
				else {
					wlEsOrder = (WlEsOrder) wlEsOrderService.getObject(orderId);
					wlEsOrder.setModifier(user.getName());
					wlEsOrder.setModifyTime(new Date());

				}
			}
			return wlEsOrder;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 新增数据
	 * @param wlEsOrder 对象
	 * @param user 用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(@ModelAttribute("saveModel") WlEsOrder wlEsOrder, User user) {
		try {
			wlEsOrderService.saveObject(wlEsOrder);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改发货信息
	 * @param orderId 订单ID
	 * @param deliveryNo 发货单号
	 * @param logisticEk 物流公司
	 * @return 结果
	 */
	@RequestMapping(params = "action=updateDeliveryData")
	@ResponseBody
	public String updateDeliveryData(String orderId, String deliveryNo, String logisticEk) {
		try {
			User user = Session.getCurrUser();
			wlEsOrderService.updateDeliveryData(orderId, deliveryNo, logisticEk, user);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), Lang.getString("wl.es.wlEsOrderAction.updateDelivery"));
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存修改价格
	 * @param orderId 订单id
	 * @param payAmtAfter 修改后价格
	 * @return 结果
	 */
	@RequestMapping(params = "action=updatePriceData")
	@ResponseBody
	public String updatePriceData(String orderId, double payAmtAfter) {
		try {
			WlEsOrder order = (WlEsOrder) wlEsOrderService.getObject(orderId);
			order.setPayAmt(payAmtAfter);
			wlEsOrderService.updateObject(order);
			this.addLog(Session.getCurrUser(), SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), Lang.getString("wl.es.wlEsOrderAction.orderNo") + order.getOrderNo() + Lang.getString("wl.es.wlEsOrderAction.updatePrice") + order.getPayAmt());
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 关闭订单
	 * @param id 订单id
	 * @param orderStateEk 订单状态
	 * @param user 用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=updateData")
	@ResponseBody
	public String updateData(String id, String orderStateEk, User user) {
		try {
			WlEsOrder wlEsOrder = (WlEsOrder) wlEsOrderService.getObject(id);
			// 关闭订单
			wlEsOrder.setOrderStateEk(orderStateEk);
			wlEsOrder.setCloseTime(new Date());
			wlEsOrder.setModifier(user.getName());
			wlEsOrder.setModifyTime(new Date());
			wlEsOrderService.updateObject(wlEsOrder);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), Lang.getString("wl.es.wlEsOrderAction.closeOrder") + Lang.getString("wl.es.wlEsOrderAction.orderNo") + wlEsOrder.getOrderNo());
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 订单详情
	 * @param orderId 订单ID
	 * @return 订单对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String orderId) {
		try {
			WlEsOrder wlEsOrder = (WlEsOrder) wlEsOrderService.getObject(orderId);
			List<WlEsOrderGood> wlpEsOrderGoodList = wlEsOrderService.findOrderGoodList(orderId);
			for(WlEsOrderGood wlEsOrderGood : wlpEsOrderGoodList){
				if(!ValidateUtil.isEmpty(wlEsOrderGood.getSellId())){
					WlCmSellPic wlCmSellPic = wlCmSellService.getSellPicByIsThumFlag(wlEsOrderGood.getSellId(),"1");
					if(!ValidateUtil.isNull(wlCmSellPic)){//防止空指针
						wlEsOrderGood.setPath(wlCmSellPic.getPath());//设置图片
					}
				}
			}
			wlEsOrder.setGoodList(wlpEsOrderGoodList);
			List<Type> orderStateEkList = wlCmTypeService.findByCode("ORDER_STATE_EK");
			for (Type type : orderStateEkList) {
				if (type.getId().equals(wlEsOrder.getOrderStateEk())) {
					wlEsOrder.setOrderStateEkValue(type.getLable());
					break;
				}
			}
			List<Type> orderStateEkEnList = wlCmTypeService.findByCode("ORDER_STATE_EK_EN");
			for (Type type : orderStateEkEnList) {
				if (type.getId().equals(wlEsOrder.getOrderStateEk())) {
					wlEsOrder.setOrderStateEkEnValue(type.getLable());
					break;
				}
			}
			if (!ValidateUtil.isEmpty(wlEsOrder.getMemberId())) {// 天猫订单没有会员id
				WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(wlEsOrder.getMemberId());
				if (!ValidateUtil.isNull(wlCmMember)) {
					wlEsOrder.setMemberAccount(wlCmMember.getAccount());
					wlEsOrder.setMemberEmail(wlCmMember.getEmail());
				}
			}
			return this.getJson(true, wlEsOrder);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 生成订单
	 * /**
	 * @param deliveryAddrId 收货地址
	 * @param sellIds 上架商品id 与numsList的数量一一对应
	 * @param numsList 对应的数量
	 * @param amt 订单总价
	 * @param buyMsg 买家留言
	 * @param payModeEk 支付方式
	 * @param lang 语言类型
	 * @return 订单id
	 */
	@RequestMapping(params = "action=saveOrder")
	@ResponseBody
	public String saveOrder(String deliveryAddrId, String[] sellIds, long[] numsList, double amt, String buyMsg, String payModeEk,String lang) {
		try {
			User user = Session.getCurrUser();
			String orderId = wlEsOrderService.saveOrder(deliveryAddrId, sellIds, numsList, user, amt, buyMsg, payModeEk,lang);
			return this.getJson(true, orderId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取订单orderId
	 * @param orderNo 订单编号
	 * @return 订单orderId
	 */
	@RequestMapping(params = "action=getWlEsOrderByOrderNo")
	@ResponseBody
	public String getWlEsOrderByOrderNo(String orderNo) {
		try {
			WlEsOrder wlEsOrder = wlEsOrderService.getWlEsOrderByOrderNo(orderNo);
			return this.getJson(true, wlEsOrder.getOrderId());
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 同步淘宝订单（只同步当天）
	 * @return 空
	 */
	@RequestMapping(params = "action=saveTaobaoOrder")
	@ResponseBody
	public String saveTaobaoOrder() {
		try {
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
			DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = df1.parse(df.format(date) + "0:0:0");// 当天0点时间
			wlEsOrderService.saveTaobaoOrder(startDate, date);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存上传数据（待发货订单导入，订单主表信息）
	 * @param path 上传文件ID
	 * @param buyChannelEk 购买渠道
	 * @param user 当前登录用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveOrderExcelData")
	@ResponseBody
	public String saveOrderExcelData(String path, String buyChannelEk, User user) {
		try {
			wlEsOrderService.saveOrderExcelData(path, buyChannelEk, user);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存上传数据（待发货订单导入，订单商品信息）
	 * @param path 上传文件ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveOrderDetailExcelData")
	@ResponseBody
	public String saveOrderDetailExcelData(String path) {
		try {
			wlEsOrderService.saveOrderDetailExcelData(path);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 上架商品列表
	 * @param pageSize 每页条数
	 * @param currPage 当前页码
	 * @param filterSellIds 上架商品ids数组，过滤这些指定的商品
	 * @return 上架商品列表
	 */
	@RequestMapping(params = "action=findSellDetailPage")
	@ResponseBody
	public String findSellDetailPage(int pageSize, int currPage, String[] filterSellIds) {
		try {
			return this.getJson(true, wlCmSellService.findSellDetailPage(pageSize, currPage, filterSellIds));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 上架商品列表
	 * @param sellIds 上架商品id数组
	 * @return 上架商品列表
	 */
	@RequestMapping(params = "action=findSellList")
	@ResponseBody
	public String findSellList(String[] sellIds) {
		try {
			String sellId = "";
			if (!ValidateUtil.isEmpty(sellIds)) {
				sellId = ConvertUtil.toDbString(sellIds);
			}
			Map paraMap = new HashMap();
			paraMap.put("sellIds", sellId);
			return this.getJson(true, wlCmSellService.findSellListByMap(paraMap));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存单据的商品列表
	 * @param orderId
	 * @param goodListJson
	 * @return
	 */
	@RequestMapping(params = "action=saveGoodList")
	@ResponseBody
	public String saveGoodList(String orderId, String goodListJson) {
		try {
			List<WlEsOrderGood> orderGoodList = JsonUtil.toList(goodListJson, WlEsOrderGood.class);
			wlEsOrderService.saveGoodList(orderId, orderGoodList);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改发货信息（已发货编辑）
	 * @param orderId 订单ID
	 * @param deliveryNo 发货单号
	 * @param logisticEk 物流公司
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveDeliveryData")
	@ResponseBody
	public String saveDeliveryData(String orderId, String deliveryNo, String logisticEk, User user) {
		try {
			WlEsOrder wlEsOrder = (WlEsOrder) wlEsOrderService.getObject(orderId);
			wlEsOrder.setDeliveryNo(deliveryNo);
			wlEsOrder.setLogisticEk(logisticEk);
			wlEsOrderService.saveObject(wlEsOrder);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), Lang.getString("wl.es.wlEsOrderAction.editDelivery"));
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 删除订单信息
	 * @param orderId 订单ID
	 * @return 空
	 */
	@RequestMapping(params = "action=deleteOrderData")
	@ResponseBody
	public String deleteOrderData(String orderId){
		try {
			//orderId一定存在
			wlEsOrderService.deleteOrderByOrderId(orderId);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}