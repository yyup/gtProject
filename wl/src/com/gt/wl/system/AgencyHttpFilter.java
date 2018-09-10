package com.gt.wl.system;

import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joyone.lang.BaseException;
import org.joyone.sys.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 经销商后台过滤器
 * @author liuyj
 * 
 */
public class AgencyHttpFilter implements Filter {
	private String encode = "utf-8";
	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 页面过滤处理方法(主要是编码类型转换及Session的关闭)
	 * @param req ServletRequest
	 * @param res ServletResponse
	 * @param chain FilterChain
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			// 设置编码
			request.setCharacterEncoding(this.encode);
			response.setContentType("text/html;charset=" + encode);

			// 防止页面缓冲
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			User user = (User) request.getSession().getAttribute("USER");
			if (user == null) {
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/json; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.print("{\"resultObject\":\"agencyNoSecurity\",\"success\":false}");
			}
			else {
				chain.doFilter(request, response);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 销毁
	 */
	public void destroy() {
		try {

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 初始化--获取web.xml设置的值
	 * @param config FilterConfig
	 */
	public void init(FilterConfig config) {

	}

}
