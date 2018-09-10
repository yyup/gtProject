package com.gt.wl.cm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.util.EnumUtil;
import org.joyone.util.TreeUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmTypeDao;
import com.gt.wl.cm.model.WlCmType;
import com.gt.wl.util.Type;

/**
 * 枚举Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmTypeService")
public class WlCmTypeService extends BaseService {
	private WlCmTypeDao wlCmTypeDao = (WlCmTypeDao) Sc.getBean("wl.cm.WlCmTypeDao");

	public WlCmTypeService() {
		baseDao = wlCmTypeDao;
	}

	/**
	 * 返回Tree 对象
	 * @param state 状态
	 * @param rootId 根节点ID
	 * @return map树对象
	 */
	public Map getTree(int state, String rootId) {
		try {
			return TreeUtil.getPdTree(wlCmTypeDao.findByStateAndSecurity(state, 0), rootId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 返回过滤Tree 对象
	 * @param state 状态
	 * @param id 过滤Id
	 * @param rootId 根节点ID
	 * @return map树对象
	 */
	public Map getParentTree(int state, String id, String rootId) {
		try {
			return TreeUtil.getPdTree(wlCmTypeDao.findByStateAndSecurity(state, 0), rootId, id);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 获取字典名称
	 * @param id 字典ID
	 * @return 字典名称
	 */
	public String getName(String id) {
		try {
			if (id == null) {
				return "";
			}
			WlCmType object = (WlCmType) this.getObject(id);

			return object == null ? "" : object.getName();

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据字典类型字符串数组返回枚举Map集合
	 * @param enumTypes 字典类型字符串数组
	 * @return 枚举Map集合
	 */
	public Map getEnumType(String[] enumTypes) {
		try {
			Map enumMap = new HashMap();
			if (enumTypes == null) {
				return enumMap;
			}
			for (String enumType : enumTypes) {
				enumMap.put(enumType + "Enum", EnumUtil.convertEnums(wlCmTypeDao.findListByCodeWithoutRoot(enumType)));
			}
			return enumMap;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 
	 * 根据CODE 获取根节点ID
	 * @param code CODE代码
	 * @return 根节点ID 查询不到返回null
	 */
	public String findParentIdByCode(String code) {
		try {
			return wlCmTypeDao.findParentIdByCode(code);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据字典类型字符返回List集合
	 * @param enumType 字典类型字符
	 * @return List集合
	 */
	public List findListByCodeWithoutRoot(String enumType) {
		try {
			return wlCmTypeDao.findListByCodeWithoutRoot(enumType);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据字典类型字符返回List集合
	 * @param enumType 字典类型字符
	 * @return Type类型列表
	 */
	public List<Type> findByCode(String enumType) {
		List<Type> resultList = new ArrayList<Type>();
		try {
			List<WlCmType> list = wlCmTypeDao.findListByCodeWithoutRoot(enumType);
			for (WlCmType wlCmType : list) {
				Type type = new Type(wlCmType.getName(), wlCmType.getValue());
				resultList.add(type);
			}
			return resultList;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}