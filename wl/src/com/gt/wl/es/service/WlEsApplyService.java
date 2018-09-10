package com.gt.wl.es.service;

import java.util.Date;
import java.util.List;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmArea;
import com.gt.wl.cm.service.WlCmAreaService;
import com.gt.wl.cm.service.WlCmCommonSetService;
import com.gt.wl.es.dao.WlEsApplyDao;
import com.gt.wl.es.dao.WlEsOrderGoodDao;
import com.gt.wl.es.model.WlEsApply;
import com.gt.wl.es.model.WlEsOrder;
import com.gt.wl.es.model.WlEsOrderGood;

/**
 * 配件申请Service层
 * @author liuyj
 * 
 */
@Service("wl.es.WlEsApplyService")
public class WlEsApplyService extends BaseService {
	private WlEsApplyDao wlEsApplyDao = (WlEsApplyDao) Sc.getBean("wl.es.WlEsApplyDao");
	private WlCmAreaService wlCmAreaService = (WlCmAreaService) Sc.getBean("wl.cm.WlCmAreaService");
	private WlCmCommonSetService wlCmCommonSetService = (WlCmCommonSetService) Sc.getBean("wl.cm.WlCmCommonSetService");
	private WlEsOrderGoodDao wlEsOrderGoodDao = (WlEsOrderGoodDao) Sc.getBean("wl.es.WlEsOrderGoodDao");

	public WlEsApplyService() {
		baseDao = wlEsApplyDao;
	}

