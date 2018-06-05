package cn.joyone.base.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.model.EnumModel;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.util.UuidUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.joyone.base.model.BasePerson;
import cn.joyone.base.service.BasePersonService;

@Controller
@RequestMapping("/html/base/basePersonAction.do")
public class BasePersonAction extends BaseAction {
	private BasePersonService basePersonService = (BasePersonService) Sc.getBean("base.BasePersonService");

	/**
	 * 界面初始化
	 * @param enumType 枚举类型，多个枚举项以“,”隔开，如“SEX,PARTY”
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String enumType) {
		try {
			System.out.println(Lang.getString("name"));
			Map mapResult = new HashMap();
			mapResult.put("addSmc", true);
			mapResult.put("editSmc", true);
			mapResult.put("editSmc2", false);

			mapResult.put("deleteSmc", true);
			List<EnumModel> enumList = new ArrayList();
			enumList.add(new EnumModel("男", "01"));
			enumList.add(new EnumModel("女", "02"));
			mapResult.put("sexEnum", enumList);
			enumList = new ArrayList();
			enumList.add(new EnumModel("篮球", "01"));
			enumList.add(new EnumModel("羽毛球", "02"));
			enumList.add(new EnumModel("台球", "03"));
			enumList.add(new EnumModel("卡拉OK", "04"));
			mapResult.put("hobbyEnum", enumList);
			enumList = new ArrayList();
			enumList.add(new EnumModel("技术开发部", "01"));
			enumList.add(new EnumModel("市场部", "02"));
			enumList.add(new EnumModel("行政财务部", "03"));
			enumList.add(new EnumModel("技术开发部2", "04"));
			enumList.add(new EnumModel("市场部2", "05"));
			enumList.add(new EnumModel("行政财务部2", "06"));
			enumList.add(new EnumModel("技术开发部3", "07"));
			enumList.add(new EnumModel("市场部3", "08"));
			enumList.add(new EnumModel("行政财务部3", "09"));
			enumList.add(new EnumModel("技术开发部4", "10"));
			enumList.add(new EnumModel("市场部4", "11"));
			enumList.add(new EnumModel("行政财务部4", "12"));
			mapResult.put("deptIdEnum", enumList);
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
	public String findDataList(int pageSize, int currPage, String name) {
		try {
			Page page = basePersonService.search(currPage, pageSize, name);
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
			return this.getJson(true, basePersonService.getObject(id));
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
	public BasePerson initAttribute(String action, String id) {
		BasePerson basePerson = null;
		if (action.equals("saveData")) {
			if (ValidateUtil.isEmpty(id))
				basePerson = new BasePerson();
			else
				basePerson = (BasePerson) basePersonService.getObject(id);
		}
		return basePerson;
	}

	/**
	 * 保存数据
	 * @param basePerson 人员对象
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(@ModelAttribute("saveModel") BasePerson basePerson) {
		try {
			basePersonService.saveObject(basePerson);
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
			basePersonService.deleteObject(ids);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 文件上传
	 * @param file 上传的文件
	 * @param sourceCode 代码
	 * @return
	 */
	@RequestMapping(params = "action=uploadFile")
	@ResponseBody
	public String uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, String sourceCode) {
		try {
			System.out.println(file.getOriginalFilename());
			System.out.println(file.getContentType());
			// documentID
			String documentID = UuidUtil.getUuid();
			return this.getJson(true, documentID);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 文件删除
	 * @param file 删除上传的文件
	 * @return
	 */
	@RequestMapping(params = "action=deleteFile")
	@ResponseBody
	public String deleteFile(String id) {
		try {
			System.out.println(id);

			return this.getJson(true, id);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

}