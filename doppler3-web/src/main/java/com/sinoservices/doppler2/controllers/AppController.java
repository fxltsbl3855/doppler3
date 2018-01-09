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

import com.sinoservices.doppler2.bo.AppStatBo;
import com.sinoservices.doppler2.bo.DashboardBo;
import com.sinoservices.doppler2.bo.HostStatBo;
import com.sinoservices.doppler2.bo.TopTimeBo;
import com.sinoservices.doppler2.facade.AppFacade;
import com.sinoservices.doppler2.model.DashboardModel;
import com.sinoservices.doppler2.util.LinkUtils;
import com.sinoservices.doppler2.view.AppStatVo;
import com.sinoservices.doppler2.view.HostStatVo;
import com.sinoservices.doppler2.view.TopTimeVo;
import com.sinoservices.xframework.core.utils.BeanUtils;
import com.sinoservices.xframework.core.utils.EmptyUtils;


@Controller
@RequestMapping("/doppler/app")
@Scope(value = "prototype")
public class AppController  {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);
    
    private static final boolean CONTROL = true;
    
    private static final String CONTROL_STR = "123";
    
	@Autowired
	private AppFacade appFacade;
	
	/**
	 * DashBoard
	 * @return
	 */
	@RequestMapping(value="/dashboard")
	@ResponseBody
	public DashboardModel dashboard(){
        logger.info("start dashboard process...");
        DashboardModel dashboardModel = new DashboardModel();
        DashboardBo dashboardBo = null;
		try {
			dashboardBo = appFacade.getDashboard();
		} catch (Exception e) {
			logger.error("Dashboard Exception:" + e.getMessage(),e);
			dashboardModel.setAppNum(-1);
			dashboardModel.setModuleNum(-1);
			dashboardModel.setAppReqNum(-1);
			dashboardModel.setModuleReqNum(-1);
			dashboardModel.setErrorNum(-1);
			return dashboardModel;
		}
		BeanUtils.copyProperties(dashboardModel, dashboardBo);
		return dashboardModel;
	}
	
	
	/**
	 * 应用列表
	 * @param date
	 * @return
	 */
	@RequestMapping(value="/appDetailList")
	@ResponseBody
	public List<AppStatVo> getAppDetailList(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		logger.info("start appDetailList process...");
		List<AppStatBo> appStatBoList = new ArrayList<AppStatBo>();
		try {
			appStatBoList = appFacade.getAppDetailList(startDate, endDate);
		} catch (Exception e) {
			logger.error("getAppDetailList Exception : " + e.getMessage(),e);
			return null;
		}
		
		List<AppStatVo> appStatVoList = new ArrayList<AppStatVo>();
		for (AppStatBo appStatBo : appStatBoList) {
			AppStatVo appStatVo = new AppStatVo();
			BeanUtils.copyProperties(appStatVo, appStatBo);
			if (EmptyUtils.isEmpty(appStatVo.getLinkStatus())){
				appStatVo.setLinkStatus(LinkUtils.getLinkStatusByString(CONTROL_STR));
			}
			appStatVoList.add(appStatVo);
		}
		return appStatVoList;
	}
	
	/**
	 * 主机监控
	 * @param date
	 * @return
	 */
	@RequestMapping(value="/getHostDetailList")
	@ResponseBody
	public List<HostStatVo> getHostDetailList(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		logger.info("start getHostDetailList process...");
		List<HostStatBo> hostStatBoList = new ArrayList<HostStatBo>();
		try {
			hostStatBoList = appFacade.getHostDetailList(startDate, endDate);
		} catch (Exception e) {
			logger.error("getHostDetailList Exception : " + e);
			return null;
		}
		
		List<HostStatVo> hostStatVoList = new ArrayList<HostStatVo>();
		for (HostStatBo hostStatBo : hostStatBoList) {
			HostStatVo hostStatVo = new HostStatVo();
			BeanUtils.copyProperties(hostStatVo, hostStatBo);
			if (EmptyUtils.isEmpty(hostStatVo.getLinkStatus())){
				hostStatVo.setLinkStatus(LinkUtils.getLinkStatusByBoolean(CONTROL));
			}
			hostStatVoList.add(hostStatVo);
		}
		return hostStatVoList;
	}
	
	/**
	 * 应用，模块列表页面点击 “最大耗时”展示页面；（应用列表页，模块名，方法名传空）
	 * @param startDate
	 * @param endDate
	 * @param host
	 * @param appName
	 * @param moduleName
	 * @param methodName
	 * @param size
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/topMaxTimeRequest")
	public List<TopTimeVo> topMaxTimeRequest(
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "host") String host,
			@RequestParam(value = "appName") String appName,
			@RequestParam(value = "moduleName") String moduleName,
			@RequestParam(value = "methodName") String methodName
			){
		logger.info("start topMaxTimeRequest process...");
		if (!CONTROL){
			logger.error("[topMaxTimeRequest] is disabled.");
			return null;
		}
		
		List<TopTimeBo> topTimeBoList = new ArrayList<TopTimeBo>();
		try {
			topTimeBoList = appFacade.topReq(startDate, endDate, host, appName, moduleName, methodName, 30);
		} catch (Exception e) {
			logger.error("topMaxTimeRequest Exception : " + e);
			return null;
		}
		
		List<TopTimeVo> topTimeVoList = new ArrayList<TopTimeVo>();
		for (TopTimeBo topTimeBo : topTimeBoList) {
			TopTimeVo topTimeVo = new TopTimeVo();
			BeanUtils.copyProperties(topTimeVo, topTimeBo);
			if (EmptyUtils.isEmpty(topTimeVo.getLinkStatus())){
				topTimeVo.setLinkStatus(LinkUtils.getLinkStatusByString(CONTROL_STR));
			}
			topTimeVo.setReqTime(topTimeBo.getReqTime());
			topTimeVoList.add(topTimeVo);
		}
		return topTimeVoList;
	}
}
