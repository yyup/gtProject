package cn.joyone.base.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import org.joyone.lang.BaseException;
import org.joyone.service.*;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.util.AssertUtil;
import org.joyone.util.TreeUtil;
import org.joyone.service.BaseService;

import cn.joyone.base.dao.BasePersonDao;
import cn.joyone.base.model.BasePerson;

@Service("base.BasePersonService")
public class BasePersonService extends BaseService {
	private BasePersonDao basePersonDao = new BasePersonDao();

	public BasePersonService() {
		baseDao = basePersonDao;
	}

	/**
	 * 通过姓名查询记录
	 * @param pageSize 页记录数
	 * @param currPage 页码
	 * @param name 姓名
	 * @return 数据页对象
	 */
	public Page search(int currPage, int pageSize, String name) {
		try {
			// String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			// Pattern p = Pattern.compile(regEx);
			// Matcher m = p.matcher(name);
			// name = m.replaceAll("").trim();
			return basePersonDao.search(currPage, pageSize, name);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}
}