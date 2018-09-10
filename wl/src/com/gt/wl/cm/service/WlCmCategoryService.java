package com.gt.wl.cm.service;

import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.util.TreeUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmCategoryDao;
import com.gt.wl.cm.model.WlCmCategory;

@Service("wl.cm.WlCmCategoryService")
public class WlCmCategoryService extends BaseService {
	private WlCmCategoryDao wlCmCategoryDao = (WlCmCategoryDao) Sc.getBean("wl.cm.WlCmCategoryDao");

	public WlCmCategoryService() {
		baseDao = wlCmCategoryDao;
	}

	/**
	 * 返回Tree 对象
	 * @param rootId 根节点ID
	 * @param isEnableFlag 栏目使用状态
	 * @param isLastFlag 是否末级节点
	 * @param isShowAgency 是否显示给经销商
 	 * @param itemId 物料Id号
	 * @return 结果
	 */
	public Map getTree(String rootId, String isEnableFlag, String isLastFlag, String isShowAgency,String itemId) {
		try {

			List typeList = wlCmCategoryDao.findAllColumnList(isEnableFlag, isLastFlag, isShowAgency,"");
			if(!ValidateUtil.isEmpty(itemId)){
				return TreeUtil.getPdTree(typeList, rootId,itemId);
			}else{
				return TreeUtil.getPdTree(typeList, rootId);
			}
			
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 返回过滤Tree 对象
	 * @param id 过滤Id
	 * @param isEnableFlag 栏目使用状态
	 * @param rootId 根节点ID
	 * @param isLastFlag 是否某节点
	 * @param isShowAgency 是否显示给经销商
	 * @return map树对象
	 */
	public Map getParentTree(String id, String isEnableFlag, String rootId, String isLastFlag, String isShowAgency) {
		try {
			List typeList = wlCmCategoryDao.findAllColumnList(isEnableFlag, isLastFlag, isShowAgency,"");
			return TreeUtil.getPdTree(typeList, rootId, id);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据父类型ID得到其直属子类型
	 * @param parentCategoryIds 父类型ID数组
	 * @param isEnableFlag 是否启用
	 * @param isLastFlag 是否末节点
	 * @return 类型列表
	 */
	public List<WlCmCategory> findChildList(String[] parentCategoryIds, String isEnableFlag, String isLastFlag) {
		try {
			return wlCmCategoryDao.findChildList(parentCategoryIds, isEnableFlag, isLastFlag);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据类别ID获取其所有父类名称（含本类别）
	 * @param categoryId 类别ID
	 * @param categoryNameList 所有父类别名称list
	 */
	public void getAllParentCategoryName(String categoryId, List<String> categoryNameList) {
		try {
			WlCmCategory wlCmCategory = (WlCmCategory) wlCmCategoryDao.getObject(categoryId);
			categoryNameList.add(wlCmCategory.getName());
			// 非根节点，递归
			if (!"0".equals(wlCmCategory.getParentCategoryId())) {
				getAllParentCategoryName(wlCmCategory.getParentCategoryId(), categoryNameList);
			}
			// return outCategoryName;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}
	/**
	 * 查询所有数据状态为dataState的栏目对象
	 * @param isEnableFlag 栏目使用状态
	 * @param isLastFlag 是否末节点
	 * @param isShowAgency 是否显示给经销商
	 * @param parentCategoryId 父类别id
	 * @return 栏目对象列表
	 */
	public List<WlCmCategory> findAllColumnList(String isEnableFlag, String isLastFlag, String isShowAgency,String parentCategoryId) {
		try {
			return wlCmCategoryDao.findAllColumnList(isEnableFlag, isLastFlag, isShowAgency,parentCategoryId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}