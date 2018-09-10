package com.gt.wl.cm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ConvertUtil;
import org.joyone.util.TreeUtil;
import org.joyone.util.UuidUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmSellAllocationEnDao;
import com.gt.wl.cm.dao.WlCmSellEnDao;
import com.gt.wl.cm.dao.WlCmSellPicEnDao;
import com.gt.wl.cm.model.WlCmBizAuditLog;
import com.gt.wl.cm.model.WlCmProductTypeEn;
import com.gt.wl.cm.model.WlCmSell;
import com.gt.wl.cm.model.WlCmSellAllocationEn;
import com.gt.wl.cm.model.WlCmSellEn;
import com.gt.wl.cm.model.WlCmSellPicEn;
import com.gt.wl.es.service.WlEsMyShoppingCartService;
import com.gt.wl.model.Node;
import com.gt.wl.util.CommonConf;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.service.WlWmInventoryService;

/**
 * 产品上架管理service层
 * @author huangbj
 */
@Service("wl.cm.WlCmSellEnService")
public class WlCmSellEnService extends BaseService {
	private WlCmSellEnDao wlCmSellDao = (WlCmSellEnDao) Sc.getBean("wl.cm.WlCmSellEnDao");
	private WlCmSellPicEnDao wlCmSellPicDao = (WlCmSellPicEnDao) Sc.getBean("wl.cm.WlCmSellPicEnDao");
	private WlCmProductTypeEnService wlCmProductTypeService = (WlCmProductTypeEnService) Sc.getBean("wl.cm.WlCmProductTypeEnService");
	private WlEsMyShoppingCartService wlEsMyShoppingCartService = (WlEsMyShoppingCartService) Sc.getBean("wl.es.WlEsMyShoppingCartService");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");
	private WlCmBizAuditLogService wlCmBizAuditLogService = (WlCmBizAuditLogService) Sc.getBean("wl.cm.WlCmBizAuditLogService");
	private WlCmSellAllocationEnDao wlCmSellAllocationDao= (WlCmSellAllocationEnDao) Sc.getBean("wl.cm.WlCmSellAllocationEnDao");
	public WlCmSellEnService() {
		baseDao = wlCmSellDao;
	}