	/**
	 * 查询配件申请
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
	 * @return 分页对象
	 */
	public Page search(int currPage, int pageSize, String keyId, String keyValue, String isDeliveryFlag, String applyTimeStart, String applyTimeEnd, String deliveryTimeStart, String deliveryTimeEnd, String paymentStatusEk) {
		try {
			return wlEsApplyDao.search(currPage, pageSize, keyId, keyValue, isDeliveryFlag, applyTimeStart, applyTimeEnd, deliveryTimeStart, deliveryTimeEnd, paymentStatusEk);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 
	 * @param wlEsApply 配件申请对象
	 * @param user 当前登录用户
	 * @return 订单id即orderId
	 */
	public String saveData(WlEsApply wlEsApply, User user, String langType) {
		try {
			String orderId = "";
			if (ValidateUtil.isEmpty(wlEsApply.getApplyId())) {// 新增
				wlEsApply.setName(user.getName());// 申请人
				wlEsApply.setCreator(user.getName());// 创建人
			}
			if ("CN".equals(langType)) {
				WlCmArea wlCmArea = new WlCmArea();
				if (!ValidateUtil.isEmpty(wlEsApply.getProvinceId())) {
					wlCmArea = (WlCmArea) wlCmAreaService.getObject(wlEsApply.getProvinceId());
					wlEsApply.setProvince(wlCmArea.getName());
				}
				if (!ValidateUtil.isEmpty(wlEsApply.getCityId())) {
					wlCmArea = (WlCmArea) wlCmAreaService.getObject(wlEsApply.getCityId());
					wlEsApply.setCity(wlCmArea.getName());
				}
				if (!ValidateUtil.isEmpty(wlEsApply.getAreaId())) {
					wlCmArea = (WlCmArea) wlCmAreaService.getObject(wlEsApply.getAreaId());
					wlEsApply.setArea(wlCmArea.getName());
				}
			}
			if ("1".equals(wlEsApply.getPayModeEk())) {// 如果是在线支付则生成订单
				WlEsOrderService wlEsOrderService = (WlEsOrderService) Sc.getBean("wl.es.WlEsOrderService");
				// 生成订单主表
				WlEsOrder wlEsOrder = new WlEsOrder();
				wlEsOrder.setOrderNo(wlEsOrderService.getNewOrderNO());
				wlEsOrder.setOrderTime(new Date());
				wlEsOrder.setMemberId(user.getId());
				wlEsOrder.setReceiver(wlEsApply.getReceiver());
				wlEsOrder.setProvince(wlEsApply.getProvince());
				wlEsOrder.setProvinceId(wlEsApply.getProvinceId());
				wlEsOrder.setCity(wlEsApply.getCity());
				wlEsOrder.setCityId(wlEsApply.getCityId());
				wlEsOrder.setArea(wlEsApply.getArea());
				wlEsOrder.setAreaId(wlEsApply.getAreaId());
				wlEsOrder.setAddr(wlEsApply.getAddr());
				wlEsOrder.setMobile(wlEsApply.getMobile());
				wlEsOrder.setOrderTime(new Date());
				wlEsOrder.setOrderStateEk("0");
				wlEsOrder.setGoodNum(1);
				wlEsOrder.setPayModeEk(wlEsApply.getPayModeEk());
				String postAge = wlCmCommonSetService.getWlCmCommonSetByKey("POSTAGE").getSetValue().trim();
				if (!ValidateUtil.isEmpty(postAge)) {// 设置支付的价格
					wlEsOrder.setPayAmt(Double.parseDouble(postAge));
					wlEsOrder.setAmt(Double.parseDouble(postAge));
				}
				wlEsOrderService.saveObject(wlEsOrder);// 保存订单
				wlEsApply.setPaymentStatusEk("0");// 未支付
				wlEsApply.setOrderNo(wlEsOrder.getOrderNo());// 配件申请关联订单号
				this.saveObject(wlEsApply);// 保存配件申请
				orderId = wlEsOrder.getOrderId();
				WlEsOrderGood good = new WlEsOrderGood();
				good.setOrderId(wlEsOrder.getOrderId());
				if ("EN".equals(langType)) {
					good.setProductName(Lang.getString("wl.es.wlEsApplyService.goodNameEN"));
					good.setUnitName(Lang.getString("wl.es.wlEsApplyService.unitNameEN"));
				} else {
					good.setProductName(Lang.getString("wl.es.wlEsApplyService.goodName"));
					good.setUnitName(Lang.getString("wl.es.wlEsApplyService.unitName"));
				}
				good.setNum(1);
				good.setPrice(wlEsOrder.getPayAmt());
				// 保存订单从表
				wlEsOrderGoodDao.saveObject(good);
			}
			else {// 如果是货到付款
				this.saveObject(wlEsApply);// 保存配件申请
			}
			return orderId;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据订单编号返回配件申请对象
	 * @param orderNo 订单编号
	 * @return 配件申请对象
	 */
	public WlEsApply getWlEsApplyByOrderNo(String orderNo) {
		try {
			WlEsApply wlEsApply = wlEsApplyDao.getWlEsApplyByOrderNo(orderNo);
			return wlEsApply;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改发货信息
	 * @param wlEsApply 配件申请对象
	 * @param deliveryNo 发货单号
	 * @param logisticEk 物流公司
	 * @param user 当前登录用户
	 */
	public void updateDeliveryData(WlEsApply wlEsApply, String deliveryNo, String logisticEk, User user) {
		try {
			wlEsApply.setIsDeliveryFlag("1");// 发货状态设为”是“
			wlEsApply.setCorpUserId(user.getId());
			wlEsApply.setCorpUser(user.getName());
			wlEsApply.setLogisticEk(logisticEk);
			wlEsApply.setDeliveryNo(deliveryNo);
			wlEsApply.setDeliveryTime(new Date());
			this.updateObject(wlEsApply);
			if ("1".equals(wlEsApply.getPayModeEk())) {// 如果是在线付款，将订单也改为已发货
				if (!ValidateUtil.isEmpty(wlEsApply.getOrderNo())) {
					WlEsOrderService wlEsOrderService = (WlEsOrderService) Sc.getBean("wl.es.WlEsOrderService");
					WlEsOrder order = wlEsOrderService.getWlEsOrderByOrderNo(wlEsApply.getOrderNo());
					order.setOrderStateEk("2");
					order.setCorpUserId(user.getId());
					order.setCorpUser(user.getName());
					order.setLogisticEk(logisticEk);
					order.setDeliveryNo(deliveryNo);
					order.setDeliveryTime(new Date());
					wlEsOrderService.updateObject(order);
					// wlEsOrderService.saveObject(order);
				}

			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询配件申请列表
	 * @param keyId 字段名
	 * @param keyValue 字段值
	 * @param isDeliveryFlag 是否已发货
	 * @param applyTimeStart 申请时间开始
	 * @param applyTimeEnd 申请时间结束
	 * @param deliveryTimeStart 发货时间开始
	 * @param deliveryTimeEnd 发货时间结束
	 * @param paymentStatusEk 费用支付状态
	 * @return 配件申请列表
	 */
	public List<WlEsApply> findDataList(String keyId, String keyValue, String isDeliveryFlag, String applyTimeStart, String applyTimeEnd, String deliveryTimeStart, String deliveryTimeEnd, String paymentStatusEk) {
		try {
			return wlEsApplyDao.findDataList(keyId, keyValue, isDeliveryFlag, applyTimeStart, applyTimeEnd, deliveryTimeStart, deliveryTimeEnd, paymentStatusEk);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}