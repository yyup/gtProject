package com.gt.wl.cm.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Config;
import org.joyone.sys.Page;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.gt.wl.cm.model.WlCmDocument;
import com.gt.wl.cm.model.WlCmDocumentPath;
import com.gt.wl.cm.service.WlCmDocumentPathService;
import com.gt.wl.cm.service.WlCmDocumentService;

/**
 * 文件管理action层
 * @author huangbj
 * 
 */
@Controller
@RequestMapping({ "/wl/cm/wlCmDocumentAction.do", "/wl/cm/wlCmDocumentAction.dox", "/wl/cm/wlCmDocumentAction.web" })
public class WlCmDocumentAction extends BaseAction {
	private WlCmDocumentService wlCmDocumentService = (WlCmDocumentService) Sc.getBean("wl.cm.WlCmDocumentService");
	private WlCmDocumentPathService pathService = (WlCmDocumentPathService) Sc.getBean("wl.cm.WlCmDocumentPathService");

	/**
	 * 上传文件
	 * @param file 文件
	 * @param sourceCode 来源编码
	 * @param user 用户
	 * @return 文件ID
	 */
	@RequestMapping(params = "action=uploadFile")
	@ResponseBody
	public String uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, String sourceCode, User user) {
		try {
			WlCmDocument document = wlCmDocumentService.addUploadFile(file, sourceCode, user);
			return this.getJson(true, document.getId());
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 上传视频文件
	 * @param file 文件
	 * @param sourceCode 来源编码
	 * @param request 请求
	 * @return 文件ID
	 */
	@RequestMapping(params = "action=uploadVideoFile")
	@ResponseBody
	public String uploadVideoFile(@RequestParam(value = "file", required = false) MultipartFile file, String sourceCode, HttpServletRequest request) {
		try {
			String realPath = request.getSession().getServletContext().getRealPath("/upload/");
			WlCmDocument document = wlCmDocumentService.addUploadFile(file, sourceCode, realPath, Session.getCurrUser());
			return this.getJson(true, document.getId());
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 上传文件/富文本编辑使用
	 * @param file 文件
	 * @param sourceCode 来源编码
	 * @param user 用户
	 * @return 文件详细
	 */
	@RequestMapping(params = "action=uploadEditorFile")
	@ResponseBody
	public String uploadEditorFile(@RequestParam(value = "file", required = false) MultipartFile file, String sourceCode, User user) {
		try {
			WlCmDocument document = wlCmDocumentService.addUploadFile(file, sourceCode, user);
			return this.getJson(true, document);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 文件删除
	 * @param id 文件Id
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=deleteFile")
	@ResponseBody
	public String deleteFile(String id) {
		try {
			boolean result = wlCmDocumentService.deleteFile(id);
			return this.getJson(result, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据 sourceCode 来源编码返回文件列表
	 * @param user 用户
	 * @param sourceCode 来源编码
	 * @param name 文件名称
	 * @return 文件列表
	 */
	@RequestMapping(params = "action=searchFile")
	@ResponseBody
	public String searchFile(String sourceCode, String name, User user) {
		try {

			List list = wlCmDocumentService.findByName(sourceCode, name, user);
			Page page = new Page(list, list.size());
			return JSON.toJSONString(page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查看文件
	 * @param request 请求
	 * @param response 响应
	 * @param id 文件ID
	 */
	@RequestMapping(params = "action=downloadFile")
	@ResponseBody
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, String id) {
		try {
			response.setContentType("text/html;charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			WlCmDocument doc = (WlCmDocument) wlCmDocumentService.getObject(id);
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
			String downLoadPath = Config.uploadFilesPath + "/" + docPath.getPath() + "/" + doc.getFileName();
			String realName = doc.getName() + "." + doc.getPostfix();
			long fileLength = new File(downLoadPath).length();

			// response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment; filename=" + new String(realName.getBytes("gbk"), "ISO8859-1"));
			response.addHeader("Content-Length", String.valueOf(fileLength));

			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
			// bis.close();
			// bos.close();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查看视频文件
	 * @param request 请求
	 * @param id 文件ID
	 * @return 视频文件路径
	 */
	@RequestMapping(params = "action=downloadVideoFile")
	@ResponseBody
	public String downloadVideoFile(HttpServletRequest request, String id) {
		try {
			WlCmDocument doc = (WlCmDocument) wlCmDocumentService.getObject(id);
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
			String downLoadPath = "upload/" + docPath.getPath() + "/" + doc.getFileName();
			return this.getJson(true, downLoadPath);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除视频文件
	 * @param request 请求
	 * @param id 文件ID
	 * @return 视频文件路径
	 */
	@RequestMapping(params = "action=deleteVideoFile")
	@ResponseBody
	public String deleteVideoFile(HttpServletRequest request, String id) {
		try {
			String realPath = request.getSession().getServletContext().getRealPath("/upload/");
			wlCmDocumentService.deleteVideoFile(realPath, id);
			return this.getJson(true, "");

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}