package com.gt.wl.es.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmArea;
import com.gt.wl.cm.model.WlCmCommonSet;
import com.gt.wl.cm.model.WlCmSell;
import com.gt.wl.cm.service.WlCmAreaService;
import com.gt.wl.cm.service.WlCmCommonSetService;
import com.gt.wl.cm.service.WlCmSellService;
import com.gt.wl.es.dao.WlEsArriveAreaDao;
import com.gt.wl.es.dao.WlEsFeeTempDao;
import com.gt.wl.es.dao.WlEsFeeTempRowDao;
import com.gt.wl.es.dao.WlEsTempSellDao;
import com.gt.wl.es.model.WlEsArriveArea;
import com.gt.wl.es.model.WlEsFeeTemp;
import com.gt.wl.es.model.WlEsFeeTempRow;
import com.gt.wl.es.model.WlEsTempSell;

/**
 * 运费模板Service层
 * @author huangbj
 * 
 */
@Service("wl.es.WlEsFeeTempService")
public class WlEsFeeTempService extends BaseService {
	private WlEsFeeTempDao wlEsFeeTempDao = (WlEsFeeTempDao) Sc.getBean("wl.es.WlEsFeeTempDao");
	private WlEsTempSellDao wlEsTempSellDao = (WlEsTempSellDao) Sc.getBean("wl.es.WlEsTempSellDao");
	private WlEsArriveAreaDao wlEsArriveAreaDao = (WlEsArriveAreaDao) Sc.getBean("wl.es.WlEsArriveAreaDao");
	private WlCmSellService wlCmSellService = (WlCmSellService) Sc.getBean("wl.cm.WlCmSellService");
	private WlEsFeeTempRowDao wlEsFeeTempRowDao = (WlEsFeeTempRowDao) Sc.getBean("wl.es.WlEsFeeTempRowDao");
	private WlCmAreaService wlCmAreaService = (WlCmAreaService) Sc.getBean("wl.cm.WlCmAreaService");
	private WlCmCommonSetService wlCmCommonSetService=(WlCmCommonSetService) Sc.getBean("wl.cm.WlCmCommonSetService");
	public WlEsFeeTempService() {
		baseDao = wlEsFeeTempDao;
	}

