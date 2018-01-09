package com.sinoservices.doppler2.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinoservices.doppler2.bo.AppInfoBo;
import com.sinoservices.doppler2.bo.WebBo;
import com.sinoservices.doppler2.bo.WebDetailBo;
import com.sinoservices.doppler2.facade.AppFacade;
import com.sinoservices.doppler2.facade.WebFacade;
import com.sinoservices.doppler2.model.OptionsModel;
import com.sinoservices.doppler2.util.LinkUtils;
import com.sinoservices.doppler2.view.WebVo;
import com.sinoservices.xframework.core.utils.BeanUtils;
import com.sinoservices.xframework.core.utils.EmptyUtils;

@Controller
@RequestMapping("/${module-name}/web")
@Scope(value = "prototype")
public class WebController {

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(WebController.class);
	
	private static final byte CONTROL = 92;
	
	@Autowired
	private WebFacade webFacade;
	
	@Autowired
	private AppFacade appFacade;
	
	/**
	 * web操作查询列表查询
	 * @param startDate
	 * @param endDate
	 * @param appName
	 * @param moduleName
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryWeb")
	public List<WebVo> queryWeb(
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "appName") String appName,
			@RequestParam(value = "moduleName") String moduleName,
			@RequestParam(value = "username") String username
			){
		logger.info("start queryWeb process...");
		List<WebBo> webBoList = new ArrayList<WebBo>();
		try {
			webBoList = webFacade.queryWeb(startDate, endDate, getEmptyFromDefaultValue(appName), 
					getEmptyFromDefaultValue(moduleName), username);
		} catch (Exception e) {
			logger.error("queryWeb Exception : " + e.getMessage(), e);
			return null;
		}
		
		List<WebVo> webVoList = new ArrayList<WebVo>();
		for (WebBo webBo : webBoList) {
			WebVo webVo = new WebVo();
			BeanUtils.copyProperties(webVo, webBo);
			if (EmptyUtils.isEmpty(webVo.getLinkStatus())){
				webVo.setLinkStatus(LinkUtils.getLinkStatusByByte(CONTROL));
			}
			webVo.setReqTime(webBo.getReqTime());
			webVoList.add(webVo);
		}
		
		return webVoList;
	}
	
	/**
	 * web操作查询明细查询
	 * @param startDate
	 * @param endDate
	 * @param appName
	 * @param moduleName
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryWebDetail")
	public WebDetailBo queryWebDetail(@RequestParam(value = "id") String id){
		logger.info("start queryWebDetail process...");
		WebDetailBo webDetailBo = null;
		try {
			webDetailBo = webFacade.queryWebDetail(id);
		} catch (Exception e) {
			logger.error("queryWebDetail Exception : " + e.getMessage(), e);
			return null;
		}
		return webDetailBo;
	}
	
	/**
	 * 查询应用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWebAppList")
	public List<OptionsModel> getWebAppList() {
		List<OptionsModel> optionsModels = new ArrayList<OptionsModel>();
		List<AppInfoBo> appInfoBoList = new ArrayList<AppInfoBo>();
		try {
			appInfoBoList = appFacade.getAppList(1);
		} catch (Exception e) {
			logger.error("getWebAppList Exception : " + e.getMessage(), e);
			return null;
		}

		if (appInfoBoList != null && appInfoBoList.size() > 0) {
			for (AppInfoBo appInfoBo : appInfoBoList) {
				OptionsModel model = new OptionsModel();
				model.setServiceId(appInfoBo.getAppName());
				model.setName(appInfoBo.getAppName());
				optionsModels.add(model);
			}
		}
		return optionsModels;
	}
	
	private String getEmptyFromDefaultValue(String value){
		if (value == null){
			return "";
		}
		
		if ("-1".equals(value)){
			return "";
		}
		
		return value;
	}
}
