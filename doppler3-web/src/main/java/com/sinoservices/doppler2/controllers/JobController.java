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
import com.sinoservices.doppler2.bo.JobBo;
import com.sinoservices.doppler2.bo.JobDetailBo;
import com.sinoservices.doppler2.bo.ModuleBo;
import com.sinoservices.doppler2.facade.JobFacade;
import com.sinoservices.doppler2.model.OptionsModel;
import com.sinoservices.doppler2.util.LinkUtils;
import com.sinoservices.doppler2.view.JobDetailVo;
import com.sinoservices.doppler2.view.JobVo;
import com.sinoservices.xframework.core.utils.BeanUtils;
import com.sinoservices.xframework.core.utils.EmptyUtils;

@Controller
@RequestMapping("/${module-name}/job")
@Scope(value = "prototype")
public class JobController {

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(JobController.class);
	
	private static final int CONTROL = 17;
	
	@Autowired
	private JobFacade jobFacade;
	
	/**
	 * 获取定时任务应用接口列表
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAppList")
	public List<OptionsModel> getAppList(){
		logger.info("start getAppList process...");
		List<AppInfoBo> appInfoBoList = new ArrayList<AppInfoBo>();
		List<OptionsModel> optionsModels = new ArrayList<OptionsModel>();
		try {
			appInfoBoList = jobFacade.getAppList();
		} catch (Exception e) {
			logger.error("getAppList Exception : " + e.getMessage(), e);
			return null;
		}
		
		if (EmptyUtils.isEmpty(appInfoBoList)) {
			return optionsModels;
		}
		
		for (AppInfoBo appInfoBo : appInfoBoList) {
			OptionsModel model = new OptionsModel();
			model.setServiceId(appInfoBo.getAppName());
			model.setName(appInfoBo.getAppName());
			optionsModels.add(model);
		}
		return optionsModels;
	}
	
	/**
	 * 获取服务接口列表
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getModuleList")
	public List<OptionsModel> getModuleList(@RequestParam(value = "appName") String appName){
		logger.info("start getModuleList process...");
		List<ModuleBo> moduleBoList = new ArrayList<ModuleBo>();
		List<OptionsModel> optionsModels = new ArrayList<OptionsModel>();
		try {
			moduleBoList = jobFacade.getModuleList(appName);
		} catch (Exception e) {
			logger.error("getModuleList Exception : " + e.getMessage(), e);
			return null;
		}
		
		for (ModuleBo moduleBo : moduleBoList) {
			OptionsModel model = new OptionsModel();
			model.setServiceId(moduleBo.getModuleName());
			model.setName(moduleBo.getModuleName());
			optionsModels.add(model);
		}
		return optionsModels;
	}
	
	/**
	 * 定时任务列表查询
	 * @param startDate
	 * @param endDate
	 * @param appName
	 * @param moduleName
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryJob")
	public List<JobVo> queryJob(
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "appName") String appName,
			@RequestParam(value = "moduleName") String moduleName,
			@RequestParam(value = "result") String result
			){
		logger.info("start queryJob process...");
		List<JobBo> jobBoList = new ArrayList<JobBo>();
		try {
			jobBoList = jobFacade.queryJob(startDate, endDate, 
					getEmptyFromDefaultValue(appName), 
					getEmptyFromDefaultValue(moduleName), 
					getEmptyFromDefaultValue(result));
		} catch (Exception e) {
			logger.error("queryJob Exception : " + e.getMessage(), e);
			return null;
		}
		
		List<JobVo> jobVoList = new ArrayList<JobVo>();
		for (JobBo jobBo : jobBoList) {
			JobVo jobVo = new JobVo();
			BeanUtils.copyProperties(jobVo, jobBo);
			if (EmptyUtils.isEmpty(jobVo.getLinkStatus())){
				jobVo.setLinkStatus(LinkUtils.getLinkStatusByNum(CONTROL));
			}
			jobVoList.add(jobVo);
		}
		return jobVoList;
	}
	
	/**
	 * 定时任务明细查询
	 * @param startDate
	 * @param endDate
	 * @param appName
	 * @param moduleName
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryJobDetail")
	public List<JobDetailVo> queryJobDetail(
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "appName") String appName,
			@RequestParam(value = "host") String host,
			@RequestParam(value = "moduleName") String moduleName,
			@RequestParam(value = "result") String result
			){
		logger.info("start queryJobDetail process...");
		List<JobDetailBo> jobDetailBoList = new ArrayList<JobDetailBo>();
		try {
			jobDetailBoList = jobFacade.queryJobDetail(startDate, endDate, appName, host, moduleName, result);
		} catch (Exception e) {
			logger.error("queryJobDetail Exception : " + e.getMessage(), e);
			return null;
		}
		
		List<JobDetailVo> jobDetailVoList = new ArrayList<JobDetailVo>();
		for (JobDetailBo jobDetailBo : jobDetailBoList) {
			JobDetailVo jobDetailVo = new JobDetailVo();
			BeanUtils.copyProperties(jobDetailVo, jobDetailBo);
			jobDetailVo.setExecuteTime(jobDetailBo.getExecuteTime());
			jobDetailVoList.add(jobDetailVo);
		}
		return jobDetailVoList;
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
