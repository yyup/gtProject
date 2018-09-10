package com.gt.wl.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author liuyj
 * 
 */
public class DateUtil {

	/**
	 * 计算年龄
	 * @param birthDay 出生日期
	 * @return 年龄
	 */
	public static int getAge(Date birthDay) {
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		int age = yearNow - yearBirth;
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			}
			else {
				// monthNow>monthBirth
				age++;
			}
		}
		return age;
	}

}
