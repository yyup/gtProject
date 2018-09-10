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

public class WlEsStoreOutJob implements Job {
	private WlEsStoreOutService wlEsStoreOutService = (WlEsStoreOutService) org.joyone.spring.Sc.getBean("wl.es.WlEsStoreOutService");
	static boolean isRunFlag = false;

	/**
	 * 任务执行
	 * @param context 任务上下文
	 */
	public synchronized void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			if (isRunFlag) return;
			if (!isRunFlag) {
				isRunFlag = true;
				wlEsStoreOutService.saveStoreOutData();
				isRunFlag = false;
			}
		}
		catch (Exception e) {
			throw new BaseException("");
		}
	}

}