package com.gt.wl.cm.service;

import java.util.List;

import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmItemAttrDao;
import com.gt.wl.cm.dao.WlCmItemDao;
import com.gt.wl.cm.model.WlCmCategory;
import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.model.WlCmItemAttr;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.service.WlWmInventoryService;

@Service("wl.cm.WlCmItemService")
public class WlCmItemService extends BaseService {
	private WlCmItemDao wlCmItemDao = (WlCmItemDao) Sc.getBean("wl.cm.WlCmItemDao");
    private WlCmItemAttrDao wlCmItemAttrDao=(WlCmItemAttrDao) Sc.getBean("wl.cm.WlCmItemAttrDao");
	public WlCmItemService() {
		baseDao = wlCmItemDao;
	}

	/**
	 * 查询物料管理，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param categoryId 物料类别id
	 * @param isShowAgency 是否显示给经销商
	 * @param filterItemId 用于过滤自身（物料）Id
	 * @param itemCd 物料编码
	 * @param itemName 物料名称
	 * @param spec 规格型号
	 * @param categoryIds 物料类别ids
	 * @return 结果
	 */
	public Page search(int pageSize, int currPage, String categoryId, String isShowAgency,String filterItemId,String itemCd,String itemName,String spec,String categoryIds) {
		try {
			return wlCmItemDao.search(pageSize, currPage, categoryId, isShowAgency,filterItemId,itemCd,itemName,spec,categoryIds);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 获取用于搜索的类别id(因为物料管理页面点击左边父类类别也要出现子物料，所以拆出来)
	 * @param categoryId 类别id
	 * @return 用于搜索的类别id
	 */
	public String getSearchCategoryId(String categoryId){
		try {
			WlCmCategoryService wlCmCategoryService=(WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");
			String curCategoryId="";
			if(!ValidateUtil.isEmpty(categoryId)&&!"-1".equals(categoryId)){	
				WlCmCategory wlCmCategory=(WlCmCategory) wlCmCategoryService.getObject(categoryId);
				if("0".equals(wlCmCategory.getIsLastFlag())){//当物料类别不是末级节点时
					curCategoryId=getChildCateGoryIds("",categoryId);//递归找出物料类别下的所有末级节点子孙物料类别
				}
			}
			return curCategoryId;
			
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 递归查询父类底下所有末级节点的子类id
	 * @param childCateGoryIds 子类id
	 * @param parentCategoryId 父类id
	 * @return 子类ids
	 */
	public String getChildCateGoryIds(String childCateGoryIds,String parentCategoryId){
		try {
			WlCmCategoryService wlCmCategoryService=(WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");
			List<WlCmCategory> categoryList=wlCmCategoryService.findAllColumnList("1", "", "",parentCategoryId);
			for(WlCmCategory wlCmCategory:categoryList){
				if("1".equals(wlCmCategory.getIsLastFlag())){
					childCateGoryIds+=wlCmCategory.getCategoryId()+",";
				}else{
					childCateGoryIds=getChildCateGoryIds(childCateGoryIds,wlCmCategory.getCategoryId())+",";
				}
			}
			if(!"".equals(childCateGoryIds)){
				childCateGoryIds=childCateGoryIds.substring(0, childCateGoryIds.length()-1);
			}
			return childCateGoryIds;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取物料管理对象
	 * @param key 物料管理的字段
	 * @param value key对应的字段的值
	 * @param itemId 物料管理id
	 * @return 物料管理对象列表
	 */
	public List findWlCmItemList(String key, String value, String itemId) {
		try {
			return wlCmItemDao.findWlCmItemList(key, value, itemId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除物料管理对象
	 * @param ids id数组
	 */
	public void removeData(String[] ids) {

		try {
			wlCmItemDao.removeData(ConvertUtil.toDbString(ids));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询物料管理，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param storageId 仓库id
	 * @param itemIds 物料ids数组
	 * @param categoryId 物料类别id
	 * @param inventoryIsNull 库存是否为null(1允许为null ，0不为null)
	 * @param itemName 物料名称
	 * @param itemCd 物料编码
	 * @return 结果
	 */
	public Page search2(int pageSize, int currPage, String storageId, String[] itemIds, String categoryId, String inventoryIsNull,String itemName,String itemCd) {
		try {
			String ids = "";
			if (!ValidateUtil.isEmpty(itemIds)) {
				ids = ConvertUtil.toDbString(itemIds);
			}
			return wlCmItemDao.search2(pageSize, currPage, storageId, ids, categoryId, inventoryIsNull,itemName,itemCd);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取物料管理对象
	 * @param itemIds 物料管理ids
	 * @param isEnableFlag 启用标志
	 * @return 物料管理对象列表
	 */
	public List findItemList(String[] itemIds, String isEnableFlag) {
		try {
			return wlCmItemDao.findItemList(ConvertUtil.toDbString(itemIds), isEnableFlag);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过条形码获取物料对象
	 * @param barCd 条形码
	 * @return 物料对象（没有返回null）
	 */
	public WlCmItem getItemByBarCd(String barCd) {
		try {
			return wlCmItemDao.getItemByBarCd(barCd);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过物料编码获取物料对象
	 * @param itemCd 编码
	 * @return 物料对象（没有返回null）
	 */
	public WlCmItem getItemByItemCd(String itemCd) {
		try {
			return wlCmItemDao.getItemByItemCd(itemCd);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存物料对象，并更新库存表物料名称和型号
	 * @param wlCmItem 物料对象
	 */
	public void saveData(WlCmItem wlCmItem) {
		try {
			WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");
			this.saveObject(wlCmItem);
			List<WlWmInventory> list = wlWmInventoryService.findDataList("", wlCmItem.getItemId());
			for (WlWmInventory wlWmInventory : list) {
				wlWmInventory.setSpec(wlCmItem.getSpec());
				wlWmInventory.setItemName(wlCmItem.getItemName());
				wlWmInventoryService.saveObject(wlWmInventory);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 查询物料自定义属性表
	 * @param itemId 物料ID
	 * @return 物料自定义属性表
	 */
	public List<WlCmItemAttr> findItemAttrList(String itemId, String languageType) {
		try {
			
			return wlCmItemAttrDao.findItemAttrList(itemId, languageType);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 保存物料属性
	 * @param itemAttrList 物料列表
	 * @param 要删除的属性值
	 */
	public void saveAttr(List<WlCmItemAttr> itemAttrList, String[] delAttrId) {
		try {
			wlCmItemAttrDao.saveList(itemAttrList);
			if (!ValidateUtil.isEmpty(delAttrId)) {
				// 循环要删除的属性值
				for (int i = 0; i < delAttrId.length; i++) {
					wlCmItemAttrDao.deleteObject(delAttrId[i]);
				}
			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}