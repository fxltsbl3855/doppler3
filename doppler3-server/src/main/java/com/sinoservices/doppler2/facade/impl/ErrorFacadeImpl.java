package com.sinoservices.doppler2.facade.impl;

import com.sinoservices.doppler2.bo.*;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.exception.ESParamException;
import com.sinoservices.doppler2.facade.ErrorFacade;
import com.sinoservices.doppler2.facade.check.ParamCheck;
import com.sinoservices.doppler2.service.ErrorService;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
@Service("errorFacade")
public class ErrorFacadeImpl implements ErrorFacade {
    private static final Logger logger = LoggerFactory.getLogger(ErrorFacadeImpl.class);

    @Autowired
    ErrorService errorService;

    @Override
    public List<QueryBo> queryErrorByKey(String startDate, String endDate, String appName, String moduleName, String action, String result, String errorLevel, String host, String keys) {
        logger.info("queryErrorByKey invoked...........startDate="+startDate +",endDate="+endDate  +",appName="+appName +",moduleName="+moduleName +",action="+action +",result="+result
                +",errorLevel="+errorLevel+",host="+host+",keys="+keys);
        if(ParamCheck.checkTime(startDate)== false || ParamCheck.checkTime(endDate) == false){
            throw new ESParamException("startDate or endDate is invalid,format should be 'yyyy-MM-dd HH:mm:ss',startDate="+startDate+",endDate="+endDate);
        }
        appName =ParamCheck.change(appName);
        moduleName =ParamCheck.change(moduleName);
        errorLevel =ParamCheck.change(errorLevel);

        List<QueryBo> resList = null;
        try{
            resList = errorService.queryErrorByKey(startDate,endDate,appName,errorLevel,host,keys);
        }catch (Exception e){
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("queryErrorByKey invoked end,resList.size="+(resList==null?0:resList.size()));
        return resList;
    }

    @Override
    public List<QueryBo> queryErrorById(String id, int forward,String host) {
        logger.info("queryErrorById invoked...........id="+id+",forward="+forward+",host="+host);
        if(StringUtil.isBlank(id)){
            throw new ESParamException("id should not be empty");
        }
        if(forward<-1 || forward > 1){
            throw new ESParamException("forward should be[-1,1],forward="+forward);
        }
        List<QueryBo> resList = null;
        try{
            resList =  errorService.queryErrorById(id,forward,host);
        }catch (Exception e){
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("queryErrorById invoked end,resList.size="+resList.size());
        return resList;
    }

    @Deprecated
    @Override
    public List<ErrorTypeBo> getErrorList() {
        logger.info("getErrorList invoked...........");
        List<ErrorTypeBo> resList = new ArrayList<ErrorTypeBo>();
        try{
            resList =  errorService.getErrorList();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getErrorList invoked end,resList.size="+resList.size());
        return resList;
    }

    @Override
    public List<ErrorTypeBo> getErrorList(String startDateStr,String endDateStr ,String errorName) {
        logger.info("getErrorList invoked...........startDateStr={},endDateStr={},errorName="+errorName,startDateStr ,endDateStr );

        Date startDate  = DateUtil.stirng2Date(startDateStr,"yyyy-MM-dd");
        Date endDate  = DateUtil.stirng2Date(endDateStr,"yyyy-MM-dd");

        if(startDate == null){
            throw new ESParamException("startDateStr is invalid,format should be 'yyyy-MM-dd',startDateStr="+startDateStr);
        }
        if(endDate == null){
            throw new ESParamException("endDateStr is invalid,format should be 'yyyy-MM-dd',endDateStr="+endDateStr);
        }

        if(startDate.compareTo(endDate) > 0){
            throw new ESParamException("startDateStr is greater than endDateStr, please check param");
        }

        Date today = DateUtil.stirng2Date(DateUtil.Date2String(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd");
        Date minDate = DateUtil.getNewDate(today,-30);
        if(startDate.compareTo(minDate) < 0){
            logger.warn("startDateStr is too old , reset to before 30 days");
            startDate = minDate;
        }

        if(endDate.compareTo(today) > 0){
            logger.warn("endDateStr is after today , reset to today");
            endDate = today;
        }

        endDate = DateUtil.getNewDate(endDate,1);
        logger.info("param process over,startDate = {} ,endDate = {}",startDate,endDate);


        List<ErrorTypeBo> resList = new ArrayList<ErrorTypeBo>();
        try{
            resList =  errorService.getErrorList(startDate,endDate,errorName);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getErrorList invoked end,resList.size="+resList.size());
        return resList;
    }

    @Override
    public List<ErrorHostBo> getErrorHostList(String errorName,String appName) {
        logger.info("getErrorHostList invoked...........errorName="+errorName+",appName="+appName);
        List<ErrorHostBo> resList = new ArrayList<ErrorHostBo>();
        try{
            resList =  errorService.getErrorHostList(errorName,appName);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getErrorHostList invoked end,resList.size="+resList.size());
        return resList;
    }

    @Override
    public List<ErrorHostBo> getErrorHostList(String errorName,String appName ,String startDateStr,String endDateStr) {
        logger.info("getErrorHostList invoked...........errorName="+errorName+",appName="+appName+",startDateStr="+startDateStr+",endDateStr="+endDateStr);

        Date startDate  = DateUtil.stirng2Date(startDateStr,"yyyy-MM-dd");
        Date endDate  = DateUtil.stirng2Date(endDateStr,"yyyy-MM-dd");
        endDate = DateUtil.getNewDate(endDate,1);

        List<ErrorHostBo> resList = new ArrayList<ErrorHostBo>();
        try{
            resList =  errorService.getErrorHostList(errorName,appName,startDate,endDate);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getErrorHostList invoked end,resList.size="+resList.size());
        return resList;
    }

    @Override
    public List<ProblemBo> getProblemList(String startDateStr, String endDateStr, String appName, String errorName) {
        logger.info("getProblemList invoked...........errorName="+errorName+",appName="+appName+",startDateStr="+startDateStr+",endDateStr="+endDateStr);

        Date startDate  = DateUtil.stirng2Date(startDateStr,"yyyy-MM-dd");
        Date endDate  = DateUtil.stirng2Date(endDateStr,"yyyy-MM-dd");
        endDate = DateUtil.getNewDate(endDate,1);

        List<ProblemBo> resList = new ArrayList<ProblemBo>();
        try{
            resList =  errorService.getProblemList(startDate,endDate,appName,errorName);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getProblemList invoked end,resList.size="+resList.size());
        return resList;
    }

    @Override
    public List<ProblemDetailBo> getProblemDetailList(String startDateStr, String endDateStr, String appName, String errorName, String className, String methodName, long lineNum) {
        logger.info("getProblemDetailList invoked...........errorName="+errorName+",appName="+appName+
                ",className="+className+",methodName="+methodName+",lineNum="+lineNum+",startDateStr="+startDateStr+",endDateStr="+endDateStr);
        Date startDate  = DateUtil.stirng2Date(startDateStr,"yyyy-MM-dd");
        Date endDate  = DateUtil.stirng2Date(endDateStr,"yyyy-MM-dd");
        endDate = DateUtil.getNewDate(endDate,1);

        List<ProblemDetailBo> resList = new ArrayList<ProblemDetailBo>();
        try{
            resList =  errorService.getProblemDetailList(startDate,endDate,appName,errorName,className,methodName,lineNum);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("facade error,e="+e.getMessage(),e);
        }
        logger.info("getProblemDetailList invoked end,resList.size="+resList.size());
        return resList;
    }


}
