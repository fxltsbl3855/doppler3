package com.sinoservices.doppler2.facade.impl;

import com.sinoservices.doppler2.bo.*;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.exception.ESParamException;
import com.sinoservices.doppler2.facade.AppFacade;
import com.sinoservices.doppler2.facade.assimble.FacadeAssimble;
import com.sinoservices.doppler2.facade.check.ParamCheck;
import com.sinoservices.doppler2.service.AppService;
import com.sinoservices.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
@Service("appFacade")
public class AppFacadeImpl implements AppFacade {
    private static final Logger logger = LoggerFactory.getLogger(AppFacadeImpl.class);

    public static void main(String[] aa){
        AppFacadeImpl appFacadeImpl = new AppFacadeImpl();
        appFacadeImpl.test();
    }

    @Autowired
    AppService appService;

    @Override
    public String test() {
        return null;
    }

    @Override
    public DashboardBo getDashboard() {
        logger.info("getDashboard invoked...........");
        try {
            DashboardBo dashboardBo = appService.getDashboard();
            FacadeAssimble.sortList(dashboardBo);
            logger.info("getDashboard invoked end...........");
            return dashboardBo;
        }catch (Exception e){
            logger.error("getDashboard error",e);
            return new DashboardBo();
        }
    }

    @Override
    public List<AppStatBo> getAppDetailList(String startDate,String endDate) {
        logger.info("getAppDetailList invoked...........");
        if(!ParamCheck.checkDate(startDate)){
            throw new ESParamException("startDate is invalid,format should be 'yyyy-MM-dd',startDate="+startDate);
        }
        if(!ParamCheck.checkDate(endDate)){
            throw new ESParamException("endDate is invalid,format should be 'yyyy-MM-dd',endDate="+endDate);
        }
        List<AppStatBo> resList = null;
        try {
            resList = appService.getAppDetailList(DateUtil.stirng2Date(startDate,"yyyy-MM-dd"),DateUtil.getNewDate(DateUtil.stirng2Date(endDate,"yyyy-MM-dd"),1));
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getAppDetailList invoked end,resList.size="+resList.size());
        return resList;
    }

    @Override
    public List<HostStatBo> getHostDetailList(String startDate,String endDate) {
        logger.info("getHostDetailList invoked...........");
        if(!ParamCheck.checkDate(startDate)){
            throw new ESParamException("startDate is invalid,format should be 'yyyy-MM-dd',startDate="+startDate);
        }
        if(!ParamCheck.checkDate(endDate)){
            throw new ESParamException("endDate is invalid,format should be 'yyyy-MM-dd',endDate="+endDate);
        }
        List<HostStatBo> resList = null;
        try{
            resList =appService.getHostDetailList(DateUtil.stirng2Date(startDate,"yyyy-MM-dd"),DateUtil.getNewDate(DateUtil.stirng2Date(endDate,"yyyy-MM-dd"),1));
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getHostDetailList invoked end,resList.size="+resList.size());
        return resList;
    }

    @Override
    public List<AppInfoBo> getAppList() {
        logger.info("getAppList invoked...........");
        List<AppInfoBo> resList = null;
        try{
            resList = appService.getAppList();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getAppList invoked end,resList.size="+resList.size());
        return resList;
    }

    @Override
    public List<AppInfoBo> getAppList(int appType) {
        logger.info("getAppList invoked...........appType={}",appType);
        List<AppInfoBo> resList = null;
        try{
            resList = appService.getWebAppList();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getAppList invoked end,resList.size="+resList.size());
        return resList;
    }

    @Override
    public List<TopTimeBo> topReq(String startDate,String endDate,String host,String appName,String moduleName,String method, int size) {
        logger.info("topReq invoked...........startDate="+startDate+",endDate="+endDate+",appName="+appName+",host="+host+",moduleName="+moduleName+",method="+method+",size="+size);
        List<TopTimeBo> resList = null;
        try{
            resList = appService.topReq(DateUtil.stirng2Date(startDate,"yyyy-MM-dd"),DateUtil.stirng2Date(endDate,"yyyy-MM-dd"),host,appName,moduleName,method ,size);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("topReq invoked end,resList.size="+resList.size());
        return resList;
    }
}
