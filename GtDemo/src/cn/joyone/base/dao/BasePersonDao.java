package cn.joyone.base.dao;

import java.util.List;

import org.joyone.dao.*;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;

import cn.joyone.base.model.BasePerson;

import org.springframework.stereotype.Repository;

public class BasePersonDao extends BaseDao {

	public BasePersonDao() {
		modelClass = BasePerson.class;
		defaultOrder = "";
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
			String where = " where  1 = 1 ";
			if (!ValidateUtil.isEmpty(name)) {
				where += " and t.name like '%" + name + "%' ";
			}
			String hql = " from BasePerson as t " + where + " order by t.id desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
