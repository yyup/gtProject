package com.unionpay.acp.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 工具类
 * @author liuyj
 * 
 */
public class RequestUtil {
	/**
	 * 获取请求参数中所有的信息
	 * @param request 请求对象
	 * @return 参数组成Map格式
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> param = request.getParameterNames();
		if (null != param) {
			while (param.hasMoreElements()) {
				String element = (String) param.nextElement();
				String value = request.getParameter(element);
				map.put(element, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (map.get(element) == null || "".equals(map.get(element))) {
					// System.out.println("======为空的字段名===="+element);
					map.remove(element);
				}
			}
		}
		return map;
	}

}
