package com.gt.wl.cm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmInfoEnDao;
import com.gt.wl.cm.model.WlCmBizAuditLog;
import com.gt.wl.cm.model.WlCmColumnEn;
import com.gt.wl.cm.model.WlCmInfoEn;

/**
 * 信息发布service层
 * @author huangbj
 * 
 */
@Service("wl.cm.WlCmInfoEnService")
public class WlCmInfoEnService extends BaseService {
	private WlCmInfoEnDao wlCmInfoDao = (WlCmInfoEnDao) Sc.getBean("wl.cm.WlCmInfoEnDao");
	private WlCmColumnEnService wlCmColumnService = (WlCmColumnEnService) Sc.getBean("wl.cm.WlCmColumnEnService");
	private WlCmBizAuditLogService wlCmBizAuditLogService = (WlCmBizAuditLogService) Sc.getBean("wl.cm.WlCmBizAuditLogService");
	private static Map<String, WlCmColumnEn> columnMap = new LinkedHashMap<String, WlCmColumnEn>();

	public WlCmInfoEnService() {
		baseDao = wlCmInfoDao;
	}

	/**
	 * 获取栏目树
	 * @param rootId 广告栏目的首节点id
	 * @param isFirst 是否首次点开信息管理，空首次点开，非空不是
	 * @param smc 当前用户拥有的权限，0代表没有权限，1代表初审，2，代表终审
	 * @return 广告栏目树的map
	 */
	public Map getTree(String rootId, String isFirst, String smc) {
		try {
			synchronized (this) {
				if (ValidateUtil.isEmpty(isFirst)) {// 首次打开菜单
					saveColumnToMap(smc);
				}
				List list = findcolumnMapToList(smc);
				return TreeUtil.getPdTree(list, rootId);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 将栏目转存到map里面，方便查找
	 */
	public void saveColumnToMap(String smc) {
		try {
			List<WlCmColumnEn> list = wlCmColumnService.findAllColumnList("1", "","");
			for (WlCmColumnEn wlCmColumn : list) {
				columnMap.put(wlCmColumn.getColumnId(), wlCmColumn);
			}

			if ("1".equals(smc)) {// 初审
				// 查找末级节点
				List<WlCmColumnEn> lastList = wlCmColumnService.findAllColumnList("1", "1","");
				for (WlCmColumnEn lastWlCmColumnEn : lastList) {
					long firstAuditNum = wlCmInfoDao.getInfoCount(lastWlCmColumnEn.getColumnId(), "1", "0");
					if (firstAuditNum != 0) {
						updateColumnMapFirstAuditNum(lastWlCmColumnEn.getColumnId(), firstAuditNum);
					}
				}
			}
			else if ("2".equals(smc)) {// 终审
				// 查找末级节点
				List<WlCmColumnEn> lastList = wlCmColumnService.findAllColumnList("1", "1","");
				for (WlCmColumnEn lastWlCmColumnEn : lastList) {
					long finalAuditNum = wlCmInfoDao.getInfoCount(lastWlCmColumnEn.getColumnId(), "2", "0");
					if (finalAuditNum != 0) {
						updateColumnMapFinalAuditNum(lastWlCmColumnEn.getColumnId(), finalAuditNum);
					}
				}
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更新columnMap里的初审数量
	 * @param columnId 栏目id
	 * @param childFirstAuditNum columnId的某个子节点数量或指定添加数量
	 */
	public void updateColumnMapFirstAuditNum(String columnId, long childFirstAuditNum) {
		// 查找初审未审核数量
		try {
			WlCmColumnEn wlCmColumn = columnMap.get(columnId);
			wlCmColumn.setFirstAuditNum(wlCmColumn.getFirstAuditNum() + childFirstAuditNum);
			if (!"-1".equals(wlCmColumn.getParentId())) {
				updateColumnMapFirstAuditNum(wlCmColumn.getParentId(), childFirstAuditNum);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更新columnMap里的终审数量
	 * @param columnId 栏目id
	 * @param childFirstAuditNum columnId的某个子节点数量
	 */
	public void updateColumnMapFinalAuditNum(String columnId, long childFinalAuditNum) {
		// 查找终审未审核数量
		try {
			WlCmColumnEn wlCmColumn = columnMap.get(columnId);
			wlCmColumn.setFinalAuditNum(wlCmColumn.getFinalAuditNum() + childFinalAuditNum);
			if (!"-1".equals(wlCmColumn.getParentId())) {
				updateColumnMapFinalAuditNum(wlCmColumn.getParentId(), childFinalAuditNum);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 将columnMap转为list
	 * @param smc 当前用户拥有的权限，0代表没有权限，1代表初审，2代表终审
	 * @return 栏目树列表
	 */
	public List findcolumnMapToList(String smc) {
		try {
			List<WlCmColumnEn> list = new ArrayList();
			for (WlCmColumnEn wlCmColumn : columnMap.values()) {
				if ("1".equals(smc)) {
					if (0 == wlCmColumn.getFirstAuditNum()) {
						wlCmColumn.setShowAuditNum("");// 初审数量为0，用于前端显示的为空
					}
					else {
						wlCmColumn.setShowAuditNum("(" + wlCmColumn.getFirstAuditNum() + ")");// 显示数量放初审数量
					}
				}
				else if ("2".equals(smc)) {
					if (0 == wlCmColumn.getFinalAuditNum()) {
						wlCmColumn.setShowAuditNum("");// 终审数量为0，用于前端显示的为空

					}
					else {
						wlCmColumn.setShowAuditNum("(" + wlCmColumn.getFinalAuditNum() + ")");// 显示数量放终审数量
					}
				}
				else {
					wlCmColumn.setShowAuditNum("");
				}
				list.add(wlCmColumn);
			}
			return list;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取信息对象
	 * @param key 发布信息的字段
	 * @param value key对应的字段的值
	 * @return 信息对象列表
	 */
	public List getWlCmInfoList(String key, String value) {
		try {
			return wlCmInfoDao.getWlCmInfoList(key, value);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 查询信息发布管理，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param columnId 信息栏目id
	 * @param title 标题
	 * @param issueStateEk 发布状态
	 * @param dateState 数据状态
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param titleAndContent 标题和内容
	 * @return 结果
	 */
	public Page search(int pageSize, int currPage, String columnId, String title, String issueStateEk, String dateState, String auditNodeEk, String auditState,String titleAndContent) {
		try {
			return wlCmInfoDao.search(pageSize, currPage, columnId, title, issueStateEk, dateState, auditNodeEk, auditState,titleAndContent);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除发布信息
	 * @param ids 类型ID数组
	 * @param dataState 数据状态
	 */
	public void updateWlCmInfoEnsDataState(String[] ids, String dataState) {
		try {
			// 判断是否有子类型，如有则提示不允许删除
			wlCmInfoDao.updateWlCmInfosDataState(ids, dataState);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 物理删除发布信息
	 * @param ids 类型ID数组
	 */
	public void deletePhysicalWlCmInfoEns(String[] ids) {
		try {
			// 判断是否有子类型，如有则提示不允许删除
			wlCmInfoDao.deletePhysicalWlCmInfos(ids);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改信息发布的状态
	 * @param infoIds 信息的id
	 * @param issueStateEk 信息的状态
	 */
	public void updateWlCmInfoState(String[] infoIds, String issueStateEk) {
		try {
			if ("2".equals(issueStateEk)) {// 要发布
				List list = wlCmInfoDao.findInfoList(ConvertUtil.toDbString(infoIds),"", issueStateEk, "", "","","");
				if (!ValidateUtil.isEmpty(list)) {// 勾选的数据中含有已发布的，不能发布
					throw new BaseException(Lang.getString("wl.cm.WlCmInfoEnService.notAllIssue"));
				}
				else {
					list = wlCmInfoDao.findInfoList(ConvertUtil.toDbString(infoIds),"", "", "2", "3","","");
					if (infoIds.length != list.size()) {// 勾选的数据不是全部都终审通过的，不能发布
						throw new BaseException(Lang.getString("wl.cm.WlCmInfoEnService.notFinalPassIssue"));
					}
				}

			}
			Date date = new Date();
			for (String infoId : infoIds) {
				WlCmInfoEn wlCmInfo = (WlCmInfoEn) this.getObject(infoId);
				wlCmInfo.setIssueStateEk(issueStateEk);
				if ("2".equals(issueStateEk)) {
					wlCmInfo.setIssueTime(date);
				}
				else {
					wlCmInfo.setIssueTime(null);
				}
				this.updateObject(wlCmInfo);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存信息
	 * @param wlCmInfo 信息对象
	 */
	public void saveData(WlCmInfoEn wlCmInfo, User user) {
		try {
			// wlCmInfo.setAuditNodeEk("1");
			// wlCmInfo.setAuditState("0");
			this.saveObject(wlCmInfo);
			WlCmBizAuditLog wlCmBizAuditLog = new WlCmBizAuditLog();
			wlCmBizAuditLog.setBizTypeEk("INFO");
			wlCmBizAuditLog.setBizObjectId(wlCmInfo.getInfoId());
			wlCmBizAuditLog.setAuditOperateEk("SUBMIT");
			wlCmBizAuditLog.setModifyTime(new Date());
			wlCmBizAuditLog.setAuditorId(user.getId());
			wlCmBizAuditLog.setAuditorName(user.getName());
			wlCmBizAuditLogService.saveObject(wlCmBizAuditLog);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更为为初审通过或初审驳回
	 * @param infoIds 信息id数组
	 * @param auditRemark 审核意见
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param user 当前登录用户
	 */
	public void updateFirstAudit(String[] infoIds, String auditRemark, String auditNodeEk, String auditState, User user) {
		String resultIds = "";
		if (!ValidateUtil.isEmpty(infoIds)) {
			List list = wlCmInfoDao.findInfoList(ConvertUtil.toDbString(infoIds), "","", auditNodeEk, "0","","");
			if (list.size() != infoIds.length) {// 勾选的数据中并非都是初审未审核
				throw new BaseException(Lang.getString("wl.cm.WlCmInfoEnService.noAllFirstAudit"));
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
			for (String infoId : infoIds) {
				WlCmInfoEn wlCmInfo = (WlCmInfoEn) this.getObject(infoId);
				wlCmInfo.setAuditNodeEk(auditNodeEk);
				wlCmInfo.setAuditState(auditState);
				wlCmInfo.setAuditTime(new Date());
				this.saveObject(wlCmInfo);
				WlCmBizAuditLog wlCmBizAuditLog = new WlCmBizAuditLog();
				wlCmBizAuditLog.setBizTypeEk("INFO");
				wlCmBizAuditLog.setBizObjectId(wlCmInfo.getInfoId());
				wlCmBizAuditLog.setAuditorId(user.getId());
				wlCmBizAuditLog.setAuditorName(user.getName());
				wlCmBizAuditLog.setAuditNodeEk("1");
				wlCmBizAuditLog.setAuditOperateEk(auditOperateEk);
				wlCmBizAuditLog.setAuditRemark(auditRemark);
				wlCmBizAuditLog.setAuditTime(new Date());
				wlCmBizAuditLog.setModifyTime(new Date());
				// 保存操作日志
				wlCmBizAuditLogService.saveObject(wlCmBizAuditLog);
				// 更新columnMap
				updateColumnMapFirstAuditNum(wlCmInfo.getColumnId(), -1);
			}
		}
	}

	/**
	 * 更改为终审通过或终审驳回
	 * @param infoIds 信息id数组
	 * @param auditRemark 审核意见
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param columnType 栏目类型，single 代表单边或文章，list代表列表
	 * @param user 当前登录用户
	 * 
	 */
	public void updateFinalAudit(String[] infoIds, String auditRemark, String auditNodeEk, String auditState, String columnType, User user) {
		String resultIds = "";
		if (!ValidateUtil.isEmpty(infoIds)) {
			List list = wlCmInfoDao.findInfoList(ConvertUtil.toDbString(infoIds),"", "", auditNodeEk, "0","","");
			if (list.size() != infoIds.length) {// 勾选的数据中并非都是终审未审核
				throw new BaseException(Lang.getString("wl.cm.WlCmInfoEnService.noAllFinalAudit"));
			}
			String auditOperateEk = "";
			if ("3".equals(auditState)) {// 终审 审核通过
				auditOperateEk = "PASS";
			}
			else if ("2".equals(auditState)) {// 驳回
				auditOperateEk = "REJECT";
			}
			for (String infoId : infoIds) {
				WlCmInfoEn wlCmInfo = (WlCmInfoEn) this.getObject(infoId);
				if ("single".equals(columnType) && "3".equals(auditState)) {// 单篇且终审通过，自动变成发布状态
					wlCmInfo.setIssueStateEk("2");
				}
				wlCmInfo.setAuditNodeEk(auditNodeEk);
				wlCmInfo.setAuditState(auditState);
				wlCmInfo.setAuditTime(new Date());
				this.saveObject(wlCmInfo);
				WlCmBizAuditLog wlCmBizAuditLog = new WlCmBizAuditLog();
				wlCmBizAuditLog.setBizTypeEk("INFO");
				wlCmBizAuditLog.setBizObjectId(wlCmInfo.getInfoId());
				wlCmBizAuditLog.setAuditorId(user.getId());
				wlCmBizAuditLog.setAuditorName(user.getName());
				wlCmBizAuditLog.setAuditNodeEk("2");
				wlCmBizAuditLog.setAuditOperateEk(auditOperateEk);
				wlCmBizAuditLog.setModifyTime(new Date());
				wlCmBizAuditLog.setAuditTime(new Date());
				wlCmBizAuditLog.setAuditRemark(auditRemark);
				// 保存操作日志
				wlCmBizAuditLogService.saveObject(wlCmBizAuditLog);
				// 更新columnMap
				updateColumnMapFinalAuditNum(wlCmInfo.getColumnId(), -1);
			}
		}

	}
	/***
	 * 查询信息列表
	 * @param infoIds 信息ids，格式为('1','2','3')
	 * @param columnId 栏目Id号
	 * @param issueStateEk 发布状态
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核标志
	 * @param dataState 数据状态
	 * @param title 标题
	 * @return 信息列表
	 */
	public List findInfoList(String infoIds,String columnId, String issueStateEk, String auditNodeEk, String auditState,String dataState,String title) {
		try {
			return wlCmInfoDao.findInfoList(null,columnId, issueStateEk, auditNodeEk,auditState,dataState,title);
		}
		catch (Exception e) {
			throw new BaseException(e,log);
		}
		
	}
}