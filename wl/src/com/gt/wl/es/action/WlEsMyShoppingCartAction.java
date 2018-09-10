package com.gt.wl.es.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joyone.action.BaseAction;
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

import com.alibaba.fastjson.JSONObject;
import com.gt.wl.cm.model.WlCmSell;
import com.gt.wl.cm.service.WlCmSellService;
import com.gt.wl.es.model.WlEsMyShoppingCart;
import com.gt.wl.es.service.WlEsMyShoppingCartService;
import com.gt.wl.util.CommonConf;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.service.WlWmInventoryService;

/**
 * 我的购物车Action层
 * @author liuyj
 * 
 */

@Controller
@RequestMapping({ "/wl/es/wlEsMyShoppingCartAction.do", "/wl/es/wlEsMyShoppingCartAction.web" })
public class WlEsMyShoppingCartAction extends BaseAction {
	private WlEsMyShoppingCartService wlEsMyShoppingCartService = (WlEsMyShoppingCartService) Sc.getBean("wl.es.WlEsMyShoppingCartService");
	private WlCmSellService wlCmSellService = (WlCmSellService) Sc.getBean("wl.cm.WlCmSellService");
	private WlWmInventoryService wlWmInventoryService=(WlWmInventoryService)Sc.getBean("wl.wm.WlWmInventoryService");
	/**
	 * 加入购物车
	 * @param sellId 上架产品id
	 * @return 结果
	 */
	@RequestMapping(params = "action=addToShoppingCart")
	@ResponseBody
	public String addToShoppingCart(String sellId, @RequestParam(defaultValue="1")Integer num) {
		try {
			WlCmSell wlCmSell = (WlCmSell) wlCmSellService.getObject(sellId);
			if ("HAS_SHELVE".equals(wlCmSell.getSellStateEk())) {// 商品已上架
				User user = Session.getCurrUser();
				WlEsMyShoppingCart wlEsMyShoppingCart = wlEsMyShoppingCartService.getMyShoppingCart(sellId, user.getId());
				if (ValidateUtil.isNull(wlEsMyShoppingCart)) {
					wlEsMyShoppingCart = new WlEsMyShoppingCart();
					wlEsMyShoppingCart.setSellId(sellId);
					wlEsMyShoppingCart.setProductName(wlCmSell.getProductName());
					wlEsMyShoppingCart.setPrice(wlCmSell.getPrice());
					wlEsMyShoppingCart.setNum(num);
					wlEsMyShoppingCart.setUnitName(wlCmSell.getUnitName());
					wlEsMyShoppingCart.setMemberId(user.getId());
					wlEsMyShoppingCart.setAccount(user.getLoginName());
					wlEsMyShoppingCart.setCreator(user.getName());
					wlEsMyShoppingCart.setSellStateEk(wlCmSell.getSellStateEk());
				}
				else {
					wlEsMyShoppingCart.setModifier(user.getName());
					wlEsMyShoppingCart.setModifyTime(new Date());
					wlEsMyShoppingCart.setNum(wlEsMyShoppingCart.getNum() + num);
				}
				wlEsMyShoppingCartService.saveObject(wlEsMyShoppingCart);
			}
			else {// 商品下架
				return this.getJson(true, Lang.getString("wl.es.wlEsMyShoppingCartAction.sellNoShelve"));// 提示加入购物车失败
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询我的购物车列表
	 * @return 我的购物车列表
	 */
	@RequestMapping(params = "action=findMyShoppingCartList")
	@ResponseBody
	public String findMyShoppingCartList() {
		try {
			User user = Session.getCurrUser();

			return this.getJson(true, wlEsMyShoppingCartService.findMyShoppingCartList(user.getId()));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * /**
	 * 查询我的购物车列表
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param isThumFlag 是否缩略图
	 * @param sellIds sellIds数组
	 * @return 我的购物车列表
	 */
	@RequestMapping(params = "action=findMyShoppingCartPage")
	@ResponseBody
	public String findMyShoppingCartPage(int pageSize, int currPage, String isThumFlag, String[] sellIds,String lang) {
		try {
			User user = Session.getCurrUser();
			Page page = wlEsMyShoppingCartService.findMyShoppingCartPage(pageSize, currPage, user.getId(), isThumFlag, sellIds,lang);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除购物车商品
	 * @param shoppingCartId 购物车id
	 * @param ids 购物车ids数组
	 * @return 空
	 */
	@RequestMapping(params = "action=delCarGoods")
	@ResponseBody
	public String delCarGoods(String shoppingCartId,String[] ids) {
		try {
			//单个删除
			if(!ValidateUtil.isEmpty(shoppingCartId)){
				wlEsMyShoppingCartService.deleteObject(shoppingCartId);
			}
			//批量删除
			if(!ValidateUtil.isNull(ids)){
				wlEsMyShoppingCartService.deleteObject(ids);
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更新购物车数量，并添加到session
	 * @param request 请求
	 * @param response 响应
	 * @param sellIds 上架商品id数组
	 * @param nums 对应的商品的数量
	 * @param isSet set往session设置值，get取得session的值
	 * @return 参数或null
	 */
	@RequestMapping(params = "action=setOrGetShopSellId")
	@ResponseBody
	public String setOrRemoveShopSellId(HttpServletRequest request, HttpServletResponse response, String[] sellIds, long[] nums, String isSet) {
		try {
			HttpSession session = request.getSession(true);
			User user = Session.getCurrUser();

			if ("set".equals(isSet)) {
				//
				int result = wlCmSellService.getSellCountBySellIdsAndState(sellIds, "NO_SHELVE");// 查询勾选的商品是否含有已下架
				if (result > 0) {// 如果含有已下架，则停止当前操作
					throw new BaseException(Lang.getString("wl.es.wlEsMyShoppingCartAction.containDown"));
				}
				session.setAttribute("sellIds", sellIds);
				int i = 0;
				for (String sellId : sellIds) {
					WlEsMyShoppingCart wlEsMyShoppingCart = wlEsMyShoppingCartService.getMyShoppingCart(sellId, user.getId());
					if (!ValidateUtil.isNull(wlEsMyShoppingCart)) {
						wlEsMyShoppingCart.setNum(nums[i]);
						i++;
						wlEsMyShoppingCartService.saveObject(wlEsMyShoppingCart);
					}
				}
			}
			else if ("get".equals(isSet)) {
				String[] sellids = (String[]) session.getAttribute("sellIds");
				return this.getJson(true, sellids);
			}
			return this.getJson(true, "");

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 暂存生成订单的信息
	 * @param request 请求
	 * @param response 响应
	 * @param deliveryAddrId 收货地址id
	 * @param buyMsg 卖家留言
	 * @param sellIds 上架商品id数组
	 * @param nums 对应的商品的数量
	 * @param source instantlyBuy代表立即购买，myCar我的购物车
	 * @param isSet set往session设置值，get取得session的值
	 * @return 参数或null
	 */
	@RequestMapping(params = "action=setOrGetOrderInfo")
	@ResponseBody
	public String setOrGetOrderInfo(HttpServletRequest request, HttpServletResponse response, String deliveryAddrId, String buyMsg, String[] sellIds, long[] nums, String source, String isSet) {
		try {
			HttpSession session = request.getSession(true);
			User user = Session.getCurrUser();
			if ("set".equals(isSet)) {
				int result = wlCmSellService.getSellCountBySellIdsAndState(sellIds, "NO_SHELVE");// 查询勾选的商品是否含有已下架
				if (result > 0) {// 如果含有已下架，则停止当前操作
					throw new BaseException(Lang.getString("wl.es.wlEsMyShoppingCartAction.containDown"));
				}
				session.setAttribute("deliveryAddrId", deliveryAddrId);
				session.setAttribute("buyMsg", buyMsg);
				double amt = 0;
				if ("myCar".equals(source)) {
					int i = 0;
					for (String sellId : sellIds) {
						WlEsMyShoppingCart wlEsMyShoppingCart = wlEsMyShoppingCartService.getMyShoppingCart(sellId, user.getId());
						WlCmSell wlCmSell = (WlCmSell) wlCmSellService.getObject(sellId);
						if (!ValidateUtil.isNull(wlEsMyShoppingCart)) {
							wlEsMyShoppingCart.setNum(nums[i]);// 将数量更新到购物车里面
							amt += (double) wlCmSell.getPrice() * nums[i];// 去商品上架取商品价格（还为下单，需上架表那最新价格）
							i++;
							wlEsMyShoppingCartService.saveObject(wlEsMyShoppingCart);
						}
					}
				}
				else if ("instantlyBuy".equals(source)) {
					WlCmSell wlCmSell = (WlCmSell) wlCmSellService.getObject(sellIds[0]);
					amt = (double) wlCmSell.getPrice() * nums[0];
				}
				session.setAttribute("amt", amt);
				session.setAttribute("orderSellIds", sellIds);
				session.setAttribute("nums", nums);
			}
			else if ("get".equals(isSet)) {
				JSONObject param = new JSONObject();
				deliveryAddrId = (String) session.getAttribute("deliveryAddrId");
				if (!ValidateUtil.isNull(deliveryAddrId)) {
					buyMsg = (String) session.getAttribute("buyMsg");
					double amt = (Double) session.getAttribute("amt");
					String[] orderSellIds = (String[]) session.getAttribute("orderSellIds");
					long[] numsList = (long[]) session.getAttribute("nums");
					param.put("deliveryAddrId", deliveryAddrId);
					param.put("buyMsg", buyMsg);
					param.put("amt", amt);
					param.put("orderSellIds", orderSellIds);
					param.put("numsList", numsList);
					return this.getJson(true, param);
				}
				else {
					return this.getJson(true, null);
				}

			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除和购物车有关的session信息
	 * @param request 请求
	 * @param response 响应
	 * @return 我的购物车列表
	 */
	@RequestMapping(params = "action=removeShopSession")
	@ResponseBody
	public String removeShopSession(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			session.removeAttribute("sellIds");
			session.removeAttribute("buyMsg");
			session.removeAttribute("amt");
			session.removeAttribute("deliveryAddrId");
			session.removeAttribute("orderSellIds");
			session.removeAttribute("nums");
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 修改购物车数量
	 * @param shoppingCartId 购物车id
	 * @param num 数量
	 * @return 修改购物车数量
	 */
	@RequestMapping(params = "action=updateCarNum")
	@ResponseBody
	public String updateCarNum(String shoppingCartId,long num) {
		try {
			WlEsMyShoppingCart myShoppingCart=(WlEsMyShoppingCart) wlEsMyShoppingCartService.getObject(shoppingCartId);
			myShoppingCart.setNum(num);
			wlEsMyShoppingCartService.saveObject(myShoppingCart);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 验证库存是否足够
	 * @param sellIds 上架产品ids
	 * @param nums 数量
	 * @param language 语言
	 * @return 空
	 */
	@RequestMapping(params = "action=validateInventory")
	@ResponseBody
	public String validateInventory(String[] sellIds,String[] nums,String language){
		try {
			for(int i=0;i<sellIds.length;i++){
				String sellId=sellIds[i];
				String num=nums[i];
				WlCmSell wlCmSell=(WlCmSell) wlCmSellService.getObject(sellId);
				WlWmInventory wlWmInventory=wlWmInventoryService.getData(CommonConf.storageId, wlCmSell.getItemId());
				if(!ValidateUtil.isNull(wlWmInventory)){
					if((wlWmInventory.getBaseUnitQty()-Double.parseDouble(num))<5){
						if("engilsh".equals(language)){
							throw new BaseException(Lang.getString("wl.es.wlEsMyShoppingCartAction.orderErrorEn"));
						}else{
							throw new BaseException(Lang.getString("wl.es.wlEsMyShoppingCartAction.orderError"));
						}					
					}		
				}else{
					if("engilsh".equals(language)){
						throw new BaseException(Lang.getString("wl.es.wlEsMyShoppingCartAction.orderErrorEn"));
					}else{
						throw new BaseException(Lang.getString("wl.es.wlEsMyShoppingCartAction.orderError"));
					}	
				}
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
		
	}
	
}