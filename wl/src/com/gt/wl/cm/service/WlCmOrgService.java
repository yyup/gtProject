package com.gt.wl.cm.service;

import java.util.List;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmOrgDao;
import com.gt.wl.util.Type;

@Service("wl.cm.WlCmOrgService")
public class WlCmOrgService extends BaseService {
	private WlCmOrgDao wlCmOrgDao = (WlCmOrgDao) Sc.getBean("wl.cm.WlCmOrgDao");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");

	public WlCmOrgService() {
		baseDao = wlCmOrgDao;

	}

	/**
	 * 查询往来单位，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param orgTypeEk 单位类型
	 * @param content 要查询的内容编号或者名称
	 * @return 结果
	 */
	public Page search(int pageSize, int currPage, String orgTypeEk, String content) {
		try {
			return wlCmOrgDao.search(pageSize, currPage, orgTypeEk, content);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 将往来单位类型从id转为value
	 * @param orgTypeEk 格式1,2,3
	 * @return 往来单位类型，多个用|隔开
	 */
	public String getOrgTypeValue(String orgTypeEk) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(orgTypeEk)) {
				String[] orgTypeEks = orgTypeEk.split(",");
				List<Type> orgTypeEkList = wlCmTypeService.findByCode("ORG_TYPE_EK");
				for (String orgType : orgTypeEks) {
					for (Type type : orgTypeEkList) {
						if (type.getId().equals(orgType)) {
							result += type.getLable() + "|";
							break;
						}
					}
				}
			}
			if (!ValidateUtil.isEmpty(result)) {
				result = result.substring(0, result.length() - 1);
			}
			return result;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取往来单位列表
	 * @param orgCd 单位编码
	 * @param orgId 单位id
	 * @param isEnableFlag 是否启用
	 * @param orgTypeEk 往来单位类型
	 * @return 往来单位列表
	 */
	public List findWlCmOrgList(String orgCd, String orgId, String isEnableFlag, String orgTypeEk) {
		try {

			return wlCmOrgDao.findWlCmOrgList(orgCd, orgId, isEnableFlag, orgTypeEk);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}