package com.sinoservices.doppler2.service.impl;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.doppler2.bo.*;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.es.ESTemplete;
import com.sinoservices.doppler2.es.entity.*;
import com.sinoservices.doppler2.service.ErrorService;
import com.sinoservices.doppler2.service.assimble.Assimble;
import com.sinoservices.doppler2.service.assimble.ComputeAssimble;
import com.sinoservices.doppler2.service.assimble.ErrorAssimble;
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
@Service("errorService")
public class ErrorServiceImpl implements ErrorService {
    private static final Logger logger = LoggerFactory.getLogger(ErrorServiceImpl.class);

    @Override
    public List<QueryBo> queryErrorByKey(String startTime, String endTime, String appName, String errorLevel, String host, String keys) {
        Map<String,String> musuMap =  Assimble.getNormalMap(appName,null);
        if(!StringUtil.isBlank(host)) {
            musuMap.put(Constant.FIELD_HOST, host);
        }
        if(!StringUtil.isBlank(errorLevel)) {
            musuMap.put(Constant.FIELD_LOGLEVEL, errorLevel);
        }

        MustRangeTimestamp timestamp =  Assimble.getMRTByCSTDate(startTime,endTime);
        Map<String, String> shouldMatchMap = Assimble.getMapValueNotNull(Constant.FIELD_ORIGINAL,keys);
        List<LogRecordEntity> recordList = ESTemplete.getIns().queryLogRecordByScore(DopplerConstants.INDEX_NAME,musuMap,timestamp,shouldMatchMap,1,20 );
        return  ErrorAssimble.translate(recordList);
    }


    @Override
    public List<QueryBo> queryErrorById(String id, int forward,String host) {
        String appName = Assimble.getAppNameFromId(id);
        String file = Assimble.getFileFromId(id);
        long currentOffset = Assimble.getLinePositionFromId(id);

        long startOffset = ComputeAssimble.getStartOffset(currentOffset,forward);
        long endOffset = ComputeAssimble.getEndOffset(currentOffset,forward);
        MustRange<Long> mustRange = new MustRange<Long>(Constant.FIELD_POSITION,startOffset,endOffset);
        Map<String, String> mustparam = Assimble.getMapValueNotNull(Constant.FIELD_APP_NAME,appName,Constant.FIELD_HOST,host);
        Assimble.addToMap(mustparam,Constant.FIELD_FILE,file);

        List<LogRecordEntity> queryList = ESTemplete.getIns().queryLogRecordSort(DopplerConstants.INDEX_NAME,mustparam,
                mustRange,null,Constant.FIELD_POSITION,true,1,65);
        List<QueryBo> list = ErrorAssimble.translate(queryList,forward,currentOffset);

        return list;
    }

