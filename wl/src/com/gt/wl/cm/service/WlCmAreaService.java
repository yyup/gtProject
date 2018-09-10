package com.gt.wl.cm.service;

import java.util.List;
import java.util.Map;
import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.util.TreeUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;
import com.gt.wl.cm.dao.WlCmAreaDao;
import com.gt.wl.cm.model.WlCmArea;

/**
 * 区域管理Service层
 * @author huangbj
 * 
 */
@Service("wl.cm.WlCmAreaService")
public class WlCmAreaService extends BaseService {
	private WlCmAreaDao wlCmAreaDao = (WlCmAreaDao) Sc.getBean("wl.cm.WlCmAreaDao");

	public WlCmAreaService() {
		baseDao = wlCmAreaDao;
	}
	/**
	 * 获取省市区域tree
	 * @param level 省市区层级
	 * @return 返回省市区域tree

	 */
	public Map getAreaTree(String level) {
		try {
			List<Object> list = wlCmAreaDao.getByHql(level);
			return TreeUtil.getPdTree(list, "R00000");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回中国 省市地域树型数据
	 * @param parentAreaId 父id
	 * @return 对应父节点下的区域列表
	 */
	public List findAreByParentId(String parentAreaId) {
		try {
			if (ValidateUtil.isEmpty(parentAreaId)) {// R00000是最大的节点，如果为空则为最大的节点
				parentAreaId = "R00000";
			}
			return wlCmAreaDao.findAreByParentId(parentAreaId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回层级以内的区域列表
	 * @param levelId 等级ID
	 * @return 对应父节点下的区域列表
	 */
	public List findAreByLevelId(String levelId) {
		try {
			if (ValidateUtil.isEmpty(levelId)) {
				return null;
			}
			return wlCmAreaDao.findAreByLevelId(levelId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据名字查找省、市、区id App
	 * @author luozp
	 * @param levelEk 级别
	 * @param name 名字
	 * @return 省id
	 */
	public String findAreaProvinceId(String levelEk, String name) {
		try {

			List<WlCmArea> list = wlCmAreaDao.findAreaProvinceId(levelEk, name);
			if (!ValidateUtil.isEmpty(list)) {

				WlCmArea tpCmArea = list.get(0);
				return tpCmArea.getAreaId();
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}