package com.gt.wl.es.service;

import org.joyone.lang.BaseException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 自动将订单超过一天的关闭
 * @author huangbj
 * 
 */
public class WlEsOrderJob implements Job {
	/**
	 * 任务执行
	 * @param context 任务上下文
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			WlEsOrderService wlEsOrderService = (WlEsOrderService) org.joyone.spring.Sc.getBean("wl.es.WlEsOrderService");
			wlEsOrderService.updateOrderState();

		}
		catch (Exception e) {
			throw new BaseException("");
		}
	}
}
