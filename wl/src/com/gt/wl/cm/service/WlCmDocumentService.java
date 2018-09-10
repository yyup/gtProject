package com.gt.wl.cm.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Config;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gt.wl.cm.dao.WlCmDocumentDao;
import com.gt.wl.cm.model.WlCmDocument;
import com.gt.wl.cm.model.WlCmDocumentPath;

/**
 * 文件管理Service层
 * @author huangbj
 * 
 */
@Service("wl.cm.WlCmDocumentService")
public class WlCmDocumentService extends BaseService {
	private WlCmDocumentDao wlCmDocumentDao = (WlCmDocumentDao) Sc.getBean("wl.cm.WlCmDocumentDao");
	private WlCmDocumentPathService pathService = (WlCmDocumentPathService) Sc.getBean("wl.cm.WlCmDocumentPathService");

	public WlCmDocumentService() {
		baseDao = wlCmDocumentDao;
	}

	/**
	 * 根据名称原名称和源文件夹返回对象集
	 * @param name 名称
	 * @param source :文件夹
	 * @return 对象集
	 */
	public List findByName(String source, String name) {
		try {
			if (ValidateUtil.isEmpty(name)) {
				name = "";
			}
			return wlCmDocumentDao.findByName(source, name);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据名称原名称和源文件夹返回用户图片集
	 * @param user 当前登录用户
	 * @param name 名称
	 * @param source :文件夹
	 * @return 对象集
	 */
	public List findByName(String source, String name, User user) {
		try {
			if (ValidateUtil.isEmpty(name)) {
				name = "";
			}
			return wlCmDocumentDao.findByName(source, name, user.getId());
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 上传文件
	 * @param file 文件
	 * @param sourceCode 来源编码
	 * @param user 用户
	 * @return 操作结果
	 */
	public WlCmDocument addUploadFile(MultipartFile file, String sourceCode, User user) {
		try {
			if (file == null) {
				throw new Exception("前台限制有问题！");
			}
			String fileName = file.getOriginalFilename();
			String name = fileName.split("\\.")[0];
			String postfix = fileName.split("\\.")[1];
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			Date createTime = new Date();
			String date = format.format(createTime);
			Integer num = (new Random().nextInt(100) + 1);
			fileName = name + date + num.toString() + "." + postfix;
			WlCmDocument doc = new WlCmDocument();
			doc.setName(name);
			doc.setFileName(fileName);
			doc.setPostfix(postfix);
			doc.setCreateTime(createTime);
			doc.setSourceCode(sourceCode);
			doc.setFileSize(file.getSize());
			doc.setUserId(user.getId());
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(sourceCode);
			File targetFile = new File(Config.uploadFilesPath + "//" + docPath.getPath(), fileName);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			// 保存
			file.transferTo(targetFile);
			doc.setFilePath(targetFile.getCanonicalPath().replace("\\", "/"));
			wlCmDocumentDao.addObject(doc);
			return doc;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 上传视频文件
	 * @param file 文件
	 * @param sourceCode 来源编码
	 * @param realPath 真实路径
	 * @param user 用户
	 * @return 操作结果
	 */
	public WlCmDocument addUploadFile(MultipartFile file, String sourceCode, String realPath, User user) {
		try {
			if (file == null) {
				throw new Exception("前台限制有问题！");
			}
			String fileName = file.getOriginalFilename();
			String name = fileName.split("\\.")[0];
			String postfix = fileName.split("\\.")[1];
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			Date createTime = new Date();
			String date = format.format(createTime);
			Integer num = (new Random().nextInt(100) + 1);
			// fileName = name + date + num.toString() + "." + postfix;
			fileName = date + num.toString() + "." + postfix;
			WlCmDocument doc = new WlCmDocument();
			doc.setName(name);
			doc.setFileName(fileName);
			doc.setPostfix(postfix);
			doc.setCreateTime(createTime);
			doc.setSourceCode(sourceCode);
			doc.setFileSize(file.getSize());
			doc.setUserId(user.getId());

			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(sourceCode);
			File targetFile = new File(realPath + "//" + docPath.getPath(), fileName);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			// 保存
			file.transferTo(targetFile);
			doc.setFilePath(targetFile.getCanonicalPath().replace("\\", "/"));
			wlCmDocumentDao.addObject(doc);
			return doc;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * APP端上传文件
	 * @param file 文件
	 * @param sourceCode 来源编码
	 * @param user 用户
	 * @return 操作结果
	 * @author zhaown
	 */
	public WlCmDocument addUploadFileByApp(MultipartFile file, String sourceCode, User user) {
		try {
			if (file == null) {
				throw new Exception("前台限制有问题！");
			}
			String fileName = file.getOriginalFilename();
			String name = fileName.split("\\.")[0];
			String postfix;
			if (!file.getOriginalFilename().contains(".")) {
				postfix = "jpg";
			}
			else {
				postfix = fileName.split("\\.")[1];
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			Date createTime = new Date();
			String date = format.format(createTime);
			Integer num = (new Random().nextInt(100) + 1);
			fileName = name + date + num.toString() + "." + postfix;
			WlCmDocument doc = new WlCmDocument();
			doc.setName(name);
			doc.setFileName(fileName);
			doc.setPostfix(postfix);
			doc.setCreateTime(createTime);
			doc.setSourceCode(sourceCode);
			doc.setFileSize(file.getSize());
			doc.setUserId(user.getId());
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(sourceCode);
			File targetFile = new File(Config.uploadFilesPath + "//" + docPath.getPath(), fileName);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			// 保存
			file.transferTo(targetFile);
			doc.setFilePath(targetFile.getCanonicalPath().replace("\\", "/"));
			wlCmDocumentDao.addObject(doc);
			return doc;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 下载文件
	 * @param request 请求
	 * @param response 响应
	 * @param id 文件ID
	 * @author zhaown
	 */
	@RequestMapping(params = "action=downloadFile")
	@ResponseBody
	public void getFile(HttpServletRequest request, HttpServletResponse response, String id) {
		try {
			if (!ValidateUtil.isEmpty(id)) {
				response.setContentType("text/html;charset=UTF-8");
				request.setCharacterEncoding("UTF-8");
				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;

				WlCmDocument doc = (WlCmDocument) wlCmDocumentDao.getObject(id);
				WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
				String downLoadPath = Config.uploadFilesPath + "/" + docPath.getPath() + "/" + doc.getFileName();
				String realName = doc.getName() + "." + doc.getPostfix();
				long fileLength = new File(downLoadPath).length();

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
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除文件
	 * @param id 文件ID
	 * @return 操作结果
	 */
	public boolean deleteFile(String id) {
		try {
			WlCmDocument doc = (WlCmDocument) wlCmDocumentDao.getObject(id);
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
			File file = new File(Config.uploadFilesPath + "//" + docPath.getPath(), doc.getFileName());
			if (file.isFile() && file.exists()) {
				file.delete();
				wlCmDocumentDao.deleteObject(doc.getId());
				return true;
			}
			else {
				return false;
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除文件
	 * @param realPath 真实路径
	 * @param id 文件ID
	 * @return 操作结果
	 */
	public boolean deleteVideoFile(String realPath, String id) {
		try {
			WlCmDocument doc = (WlCmDocument) wlCmDocumentDao.getObject(id);
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
			File file = new File(realPath + "//" + docPath.getPath(), doc.getFileName());
			if (file.isFile() && file.exists()) {
				file.delete();
				wlCmDocumentDao.deleteObject(doc.getId());
				return true;
			}
			else {
				return false;
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过soucseId查找文件
	 * @param sourceId 文件的sourceId
	 * @return 文件信息
	 */
	public WlCmDocument findBySourceId(String sourceId) {
		try {
			return wlCmDocumentDao.findBySourceId(sourceId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过id和workUploadFile表相同查询文件信息
	 * @return 文件信息
	 */
	public List findByUploadId() {
		try {
			return wlCmDocumentDao.findByUploadId();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}