	/**
	 * 运费模板详细信息
	 * @param sellIds 上架商品ids数组
	 * @param areaIds 达到地区ids数组
	 * @param tempId 运费模板id(过滤自身)
	 * @return 运费模板详细信息
	 */
	public List findListBySellIdAndAreaId(String[] sellIds, String[] areaIds, String tempId) {
		try {
			String sellIdsResult = "";
			String areaIdsResult = "";
			if (!ValidateUtil.isEmpty(sellIds)) {
				sellIdsResult = ConvertUtil.toDbString(sellIds);
			}
			if (!ValidateUtil.isEmpty(areaIds)) {
				areaIdsResult = ConvertUtil.toDbString(areaIds);
			}
			return wlEsFeeTempDao.findListBySellIdAndAreaId(sellIdsResult, areaIdsResult, tempId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 
	 * @param wlEsFeeTemp 运费模板
	 * @param wlEsFeeTempRowList 模板行列表且含有到底地区
	 */
	public void saveData(WlEsFeeTemp wlEsFeeTemp, List<WlEsFeeTempRow> wlEsFeeTempRowList) {
		try {
			// 保存运费模板 // wlEsTempSellDao wlEsArriveAreaDao
			wlEsFeeTemp.setStartAreaId("R01778");// 默认厦门
			this.saveObject(wlEsFeeTemp);
			String[] sellIds = wlEsFeeTemp.getSellIds().split(",");
			int sequ = 1;
			wlEsArriveAreaDao.removeData("", wlEsFeeTemp.getTempId());// 清除模板下模板行下的地区
			wlEsFeeTempRowDao.removeData(wlEsFeeTemp.getTempId());// 清除运费模板下模板行
			for (WlEsFeeTempRow wlEsFeeTempRow : wlEsFeeTempRowList) {
				String[] areaIds = wlEsFeeTempRow.getAllAreaId().split(",");// 运费模板行下的所有地区id
				List<Map> sellAreaList = this.findListBySellIdAndAreaId(sellIds, areaIds, wlEsFeeTemp.getTempId());
				if (ValidateUtil.isEmpty(sellAreaList)) {// 商品到达地区的运费不存在
					// 保存模板行
					WlEsFeeTempRow newWlEsFeeTempRow = new WlEsFeeTempRow();
					newWlEsFeeTempRow.setTempId(wlEsFeeTemp.getTempId());
					newWlEsFeeTempRow.setFirstInterval(1);
					newWlEsFeeTempRow.setAddInterval(1);
					newWlEsFeeTempRow.setAddFee(wlEsFeeTempRow.getAddFee());
					newWlEsFeeTempRow.setFirstFee(wlEsFeeTempRow.getFirstFee());
					newWlEsFeeTempRow.setSequ(sequ++);
					newWlEsFeeTempRow.setAreaAttr(wlEsFeeTempRow.getAreaAttr());
					wlEsFeeTempRowDao.saveObject(newWlEsFeeTempRow);
					// 保存到达地区
					List<WlEsArriveArea> arriveAreaList = wlEsFeeTempRow.getArriveAreaList();
					List<WlEsArriveArea> arriveList = new ArrayList();
					for (WlEsArriveArea wlEsArriveArea : arriveAreaList) {
						WlEsArriveArea newwlEsArriveArea = new WlEsArriveArea();
						newwlEsArriveArea.setAreaId(wlEsArriveArea.getAreaId());
						newwlEsArriveArea.setName(wlEsArriveArea.getName());
						newwlEsArriveArea.setRowId(newWlEsFeeTempRow.getRowId());
						arriveList.add(newwlEsArriveArea);
					}
					wlEsArriveAreaDao.saveList(arriveList);
				}
				else {// 商品到达地区的运费已存在，不保存
					Map map = sellAreaList.get(0);
					String tempName = (String) map.get("tempName");
					String productName = (String) map.get("productName");
					String name = (String) map.get("name");
					String areaId = (String) map.get("areaId");
					WlCmArea wlCmArea = (WlCmArea) wlCmAreaService.getObject(areaId);
					if ("2".equals(wlCmArea.getLevelEk())) {// 如果是市
						WlCmArea proWlCmArea = (WlCmArea) wlCmAreaService.getObject(wlCmArea.getParentAreaId());
						name = proWlCmArea.getName() + name;
					}
					String result = tempName + Lang.getString("wl.es.WlEsFeeTempService.hasExist");
					result += productName + Lang.getString("wl.es.WlEsFeeTempService.sendTo");
					result += name + Lang.getString("wl.es.WlEsFeeTempService.fee");
					throw new BaseException(result);
				}
			}
			// 保存试用上架商品
			wlEsTempSellDao.removeData(wlEsFeeTemp.getTempId());// 清空模板下的试用商品
			List<WlEsTempSell> tempSellList = new ArrayList();
			for (String sellId : sellIds) {
				WlCmSell wlCmSell = (WlCmSell) wlCmSellService.getObject(sellId);
				WlEsTempSell wlEsTempSell = new WlEsTempSell();
				wlEsTempSell.setProductName(wlCmSell.getProductName());
				wlEsTempSell.setSellId(sellId);
				wlEsTempSell.setTempId(wlEsFeeTemp.getTempId());
				tempSellList.add(wlEsTempSell);
			}
			wlEsTempSellDao.saveList(tempSellList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取模板分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 模板分页数据
	 **/
	public Page search(int pageSize, int currPage, Map paraMap) {
		try {
			return wlEsFeeTempDao.search(pageSize, currPage, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 运费详细信息
	 * @param tempId 运费模板id
	 * @return 运费详细信息
	 */
	public Map getData(String tempId) {
		try {

			WlEsFeeTemp wlEsFeeTemp = (WlEsFeeTemp) this.getObject(tempId);
			List<WlEsTempSell> tempSellList = wlEsTempSellDao.findTempSellList(tempId,"","");
			List<WlEsFeeTempRow> tempRowList = wlEsFeeTempRowDao.findFeeTempRowList(tempId);
			List<WlEsArriveArea> arriveAreaList;
			for (WlEsFeeTempRow wlEsFeeTempRow : tempRowList) {
				arriveAreaList = wlEsArriveAreaDao.findArriveAreaList(wlEsFeeTempRow.getRowId());
				wlEsFeeTempRow.setArriveAreaList(arriveAreaList);
			}
			Map map = new HashMap();
			map.put("wlEsFeeTemp", wlEsFeeTemp);
			map.put("tempRowList", tempRowList);
			map.put("tempSellList", tempSellList);
			return map;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除模板（同时删除试用商品，模板行，到达地区）
	 * @param tempId 模板id
	 */
	public void deleteData(String tempId) {
		try {
			wlEsTempSellDao.removeData(tempId);// 删除试用商品
			this.deleteObject(tempId);// 删除模板
			List<WlEsFeeTempRow> list = wlEsFeeTempRowDao.findFeeTempRowList(tempId);
			for (WlEsFeeTempRow wlEsFeeTempRow : list) {
				List<WlEsArriveArea> arriveList = wlEsArriveAreaDao.findArriveAreaList(wlEsFeeTempRow.getRowId());
				for (WlEsArriveArea wlEsArriveArea : arriveList) {
					wlEsArriveAreaDao.deleteObject(wlEsArriveArea.getArriveAreaId());// 删除到达地区
				}
			}
			wlEsFeeTempRowDao.removeData(tempId);// 删除模板行
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 获取运费
	 * @param sellId 产品id
	 * @param sellIds 产品ids
	 * @param areaId 区域id
	 * @return 运费
	 */
	public Map getFreight(String sellId,String[] sellIds,String areaId) {
		try {
			String result="";
			if (!ValidateUtil.isNull(sellIds)) {
				result = ConvertUtil.toDbString(sellIds);
			}
			WlCmCommonSet wlCmCommonSet=wlCmCommonSetService.getWlCmCommonSetByKey("MAIN_SELL");//获取主体产品的模板id
			String tempId=wlCmCommonSet.getSetValue();
			Map freight=new HashMap();
			double firstFee=0;
			double addFee=0;
			List mainSellList=wlEsTempSellDao.findTempSellList(tempId, sellId, result);	//获取属于主体产品的运费模板
			if(ValidateUtil.isEmpty(mainSellList)){
				firstFee=wlEsFeeTempRowDao.getFirstFee(sellId,result,areaId);
				addFee=wlEsFeeTempRowDao.getAddFee(sellId,result,areaId);
			}			
			freight.put("firstFee", firstFee);
			freight.put("addFee", addFee);
			return freight;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}