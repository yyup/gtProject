package com.gt.wl.cm.service;

import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.util.ConvertUtil;
import org.joyone.util.TreeUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmColumnDao;
import com.gt.wl.cm.model.WlCmColumn;

/**
 * 栏目管理Service层
 * @author huangbj
 * 
 */
@Service("wl.cm.WlCmColumnService")
public class WlCmColumnService extends BaseService {
	private WlCmColumnDao wlCmColumnDao = (WlCmColumnDao) Sc.getBean("wl.cm.WlCmColumnDao");

	public WlCmColumnService() {
		baseDao = wlCmColumnDao;
	}

	/**
	 * 返回Tree 对象
	 * @param rootId 根节点ID
	 * @param isEnableFlag 栏目使用状态
	 * @param isLastFlag 是否末级节点
	 * @return 结果
	 */
	public Map getTree(String rootId, String isEnableFlag, String isLastFlag) {
		try {

			List typeList = this.findAllColumnList(isEnableFlag, isLastFlag,"");
			return TreeUtil.getPdTree(typeList, rootId);
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
	 * @return map树对象
	 */
	public Map getParentTree(String id, String isEnableFlag, String rootId, String isLastFlag) {
		try {
			List typeList = this.findAllColumnList(isEnableFlag, isLastFlag,"");
			return TreeUtil.getPdTree(typeList, rootId, id);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 查询指定数据状态的栏目，为空查询所有
	 * @param isEnableFlag 栏目使用状态
	 * @param isLastFlag 是否末节点
	 * @param rootId 跟节点
	 * @return 栏目列表
	 */
	public List<WlCmColumn> findAllColumnList(String isEnableFlag, String isLastFlag,String rootId) {
		try {
			return wlCmColumnDao.findAllColumnList(isEnableFlag, isLastFlag,rootId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除栏目
	 * @param ids 类型ID数组
	 */
	public void deleteColumns(String[] ids) {
		try {
			// 判断是否有子类型，如有则提示不允许删除

			List childTypeList = this.findChildColumnList(ids, "1", "1");
			if (!ValidateUtil.isEmpty(childTypeList)) {
				throw new BaseException(Lang.getString("wl.cm.WlCmColumnService.deleteConfirm"));
			}
			wlCmColumnDao.deleteColumns(ids, "0");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据父类型ID得到其直属子类型
	 * @param parentColumnIds 父类型ID数组
	 * @param dataState 数据状态
	 * @param isEnableFlag 是否启用
	 * @return 类型列表
	 */
	public List<WlCmColumn> findChildColumnList(String[] parentColumnIds, String dataState, String isEnableFlag) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(parentColumnIds)) {
				result = ConvertUtil.toDbString(parentColumnIds);
			}
			return wlCmColumnDao.findChildColumnList(result, dataState, isEnableFlag);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据父类型ID得到其直属子类型
	 * @param parentColumnIds 父类型ID数组
	 * @param dataState 数据状态
	 * @param isEnableFlag 是否启用
	 * @param isLastFlag 是否末节点
	 * @return 类型列表
	 */
	public List<WlCmColumn> findChildList(String[] parentColumnIds, String dataState, String isEnableFlag, String isLastFlag) {
		try {
			return wlCmColumnDao.findChildList(parentColumnIds, dataState, isEnableFlag, isLastFlag);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改信息发布的状态
	 * @param ids 信息的id
	 * @param isEnableFlag 栏目的使用状态
	 */
	public void updateWlCmColumnState(String[] ids, String isEnableFlag) {
		try {
			for (String id : ids) {
				WlCmColumn wlCmColumn = (WlCmColumn) this.getObject(id);
				wlCmColumn.setIsEnableFlag(isEnableFlag);
				this.updateObject(wlCmColumn);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找信息管理初始化界面
	 * @param parentId 父节点id
	 * @param isEnableFlag 是否启用
	 * @param dataState 数据状态
	 * @param result 用于存放结果
	 */
	public void getInitializePage(String parentId, String isEnableFlag, String dataState, List result) {
		try {
			String[] parentIds = new String[1];
			parentIds[0] = parentId;
			List<WlCmColumn> list = wlCmColumnDao.findChildList(parentIds, dataState, isEnableFlag, "");
			for (WlCmColumn wlCmColumn : list) {
				if ("1".equals(wlCmColumn.getIsLastFlag())) {// 将找到的栏目放到result里面
					result.add(wlCmColumn);
					break;
				}
				else if (!ValidateUtil.isEmpty(result)) {// 如果已经找到要的栏目就停止循环
					break;
				}
				else if (ValidateUtil.isEmpty(result)) {// 如果还没找到要的栏目，继续搜索
					getInitializePage(wlCmColumn.getColumnId(), isEnableFlag, dataState, result);
				}

			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

}