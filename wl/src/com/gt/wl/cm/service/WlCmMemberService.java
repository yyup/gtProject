package com.gt.wl.cm.service;

import java.util.Random;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.util.Md5Util;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmMemberDao;
import com.gt.wl.cm.model.WlCmMember;
import com.gt.wl.util.MailUtil;

/**
 * 注册会员Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmMemberService")
public class WlCmMemberService extends BaseService {
	private WlCmMemberDao wlCmMemberDao = (WlCmMemberDao) Sc.getBean("wl.cm.WlCmMemberDao");

	public WlCmMemberService() {
		baseDao = wlCmMemberDao;
	}

	/**
	 * 查询注册会员分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param levelId 等级ID
	 * @param name 用户名
	 * @param memberStateEk 会员状态
	 * @param isBuyFlag 是否有购买商品
	 * @param email 邮箱
	 * @param account 用户账号
	 * @return 分页结果
	 */
	public Page search(int pageSize, int currPage, String levelId, String name, String memberStateEk, String isBuyFlag, String email, String account) {
		try {
			return wlCmMemberDao.search(currPage, pageSize, levelId, name, memberStateEk, isBuyFlag, email, account);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过账号查询会员
	 * @param account 账号
	 * @param email 会员邮箱
	 * @param name 姓名
	 * @param qqAccount qq账号
	 * @return 会员信息
	 */
	public WlCmMember getMemberByAccountOrEmail(String account, String email,String name,String qqAccount) {
		try {
			return wlCmMemberDao.getMemberByAccountOrEmail(account, email,name,qqAccount);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据性别查询会员
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param sexEk 性别
	 * @return 结果
	 */
	public Page searchBySex(int pageSize, int currPage, String sexEk) {
		try {
			return wlCmMemberDao.searchBySex(pageSize, currPage, sexEk);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更新新密码发送到指定邮箱
	 * @param wlCmMember 会员对象
	 */
	public void updatePasswordSendToEmail(WlCmMember wlCmMember) {
		try {
			String subJect = Lang.getString("wl.cm.WlCmMemberService.subJect");// 邮件主题
			String content = Lang.getString("wl.cm.WlCmMemberService.dearUser");
			content += wlCmMember.getName();
			content += Lang.getString("wl.cm.WlCmMemberService.yourPasswordIs");
			Random random = new Random();
			int password = random.nextInt(899999);
			password = password + 100000;// 获取要发送的6位随机密码
			content += password;
			content += Lang.getString("wl.cm.WlCmMemberService.emailContent");
			content += Lang.getString("wl.cm.WlCmMemberService.noReply");
			MailUtil mailUtil = new MailUtil();
			mailUtil.sendSimpleEmail(wlCmMember.getEmail(), subJect, content);
			wlCmMember.setPassword(Md5Util.encrypt(password + ""));
			this.saveObject(wlCmMember);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}