package cn.joyone.base.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.model.EnumModel;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.joyone.base.model.WlCmStorage;
import cn.joyone.base.service.WlCmStorageService;

/**
 * 
 * @author rdsf04
 * 
 */
@Controller
@RequestMapping("/wl/cm/wlCmStorageAction.do")
public class WlCmStorageAction extends BaseAction {
	WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");

	/**
	 * 界面初始化
	 * @param enumType 枚举类型，多个枚举项以“,”隔开，如“SEX,PARTY”
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String enumType) {
		try {
			@SuppressWarnings("rawtypes") Map mapResult = new HashMap();
			mapResult.put("addSmc", true);
			mapResult.put("editSmc", true);
			mapResult.put("editSmc2", false);

			mapResult.put("deleteSmc", true);
			mapResult.put("ADMINSmc", true);
			List<EnumModel> enumList = new ArrayList();
			enumList.add(new EnumModel(Lang.getString("joyone.base.WlCmStorageAction.yes"), "1"));
			enumList.add(new EnumModel(Lang.getString("joyone.base.WlCmStorageAction.no"), "0"));
			mapResult.put("isInFlagEnum", enumList);
			enumList = new ArrayList();
			enumList.add(new EnumModel(Lang.getString("joyone.base.WlCmStorageAction.yes"), "1"));
			enumList.add(new EnumModel(Lang.getString("joyone.base.WlCmStorageAction.no"), "0"));
			mapResult.put("isOutFlagEnum", enumList);
			enumList = new ArrayList();
			enumList.add(new EnumModel(Lang.getString("joyone.base.WlCmStorageAction.use"), "1"));
			enumList.add(new EnumModel(Lang.getString("joyone.base.WlCmStorageAction.noUse"), "0"));
			mapResult.put("isEnableFlagEnum", enumList);
			return this.getJson(true, mapResult);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取数据列表
	 * @param pageSize 页记录数
	 * @param currPage 页码
	 * @param name 姓名
	 * @return 数据列表
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int pageSize, int currPage, String storageCd) {
		try {
			Page page = wlCmStorageService.search(currPage, pageSize, storageCd);
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过 ID获取实体对象
	 * @param id ID号
	 * @return 实体对象
	 */
	@RequestMapping(params = "action=getDataObject")
	@ResponseBody
	public String getDataObject(String id) {
		try {
			return this.getJson(true, wlCmStorageService.getObject(id));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 初始化对象属性
	 * @param action 操作名称
	 * @param id ID号
	 * @return 对易用
	 */
	@ModelAttribute("saveModel")
	public WlCmStorage initAttribute(String action, String id) {
		WlCmStorage wlCmStorage = null;
		if (action.equals("saveData")) {
			if (ValidateUtil.isEmpty(id))
				wlCmStorage = new WlCmStorage();
			else
				wlCmStorage = (WlCmStorage) wlCmStorageService.getObject(id);
		}
		return wlCmStorage;
	}

	/**
	 * 保存数据
	 * @param basePerson 人员对象
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(@ModelAttribute("saveModel") WlCmStorage wlCmStorage) {
		try {
			wlCmStorageService.saveObject(wlCmStorage);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 批量删除数据
	 * @param ids 数据ID
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String ids[]) {
		try {
			System.out.println(ids.length);
			wlCmStorageService.deleteObject(ids);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改启用状态
	 * @param ids 数据ID
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateIsEnableFlag")
	@ResponseBody
	public String updateIsEnableFlag(WlCmStorage wlCmStorage, HttpServletResponse response) {
		try {
			WlCmStorage wlCmStorage1 = (WlCmStorage) wlCmStorageService.getObject(wlCmStorage.getStorageId());
			wlCmStorage1.setIsEnableFlag(wlCmStorage.getIsEnableFlag());
			wlCmStorageService.saveObject(wlCmStorage1);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}
