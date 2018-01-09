package com.sinoservices.doppler2.facade.impl;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.bo.MethodBo;
import com.sinoservices.doppler2.bo.ModuleBo;
import com.sinoservices.doppler2.bo.ModuleStatBo;
import com.sinoservices.doppler2.bo.QueryBo;
import com.sinoservices.doppler2.exception.ESParamException;
import com.sinoservices.doppler2.exception.RunTaskException;
import com.sinoservices.doppler2.facade.ErrorFacade;
import com.sinoservices.doppler2.facade.ServiceFacade;
import com.sinoservices.doppler2.facade.check.ParamCheck;
import com.sinoservices.doppler2.service.ErrorService;
import com.sinoservices.doppler2.service.ServiceService;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
@Service("serviceFacade")
public class ServiceFacadeImpl implements ServiceFacade {
    private static final Logger logger = LoggerFactory.getLogger(ServiceFacadeImpl.class);

    @Autowired
    ServiceService serviceService;

    @Override
    public List<ModuleBo> getServiceList(String appName) {
        logger.info("getServiceList invoked...........");
        if(StringUtil.isBlank(appName)){
            throw new ESParamException("appName is empty, appName="+appName);
        }
        List<ModuleBo> resList = null;
        try {
            resList = serviceService.getServiceList(appName);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getServiceList invoked end,resList.size="+resList.size());
        return resList;
    }

    @Override
    public List<ModuleStatBo> getServiceDetailList(String startDate,String endDate,String appName, String moduleName) {
        logger.info("getServiceDetailList invoked...........startDate="+startDate+",endDate="+endDate+",appName="+appName+",moduleName="+moduleName);
        if(!ParamCheck.checkDate(startDate)){
            throw new ESParamException("startDate is invalid,format should be 'yyyy-MM-dd',startDate="+startDate);
        }
        if(!ParamCheck.checkDate(endDate)){
            throw new ESParamException("endDate is invalid,format should be 'yyyy-MM-dd',endDate="+endDate);
        }
        if(StringUtil.isBlank(appName)){
            throw new ESParamException("appName is empty, appName="+appName);
        }
        moduleName =ParamCheck.change(moduleName);
        try {
            List<ModuleStatBo> resList = serviceService.getServiceDetailList(DateUtil.stirng2Date(startDate,"yyyy-MM-dd"),DateUtil.getNewDate(DateUtil.stirng2Date(endDate,"yyyy-MM-dd"),1), appName, moduleName);
            logger.info("getServiceDetailList invoked end,resList.size="+resList.size());
            return resList;
        }catch (Exception e){
            throw new RunTaskException("getServiceDetailList error,e="+e.getMessage(),e);
        }
    }

    @Override
    public List<MethodBo> getModuleChart(String appName,String moduleName,String method, String startDate,String endDate) {
        logger.info("getModuleChart invoked...........");
        if(!ParamCheck.checkDate(startDate)){
            throw new ESParamException("startDate is invalid,format should be 'yyyy-MM-dd',startDate="+startDate);
        }
        if(!ParamCheck.checkDate(endDate)){
            throw new ESParamException("endDate is invalid,format should be 'yyyy-MM-dd',endDate="+endDate);
        }
        if(StringUtil.isBlank(appName)){
            throw new ESParamException("appName is empty, appName="+appName);
        }
        if(StringUtil.isBlank(moduleName)){
            throw new ESParamException("moduleName is empty, moduleName="+moduleName);
        }
        List<MethodBo> resList = null;
        try{
            resList = serviceService.getModuleChart(appName,moduleName,method,DateUtil.stirng2Date(startDate,"yyyy-MM-dd"),DateUtil.getNewDate(DateUtil.stirng2Date(endDate,"yyyy-MM-dd"),1));
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getModuleChart invoked end,resList.size="+resList.size());
        return resList;
    }
}
