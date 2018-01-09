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
import com.sinoservices.doppler2.bo.ModuleBo;
import com.sinoservices.doppler2.facade.AppFacade;
import com.sinoservices.doppler2.facade.ServiceFacade;
import com.sinoservices.doppler2.model.OptionsModel;

/**
 * 获取下拉框
 */
@Controller
@RequestMapping("/${module-name}/components/options")
@Scope(value = "prototype")
public class OptionsController {

	private static final Logger logger = LoggerFactory
			.getLogger(OptionsController.class);

	@Autowired
	private AppFacade appFacade;
	@Autowired
	private ServiceFacade serviceFacade;

	/**
	 * 应用名下拉框
	 * 
	 * @return List<OptionsModel>
	 */
	@RequestMapping(value = "/getAppList")
	@ResponseBody
	public List<OptionsModel> getAppList() {
		List<OptionsModel> optionsModels = new ArrayList<OptionsModel>();
		List<AppInfoBo> appInfoBoList = new ArrayList<AppInfoBo>();
		try {
			appInfoBoList = appFacade.getAppList();
		} catch (Exception e) {
			logger.error("getAppList Exception : " + e.getMessage(), e);
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

	/**
	 * 模块名下拉框
	 * 
	 * @param appName
	 * @return List<OptionsModel>
	 */
	@RequestMapping(value = "/getServiceList")
	@ResponseBody
	public List<OptionsModel> getServiceList(
			@RequestParam(value = "appName") String appName) {

		List<OptionsModel> optionsModels = new ArrayList<OptionsModel>();
		List<ModuleBo> moduleBoList = new ArrayList<ModuleBo>();
		try {
			moduleBoList = serviceFacade.getServiceList(appName);
		} catch (Exception e) {
			logger.error("getServiceList Exception : " + e.getMessage(),e);
			return null;
		}

		if (moduleBoList != null && moduleBoList.size() > 0) {
			for (ModuleBo moduleBo : moduleBoList) {
				OptionsModel model = new OptionsModel();
				model.setServiceId(moduleBo.getModuleName());
				model.setName(moduleBo.getModuleName());
				optionsModels.add(model);
			}
		}
		return optionsModels;
	}
}
