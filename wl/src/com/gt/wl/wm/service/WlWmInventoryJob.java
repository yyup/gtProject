package com.gt.wl.wm.service;

import org.joyone.lang.BaseException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 将库存不足保存到待发送
 * @author huangbj
 * 
 */
public class WlWmInventoryJob implements Job {

	/**
	 * 任务执行
	 * @param context 任务上下文
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) org.joyone.spring.Sc.getBean("wl.wm.WlWmInventoryService");
			wlWmInventoryService.saveToDoMsg();
		}
		catch (Exception e) {
			throw new BaseException("");
		}
	}
}