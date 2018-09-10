package com.gt.wl.es.service;

import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.es.dao.WlEsMyShoppingCartDao;
import com.gt.wl.es.model.WlEsMyShoppingCart;
import com.gt.wl.util.CommonConf;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.service.WlWmInventoryService;

/**
 * 我的购物车Service层
 * @author liuyj
 * 
 */

@Service("wl.es.WlEsMyShoppingCartService")
public class WlEsMyShoppingCartService extends BaseService {
	private WlEsMyShoppingCartDao wlEsMyShoppingCartDao = (WlEsMyShoppingCartDao) Sc.getBean("wl.es.WlEsMyShoppingCartDao");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");

	public WlEsMyShoppingCartService() {
		baseDao = wlEsMyShoppingCartDao;
	}

	/**
	 * 查询我的购物车列表
	 * @param memberId 会员Id
	 * @return 我的购物车列表
	 */
	public List findMyShoppingCartList(String memberId) {
		try {
			return wlEsMyShoppingCartDao.findMyShoppingCartList(memberId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询我的购物车列表已存在商品
	 * @param sellId 商品Id
	 * @param memberId 会员Id
	 * @return 已存在商品购物车
	 */
	public WlEsMyShoppingCart getMyShoppingCart(String sellId, String memberId) {
		try {
			return wlEsMyShoppingCartDao.getMyShoppingCart(sellId, memberId);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过 购车id数组搜索对应购物车列表
	 * @param cartIds 购车id数组
	 * @return 购物车列表
	 */
	public List findShoppingCartListByCartIds(String[] cartIds) {
		try {
			return wlEsMyShoppingCartDao.findShoppingCartListByCartIds(ConvertUtil.toDbString(cartIds));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询我的购物车的数量
	 * @param memberId 会员Id
	 * @return 购物车的数量
	 */
	public List findMyShoppingCartTotalNum(String memberId) {
		try {
			return wlEsMyShoppingCartDao.findMyShoppingCartTotalNum(memberId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询我的购物车列表
	 * @param pageSize 每页条数
	 * @param currPage 当前页码
	 * @param memberId 会员Id
	 * @param isThumFlag 是否缩略图
	 * @param sellIds 上架商品id数组
	 * @param lang 语言类型
	 * @return 我的购物车列表
	 */

	public Page findMyShoppingCartPage(int pageSize, int currPage, String memberId, String isThumFlag, String[] sellIds,String lang) {
		try {
			String sellIdsList = null;
			if (!ValidateUtil.isNull(sellIds)) {
				sellIdsList = ConvertUtil.toDbString(sellIds);
			}
			Page page=null;
			if("english".equals(lang)){
				page = wlEsMyShoppingCartDao.findMyShoppingCartEnPage(pageSize, currPage, memberId, isThumFlag, sellIdsList);
			}else{
				page = wlEsMyShoppingCartDao.findMyShoppingCartPage(pageSize, currPage, memberId, isThumFlag, sellIdsList);
			}			
			List<Map> list = page.getItems();
			String storageId = CommonConf.storageId;
			for (Map map : list) {
				String itemId = (String) map.get("itemId");
				double outStockNum = Double.parseDouble(map.get("outStockNum").toString());
				double lackStockNum = Double.parseDouble(map.get("lackStockNum").toString());
				// 4028814050d640b00150d6797083007e 翔安仓库32位id
				WlWmInventory wlWmInventory = wlWmInventoryService.getData(storageId, itemId);
				map.put("inventoryState", "0");// 默认正常
				double baseUnitQty = 0;// 库存数量，默认为0个
				if (!ValidateUtil.isNull(wlWmInventory)) {// 库存存在
					baseUnitQty = wlWmInventory.getBaseUnitQty();
				}
				if (baseUnitQty <= outStockNum) {// 缺货
					map.put("inventoryState", "1");
				}
				else if (baseUnitQty <= lackStockNum) {// 库存紧张
					map.put("inventoryState", "2");
				}
				else {// 库存正常
					map.put("inventoryState", "0");
				}

			}
			return page;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除购物车
	 * @param sellIds 数组
	 */
	public void deleteMyShoppingCart(String[] sellIds) {
		try {
			wlEsMyShoppingCartDao.deleteMyShoppingCart(ConvertUtil.toDbString(sellIds));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改购物车里商品上架状态
	 * @param sellIds 上架商品的id数组
	 * @param sellStateEk 上架的ek字段
	 */
	public void updateSellStateEk(String[] sellIds, String sellStateEk) {
		try {
			String strHql = ConvertUtil.toDbString(sellIds);
			wlEsMyShoppingCartDao.updateSellStateEk(strHql, sellStateEk);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}