package com.gt.wl.es.action;

import java.util.Date;
import java.util.List;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.es.model.WlEsMyDeliveryAddr;
import com.gt.wl.es.service.WlEsMyDeliveryAddrService;

/**
 * 我的收货地址Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/wl/es/wlEsMyDeliveryAddrAction.do", "/wl/es/wlEsMyDeliveryAddrAction.web" })
public class WlEsMyDeliveryAddrAction extends BaseAction {
	private WlEsMyDeliveryAddrService wlEsMyDeliveryAddrService = (WlEsMyDeliveryAddrService) Sc.getBean("wl.es.WlEsMyDeliveryAddrService");

	/**
	 * 保存数据
	 * @param myDeliveryAddr 收货地址对象
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlEsMyDeliveryAddr myDeliveryAddr) {
		try {
			User user = Session.getCurrUser();
			if (ValidateUtil.isEmpty(myDeliveryAddr.getDeliveryAddrId())) {// 新增
				myDeliveryAddr.setCreator(user.getName());
				myDeliveryAddr.setCreateTime(new Date());				
			}
			else {// 更新
				myDeliveryAddr.setModifier(user.getName());
				myDeliveryAddr.setModifyTime(new Date());
			}
			WlEsMyDeliveryAddr myDefalutDeliveryAddr = wlEsMyDeliveryAddrService.getMyDeliveryAddrData(user.getId(), "1");
			if (ValidateUtil.isNull(myDefalutDeliveryAddr)){
				myDeliveryAddr.setIsDefaultFlag("1");
			}
			myDeliveryAddr.setAccount(user.getLoginName());
			myDeliveryAddr.setMemberId(user.getId());

			wlEsMyDeliveryAddrService.saveData(myDeliveryAddr, user.getId());

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取会员收货地址列表
	 * @return 结果
	 */
	@RequestMapping(params = "action=getMyDeliveryAddrList")
	@ResponseBody
	public String getMyDeliveryAddrList() {
		try {
			User user = Session.getCurrUser();

			List<WlEsMyDeliveryAddr> list = wlEsMyDeliveryAddrService.getMyDeliveryAddrList(user.getId());

			return this.getJson(true, list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除会员收货地址
	 * @param deliveryAddrId 收货地址Id
	 * @return 结果
	 */
	@RequestMapping(params = "action=delMyDeliveryAddr")
	@ResponseBody
	public String delMyDeliveryAddr(String deliveryAddrId) {
		try {
			wlEsMyDeliveryAddrService.deleteObject(deliveryAddrId);

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 设置为默认的收货地址
	 * @param deliveryAddrId 收货地址Id
	 * @return 结果
	 */
	@RequestMapping(params = "action=setAsDefaultAddr")
	@ResponseBody
	public String setAsDefaultAddr(String deliveryAddrId) {
		try {
			User user = Session.getCurrUser();

			wlEsMyDeliveryAddrService.setAsDefaultAddr(deliveryAddrId, user.getId());

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取当前登录用户收货地址，如果有默认地址获取默认地址，没有获取最新的地址
	 * @return 结果
	 */
	@RequestMapping(params = "action=getDeliveryAddr")
	@ResponseBody
	public String getDeliveryAddr() {
		try {
			User user = Session.getCurrUser();

			WlEsMyDeliveryAddr wlEsMyDeliveryAddr = wlEsMyDeliveryAddrService.getMyDeliveryAddrData(user.getId(), "1");
			if (ValidateUtil.isNull(wlEsMyDeliveryAddr)) {
				wlEsMyDeliveryAddr = wlEsMyDeliveryAddrService.getMyDeliveryAddrData(user.getId(), "0");
			}
			return this.getJson(true, wlEsMyDeliveryAddr);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取收货地址
	 * @param deliveryAddrId 收货地址Id
	 * @return 结果
	 */
	@RequestMapping(params = "action=getMyDeliveryAddr")
	@ResponseBody
	public String getMyDeliveryAddr(String deliveryAddrId) {
		try {
			WlEsMyDeliveryAddr wlEsMyDeliveryAddr=null;
			if(!ValidateUtil.isEmpty(deliveryAddrId)){
				wlEsMyDeliveryAddr = (WlEsMyDeliveryAddr) wlEsMyDeliveryAddrService.getObject(deliveryAddrId);
			}else{
				wlEsMyDeliveryAddr = new WlEsMyDeliveryAddr();
			}			
			return this.getJson(true, wlEsMyDeliveryAddr);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}