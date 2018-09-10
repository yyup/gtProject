package com.gt.wl.wm.service;

import java.util.ArrayList;
import java.util.List;

import org.ddpush.im.v1.client.appserver.Pusher;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmUser;
import com.gt.wl.cm.service.WlCmUserService;
import com.gt.wl.util.MD5Util;
import com.gt.wl.util.PushConfig;

/**
 * 消息推送
 * @author liuyj
 * 
 */
@Service("wl.wm.WlWmPushService")
public class WlWmPushService {
	private WlCmUserService wlCmUserService = (WlCmUserService) Sc.getBean("wl.cm.WlCmUserService");
	private WlWmNoticeCheckService wlWmNoticeCheckService = (WlWmNoticeCheckService) Sc.getBean("wl.wm.WlWmNoticeCheckService");
	private WlWmNoticeInService wlWmNoticeInService = (WlWmNoticeInService) Sc.getBean("wl.wm.WlWmNoticeInService");
	private WlWmNoticeOutService wlWmNoticeOutService = (WlWmNoticeOutService) Sc.getBean("wl.wm.WlWmNoticeOutService");

	public WlWmPushService() {

	}

	/**
	 * 生成提醒消息
	 * @param busType 业务类型（0-盘点通知 1-入库通知 2-出库通知）
	 */
	public void getMessage(int busType) {
		Pusher pusher = null;
		List<WlCmUser> failList = new ArrayList<WlCmUser>();
		String msg = "";
		try {
			// 查找有效用户列表
			List<WlCmUser> userList = wlCmUserService.findAllUser();
			if (!ValidateUtil.isEmpty(userList)) {
				pusher = new Pusher(PushConfig.PUSH_SERVER, Integer.parseInt(PushConfig.PUSH_PORT), 1000 * 5);
			}
			// 获取待办数
			int total = (int) Math.floor(getNoticeTotal());
			msg = Lang.getString("wl.wm.wlWmPushService.messageHeader") + total + Lang.getString("wl.wm.wlWmPushService.messageFooter");
			for (WlCmUser wlCmUser : userList) {
				boolean result = pusher.push0x20Message(MD5Util.md5Byte(wlCmUser.getLoginName()), msg.getBytes("UTF-8"));
				if (!result) {
					failList.add(wlCmUser);
				}
			}
			// 循环
			for (WlCmUser wlCmUser : failList) {
				pusher.push0x20Message(MD5Util.md5Byte(wlCmUser.getLoginName()), msg.getBytes());
			}
		}
		catch (Exception e) {
			throw new BaseException("");
		}
		finally {
			if (pusher != null) {
				try {
					pusher.close();
				}
				catch (Exception e) {}
			}
		}
	}

	private long getNoticeTotal() {
		long inTotal = 0;// 入库通知单数
		long outTotal = 0;// 出库通知单数
		long checkTotal = 0;// 盘点通知单数
		long allTotal = 0;// 合计
		String storageId = "";
		try {
			checkTotal = wlWmNoticeCheckService.getTodoCount(storageId);
			inTotal = wlWmNoticeInService.getTodoCount(storageId);
			outTotal = wlWmNoticeOutService.getTodoCount(storageId);
			allTotal = checkTotal + inTotal + outTotal;
			return allTotal;
		}
		catch (Exception e) {
			throw new BaseException("");
		}
	}

}