	/**
	 * 返回上架商品列表
	 * @param pageSize 每页条数
	 * @param currPage 当前页码
	 * @param sellStateEk 产品上架状态
	 * @param typeId 商品类型
	 * @param productName 产品名称
	 * @param isThumFlag 是否缩略图
	 * @param sellIds 上架商品sellIds
	 * @return 上架商品列表
	 */
	public Page findSellsPages(int pageSize, int currPage, String sellStateEk, String typeId, String productName, String isThumFlag, String[] sellIds) {
		try {
			String sellIdsList = null;
			if (!ValidateUtil.isNull(sellIds)) {
				sellIdsList = ConvertUtil.toDbString(sellIds);
			}
			Page page = wlCmSellDao.findSellsPages(pageSize, currPage, sellStateEk, typeId, productName, isThumFlag, sellIdsList, "", "", "");
			List<Map> list = page.getItems();
			String storageId = CommonConf.storageId;
			for (Map map : list) {
				String itemId = (String) map.get("itemId");
				double outStockNum = Double.parseDouble(map.get("outStockNum").toString());// 缺货警示数量
				double lackStockNum = Double.parseDouble(map.get("lackStockNum").toString());// 库存紧张警示数量
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
	 * 获取上架商品
	 * @param sellStateEk 状态
	 * @param typeId 商品类型id
	 * @param productName 商品名称
	 * @param isIndexEk 是否缩略图
	 */
	public void findSellList(List resultList, String sellStateEk, String typeId, String productName, String isThumFlag,String isIndexEk) {
		try {
			List<Map> list = wlCmSellDao.findSellList(sellStateEk, typeId, productName, isThumFlag,isIndexEk);
			String storageId = CommonConf.storageId;
			resultList.addAll(list);
			List<WlCmProductTypeEn> typeList = wlCmProductTypeService.findChildTypeList(typeId);
			for (WlCmProductTypeEn wlCmProductType : typeList) {
				findSellList(resultList, sellStateEk, wlCmProductType.getTypeId(), productName, isThumFlag,isIndexEk);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 查询上架产品列表
	 * @param sellIds 上架产品Ids
	 * @return 上架产品列表
	 */
	public List findSellListByIds(String[] sellIds){
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(sellIds)) {
				result = ConvertUtil.toDbString(sellIds);
			}
			return wlCmSellDao.findSellListByIds(result);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回上架商品列表
	 * @param pageSize 每页条数
	 * @param currPage 当前页码
	 * @param sellStateEk 产品上架状态
	 * @param typeId 商品类型
	 * @param productName 产品名称
	 * @param isThumFlag 是否缩略图
	 * @param auditNodeEk 审核环节代码
	 * @param auditState 审核标志
	 * @param filterSellIds 用于过滤的上架产品id
	 * @return 上架商品列表
	 */
	public Page searchSellPage(int pageSize, int currPage, String sellStateEk, String typeId, String productName, String isThumFlag, String auditNodeEk, String auditState,String[] filterSellIds) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(filterSellIds)) {
				result = ConvertUtil.toDbString(filterSellIds);
			}
			return wlCmSellDao.findSellsPages(pageSize, currPage, sellStateEk, typeId, productName, isThumFlag, "", result, auditNodeEk, auditState);
		}
		catch (Exception e) {
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
	public Page findSellDetailPage(int pageSize, int currPage, String[] filterSellIds) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(filterSellIds)) {
				result = ConvertUtil.toDbString(filterSellIds);
			}
			return wlCmSellDao.findSellsPages(pageSize, currPage, "", "", "", "", "", result, "", "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除上架商品
	 * @param ids id数组
	 */
	public void removeData(String[] ids) {

		try {
			wlCmSellPicDao.removeDataBySellId(ConvertUtil.toDbString(ids));
			wlCmSellDao.removeData(ConvertUtil.toDbString(ids));
			wlCmSellAllocationDao.removeData(ConvertUtil.toDbString(ids), ConvertUtil.toDbString(ids));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改上架状态
	 * @param sellIds 修改的id数组
	 * @param ek 修改成的ek,HAS_SHELVE：已上架 NO_SHELVE：未上架
	 */
	public void updateState(String[] sellIds, String ek) {
		try {
			String strHql = ConvertUtil.toDbString(sellIds);

			String curDate = null;
			if ("HAS_SHELVE".equals(ek)) {// 商品上架
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// 取得当前时间
				Date curuentDate = new Date();
				curDate = sdf.format(curuentDate);
				// 含有已上架的商品不能上架
				int result = wlCmSellDao.getSellCountBySellIdsAndState(strHql, "HAS_SHELVE");
				if (result > 0) {
					throw new BaseException(Lang.getString("wl.cm.WlCmSellEnAction.containUp"));
				}
				// 含有未上架的商品未上传图片，不能上架
				List list = wlCmSellDao.findTpEsSellBySellIdsAndState(strHql, "NO_SHELVE");
				if (!ValidateUtil.isEmpty(list)) {
					WlCmSellEn wlCmSell = (WlCmSellEn) list.get(0);
					throw new BaseException(wlCmSell.getProductName() + Lang.getString("wl.cm.WlCmSellAction.notUploadPic"));
				}
				// 终审为审核通过的不能上架
				Map map = new HashMap();
				map.put("sellIds", ConvertUtil.toDbString(sellIds));
				map.put("auditNodeEk", "2");
				map.put("auditState", "3");
				list = wlCmSellDao.findSellListByMap(map);
				if (list.size() != sellIds.length) {// 上架的商品并非都是终审通过
					throw new BaseException(Lang.getString("wl.cm.WlEsSellService.noAllFinalAuditPass"));
				}

			}
			wlEsMyShoppingCartService.updateSellStateEk(sellIds, ek);// 修改购物车的商品状态

			wlCmSellDao.updateState(strHql, ek, curDate);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取上架商品的图片列表
	 * @param sellId 上架商品id
	 * @param isThumFlag 是否缩略图
	 * @param sellStateEk 商品上架状态
	 * @return 上架商品的图片列表
	 */
	public List findSellPic(String sellId, String isThumFlag, String sellStateEk) {
		try {
			return wlCmSellPicDao.findSellPic(sellId, isThumFlag, sellStateEk);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据上架状态获取上架商品
	 * @author dongyl
	 * @param sellState 上架状态
	 * @return 上架商品对象列表
	 */
	public List findEsSellByState(String sellState) {
		try {
			return wlCmSellDao.findEsSellByState(sellState);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 添加和删除图片
	 * @param user 登录用户
	 * @param sellId 商品上架id
	 * @param picId 图片id
	 * @param imgCount 上传的图片数量
	 * @param fg true 为添加，false 为删除
	 */
	public void saveAndReomovePic(User user, String sellId, String picId, boolean fg, String imgCount) {
		try {
			if (fg) {// 添加
				// 获取最大sequ的对象
				WlCmSellPicEn pic = (WlCmSellPicEn) wlCmSellPicDao.getMaxObjectBySellId(sellId);
				WlCmSellPicEn newPic = new WlCmSellPicEn();
				int max = 0;
				if (!ValidateUtil.isNull(pic)) {
					max = pic.getSequ();
				}
				newPic.setPath(picId);
				newPic.setSellId(sellId);
				newPic.setSequ(++max);
				if ("0".equals(imgCount)) {// 如果是第一张图片，默认为缩略图
					newPic.setIsThumFlag("1");
				}
				else {
					newPic.setIsThumFlag("0");
				}
				wlCmSellPicDao.addObject(newPic);
			}
			else {// 删除
				WlCmSellPicEn pic = (WlCmSellPicEn) wlCmSellPicDao.getObjectBySellId(sellId, picId);
				if (!ValidateUtil.isNull(pic)) {
					wlCmSellPicDao.deleteObject(pic.getPicId());
				}

			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取上架商品树（含类别）
	 * @return map
	 */
	public Map getSellTree() {
		List nodeList = new ArrayList();
		List dataList = new ArrayList();
		try {
			List<Object> typeList = wlCmProductTypeService.getObjects();
			for (Object obj : typeList) {
				WlCmProductTypeEn wlCmProductType = (WlCmProductTypeEn) obj;
				if ("1".equals(wlCmProductType.getDataState())) {// 如果数据是启用
					Node node = new Node();
					node.setNodeId(wlCmProductType.getNodeId());
					node.setParentId(wlCmProductType.getParentId());
					node.setName(wlCmProductType.getTypeName());
					node.setLast(false);
					dataList.add(node);
				}
			}
			List<WlCmSellEn> sellList = wlCmSellDao.findEsSellByState("");
			for (WlCmSellEn wlCmSell : sellList) {
				Node node = new Node();
				node.setNodeId(wlCmSell.getNodeId());
				node.setParentId(wlCmSell.getParentId());
				node.setName(wlCmSell.getProductName());
				node.setLast(true);
				nodeList.add(node);
			}

			if (!ValidateUtil.isEmpty(nodeList)) {
				dataList.addAll(nodeList);
			}
			return TreeUtil.getPdTree(dataList, "0");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据上架商品id和状态查询商品条数
	 * @param sellIds 上架商品的id 格式为('1','2')
	 * @param sellState 上架状态
	 * @return 查询的条数
	 */
	public int getSellCountBySellIdsAndState(String[] sellIds, String sellState) {
		try {
			String strHql = ConvertUtil.toDbString(sellIds);
			int result = wlCmSellDao.getSellCountBySellIdsAndState(strHql, sellState);
			return result;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 根据物料编码获取上架商品
	 * @param itemCd 物料编码
	 * @return 上架商品
	 */
	public WlCmSellEn getWlCmSellByItemCd(String itemCd) {
		try {

			return wlCmSellDao.getWlCmSellByItemCd(itemCd);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回商品列表
	 * @param map 条件
	 * @return 商品列表
	 */
	public List<Map> findSellListByMap(Map map) {
		try {
			return wlCmSellDao.findSellListByMap(map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 商品保存
	 * @param sell 上架商品
	 * @param user 当前登录用户
	 */
	public void saveSell(WlCmSellEn sell, User user) {
		try {
			// sell.setAuditNodeEk("1");
			// sell.setAuditState("0");
			// sell.setSellStateEk("NO_SHELVE");
			boolean addState=false;
			if (ValidateUtil.isEmpty(sell.getSellId())){
				sell.setSellId(UuidUtil.getUuid());
				addState=true;
			}
			this.saveObject(sell);
			if (addState){
				WlCmSellService wlCmSellService= (WlCmSellService) Sc.getBean("wl.cm.WlCmSellService");
				WlCmSell wlCmSell=new WlCmSell();
				wlCmSell.setAuditNodeEk(sell.getAuditNodeEk());
				wlCmSell.setAuditState(sell.getAuditState());
				wlCmSell.setAuditTime(sell.getAuditTime());
				wlCmSell.setContent(sell.getContent());
				wlCmSell.setCreateTime(sell.getCreateTime());
				wlCmSell.setCreator(sell.getCreator());
				wlCmSell.setDisct(sell.getDisct());
				wlCmSell.setIsHasGift(sell.getIsHasGift());
				wlCmSell.setIsIndexEk(sell.getIsIndexEk());
				wlCmSell.setGiftDesc(sell.getGiftDesc());
				wlCmSell.setItemCd(sell.getItemCd());
				wlCmSell.setJdLink(sell.getJdLink());
				wlCmSell.setLackStockNum(sell.getLackStockNum());
				wlCmSell.setMemo(sell.getMemo());
				wlCmSell.setModifier(sell.getModifier());
				wlCmSell.setModifyTime(sell.getModifyTime());
				wlCmSell.setNetWeight(sell.getNetWeight());
				wlCmSell.setOutlineDim(sell.getOutlineDim());
				wlCmSell.setPrice(sell.getPrice());
				wlCmSell.setProductName(sell.getProductName());
				wlCmSell.setSaleTotal(sell.getSaleTotal());
				wlCmSell.setSellStateEk(sell.getSellStateEk());
				wlCmSell.setSellTime(sell.getSellTime());
				wlCmSell.setSequ(sell.getSequ());
				wlCmSell.setTmallLink(sell.getTmallLink());
				wlCmSell.setTypeId(sell.getTypeId());
				wlCmSell.setUnitName(sell.getUnitName());			
				wlCmSell.setSellId(sell.getSellId());
				wlCmSellService.addObject(wlCmSell);
			}
			WlCmBizAuditLog wlCmBizAuditLog = new WlCmBizAuditLog();
			wlCmBizAuditLog.setBizTypeEk("SELL");
			wlCmBizAuditLog.setBizObjectId(sell.getSellId());
			wlCmBizAuditLog.setAuditOperateEk("SUBMIT");
			wlCmBizAuditLog.setModifyTime(new Date());
			wlCmBizAuditLog.setAuditorId(user.getId());
			wlCmBizAuditLog.setAuditorName(user.getName());
			wlCmBizAuditLogService.saveObject(wlCmBizAuditLog);
			String[] sellIds = new String[1];
			sellIds[0] = sell.getSellId();
			wlEsMyShoppingCartService.updateSellStateEk(sellIds, sell.getSellStateEk());// 修改购物车的商品跟商品 一样的状态
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更为为初审通过或初审驳回
	 * @param sellIds 上架商品id数组
	 * @param auditRemark 审核意见
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param user 当前登录用户
	 */
	public void updateFirstAudit(String[] sellIds, String auditRemark, String auditNodeEk, String auditState, User user) {
		try {
			if (!ValidateUtil.isEmpty(sellIds)) {
				Map map = new HashMap();
				map.put("sellIds", ConvertUtil.toDbString(sellIds));
				map.put("auditNodeEk", auditNodeEk);
				map.put("auditState", "0");
				List list = wlCmSellDao.findSellListByMap(map);
				if (list.size() != sellIds.length) {// 勾选的数据中并非都是初审未审核
					throw new BaseException(Lang.getString("wl.cm.WlEsSellService.noAllFirstAudit"));
				}
				String auditOperateEk = "";
				if ("1".equals(auditState)) {// 初审审核通过
					auditOperateEk = "PASS";
				}
				else if ("2".equals(auditState)) {// 驳回
					auditOperateEk = "REJECT";
				}
				if ("1".equals(auditNodeEk) && "1".equals(auditState)) {// 如果是初审审核通过，直接转到终审未审核
					auditNodeEk = "2";
					auditState = "0";
				}
				for (String sellId : sellIds) {
					WlCmSellEn wlCmSell = (WlCmSellEn) this.getObject(sellId);
					wlCmSell.setAuditNodeEk(auditNodeEk);
					wlCmSell.setAuditState(auditState);
					wlCmSell.setAuditTime(new Date());
					this.saveObject(wlCmSell);
					WlCmBizAuditLog wlCmBizAuditLog = new WlCmBizAuditLog();
					wlCmBizAuditLog.setBizTypeEk("SELL");
					wlCmBizAuditLog.setBizObjectId(wlCmSell.getSellId());
					wlCmBizAuditLog.setAuditorId(user.getId());
					wlCmBizAuditLog.setAuditorName(user.getName());
					wlCmBizAuditLog.setAuditNodeEk("1");
					wlCmBizAuditLog.setAuditOperateEk(auditOperateEk);
					wlCmBizAuditLog.setAuditRemark(auditRemark);
					wlCmBizAuditLog.setAuditTime(new Date());
					wlCmBizAuditLog.setModifyTime(new Date());
					// 保存操作日志
					wlCmBizAuditLogService.saveObject(wlCmBizAuditLog);
				}
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改为终审通过或终审驳回
	 * @param sellIds 上架商品id数组
	 * @param auditRemark 审核意见
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param user 当前登录用户
	 * 
	 */
	public void updateFinalAudit(String[] sellIds, String auditRemark, String auditNodeEk, String auditState, User user) {
		if (!ValidateUtil.isEmpty(sellIds)) {
			Map map = new HashMap();
			map.put("sellIds", ConvertUtil.toDbString(sellIds));
			map.put("auditNodeEk", auditNodeEk);
			map.put("auditState", "0");
			List list = wlCmSellDao.findSellListByMap(map);
			if (list.size() != sellIds.length) {// 勾选的数据中并非都是终审未审核
				throw new BaseException(Lang.getString("wl.cm.WlEsSellService.noAllFinalAudit"));
			}
			String auditOperateEk = "";
			if ("3".equals(auditState)) {// 终审 审核通过
				auditOperateEk = "PASS";
			}
			else if ("2".equals(auditState)) {// 驳回
				auditOperateEk = "REJECT";
			}
			for (String sellId : sellIds) {
				WlCmSellEn wlCmSell = (WlCmSellEn) this.getObject(sellId);
				wlCmSell.setAuditNodeEk(auditNodeEk);
				wlCmSell.setAuditState(auditState);
				wlCmSell.setAuditTime(new Date());
				this.saveObject(wlCmSell);
				WlCmBizAuditLog wlCmBizAuditLog = new WlCmBizAuditLog();
				wlCmBizAuditLog.setBizTypeEk("INFO");
				wlCmBizAuditLog.setBizObjectId(wlCmSell.getSellId());
				wlCmBizAuditLog.setAuditorId(user.getId());
				wlCmBizAuditLog.setAuditorName(user.getName());
				wlCmBizAuditLog.setAuditNodeEk("2");
				wlCmBizAuditLog.setAuditOperateEk(auditOperateEk);
				wlCmBizAuditLog.setModifyTime(new Date());
				wlCmBizAuditLog.setAuditTime(new Date());
				wlCmBizAuditLog.setAuditRemark(auditRemark);
				// 保存操作日志
				wlCmBizAuditLogService.saveObject(wlCmBizAuditLog);
			}
		}

	}
	/***
	 * 保存配置列表
	 * @param allocationList 配置列表
	 * @param delAllocationId 要删除的配置id
	 */
	public void saveAllocation(List<WlCmSellAllocationEn> allocationList,String[] delAllocationId){
		try {
			wlCmSellAllocationDao.saveList(allocationList);//保存配置
			if(!ValidateUtil.isEmpty(delAllocationId)){//删除配置
				for (int i = 0; i < delAllocationId.length; i++) {
					wlCmSellAllocationDao.deleteObject(delAllocationId[i]);
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 查询配置列表
	 * @param parentSellId 关联上架产品id
	 * @return 查询配置列表
	 */
	public List findAllocationList(String parentSellId){
		try {
			List<WlCmSellAllocationEn> list=wlCmSellAllocationDao.findAllocationList(parentSellId);
			for(WlCmSellAllocationEn wlCmSellAllocation:list){
				WlCmSellEn wlCmSell=(WlCmSellEn) this.getObject(wlCmSellAllocation.getSellId());
				if(!ValidateUtil.isNull(wlCmSell)){//防止空指针异常
					WlCmProductTypeEn wlCmProductType=(WlCmProductTypeEn) wlCmProductTypeService.getObject(wlCmSell.getTypeId());
					wlCmSellAllocation.setTypeName(wlCmProductType.getTypeName());
					wlCmSellAllocation.setPrice(wlCmSell.getPrice());
					wlCmSellAllocation.setProductName(wlCmSell.getProductName());
				}
				
			}
			return list;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
/**
 * 获取上架产品图片对象
 * @param sellId  上架产品id
 * @param isThumFlag 是否缩略图
 * @return 上架产品图片对象
 */
	public WlCmSellPicEn getSellPicByIsThumFlag(String sellId, String isThumFlag) {
		try {
			WlCmSellPicEn wlCmSellPic = wlCmSellPicDao.getSellPicByIsThumFlag(sellId, isThumFlag);
			return wlCmSellPic;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
/**
 * 查询配置列表(产品图片)
 * @param sellId
 * @return
 */
public List findSellAllocationList(String sellId) {
	try {
		List<WlCmSellAllocationEn> list=wlCmSellAllocationDao.findSellAllocationList(sellId);
		return list;
	}
	catch (Exception e) {
		throw new BaseException(e, log);
	}
}

		
}