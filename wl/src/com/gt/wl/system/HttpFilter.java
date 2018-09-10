package com.gt.wl.system;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.wl.cm.service.WlCmSecurityService;

public class HttpFilter implements Filter {
	private String encode = "utf-8";
	private boolean security; // 是否进行权限检测
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
			String url = request.getRequestURI();

			if (security && (url.indexOf("/noSecurity/") < 0) && (url.indexOf("/app/") < 0)) // 进行权限检测
			{
				User user = (User) request.getSession().getAttribute("USER");

				if (user == null) {
					response.setCharacterEncoding("utf-8");
					response.setContentType("application/json; charset=utf-8");
					PrintWriter out = response.getWriter();
					out.print("{\"resultObject\":\"noSecurity\",\"success\":false}");
				}
				else {

					boolean isAccess = checkUrl(user, request, response);
					if (isAccess || user.isAdmin() || user.isProgrammer()) {
						chain.doFilter(request, response);
					}
					else {
						response.setCharacterEncoding("utf-8");
						response.setContentType("application/json; charset=utf-8");
						PrintWriter out = response.getWriter();
						out.print("{\"resultObject\":\"noSecurity\",\"success\":false}");
					}
				}
			}
			else {
				response.setHeader("Access-Control-Allow-Origin", "*");
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
	 * 通过网页进行权限检测
	 * @param user User 用户对象
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return boolean
	 */
	private boolean checkUrl(User user, HttpServletRequest request, HttpServletResponse response) {
		try {
			String url = request.getRequestURI();
			String query = request.getQueryString();
			System.out.println(url);
			System.out.println(query);
			if (query != null) {
				int len = query.length();
				byte[] bytes = new byte[len];
				query.getBytes(0, len, bytes, 0);
				try {
					query = new String(bytes, request.getCharacterEncoding());
				}
				catch (UnsupportedEncodingException ex) {}
			}
			else {
				query = "";
			}
			WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
			return wlCmSecurityService.checkUrl(user, url, query);
			// return false;
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
		try {
			this.security = config.getInitParameter("security") == null ? false : "true".equals(config.getInitParameter("security"));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
