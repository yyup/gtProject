package com.gt.wl.cm.service;

import java.util.Date;
import java.util.List;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.User;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmFocusPicDao;
import com.gt.wl.cm.model.WlCmBizAuditLog;
import com.gt.wl.cm.model.WlCmFocusPic;

/**
 * 焦点图片Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmFocusPicService")
public class WlCmFocusPicService extends BaseService {
	private WlCmFocusPicDao wlCmFocusPicDao = (WlCmFocusPicDao) Sc.getBean("wl.cm.WlCmFocusPicDao");
	private WlCmBizAuditLogService wlCmBizAuditLogService = (WlCmBizAuditLogService) Sc.getBean("wl.cm.WlCmBizAuditLogService");

	public WlCmFocusPicService() {
		baseDao = wlCmFocusPicDao;
	}

	/**
	 * 获取焦点图片列表
	 * @param isEnableFlag 是否启用
	 * @return 焦点图片列表
	 */
	public List findFocusPic(String isEnableFlag, String auditNodeEk, String auditState) {
		try {
			return wlCmFocusPicDao.findFocusPic(isEnableFlag, auditNodeEk, auditState);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取焦点图片列表
	 * @param picIds 图片数组
	 * @param isEnableFlag 是否启用
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @return 焦点图片列表
	 */
	public List findFocusPicList(String picIds[], String isEnableFlag, String auditNodeEk, String auditState) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(picIds)) {
				result = ConvertUtil.toDbString(picIds);
			}
			return wlCmFocusPicDao.findFocusPicList(result, isEnableFlag, auditNodeEk, auditState);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 更新焦点图片状态
	 * @param picIds 焦点图片id数组
	 * @param isEnableFlag 启用或禁用
	 * @param user 当前登录用户
	 */
	public void updateState(String picIds[], String isEnableFlag, User user) {
		try {
			List list = this.findFocusPicList(picIds, isEnableFlag, "", "");
			if (ValidateUtil.isEmpty(list)) {// 全部是同一种状态，启用或者禁用
				list = this.findFocusPicList(picIds, "", "2", "3");
				if (list.size() == picIds.length) {// 全部是终审通过
					for (String piciId : picIds) {
						WlCmFocusPic wlCmFocusPic = (WlCmFocusPic) this.getObject(piciId);
						wlCmFocusPic.setIsEnableFlag(isEnableFlag);
						wlCmFocusPic.setModifier(user.getName());
						wlCmFocusPic.setModifyTime(new Date());
						this.saveObject(wlCmFocusPic);
					}
				}
				else {
					throw new BaseException(Lang.getString("wl.cm.WlCmFocusPicService.FinalPassIsAble"));
				}
			}
			else {
				if ("1".equals(isEnableFlag)) {// 启用
					throw new BaseException(Lang.getString("wl.cm.WlCmFocusPicService.hasUse"));
				}
				else if ("0".equals(isEnableFlag)) {// 禁用
					throw new BaseException(Lang.getString("wl.cm.WlCmFocusPicService.hasLock"));
				}

			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存
	 * @param wlCmFocusPic 焦点图片对象
	 * @param user 当前登录用户
	 */
	/**
	 * @param wlCmFocusPic
	 * @param user
	 */
	public void saveData(WlCmFocusPic wlCmFocusPic, User user) {
		try {
			// wlCmFocusPic.setAuditNodeEk("1");
			// wlCmFocusPic.setAuditState("0");
			// wlCmFocusPic.setAuditTime(new Date());
			// wlCmFocusPic.setIsEnableFlag("0");
			this.saveObject(wlCmFocusPic);
			WlCmBizAuditLog wlCmBizAuditLog = new WlCmBizAuditLog();
			wlCmBizAuditLog.setBizTypeEk("FOCUS");
			wlCmBizAuditLog.setBizObjectId(wlCmFocusPic.getPicId());
			wlCmBizAuditLog.setAuditorId(user.getId());
			wlCmBizAuditLog.setAuditorName(user.getName());
			wlCmBizAuditLog.setAuditOperateEk("SUBMIT");
			wlCmBizAuditLog.setModifyTime(new Date());
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 终审（通过或驳回）
	 * @param picIds 焦点图片ids
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param auditRemark审核意见
	 */
	public void updateFinalAudit(String picIds[], String auditNodeEk, String auditState, String auditRemark, User user) {
		try {
			List list = this.findFocusPicList(picIds, "", "2", "0");
			if (picIds.length != list.size()) {
				throw new BaseException(Lang.getString("wl.cm.WlCmFocusPicService.noAllFinalAudit"));
			}
			else {
				String auditOperateEk = "";
				if ("3".equals(auditState)) {// 终审 审核通过
					auditOperateEk = "PASS";
				}
				else if ("2".equals(auditState)) {// 驳回
					auditOperateEk = "REJECT";
				}

				for (String picId : picIds) {
					WlCmFocusPic wlCmFocusPic = (WlCmFocusPic) this.getObject(picId);
					wlCmFocusPic.setAuditNodeEk(auditNodeEk);
					wlCmFocusPic.setAuditState(auditState);
					wlCmFocusPic.setAuditTime(new Date());
					this.saveObject(wlCmFocusPic);// 保存焦点图片
					WlCmBizAuditLog wlCmBizAuditLog = new WlCmBizAuditLog();
					wlCmBizAuditLog.setBizTypeEk("FOCUS");
					wlCmBizAuditLog.setBizObjectId(wlCmFocusPic.getPicId());
					wlCmBizAuditLog.setAuditorId(user.getId());
					wlCmBizAuditLog.setAuditorName(user.getName());
					wlCmBizAuditLog.setAuditNodeEk(auditNodeEk);
					wlCmBizAuditLog.setAuditOperateEk(auditOperateEk);
					wlCmBizAuditLog.setAuditRemark(auditRemark);
					wlCmBizAuditLog.setAuditTime(new Date());
					wlCmBizAuditLog.setModifyTime(new Date());
					// 保存操作日志
					wlCmBizAuditLogService.saveObject(wlCmBizAuditLog);
				}

			}

		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 初审（通过或驳回）
	 * @param picIds 焦点图片ids
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param auditRemark审核意见
	 */
	public void updateFirstAudit(String picIds[], String auditNodeEk, String auditState, String auditRemark, User user) {
		try {
			List list = this.findFocusPicList(picIds, "", "1", "0");
			if (picIds.length != list.size()) {
				throw new BaseException(Lang.getString("wl.cm.WlCmFocusPicService.noAllFirstAudit"));
			}
			else {
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
				for (String picId : picIds) {
					WlCmFocusPic wlCmFocusPic = (WlCmFocusPic) this.getObject(picId);
					wlCmFocusPic.setAuditNodeEk(auditNodeEk);
					wlCmFocusPic.setAuditState(auditState);
					wlCmFocusPic.setAuditTime(new Date());
					this.saveObject(wlCmFocusPic);// 保存焦点图片
					WlCmBizAuditLog wlCmBizAuditLog = new WlCmBizAuditLog();
					wlCmBizAuditLog.setBizTypeEk("FOCUS");
					wlCmBizAuditLog.setBizObjectId(wlCmFocusPic.getPicId());
					wlCmBizAuditLog.setAuditorId(user.getId());
					wlCmBizAuditLog.setAuditorName(user.getName());
					wlCmBizAuditLog.setAuditNodeEk(auditNodeEk);
					wlCmBizAuditLog.setAuditOperateEk(auditOperateEk);
					wlCmBizAuditLog.setAuditRemark(auditRemark);
					wlCmBizAuditLog.setAuditTime(new Date());
					wlCmBizAuditLog.setModifyTime(new Date());
					// 保存操作日志
					wlCmBizAuditLogService.saveObject(wlCmBizAuditLog);
				}

			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}