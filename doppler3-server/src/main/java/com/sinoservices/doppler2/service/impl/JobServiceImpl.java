package com.sinoservices.doppler2.service.impl;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.doppler2.bo.AppInfoBo;
import com.sinoservices.doppler2.bo.JobBo;
import com.sinoservices.doppler2.bo.JobDetailBo;
import com.sinoservices.doppler2.bo.ModuleBo;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.es.ESTemplete;
import com.sinoservices.doppler2.es.entity.AggsBucketsEntity;
import com.sinoservices.doppler2.es.entity.LogRecordJobEntity;
import com.sinoservices.doppler2.service.JobService;
import com.sinoservices.doppler2.service.assimble.Assimble;
import com.sinoservices.doppler2.service.assimble.JobAssimble;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */

@Service("jobService")
public class JobServiceImpl implements JobService {
    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Override
    public List<AppInfoBo> getAppList() {
        Map<String,AggsBucketsEntity> resMap = ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_STAT_JOB_NAME,null,null,new String[]{Constant.FIELD_APP_NAME});
        List<AppInfoBo> resList = Assimble.toAppInfoBo(resMap);
        return resList;
    }

    @Override
    public List<ModuleBo> getModuleList(String appName) {
        Map<String,AggsBucketsEntity> resMap =  ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_STAT_JOB_NAME, Assimble.getNormalMap(appName,null),null,new String[]{Constant.FIELD_CLASS_NAME});
        List<ModuleBo> resList = Assimble.aggsMapToModuleBo(resMap);
        return resList;
    }

    @Override
    public List<JobBo> queryJob(Date startDate, Date endDate, String appName, String moduleName, String result) {
        logger.info("queryJob ,startDate={},endDate={}",startDate,endDate);
        Map<String,String> boolMap = Assimble.getNormalMap(appName, moduleName);
        JobAssimble.addResultToMap(boolMap,Constant.FIELD_RESULT,result);
        Assimble.addToMap(boolMap,Constant.FIELD_ACTION,Constant.FIELD_ACTION_JOBIN);

        String[] groupByFields = Assimble.eyToArrays("appName","host","moduleName","result");
        Map<String,AggsBucketsEntity> resInMap =  ESTemplete.getIns().queryAppReqStatAggs2(DopplerConstants.INDEX_STAT_JOB_NAME,boolMap ,
                Assimble.getMRTByCSTDate(startDate,endDate),null,groupByFields,groupByFields.length,Constant.FIELD_TIME,null);

//        Assimble.addToMap(boolMap,Constant.FIELD_ACTION,Constant.FIELD_ACTION_JOBOUT);
//        groupByFields = Assimble.eyToArrays("appName","host","moduleName","result");
//        Map<String,AggsBucketsEntity> resOutMap =  ESTemplete.getIns().queryAppReqStatAggs2(DopplerConstants.INDEX_STAT_JOB_NAME,boolMap ,
//                Assimble.getMRTByCSTDate(startDate,endDate),null,groupByFields,groupByFields.length,Constant.FIELD_TIME);

        List<JobBo> resInList  = JobAssimble.assimbleOut(resInMap);
//        List<JobBo> resOutList  = JobAssimble.assimbleOut(resOutMap);

//        List<JobBo> unknownList = JobAssimble.getUnknownList(resInList,resOutList);
//        resOutList.addAll(unknownList);
        return resInList;
    }

    @Override
    public List<JobDetailBo> queryJobDetail(Date startDate, Date endDate, String appName,String host, String moduleName, String result) {
        Map<String,String> mustMap = Assimble.getNormalMap(appName, moduleName);
        Assimble.addToMap(mustMap,Constant.FIELD_HOST,host);
        JobAssimble.addResultToMap(mustMap,Constant.FIELD_RESULT,result);
        Assimble.addToMap(mustMap,Constant.FIELD_ACTION,Constant.FIELD_ACTION_JOBIN);

        List<LogRecordJobEntity> jobRecordList = ESTemplete.getIns().queryJobLogRecordSort(DopplerConstants.INDEX_STAT_JOB_NAME,mustMap,Assimble.getMRTByCSTDate(startDate,endDate),null,Constant.FIELD_TIMESTAMP,false,1,50);
        List<JobDetailBo> resList = JobAssimble.jobEntityToBo(jobRecordList);
        if(resList == null || resList.size() ==0) return Collections.emptyList();

//        Map<String,String> mustMapOut = new HashMap<String,String>(1);mustMapOut.put(Constant.FIELD_ACTION,Constant.FIELD_ACTION_JOBOUT);
//        Map<String,List<String>> shouldMap = JobAssimble.getJobIdMap(resList);
//        List<LogRecordJobEntity> outJobList = ESTemplete.getIns().queryJobLogRecordSort(DopplerConstants.INDEX_STAT_JOB_NAME,mustMapOut,null,shouldMap,Constant.FIELD_TIMESTAMP,true,1,60);
//
//        JobAssimble.fillInJob(resList,outJobList);
        return resList;
    }

    public static void main(String[] a){
        UpMessageConfig.es_addr = "192.168.0.88:9300";

        JobServiceImpl jobServiceImpl = new JobServiceImpl();
        List ll = jobServiceImpl.queryJobDetail(DateUtil.stirng2Date("2017-2-13","yyyy-MM-dd"),DateUtil.stirng2Date("2017-2-18","yyyy-MM-dd"),"ruite-web","","com.ruite.demo.task.TaskThread","UNKNOWN");
        logger.info(JsonUtils.object2Json(ll));
    }
}
