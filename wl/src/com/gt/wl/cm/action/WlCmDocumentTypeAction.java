package com.gt.wl.cm.action;

import org.joyone.action.BaseAction;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gt.wl.cm.service.WlCmDocumentTypeService;

@Controller
@RequestMapping("/wl/wlCmDocumentTypeAction.do")
public class WlCmDocumentTypeAction extends BaseAction {
	private WlCmDocumentTypeService wlCmDocumentTypeService = (WlCmDocumentTypeService) Sc.getBean("wl.WlCmDocumentTypeService");

}