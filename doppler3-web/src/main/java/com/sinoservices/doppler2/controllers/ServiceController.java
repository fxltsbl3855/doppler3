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

import com.sinoservices.doppler2.bo.MethodBo;
import com.sinoservices.doppler2.bo.ModuleBo;
import com.sinoservices.doppler2.bo.ModuleStatBo;
import com.sinoservices.doppler2.facade.ServiceFacade;
import com.sinoservices.doppler2.model.OptionsModel;
import com.sinoservices.doppler2.util.LinkUtils;
import com.sinoservices.doppler2.view.ModuleStatVo;
import com.sinoservices.xframework.core.utils.BeanUtils;
import com.sinoservices.xframework.core.utils.EmptyUtils;

@Controller
@RequestMapping("/${module-name}/service")
@Scope(value = "prototype")
public class ServiceController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

	private static final byte CONTROL = 93;
	
	@Autowired
	private ServiceFacade serviceFacade;

	/**
	 * 应用模块查询
	 * @param date
	 * @param appName
	 * @param moduleName
	 * @return List<ModuleStatBo>
	 */
	@RequestMapping(value = "/getServiceDetailList")
	@ResponseBody
	public List<ModuleStatVo> getServiceDetailList(
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "appName") String appName,
			@RequestParam(value = "moduleName") String moduleName) {
		logger.info("start serviceDetailList process...");

		List<ModuleStatBo> moduleStatBoList = new ArrayList<ModuleStatBo>();
		try {
			moduleStatBoList = serviceFacade.getServiceDetailList(startDate, endDate, appName,
					moduleName);
		} catch (Exception e) {
			logger.error("getServiceDetailList Exception : " + e.getMessage(),e);
			return null;
		}

		List<ModuleStatVo> moduleStatVoList = new ArrayList<ModuleStatVo>();
		for (ModuleStatBo moduleStatBo : moduleStatBoList) {
			ModuleStatVo moduleStatVo = new ModuleStatVo();
			BeanUtils.copyProperties(moduleStatVo, moduleStatBo);
			if (EmptyUtils.isEmpty(moduleStatVo.getLinkStatus())){
				moduleStatVo.setLinkStatus(LinkUtils.getLinkStatusByByte(CONTROL));
			}
			moduleStatVoList.add(moduleStatVo);
		}
		return moduleStatVoList;
	}

	/**
	 * 应用模块方法趋势图
	 * @param date
	 * @param appName
	 * @param moduleName
	 * @param methodName
	 * @return List<MethodBo>
	 */
	@RequestMapping(value = "/serviceChart")
	@ResponseBody
	public List<MethodBo> getServiceChart(
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "appName") String appName,
			@RequestParam(value = "moduleName") String moduleName,
			@RequestParam(value = "methodName") String methodName
			) {
		logger.info("start serviceChart process...");
		List<MethodBo> methodBos = new ArrayList<MethodBo>();
		try {
			methodBos = serviceFacade.getModuleChart(appName, moduleName, methodName, startDate, endDate);
		} catch (Exception e) {
			logger.error("getServiceChart Exception : " + e.getMessage(),e);
			return null;
		}
		return methodBos;
	}
	
	/**
	 * 根据APPName查询URL
	 * @param appName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryUrlList")
	public List<OptionsModel> queryUrlList(@RequestParam(value = "appName") String appName){
		logger.info("start queryUrlList process...");
		List<ModuleBo> moduleBoList = new ArrayList<ModuleBo>();
		List<OptionsModel> optionsModelList = new ArrayList<OptionsModel>();
		try {
			moduleBoList = serviceFacade.getServiceList(appName);
		} catch (Exception e) {
			logger.error("queryUrlList Exception : " + e.getMessage(),e);
			return null;
		}
		
		for (ModuleBo moduleBo : moduleBoList) {
			OptionsModel model = new OptionsModel();
			model.setServiceId(moduleBo.getModuleName());
			model.setName(moduleBo.getModuleName());
			optionsModelList.add(model);
		}
		return optionsModelList;
	}
}
