package com.sinoservices.doppler2.facade.impl;

import com.sinoservices.doppler2.bo.AppInfoBo;
import com.sinoservices.doppler2.bo.JobBo;
import com.sinoservices.doppler2.bo.JobDetailBo;
import com.sinoservices.doppler2.bo.ModuleBo;
import com.sinoservices.doppler2.exception.ESParamException;
import com.sinoservices.doppler2.facade.JobFacade;
import com.sinoservices.doppler2.facade.check.ParamCheck;
import com.sinoservices.doppler2.service.JobService;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.StringUtil;
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
@Service("jobFacade")
public class JobFacadeImpl implements JobFacade {
    private static final Logger logger = LoggerFactory.getLogger(JobFacadeImpl.class);

    @Autowired
    JobService jobService;

    @Override
    public List<AppInfoBo> getAppList() {
        logger.info("getAppList invoke....");
        List<AppInfoBo> resList = jobService.getAppList();
        logger.info("getAppList invoke end....,resList = {}", JsonUtils.object2Json(resList));
        return resList;
    }

    @Override
    public List<ModuleBo> getModuleList(String appName) {
        logger.info("getModuleList invoke....appName="+appName);
        if(StringUtil.isBlank(appName)){
            throw new ESParamException("appName is empty, appName="+appName);
        }
        List<ModuleBo> resList = jobService.getModuleList(appName);
        logger.info("getModuleList invoke end....,resList = {}", JsonUtils.object2Json(resList));
        return resList;
    }

    @Override
    public List<JobBo> queryJob(String startDate, String endDate, String appName, String moduleName, String result) {
        logger.info("queryJob invoke....startDate="+startDate+",endDate="+endDate+",appName="+appName+",moduleName="+moduleName+",result="+result);
        if(!ParamCheck.checkDate(startDate,"yyyy-MM-dd HH:mm")){
            logger.error("startDate is invalid,format should be 'yyyy-MM-dd HH:mm',startDate="+startDate);
            throw new ESParamException("startDate is invalid,format should be 'yyyy-MM-dd HH:mm',startDate="+startDate);
        }
        if(!ParamCheck.checkDate(endDate,"yyyy-MM-dd HH:mm")){
            logger.error("endDate is invalid,format should be 'yyyy-MM-dd HH:mm',endDate="+endDate);
            throw new ESParamException("endDate is invalid,format should be 'yyyy-MM-dd HH:mm',endDate="+endDate);
        }

        List<JobBo> resList = jobService.queryJob(DateUtil.stirng2Date(startDate,"yyyy-MM-dd HH:mm"),DateUtil.stirng2Date(endDate,"yyyy-MM-dd HH:mm"),appName,moduleName,result);
        logger.info("queryJob invoke end....,resList = {}", resList.size());
        return resList;
    }

    @Override
    public List<JobDetailBo> queryJobDetail(String startDate, String endDate, String appName,String host, String moduleName, String result) {
        logger.info("queryJobDetail invoke....startDate="+startDate+",endDate="+endDate+",appName="+appName+",host="+host+",moduleName="+moduleName+",result="+result);
        if(!ParamCheck.checkDate(startDate,"yyyy-MM-dd HH:mm")){
            throw new ESParamException("startDate is invalid,format should be 'yyyy-MM-dd HH:mm',startDate="+startDate);
        }
        if(!ParamCheck.checkDate(endDate,"yyyy-MM-dd HH:mm")){
            throw new ESParamException("endDate is invalid,format should be 'yyyy-MM-dd HH:mm',endDate="+endDate);
        }
        if(StringUtil.isBlank(appName)){
            throw new ESParamException("appName is empty, appName="+appName);
        }
        if(StringUtil.isBlank(host)){
            throw new ESParamException("host is empty, host="+host);
        }
        List<JobDetailBo> resList = jobService.queryJobDetail(DateUtil.stirng2Date(startDate,"yyyy-MM-dd HH:mm"),DateUtil.stirng2Date(endDate,"yyyy-MM-dd HH:mm"),appName,host,moduleName,result);
        logger.info("queryJobDetail invoke end....,resList.size = {}", resList.size());
        return resList;
    }
}
