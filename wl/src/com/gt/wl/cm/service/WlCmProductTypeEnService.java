package com.gt.wl.cm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.util.TreeUtil;
import org.joyone.util.UuidUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;


import com.gt.wl.cm.dao.WlCmProductTypeEnDao;
import com.gt.wl.cm.model.WlCmProductType;
import com.gt.wl.cm.model.WlCmProductTypeEn;

@Service("wl.cm.WlCmProductTypeEnService")
public class WlCmProductTypeEnService extends BaseService {
	private WlCmProductTypeEnDao wlCmProductTypeDao = (WlCmProductTypeEnDao) Sc.getBean("wl.cm.WlCmProductTypeEnDao");	
	private List<WlCmProductTypeEn> childList = new ArrayList<WlCmProductTypeEn>();

	public WlCmProductTypeEnService() {
		baseDao = wlCmProductTypeDao;
	}
	/**
	 * 查询所有数据状态为“1”的商品类型对象
	 * @return 商品类型对象列表
	 */
	public List<WlCmProductTypeEn> findAllTypeList() {
		try {
			return wlCmProductTypeDao.findAllTypeList();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回Tree 对象
	 * @param rootId 根节点ID
	 * @return 结果
	 */
	public Map getTree(String rootId) {
		try {
			List typeList = this.findAllTypeList();
			
			return TreeUtil.getPdTree(typeList, rootId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 返回过滤Tree 对象
	 * @param id 过滤Id
	 * @param rootId 根节点ID
	 * @return map树对象
	 */
	public Map getParentTree(String id, String rootId) {
		try {
			List typeList = this.findAllTypeList();
			return TreeUtil.getPdTree(typeList, rootId, id);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 根据父类型ID得到其直属子类型
	 * @param parentTypeId 父类型ID
	 * @return 类型列表
	 */
	public List<WlCmProductTypeEn> findChildTypeList(String parentTypeId) {
		try {
			return wlCmProductTypeDao.findChildTypeList(parentTypeId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据类型ID获得所有子类型
	 * @param typeId 类型ID
	 * @return 所有子类型列表
	 */
	public List<WlCmProductTypeEn> findAllChildTypeList(String typeId) {
		try {
			childList.clear();
			getAllChildType(typeId);
			return childList;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 递归查找所有子类型
	 * @param typeId 类型ID
	 */
	private void getAllChildType(String typeId) {
		try {
			// 得到直属下级类型
			List<WlCmProductTypeEn> childTypeList = wlCmProductTypeDao.findChildTypeList(typeId);
			for (WlCmProductTypeEn wlCmProductType : childTypeList) {
				childList.add(wlCmProductType);
				if (wlCmProductTypeDao.validateHasChild(wlCmProductType.getTypeId())) {
					getAllChildType(wlCmProductType.getTypeId());
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取所有末级节点的产品类型
	 * @param dataState 数据状态
	 * @return 产品类型对象列表
	 */
	public List<WlCmProductTypeEn> findAllLastType(String dataState) {
		try {
			return wlCmProductTypeDao.findAllLastType(dataState);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 设置商品类型的种类
	 * @param ids 选中类型ID数组
	 * @param categoryId 种类ID
	 */
	public void setCategory(String[] ids, String categoryId) {
		try {
			for (int i = 0; i < ids.length; i++) {
				// 设置类型的种类
				WlCmProductTypeEn type = (WlCmProductTypeEn) this.getObject(ids[i]);
				
				this.saveObject(type);
				// 获取所有子类型
				List<WlCmProductTypeEn> typeList = findAllChildTypeList(ids[i]);
				for (WlCmProductTypeEn wlCmProductType : typeList) {
					// 设置类型的种类
					this.saveObject(wlCmProductType);
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除商品类型
	 * @param ids 类型ID数组
	 */
	public void deleteProductType(String[] ids) {
		try {
			// 判断是否有子类型，如有则提示不允许删除
			for (int i = 0; i < ids.length; i++) {
				List childTypeList = this.findChildTypeList(ids[i]);
				if (!ValidateUtil.isEmpty(childTypeList)) {
					throw new BaseException(Lang.getString("wl.cm.WlCmProductTypeAction.deleteConfirm"));
				}
			}
			wlCmProductTypeDao.deleteProductType(ids);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 保存产品类型
	 * @param wlCmProductTypeEn 产品类型
	 */
	public void saveProductType(WlCmProductTypeEn wlCmProductTypeEn){
		try {
			boolean addState=false;
			if (ValidateUtil.isEmpty(wlCmProductTypeEn.getTypeId())){
				wlCmProductTypeEn.setTypeId(UuidUtil.getUuid());
				addState=true;
			}
			this.saveObject(wlCmProductTypeEn);
			if (addState){
				WlCmProductTypeService wlCmProductTypeService= (WlCmProductTypeService) Sc.getBean("wl.cm.WlCmProductTypeService");
				WlCmProductType wlCmProductType=new WlCmProductType();
				wlCmProductType.setBackgroundPath(wlCmProductTypeEn.getBackgroundPath());
				wlCmProductType.setCreateTime(wlCmProductTypeEn.getCreateTime());
				wlCmProductType.setCreator(wlCmProductTypeEn.getCreator());
				wlCmProductType.setDataState(wlCmProductTypeEn.getDataState());
				wlCmProductType.setDescription(wlCmProductTypeEn.getDescription());
				wlCmProductType.setMemo(wlCmProductTypeEn.getMemo());
				wlCmProductType.setModifier(wlCmProductTypeEn.getModifier());
				wlCmProductType.setModifyTime(wlCmProductTypeEn.getModifyTime());
				wlCmProductType.setParentTypeId(wlCmProductTypeEn.getParentTypeId());
				wlCmProductType.setPath(wlCmProductTypeEn.getPath());
				wlCmProductType.setSequ(wlCmProductTypeEn.getSequ());
				wlCmProductType.setTypeId(wlCmProductTypeEn.getTypeId());
				wlCmProductType.setTypeName(wlCmProductTypeEn.getTypeName());
				wlCmProductTypeService.addObject(wlCmProductType);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}