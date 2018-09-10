package com.gt.wl.es.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Config;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmDocument;
import com.gt.wl.cm.model.WlCmDocumentPath;
import com.gt.wl.cm.model.WlCmSell;
import com.gt.wl.cm.model.WlCmSellEn;
import com.gt.wl.cm.service.WlCmAreaService;
import com.gt.wl.cm.service.WlCmDocumentPathService;
import com.gt.wl.cm.service.WlCmDocumentService;
import com.gt.wl.cm.service.WlCmSellEnService;
import com.gt.wl.cm.service.WlCmSellService;
import com.gt.wl.es.dao.WlEsOrderDao;
import com.gt.wl.es.dao.WlEsOrderGoodDao;
import com.gt.wl.es.model.WlEsApply;
import com.gt.wl.es.model.WlEsCustRepair;
import com.gt.wl.es.model.WlEsMyDeliveryAddr;
import com.gt.wl.es.model.WlEsMyShoppingCart;
import com.gt.wl.es.model.WlEsOrder;
import com.gt.wl.es.model.WlEsOrderGood;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

/**
 * 订单管理Service层
 * @author liuyj
 * 
 */
@Service("wl.es.WlEsOrderService")
public class WlEsOrderService extends BaseService {
	private WlEsOrderDao wlEsOrderDao = (WlEsOrderDao) Sc.getBean("wl.es.WlEsOrderDao");
	private WlEsOrderGoodDao wlEsOrderGoodDao = (WlEsOrderGoodDao) Sc.getBean("wl.es.WlEsOrderGoodDao");
	private WlCmAreaService wlCmAreaService = (WlCmAreaService) Sc.getBean("wl.cm.WlCmAreaService");
	private WlCmSellService wlCmSellService = (WlCmSellService) Sc.getBean("wl.cm.WlCmSellService");
	private WlEsMyShoppingCartService wlEsMyShoppingCartService = (WlEsMyShoppingCartService) Sc.getBean("wl.es.WlEsMyShoppingCartService");
	private WlEsMyDeliveryAddrService wlEsMyDeliveryAddrService = (WlEsMyDeliveryAddrService) Sc.getBean("wl.es.WlEsMyDeliveryAddrService");
	private WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) Sc.getBean("wl.es.WlEsTodoMsgService");
	private WlCmDocumentService wlCmDocumentService = (WlCmDocumentService) Sc.getBean("wl.cm.WlCmDocumentService");
	private WlCmDocumentPathService pathService = (WlCmDocumentPathService) Sc.getBean("wl.cm.WlCmDocumentPathService");
	private WlEsFeeTempService wlEsFeeTempService= (WlEsFeeTempService) Sc.getBean("wl.es.WlEsFeeTempService");
	private WlCmSellEnService wlCmSellEnService = (WlCmSellEnService) Sc.getBean("wl.cm.WlCmSellEnService");
	public WlEsOrderService() {
		baseDao = wlEsOrderDao;
	}

	/**
	 * 通过会员ID得到其订单总数
	 * @param memberId 会员id
	 * @return 订单总数
	 */
	public int getOrderTotalByMememberId(String memberId) {
		try {
			return wlEsOrderDao.getOrderTotalByMememberId(memberId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 通过会员ID得到已消费金额
	 * @param memberId 会员id
	 * @return 已消费金额
	 */
	public double getAmountCountByMemId(String memberId) {
		try {
			return wlEsOrderDao.getAmountCountByMemId(memberId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据会员ID得到订单信息
	 * @param memberId 会员ID
	 * @return 订单信息
	 */
	public WlEsOrder getOrderByMemId(String memberId) {
		try {
			if (ValidateUtil.isEmpty(memberId)) {
				return null;
			}
			return wlEsOrderDao.getOrderByMemId(memberId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询订单信息(分页)
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param orderNo 订单号
	 * @param orderStateEk 订单状态
	 * @param memberId 会员ID
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, String orderNo, String memberId, String orderStateEk) {
		try {
			return wlEsOrderDao.search(currPage, pageSize, memberId, orderNo, orderStateEk);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询订单信息(分页)
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param orderStateEk 订单状态
	 * @param values 字段名
	 * @param namesValue 字段值
	 * @param evaluateState 评价状态
	 * @param goodName 商品名称
	 * @return 订单信息
	 */
	public Page search(int pageSize, int currPage, String orderStateEk, String values, String namesValue, String evaluateState,String goodName) {
		try {
			Page page = wlEsOrderDao.search(pageSize, currPage, orderStateEk, values, namesValue, evaluateState,goodName);
			return page;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询订单信息(分页)
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param paraMap 参数Map对象
	 * @return 订单信息
	 */
	public Page search(int pageSize, int currPage, Map paraMap) {
		try {
			return wlEsOrderDao.search(pageSize, currPage, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询订单信息
	 * @param orderStateEk 状态
	 * @param fromDate 开始时间
	 * @param toDate 结束时间
	 * @return 数据列表
	 */
	public List<Map> findOrderList(String orderStateEk, String fromDate, String toDate) {
		try {
			return wlEsOrderDao.findOrderList(orderStateEk, fromDate, toDate);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过订单id查找其下商品信息
	 * @param orderId 订单id
	 * @return 商品信息列表
	 */
	public List<WlEsOrderGood> findOrderGoodList(String orderId) {
		try {
			return wlEsOrderGoodDao.findOrderGoodList(orderId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存会员订单
	 * @param user 登录用户信息
	 * @param shoppingCartId 购物车id数组
	 * @param deliveryAddrId 收货地址id
	 * @param buyMsg 买家留言
	 * @return 订单详情
	 */
	public Map saveOrder(User user, String[] shoppingCartId, String deliveryAddrId, String buyMsg) {
		try {
			Map map = new HashMap();// 添加完返回的map信息
			List<WlEsOrder> orders = new ArrayList<WlEsOrder>();

			// 购物车列表
			List<WlEsMyShoppingCart> wlEsMyShoppingCarts = wlEsMyShoppingCartService.findShoppingCartListByCartIds(shoppingCartId);
			WlEsMyDeliveryAddrService wlEsMyDeliveryAddrService = (WlEsMyDeliveryAddrService) Sc.getBean("wl.es.WlEsMyDeliveryAddrService");
			// 收货地址
			WlEsMyDeliveryAddr wlEsMyDeliveryAddr = (WlEsMyDeliveryAddr) wlEsMyDeliveryAddrService.getObject(deliveryAddrId);

			// 订单主表添加
			WlEsOrder wlEsOrder = new WlEsOrder();
			wlEsOrder.setOrderNo(getNewOrderNO());
			wlEsOrder.setMemberId(user.getId());
			wlEsOrder.setReceiver(wlEsMyDeliveryAddr.getReceiver());
			wlEsOrder.setProvince(wlEsMyDeliveryAddr.getProvince());
			wlEsOrder.setProvinceId(wlEsMyDeliveryAddr.getProvinceId());
			wlEsOrder.setCity(wlEsMyDeliveryAddr.getCity());
			wlEsOrder.setCityId(wlEsMyDeliveryAddr.getCityId());
			wlEsOrder.setArea(wlEsMyDeliveryAddr.getArea());
			wlEsOrder.setAreaId(wlEsMyDeliveryAddr.getAreaId());
			wlEsOrder.setAddr(wlEsMyDeliveryAddr.getAddr());
			wlEsOrder.setMobile(wlEsMyDeliveryAddr.getMobile());
			wlEsOrder.setOrderTime(new Date());
			wlEsOrder.setBuyMsg(buyMsg);
			wlEsOrder.setAmt(this.getShoppingCartsAmtByCorpId(wlEsMyShoppingCarts));
			wlEsOrder.setOrderStateEk("0");
			wlEsOrderDao.saveObject(wlEsOrder);
			// 订单从表添加
			List<WlEsOrderGood> orderGoods = this.saveOrderGoods(wlEsOrder.getOrderId(), wlEsMyShoppingCarts);
			// wlEsOrder.setGoodList(orderGoods);

			orders.add(wlEsOrder);

			map.put("orders", orders);
			map.put("addr", wlEsMyDeliveryAddr);
			return map;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 存储订单从表
	 * @param orderId 订单号
	 * @param tpEsMyShoppingCarts 购物车列表
	 * @return 订单从表
	 */
	private List saveOrderGoods(String orderId, List<WlEsMyShoppingCart> wlEsMyShoppingCarts) {
		try {
			List<WlEsOrderGood> goods = new ArrayList<WlEsOrderGood>();
			for (WlEsMyShoppingCart cart : wlEsMyShoppingCarts) {
				WlEsOrderGood good = new WlEsOrderGood();
				good.setOrderId(orderId);
				// good.setName(cart.getName());
				good.setNum(cart.getNum());
				good.setPrice(cart.getPrice());
				good.setUnitName(cart.getUnitName());
				good.setSellId(cart.getSellId());
				wlEsOrderGoodDao.saveObject(good);
				// good.setFirstPic(wlCmSellService.getFirstPic(cart.getSellId()));
				goods.add(good);
				wlEsMyShoppingCartService.deleteObject(cart.getShoppingCartId());

			}
			return goods;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过购物车和企业id获取购物车内对应企业总的商品价格
	 * @param tpEsMyShoppingCarts 购物车列表
	 * @return 购物车内对应企业总的商品价格
	 */
	private double getShoppingCartsAmtByCorpId(List<WlEsMyShoppingCart> wlEsMyShoppingCarts) {
		try {
			double amt = 0;
			for (WlEsMyShoppingCart cart : wlEsMyShoppingCarts) {
				amt += cart.getPrice() * cart.getNum();
			}
			return amt;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 生成订单号
	 * @return 订单号
	 */
	public String getNewOrderNO() {
		try {
			List<String> list = wlEsOrderDao.getNewOrderCode("3");
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(list)) {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMMdd");
				String maxCodeString = list.get(0);
				if (ValidateUtil.isEmpty(maxCodeString)) {// 无数据的时候返回第一个订单编号
					newCode = nowStr + "00001";
				}
				else {
					if (maxCodeString.contains(nowStr)) {// 如果存在同一分钟内的编号，则为之后的流水号添加1
						String maxStrCode = maxCodeString.replaceAll(nowStr, "");
						int maxCode = Integer.parseInt(maxStrCode);
						maxCode++;
						maxStrCode = "" + maxCode;
						int length = maxStrCode.length();
						for (int i = 0; i < 5 - length; i++) {
							maxStrCode = "0" + maxStrCode;
						}
						newCode = nowStr + maxStrCode;
					}
					else {// 否则直接生成这一分钟的第一个流水号
						newCode = nowStr + "00001";
					}
				}
			}
			else {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMMdd");
				newCode = nowStr + "00001";
			}
			return newCode;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取当前登录用户的订单
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param type 获取数据时间类型，0：一个月内，1：一个月前
	 * @param memberId 会员id
	 * @return 用户订单列表
	 */
	public Page findOrderPageByMember(int pageSize, int currPage, String type, String memberId) {
		try {
			Page orderPage = wlEsOrderDao.findOrderPageByMember(pageSize, currPage, type, memberId);
			List<WlEsOrder> wlEsOrders = orderPage.getItems();
			for (WlEsOrder order : wlEsOrders) {
				// 先获取出订单对应的订单从表商品信息列表
				List<WlEsOrderGood> goods = wlEsOrderGoodDao.findOrderGoodList(order.getOrderId());
				for (WlEsOrderGood good : goods) {
					// 通过商品信息中的上架商品id获取对应的第一张图片的路径，如果没有图片则置为空
					// good.setFirstPic(wlCmSellService.getFirstPic(good.getSellId()));
				}
				// order.setGoodList(goods);
			}
			return orderPage;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除订单
	 * @param orderId 订单ID
	 */
	public void deleteOrderByOrderId(String orderId) {
		try {
			// 删除订单
			this.deleteObject(orderId);
			// 删除订单下货物
			wlEsOrderGoodDao.deleteOrderGood(orderId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改订单状态与商品销售数量
	 * @param wlEsOrder 订单对象
	 */
	public void updateOrderStateAndGoodTotal(WlEsOrder wlEsOrder) {
		try {
			this.saveObject(wlEsOrder);
			List<WlEsOrderGood> list = wlEsOrderGoodDao.findOrderGoodList(wlEsOrder.getOrderId());
			for (WlEsOrderGood wlEsOrderGood : list) {
				WlCmSell wlCmSell = (WlCmSell) wlCmSellService.getObject(wlEsOrderGood.getSellId());
				int num = (int) wlEsOrderGood.getNum();
				wlCmSell.setSaleTotal(wlCmSell.getSaleTotal() + num);
				wlCmSellService.saveObject(wlCmSell);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 
	 * ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
	 * 判断该笔订单是否在商户网站中已经做过处理
	 * 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
	 * 如果有做过处理，不执行商户的业务程序
	 * @param orderNo 订单号
	 */
	public void updateOrderStateToPayed(String orderNo) {
		try {
			WlEsOrder order = wlEsOrderDao.getWlEsOrderByOrderNo(orderNo);
			if ("0".equals(order.getOrderStateEk())) {// 待付款状态的才进行修改状态
				order.setOrderStateEk("1");
				this.updateObject(order);
				// User user = new User();
				// 将售后维修改为已支付
				WlEsCustRepairService wlEsCustRepairService = (WlEsCustRepairService) Sc.getBean("wl.es.WlEsCustRepairService");
				WlEsCustRepair wlEsCustRepair = wlEsCustRepairService.getWlEsCustRepairByOrderNo(order.getOrderNo());
				if (!ValidateUtil.isNull(wlEsCustRepair)) {
					wlEsCustRepair.setPaymentStatusEk("1");
					wlEsCustRepairService.saveObject(wlEsCustRepair);
				}
				// 将配件申请修改为已支付
				WlEsApplyService wlEsApplyService = (WlEsApplyService) Sc.getBean("wl.es.WlEsApplyService");
				WlEsApply wlEsApply = wlEsApplyService.getWlEsApplyByOrderNo(order.getOrderNo());
				if (!ValidateUtil.isNull(wlEsApply)) {
					wlEsApply.setPaymentStatusEk("1");
					wlEsApplyService.saveObject(wlEsApply);
				}
				wlEsTodoMsgService.saveToDoMsg(order.getOrderId(), "ORDER");// 生成待发送信息
			}
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
	 * @param user 当前登录用户
	 * @param amt 订单总价
	 * @param buyMsg 买家留言
	 * @param payModeEk 支付方式
	 * @param lang 语言类型
	 * @return 订单id
	 */
	public String saveOrder(String deliveryAddrId, String[] sellIds, long[] numsList, User user, double amt, String buyMsg, String payModeEk,String lang) {
		try {
			int result=0;
			if("english".equals(lang)){
				result = wlCmSellEnService.getSellCountBySellIdsAndState(sellIds, "NO_SHELVE");// 查询勾选的商品是否含有已下架
			}else{
				result = wlCmSellService.getSellCountBySellIdsAndState(sellIds, "NO_SHELVE");// 查询勾选的商品是否含有已下架
			}
			if (result > 0) {// 如果含有已下架，则停止当前操作
				throw new BaseException(Lang.getString("wl.es.WlEsOrderService.containDown"));
			}
			String areaId="";
			WlEsMyDeliveryAddr wlEsMyDeliveryAddr = (WlEsMyDeliveryAddr) wlEsMyDeliveryAddrService.getObject(deliveryAddrId);
			if(!ValidateUtil.isEmpty(areaId)){
				areaId=wlEsMyDeliveryAddr.getCityId();
			}else{
				areaId=wlEsMyDeliveryAddr.getProvinceId();
			}
			Map freightMap=wlEsFeeTempService.getFreight("", sellIds, areaId);
			double firstFee=(Double) freightMap.get("firstFee");
			WlEsOrder wlEsOrder = new WlEsOrder();
			wlEsOrder.setOrderNo(getNewOrderNO());
			wlEsOrder.setOrderTime(new Date());
			wlEsOrder.setMemberId(user.getId());
			wlEsOrder.setReceiver(wlEsMyDeliveryAddr.getReceiver());
			wlEsOrder.setMobile(wlEsMyDeliveryAddr.getMobile());
			wlEsOrder.setProvince(wlEsMyDeliveryAddr.getProvince());
			wlEsOrder.setProvinceId(wlEsMyDeliveryAddr.getProvinceId());
			wlEsOrder.setCity(wlEsMyDeliveryAddr.getCity());
			wlEsOrder.setCityId(wlEsMyDeliveryAddr.getCityId());
			wlEsOrder.setArea(wlEsMyDeliveryAddr.getArea());
			wlEsOrder.setAreaId(wlEsMyDeliveryAddr.getAreaId());
			wlEsOrder.setAddr(wlEsMyDeliveryAddr.getAddr());
			wlEsOrder.setAmt(amt+firstFee);
			wlEsOrder.setPayAmt(amt+firstFee);
			wlEsOrder.setBuyMsg(buyMsg);
			wlEsOrder.setOrderStateEk("0");
			wlEsOrder.setPayModeEk(payModeEk);
			wlEsOrder.setCreator(user.getName());
			wlEsOrder.setCreateTime(new Date());
			wlEsOrder.setBuyChannelEk("3");
			int goodNum = 0;
			for (long num : numsList) {
				goodNum += num;
			}
			wlEsOrder.setGoodNum(goodNum);
			this.saveObject(wlEsOrder);
			int i = 0;
			// 保存订单对应的商品列表
			for (String sellId : sellIds) {
				WlEsOrderGood wlEsOrderGood = new WlEsOrderGood();
				
				if("english".equals(lang)){
					 WlCmSellEn wlCmSellEn = (WlCmSellEn) wlCmSellEnService.getObject(sellId);
					 wlEsOrderGood.setSellId(wlCmSellEn.getSellId());
					 wlEsOrderGood.setProductName(wlCmSellEn.getProductName());
				  	 wlEsOrderGood.setPrice(wlCmSellEn.getPrice());
				  	 wlEsOrderGood.setUnitName(wlCmSellEn.getUnitName());
				}else{					
					WlCmSell wlCmSell = (WlCmSell) wlCmSellService.getObject(sellId);
					wlEsOrderGood.setSellId(wlCmSell.getSellId());
					wlEsOrderGood.setProductName(wlCmSell.getProductName());
					wlEsOrderGood.setPrice(wlCmSell.getPrice());
					wlEsOrderGood.setUnitName(wlCmSell.getUnitName());
				}				
				wlEsOrderGood.setOrderId(wlEsOrder.getOrderId());
				
				wlEsOrderGood.setNum(numsList[i]);
				i++;
				wlEsOrderGoodDao.saveObject(wlEsOrderGood);
			}
			// 删除购物车
			wlEsMyShoppingCartService.deleteMyShoppingCart(sellIds);
			if ("0".equals(wlEsOrder.getPayModeEk())) {// 如果是货到付款，生成待发送信息
				wlEsTodoMsgService.saveToDoMsg(wlEsOrder.getOrderId(), "ORDER");
			}
			return wlEsOrder.getOrderId();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 根据订单编号返回订单对象
	 * @param orderNo 订单编号
	 * @return 订单对象
	 */
	public WlEsOrder getWlEsOrderByOrderNo(String orderNo) {
		try {
			WlEsOrder order = wlEsOrderDao.getWlEsOrderByOrderNo(orderNo);
			return order;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 将一天前的待付款订单关闭
	 */
	public void updateOrderState() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 取得当前时间
			Date curuentDate = new Date();
			String curDate = sdf.format(curuentDate);
			// 取得当前时间1天前时间
			Date date = DateUtil.dateAdd(Calendar.DAY_OF_MONTH, -1, curuentDate);
			// 将一天前的订单关闭
			String oneFontDate = sdf.format(date);
			wlEsOrderDao.updateOrderState("4", curDate, oneFontDate);
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
	 * @param user 当前登录用户
	 */
	public void updateDeliveryData(String orderId, String deliveryNo, String logisticEk, User user) {
		try {
			WlEsOrder order = (WlEsOrder) this.getObject(orderId);
			order.setOrderStateEk("2");
			order.setCorpUserId(user.getId());
			order.setCorpUser(user.getName());
			order.setLogisticEk(logisticEk);
			order.setDeliveryNo(deliveryNo);
			order.setDeliveryTime(new Date());
			this.updateObject(order);
			WlEsApplyService wlEsApplyService = (WlEsApplyService) Sc.getBean("wl.es.WlEsApplyService");
			WlEsApply wlEsApply = wlEsApplyService.getWlEsApplyByOrderNo(order.getOrderNo());
			if (!ValidateUtil.isNull(wlEsApply)) {// 将配件申请更改为发货
				wlEsApply.setIsDeliveryFlag("1");// 发货状态设为”是“
				wlEsApply.setCorpUserId(user.getId());
				wlEsApply.setCorpUser(user.getName());
				wlEsApply.setLogisticEk(logisticEk);
				wlEsApply.setDeliveryNo(deliveryNo);
				wlEsApply.setDeliveryTime(new Date());
				wlEsApplyService.updateObject(wlEsApply);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存淘宝订单
	 * @param startDate 搜索开始时间
	 * @param endDate 搜索结束时间
	 */
	public void saveTaobaoOrder(Date startDate, Date endDate) {
		try {
			TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23378624", "75aae0249a4dce7c916e75a7ac387821");
			TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
			String getField = "tid,type,status,payment,orders,rx_audit_status,receiver_name,receiver_state,receiver_city,receiver_address,receiver_zip";
			getField += ",receiver_mobile,receiver_phone,receiver_town,receiver_country,total_fee,received_payment,created";
			req.setFields(getField);
			// Date date = new Date();
			req.setStartModified(startDate);
			req.setEndModified(endDate);
			req.setStatus("WAIT_SELLER_SEND_GOODS");
			// req.setType("fixed");
			// req.setBuyerNick("zhangsan");
			// req.setExtType("service");
			// req.setTag("time_card");
			// req.setPageNo(1L);
			// req.setRateStatus("RATE_UNBUYER");
			// req.setPageSize(100L);
			// req.setUseHasNext(true);
			TradesSoldIncrementGetResponse rsp = client.execute(req, "6101b27289ba6bd979c9f1d027d8c7928d11221ecd41e902292720002");
			String jsonData = rsp.getBody();// 淘宝返回来的json数据
			if (!ValidateUtil.isEmpty(jsonData)) {// 如果数据不为空
				JSONObject object = new JSONObject(jsonData);
				JSONObject root = object.getJSONObject("trades_sold_increment_get_response");
				JSONObject trades = root.getJSONObject("trades");
				JSONArray tradesList = trades.getJSONArray("trade");
				for (int i = 0; i < tradesList.length(); i++) {// 订单列表
					JSONObject entityObj = tradesList.getJSONObject(i);
					String orderNo = entityObj.getString("tid");
					if (ValidateUtil.isNull(wlEsOrderDao.getWlEsOrderByOrderNo(orderNo))) {// 如果订单不存在，则保存
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date orderTime = df.parse(entityObj.getString("created"));
						WlEsOrder wlEsOrder = new WlEsOrder();
						wlEsOrder.setOrderNo(orderNo);
						wlEsOrder.setOrderTime(orderTime);
						wlEsOrder.setCreateTime(new Date());
						wlEsOrder.setReceiver(entityObj.getString("receiver_name"));
						String mobile = entityObj.getString("receiver_mobile");
						if (!ValidateUtil.isEmpty(mobile)) {// 手机号码
							wlEsOrder.setMobile(mobile);
						}
						else {// 如果手机号码为空，则用电话号码
							if (entityObj.has("receiver_phone")) {// 如果含有receiver_phone
								wlEsOrder.setMobile(entityObj.getString("receiver_phone"));
							}
						}
						wlEsOrder.setProvince(entityObj.getString("receiver_state"));
						wlEsOrder.setCity(entityObj.getString("receiver_city"));
						// 如果含有receiver_town(镇如果没填，天猫返回来的json数据没有receiver_town字段)
						if (entityObj.has("receiver_town")) {
							wlEsOrder.setArea(entityObj.getString("receiver_town"));
						}
						wlEsOrder.setAddr(entityObj.getString("receiver_address"));
						wlEsOrder.setAmt(entityObj.getDouble("total_fee"));
						wlEsOrder.setPayAmt(entityObj.getDouble("payment"));
						wlEsOrder.setBuyChannelEk("0");// 购买渠道天猫
						wlEsOrder.setOrderStateEk("1");// 待发货
						// 保存订单主表
						this.saveObject(wlEsOrder);
						int goodNum = 0;
						// 订单商品列表
						JSONObject orders = entityObj.getJSONObject("orders");
						JSONArray orderList = orders.getJSONArray("order");
						for (int j = 0; j < orderList.length(); j++) {
							JSONObject entity = orderList.getJSONObject(j);
							WlEsOrderGood wlEsOrderGood = new WlEsOrderGood();
							wlEsOrderGood.setProductName(entity.getString("title"));
							wlEsOrderGood.setNum(entity.getLong("num"));
							wlEsOrderGood.setPrice(entity.getDouble("price"));
							wlEsOrderGood.setOrderId(wlEsOrder.getOrderId());
							// 保存商品列表
							wlEsOrderGoodDao.saveObject(wlEsOrderGood);
							goodNum += entity.getInt("num");// 商品总数量
						}
						wlEsOrder.setGoodNum(goodNum);
						this.saveObject(wlEsOrder);
						// 存到待发送消息
						wlEsTodoMsgService.saveToDoMsg(wlEsOrder.getOrderId(), "ORDER");
					}
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 上传订单信息excel
	 * @param path 上传文件ID
	 * @param buyChannelEk 购买渠道
	 * @param user 当前登录用户
	 */
	public void saveOrderExcelData(String path, String buyChannelEk, User user) {
		try {
			WlCmDocument doc = (WlCmDocument) wlCmDocumentService.getObject(path);
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
			File file = new File(Config.uploadFilesPath + "//" + docPath.getPath(), doc.getFileName());
			List<Map> list = findOrderExcelData(file);// 订单信息excel数据转成list列表
			List orderList = new ArrayList();
			for (Map map : list) {
				String orderNo = (String) map.get("orderNo");
				WlEsOrder order = this.getWlEsOrderByOrderNo(orderNo);
				if (ValidateUtil.isNull(order)) {// 订单号不存在
					String str = (String) map.get("orderTime");
					int firstIndex = str.indexOf("/");
					String month = str.substring(0, firstIndex);
					int secondIndex = str.indexOf("/", firstIndex + 1);
					String day = str.substring(firstIndex + 1, secondIndex);
					int threeIndex = str.indexOf(" ");
					String year = str.substring(secondIndex + 1, threeIndex);
					String time = str.substring(threeIndex, str.length());
					if (year.length() == 2) {
						year = "20" + year;
					}
					if (time.length() == 6) {
						time += ":00";
					}
					String dayTime = year + "-" + month + "-" + day + time;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date orderTime = sdf.parse(dayTime);
					String receiver = (String) map.get("receiver");
					String mobile = (String) map.get("mobile");
					String addr = (String) map.get("addr");
					double amt = Double.parseDouble((String) map.get("amt"));
					double payAmt = Double.parseDouble((String) map.get("payAmt"));
					String buyMsg = (String) map.get("buyMsg");
					int goodNum = Integer.parseInt((String) map.get("goodNum"));
					String taoBaoNo = (String) map.get("taoBaoNo");
					String memo = (String) map.get("memo");
					String serviceTaoBaoNo = (String) map.get("serviceTaoBaoNo");
					WlEsOrder wlEsOrder = new WlEsOrder();
					wlEsOrder.setOrderNo(orderNo);
					wlEsOrder.setOrderTime(orderTime);
					wlEsOrder.setAddr(addr);
					wlEsOrder.setReceiver(receiver);
					wlEsOrder.setMobile(mobile);
					wlEsOrder.setAmt(payAmt);
					wlEsOrder.setPayAmt(payAmt);
					wlEsOrder.setGoodNum(goodNum);
					wlEsOrder.setOrderStateEk("1");
					wlEsOrder.setBuyChannelEk(buyChannelEk);
					wlEsOrder.setBuyMsg(buyMsg);
					wlEsOrder.setMemo(memo);
					wlEsOrder.setCreateTime(new Date());
					wlEsOrder.setCreator(user.getName());
					wlEsOrder.setServiceTaoBaoNo(serviceTaoBaoNo);
					wlEsOrder.setTaoBaoNo(taoBaoNo);
					orderList.add(wlEsOrder);
				}
				else {// 订单号已存在
					throw new BaseException(orderNo + Lang.getString("wl.es.wlEsOrderService.hasExist"));
				}
			}
			// 保存订单列表
			wlEsOrderDao.saveList(orderList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 上传订单信息excel解析成list列表
	 * @return 订单信息列表
	 */
	public List findOrderExcelData(File file) {
		try {
			List resultList = new ArrayList();
			// 创建输入流
			FileInputStream fis = new FileInputStream(file);
			// 获取Excel文件对象
			jxl.Workbook rwb = Workbook.getWorkbook(fis);
			// 获取文件的第一个工作表
			Sheet sheet = rwb.getSheet(0);
			Map resultMap = new HashMap();// 用于校验eXcel表格单号是否重复
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i);
				if (cells.length == 0) {
					break;
				}
				if (cells.length < 30) {// 不是订单信息excel
					throw new BaseException(Lang.getString("wl.es.wlEsOrderService.noOrderTemp"));
				}
				Map<String, String> map = new HashMap<String, String>();
				String orderNo = cells[0].getContents();
				if (ValidateUtil.isEmpty(orderNo)) {// 订单号为空
					throw new BaseException((i + 1) + Lang.getString("wl.es.wlEsOrderService.col") + Lang.getString("wl.es.wlEsOrderService.orderNoIsEmpty"));
				}
				if (!resultMap.containsKey(orderNo)) {// 订单号不存在
					resultMap.put(orderNo, i + 1);
					String orderTime = cells[17].getContents();
					String receiver = cells[12].getContents();
					if (ValidateUtil.isEmpty(receiver)) {// 收货人为空
						throw new BaseException((i + 1) + Lang.getString("wl.es.wlEsOrderService.col") + Lang.getString("wl.es.wlEsOrderService.receiverIsEmpty"));
					}
					String mobile = cells[16].getContents();
					if (ValidateUtil.isEmpty(mobile)) {// 手机为空
						throw new BaseException((i + 1) + Lang.getString("wl.es.wlEsOrderService.col") + Lang.getString("wl.es.wlEsOrderService.mobileIsEmpty"));
					}
					String addr = "";
					if (cells.length >= 37) {// 如果有第37列的修改地址
						addr = cells[36].getContents();// 优先读取修改后的地址
					}
					if (ValidateUtil.isEmpty(addr)) {// 如果没有修改后的地址，则读原始地址
						addr = cells[13].getContents();// 原始地址
					}
					if (ValidateUtil.isEmpty(addr)) {// 地址为空
						throw new BaseException((i + 1) + Lang.getString("wl.es.wlEsOrderService.col") + Lang.getString("wl.es.wlEsOrderService.addrIsEmpty"));
					}
					String amt = cells[6].getContents();
					if (ValidateUtil.isEmpty(amt)) {// 总金额为空
						throw new BaseException((i + 1) + Lang.getString("wl.es.wlEsOrderService.col") + Lang.getString("wl.es.wlEsOrderService.amtIsEmpty"));
					}
					String payAmt = cells[8].getContents();
					if (ValidateUtil.isEmpty(payAmt)) {// 买家实际支付金额为空
						throw new BaseException((i + 1) + Lang.getString("wl.es.wlEsOrderService.col") + Lang.getString("wl.es.wlEsOrderService.payAmtIsEmpty"));
					}
					String buyMsg = cells[11].getContents();
					String goodNum = cells[24].getContents();
					String taoBaoNo = cells[1].getContents();
					String memo = cells[23].getContents();
					int index = memo.indexOf("#");// 第一个#号位置
					int secondIndex = -1;
					String serviceTaoBaoNo = "";
					if (index > -1) {
						secondIndex = memo.indexOf("#", index + 1);// 第二个#号位置
						if (secondIndex > -1) {
							serviceTaoBaoNo = memo.substring(index + 1, secondIndex);
						}
					}
					memo = memo.replaceFirst("#" + serviceTaoBaoNo + "#", "");
					map.put("orderNo", orderNo);
					map.put("orderTime", orderTime);
					map.put("receiver", receiver);
					map.put("mobile", mobile);
					map.put("addr", addr);
					map.put("amt", amt);
					map.put("payAmt", payAmt);
					map.put("buyMsg", buyMsg);
					map.put("goodNum", goodNum);
					map.put("taoBaoNo", taoBaoNo);
					map.put("memo", memo);
					map.put("serviceTaoBaoNo", serviceTaoBaoNo);
					resultList.add(map);
				}
				else {// excel里面订单号重复
					int row = (Integer) resultMap.get(orderNo);
					String result = Lang.getString("wl.es.wlEsOrderService.excel") + row + Lang.getString("wl.es.wlEsOrderService.col");
					result += Lang.getString("wl.es.wlEsOrderService.and");
					result += (i + 1) + Lang.getString("wl.es.wlEsOrderService.col");
					result += Lang.getString("wl.es.wlEsOrderService.sameOrder") + orderNo;
					throw new BaseException(result);
				}
			}
			fis.close();
			return resultList;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 上传订单商品信息excel
	 * @param path 上传文件ID
	 */
	public void saveOrderDetailExcelData(String path) {
		try {
			WlCmDocument doc = (WlCmDocument) wlCmDocumentService.getObject(path);
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
			File file = new File(Config.uploadFilesPath + "//" + docPath.getPath(), doc.getFileName());
			List<WlEsOrderGood> list = findOrderDetailExcelData(file);
			// 保存订单商品列表
			wlEsOrderGoodDao.saveList(list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	public List<WlEsOrderGood> findOrderDetailExcelData(File file) {
		try {
			List<WlEsOrderGood> resultList = new ArrayList();
			// 创建输入流
			FileInputStream fis = new FileInputStream(file);
			// 获取Excel文件对象
			jxl.Workbook rwb = Workbook.getWorkbook(fis);
			// 获取文件的第一个工作表
			Sheet sheet = rwb.getSheet(0);
			Map resultMap = new HashMap();// 用于校验Excel表格单号是否重复
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i);
				if (cells.length == 0) {
					break;
				}
				if (cells.length < 9) {// 不是订单商品信息excel
					throw new BaseException(Lang.getString("wl.es.wlEsOrderService.noOrderDetailTemp"));
				}
				String orderNo = cells[0].getContents();
				WlEsOrder wlEsOrder = this.getWlEsOrderByOrderNo(orderNo);
				if (ValidateUtil.isNull(wlEsOrder)) {// 订单不存在，需订单信息导入先导入
					throw new BaseException(orderNo + Lang.getString("wl.es.wlEsOrderService.noOrder"));
				}
				else {
					if (!"1".equals(wlEsOrder.getOrderStateEk())) {// 订单不是待发货状态
						throw new BaseException(orderNo + Lang.getString("wl.es.wlEsOrderService.isNotDelivery"));
					}
				}

				Map itemCdMap = null;
				if (cells.length == 9 || (cells.length == 10 && ValidateUtil.isEmpty(cells[9].getContents()))) {// 没有第10列的商家编码或有第10列但为空
					String result = Lang.getString("wl.es.wlEsOrderService.excel");
					result += (i + 1);
					result += Lang.getString("wl.es.wlEsOrderService.col");
					result += Lang.getString("wl.es.wlEsOrderService.itemIsEmpty");
					throw new BaseException(result);
				}
				else {// 有商家编码
					String itemCd = cells[9].getContents();
					// 校验excel里同一个订单的商品不能重复出现
					if (!resultMap.containsKey(orderNo)) {// 此orderNo在map里不存在，则添加
						itemCdMap = new HashMap();
						itemCdMap.put(itemCd, itemCd);
						resultMap.put(orderNo, itemCdMap);
					}
					else {
						itemCdMap = (Map) resultMap.get(orderNo);
						if (itemCdMap.containsKey(itemCd)) {// 同一个订单存在重复商品
							String result = orderNo + Lang.getString("wl.es.wlEsOrderService.order");
							result += itemCd + Lang.getString("wl.es.wlEsOrderService.repertItemCd");
							throw new BaseException(result);
						}
						itemCdMap.put(itemCd, itemCd);
					}
					// 校验此商家编码在产品上架管理是否有录入
					WlCmSell wlCmSell = wlCmSellService.getWlCmSellByItemCd(itemCd);
					if (ValidateUtil.isNull(wlCmSell)) {// 商品不存在，请在上架商品中先录入
						throw new BaseException(itemCd + Lang.getString("wl.es.wlEsOrderService.sellNoExist"));
					}
					// 校验此订单的这个商品是否有导入过
					WlEsOrderGood wlEsOrderGood = wlEsOrderGoodDao.getWlEsOrderGood(wlEsOrder.getOrderId(), wlCmSell.getSellId());
					if (!ValidateUtil.isNull(wlEsOrderGood)) {// 此订单已导入过此商品，不能再次导入
						throw new BaseException(orderNo + Lang.getString("wl.es.wlEsOrderService.order") + itemCd + Lang.getString("wl.es.wlEsOrderService.hasUploadItem"));
					}
					// 构造商品
					wlEsOrderGood = new WlEsOrderGood();
					double price = Double.parseDouble(cells[2].getContents());
					int goodNum = Integer.parseInt(cells[3].getContents());
					wlEsOrderGood.setNum(goodNum);
					wlEsOrderGood.setOrderId(wlEsOrder.getOrderId());
					wlEsOrderGood.setPrice(price);
					wlEsOrderGood.setProductName(wlCmSell.getProductName());
					wlEsOrderGood.setSellId(wlCmSell.getSellId());
					wlEsOrderGood.setUnitName(wlCmSell.getUnitName());
					resultList.add(wlEsOrderGood);
				}

			}
			fis.close();
			return resultList;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存订单商品列表
	 * @param orderId 订单id
	 * @param goodList 商品列表
	 */
	public void saveGoodList(String orderId, List<WlEsOrderGood> orderGoodList) {
		try {
			double amt = 0;
			int goodNum = 0;
			List tempOrderGoodList = new ArrayList();
			for (WlEsOrderGood wlEsOrderGood : orderGoodList) {
				amt += wlEsOrderGood.getPrice() * wlEsOrderGood.getNum();
				WlEsOrderGood newWlEsOrderGood = new WlEsOrderGood();
				goodNum += wlEsOrderGood.getNum();
				newWlEsOrderGood.setNum(wlEsOrderGood.getNum());
				newWlEsOrderGood.setOrderId(orderId);
				newWlEsOrderGood.setPrice(wlEsOrderGood.getPrice());
				newWlEsOrderGood.setProductName(wlEsOrderGood.getProductName());
				newWlEsOrderGood.setSellId(wlEsOrderGood.getSellId());
				newWlEsOrderGood.setUnitName(wlEsOrderGood.getUnitName());
				tempOrderGoodList.add(newWlEsOrderGood);
			}
			WlEsOrder wlEsOrder = (WlEsOrder) this.getObject(orderId);
			wlEsOrder.setAmt(amt);
			wlEsOrder.setPayAmt(amt);
			wlEsOrder.setGoodNum(goodNum);
			// 保存订单
			this.saveObject(wlEsOrder);
			// 删除订单商品
			wlEsOrderGoodDao.deleteOrderGood(orderId);
			// 保存订单商品
			wlEsOrderGoodDao.saveList(tempOrderGoodList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}