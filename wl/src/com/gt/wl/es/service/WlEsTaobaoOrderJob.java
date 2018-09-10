package com.gt.wl.es.service;

import java.util.Calendar;
import java.util.Date;

import org.joyone.lang.BaseException;
import org.joyone.util.DateUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 自动将订单超过一天的关闭
 * @author huangbj
 * 
 */
public class WlEsTaobaoOrderJob implements Job {
	/**
	 * 任务执行
	 * @param context 任务上下文
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			WlEsOrderService wlEsOrderService = (WlEsOrderService) org.joyone.spring.Sc.getBean("wl.es.WlEsOrderService");
			Date date = new Date();
			Date startDate = DateUtil.dateAdd(Calendar.HOUR_OF_DAY, -2, date);
			// wlEsOrderService.saveTaobaoOrder(startDate, date);
		}
		catch (Exception e) {
			throw new BaseException("");
		}
	}

}
