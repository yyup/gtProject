package com.gt.wl.es.service;

import org.joyone.lang.BaseException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * 发送待发送消息
 * @author huangbj
 * 
 */

public class WlEsTodoMsgJob implements Job {
	/**
	 * 任务执行
	 * @param context 任务上下文
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) org.joyone.spring.Sc.getBean("wl.es.WlEsTodoMsgService");
			wlEsTodoMsgService.updateSendToDoMsg();
		}
		catch (Exception e) {
			throw new BaseException("");
		}
	}

}