package com.gt.wl.es.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.util.ConvertUtil;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;

import org.springframework.stereotype.Service;

import com.gt.wl.es.dao.WlEsCustInstallDao;
import com.gt.wl.es.model.WlEsCustInstall;

/**
 * 安装Service层
 * @author Lizj
 * 
 */
@Service("wl.es.WlEsCustInstallService")
public class WlEsCustInstallService extends BaseService {
	private WlEsCustInstallDao wlEsCustInstallDao = (WlEsCustInstallDao) Sc.getBean("wl.es.WlEsCustInstallDao");

	public WlEsCustInstallService() {
		baseDao = wlEsCustInstallDao;
	}
	
	/**
	 * 查询安装申请
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param map 参数
	 * @return 分页对象
	 */
	public Page search(int currPage, int pageSize,Map map) {
		try {
			return wlEsCustInstallDao.search(currPage, pageSize,map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 生成维修号
	 * @return 维修单号
	 */
	public String getNewCustInstallNO() {
		try {
			List<String> list = wlEsCustInstallDao.getNewInstallNoCode();
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(list)) {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyy");
				String maxCodeString = list.get(0);
				if (ValidateUtil.isEmpty(maxCodeString)) {// 无数据的时候返回第一个维修编号
					newCode = nowStr + "00001";
				}
				else {
					if (maxCodeString.contains(nowStr)) {// 流水号添加1
						String maxStrCode = maxCodeString.replaceAll(nowStr, "");
						int maxCode = Integer.parseInt(maxStrCode);
						maxCode++;
						maxStrCode = "" + maxCode;
						int length = maxStrCode.length();
						for (int i = 0; i < 5 - length; i++) {
							maxStrCode = "0" + maxStrCode;
						}
						newCode = nowStr + maxStrCode;
					}
					else {// 否则直接生成这一分钟的第一个流水号
						newCode = nowStr + "00001";
					}
				}
			}
			else {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyy");
				newCode = nowStr + "00001";
			}
			return newCode;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 检查安装对象是否可以审核(返回值“”代表可以审核)
	 * @param installIds 安装申请ids
	 * @param installState 安装状态(1或2或3)
	 * @return 检查安装对象是否可以审核(返回值“”代表可以审核)
	 */

	public String getCheckInstallState(String[] installIds, String installState) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(installIds)) {
				result = ConvertUtil.toDbString(installIds);
			}
			if ("1".equals(installState)) {// 点击通过的时候发生事件
				List list = wlEsCustInstallDao.findCustInstallList("", "0", result);//查询安装状态不为未申请的安装列表
				if (ValidateUtil.isEmpty(list)) {
					return "";
				}
				else {
					throw new BaseException(Lang.getString("wl.es.wrEsTeachVideoService.noApply"));
				}
			}
			else if("2".equals(installState)){// 点击驳回的时候发生事件
				List list = wlEsCustInstallDao.findCustInstallList("", "1", result);//查询安装状态不为申请的安装列表
				if (ValidateUtil.isEmpty(list)) {
					return "";
				}
				else {
					throw new BaseException(Lang.getString("wl.es.wrEsTeachVideoService.noDispatch"));
				}
			}
			else{
				List list = wlEsCustInstallDao.findCustInstallList("", "2", result);//查询安装状态不为派单的安装列表
				if (ValidateUtil.isEmpty(list)) {
					return "";
				}
				else {
					throw new BaseException(Lang.getString("wl.es.wrEsTeachVideoService.noInstallSuccess"));
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}	
	
	/**
	 * 保存安装申请列表
	 * @param installList 安装申请列表
	 */
	public void saveList(List<WlEsCustInstall> installList) {
		try {
			wlEsCustInstallDao.saveList(installList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}