    @Override
    public List<ErrorTypeBo> getErrorList() {
        Map<String,AggsBucketsEntity> resMap = ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_NAME,null,
                Assimble.getLast24HoursUTC(),new String[]{Constant.FIELD_APP_NAME,Constant.FIELD_EX});
        List<ErrorTypeBo> resList = Assimble.errorList(resMap);
        Collections.sort(resList);
        return resList;
    }

    @Override
    public List<ErrorTypeBo> getErrorList(Date startDate ,Date endDate,String errorName) {

        Map<String, String> wildcard = new HashMap<String, String>(1);
        if(errorName != null && !"".equals(errorName.trim()) && !"null".equals(errorName.trim())) {
            wildcard.put(Constant.FIELD_EX, errorName.trim());
        }
        Map<String,AggsBucketsEntity> resMap = ESTemplete.getIns().queryAppReqStatAggsWildcard(DopplerConstants.INDEX_NAME,null,
                Assimble.getMRTByCSTDate(startDate,endDate,"yyyy-MM-dd HH"),new String[]{Constant.FIELD_APP_NAME,Constant.FIELD_EX},wildcard);
        List<ErrorTypeBo> resList = Assimble.errorList(resMap);
        Assimble.blackList(resList);
        Collections.sort(resList);
        return resList;
    }

    @Override
    public List<ErrorHostBo> getErrorHostList(String errorName, String appName) {
        Date startDate = DateUtil.getNewDate(new Date(),-1);
        String dateStr = DateUtil.Date2String(startDate,"yyyy-MM-dd HH");
        Map<String, String> parammap = Assimble.getMap(Constant.FIELD_APP_NAME,appName);
        parammap.put(Constant.FIELD_EX,errorName);

        List<LogRecordEntity> queryList = ESTemplete.getIns().queryLogRecord(DopplerConstants.INDEX_NAME,parammap,Assimble.getMRTByCSTDate(dateStr),null, 1,50);
        List<ErrorHostBo> list = Assimble.logRecordEntityToErrorHostBo(errorName,queryList);
        return list;
    }

    @Override
    public List<ErrorHostBo> getErrorHostList(String errorName, String appName,Date startDate,Date endDate) {
//        Date startDate = DateUtil.getNewDate(new Date(),-1);
//        String dateStr = DateUtil.Date2String(startDate,"yyyy-MM-dd HH");
//        String dateStr = DateUtil.Date2String(startDate,"yyyy-MM-dd HH");

        Map<String, String> parammap = Assimble.getMap(Constant.FIELD_APP_NAME,appName);
        parammap.put(Constant.FIELD_EX,errorName);

        List<LogRecordEntity> queryList = ESTemplete.getIns().queryLogRecord(DopplerConstants.INDEX_NAME,parammap,Assimble.getMRTByCSTDate(startDate,endDate,"yyyy-MM-dd HH"),null, 1,50);
        List<ErrorHostBo> list = Assimble.logRecordEntityToErrorHostBo(errorName,queryList);
        return list;
    }

    @Override
    public List<ProblemBo> getProblemList(Date startDate, Date endDate, String appName, String errorName) {
        Map<String, String> wildcard = new HashMap<String, String>(1);
        if(errorName != null && !"".equals(errorName.trim()) && !"null".equals(errorName.trim())) {
            wildcard.put(Constant.ExStat.FIELD_EX_NAME, errorName.trim());
        }
        Map<String, String> must = null;
        if(appName!=null && !"".equals(appName.trim()) && !"-1".equals(appName.trim())) {
            must = Assimble.getMap(Constant.ExStat.FIELD_APP_NAME, appName);
        }
        Map<String,AggsBucketsEntity> resMap = ESTemplete.getIns().queryAppReqStatAggsWildcard(DopplerConstants.INDEX_EX_NAME,must,
                Assimble.getMRTByCSTDate(startDate,endDate,"yyyy-MM-dd HH"),
                new String[]{Constant.ExStat.FIELD_APP_NAME,Constant.ExStat.FIELD_EX_NAME,Constant.ExStat.FIELD_CLASS_NAME,Constant.ExStat.FIELD_METHOD_NAME,Constant.ExStat.FIELD_LINE_NUM},
                wildcard);
        List<ProblemBo> resList = Assimble.problemList(resMap);
        Assimble.blackListProblem(resList);
        Collections.sort(resList);
        return resList;
    }

    @Override
    public List<ProblemDetailBo> getProblemDetailList(Date startDate, Date endDate, String appName, String errorName, String className, String methodName, long lineNum) {

        Map<String, String> mustMap = Assimble.getMap(Constant.ExStat.FIELD_APP_NAME,appName);
        mustMap.put(Constant.ExStat.FIELD_EX_NAME,errorName);
        if(className!=null && !"".equals(className.trim())&& !"unknown".equals(className.trim())) {
            mustMap.put(Constant.ExStat.FIELD_CLASS_NAME, className);
        }
        if(className!=null && !"".equals(className.trim())&& !"unknown".equals(className.trim())) {
            mustMap.put(Constant.ExStat.FIELD_METHOD_NAME, methodName);
        }
        if(lineNum != -1) {
            mustMap.put(Constant.ExStat.FIELD_LINE_NUM, lineNum + "");
        }
        List<ExRecordEntity> resList = ESTemplete.getIns().queryExStatRecordSort(DopplerConstants.INDEX_EX_NAME,mustMap,
                Assimble.getMRTByCSTDate(startDate,endDate,"yyyy-MM-dd HH"),null,null,null,1,30);
        List<ProblemDetailBo> resBoList = Assimble.problemDetailList(resList);
        return resBoList;
    }

    public static void main(String[] a){
        ErrorServiceImpl errorServiceImpl = new ErrorServiceImpl();
        UpMessageConfig.es_addr = "192.168.0.89:9300";

        List<ProblemDetailBo> resList = errorServiceImpl.getProblemDetailList(DateUtil.stirng2Date("2017-7-1","yyyy-MM-dd"),DateUtil.stirng2Date("2017-8-28","yyyy-MM-dd"),"ruite-mail",
                "AddressException","com.sinoservices.falcon.email.email.SendEmail","createMimeMessage",60);

        for (ProblemDetailBo queryBo : resList) {
            System.out.println(JsonUtils.object2Json(queryBo));
        }
//        System.out.println(resList.size());
//        List<ErrorHostBo> resList = errorServiceImpl.getErrorHostList("NullPointerException","ruite-web");
//        System.out.println(resList.size()+"_____________________________");
//        for(ErrorTypeBo ss : resList){
//            System.out.println(ss.getId()+"___"+ss.getLogInfo());
//        }



    }
}
