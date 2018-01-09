package com.sinoservices.doppler2.service.assimble;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.bo.JobBo;
import com.sinoservices.doppler2.bo.JobDetailBo;
import com.sinoservices.doppler2.bo.QueryBo;
import com.sinoservices.doppler2.es.entity.AggsBucketsEntity;
import com.sinoservices.doppler2.es.entity.LogRecordEntity;
import com.sinoservices.doppler2.es.entity.LogRecordJobEntity;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.NumberUtil;
import com.sinoservices.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class JobAssimble {
    private static final Logger logger = LoggerFactory.getLogger(JobAssimble.class);

    public static void main(String[] a){
//        List<JobBo> resInList = new ArrayList<JobBo>();
//        resInList.add(getJobBo("appName","className","host","",2,100,10,20));
//
//        List<JobBo> resOutList = new ArrayList<JobBo>();
//        resOutList.add(getJobBo("appName","className","host","SUCCESS",3,300,100,200));
//        resOutList.add(getJobBo("appName","className","host","EXCEPTION",1,100,0,50));
//
//
//        List<JobBo> unknownList = JobAssimble.getUnknownList(resInList,resOutList);
//        resOutList.addAll(unknownList);
//        for(JobBo ss : resOutList) {
//            System.out.println(ss.toString());
//        }
    }

    public static List<JobBo> assimbleOut(Map<String, AggsBucketsEntity> resMap) {
        if(Assimble.isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<JobBo> resList = new ArrayList<JobBo>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            AggsBucketsEntity aggsBucketsEntity = entry.getValue();
            String appName = aggsBucketsEntity.getKey();
            Map<String, AggsBucketsEntity> map2 = aggsBucketsEntity.getAggsBucketsEntityMap();
            for(Map.Entry<String, AggsBucketsEntity> entry2 : map2.entrySet()){
                String host = entry2.getKey();
                Map<String, AggsBucketsEntity> map3 = entry2.getValue().getAggsBucketsEntityMap();
                for(Map.Entry<String, AggsBucketsEntity> entry3 : map3.entrySet()){
                    String moduleName = entry3.getKey();
                    Map<String, AggsBucketsEntity> map4 = entry3.getValue().getAggsBucketsEntityMap();
                    if(map4 == null || map4.size() ==0) continue;
                    for (Map.Entry<String, AggsBucketsEntity> entry4 : map4.entrySet()) {
                        String result = entry4.getKey();
                        long count = entry4.getValue().getValue();
                        double max = entry4.getValue().getMax();
                        double min = entry4.getValue().getMin();
                        double avg = entry4.getValue().getAvg();
                        resList.add(getJobBo(appName, moduleName, strToIp(host), (result==null||"".equals(result.trim()))?"UNKNOWN":result, count, max, min, avg));
                    }
                }
            }
        }
        return resList;
    }

//    public static List<JobBo> assimbleIn(Map<String, AggsBucketsEntity> resMap) {
//        if(Assimble.isEmpty(resMap)){
//            return Collections.EMPTY_LIST;
//        }
//        List<JobBo> resList = new ArrayList<JobBo>(resMap.size());
//        int i=0;
//        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
//            AggsBucketsEntity aggsBucketsEntity = entry.getValue();
//            String appName=aggsBucketsEntity.getKey();
//            Map<String, AggsBucketsEntity> map2 = aggsBucketsEntity.getAggsBucketsEntityMap();
//            for(Map.Entry<String, AggsBucketsEntity> entry2 : map2.entrySet()){
//                String host = entry2.getKey();
//                Map<String, AggsBucketsEntity> map3 = entry2.getValue().getAggsBucketsEntityMap();
//                for(Map.Entry<String, AggsBucketsEntity> entry3 : map3.entrySet()){
//                    String moduleName = entry3.getKey();
//                    long count = entry3.getValue().getValue();
//                    double max = entry3.getValue().getMax();
//                    double min = entry3.getValue().getMin();
//                    double avg = entry3.getValue().getAvg();
//                    resList.add(getJobBo(appName, moduleName, strToIp(host), "", count, max, min, avg));
//                }
//            }
//        }
//        return resList;
//    }

    private static JobBo getJobBo(String appName, String moduleName, String host, String result, long count,double max, double min, double avg) {
        JobBo jobBo = new JobBo();
        jobBo.setAppName(appName);
        jobBo.setClassName(moduleName);
        jobBo.setHost(host);
        jobBo.setResult(result);
        jobBo.setExecuteNum(NumberUtil.doubleToInt(count));
        jobBo.setMaxTime(NumberUtil.doubleToInt(max));
        jobBo.setMinTime(NumberUtil.doubleToInt(min));
        jobBo.setAvgTime(NumberUtil.doubleToInt(avg));
        return jobBo;
    }

    public static String strToIp(String ipStr) {
        long ip = NumberUtil.formatLong(ipStr,-1);
        StringBuilder result = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            result.insert(0,Long.toString(ip & 0xff));
            if (i < 3) {
                result.insert(0,'.');
            }
            ip = ip >> 8;
        }
        return result.toString();
    }

//    public static List<JobBo> getUnknownList(List<JobBo> resInList, List<JobBo> resOutList) {
//        if(resInList == null || resInList.size() ==0){
//            return Collections.emptyList();
//        }
//        Map<String,Integer> outCountMap  = new HashMap<String, Integer>();
//        if(resOutList != null && resOutList.size() > 0){
//            for(JobBo jobBo : resOutList){
//                String key = jobBo.getAppName()+"_"+jobBo.getClassName()+"_"+jobBo.getHost();
//                Integer count = outCountMap.get(key);
//                if(count == null){
//                    outCountMap.put(key,jobBo.getExecuteNum());
//                }else {
//                    outCountMap.put(key, count + jobBo.getExecuteNum());
//                }
//            }
//        }
//
//        List<JobBo> unknowList = new ArrayList<JobBo>();
//        for(JobBo jobBo : resInList){
//            String key = jobBo.getAppName()+"_"+jobBo.getClassName()+"_"+jobBo.getHost();
//            Integer outCount = outCountMap.get(key);
//            int unknowCount = jobBo.getExecuteNum() - (outCount==null?0:outCount);
//            if(unknowCount <= 0) continue;
//            unknowList.add(getJobBo(jobBo.getAppName(),jobBo.getClassName(),jobBo.getHost(),"UNKNOWN",unknowCount,-1,-1,-1));
//        }
//        return unknowList;
//    }

    public static List<JobDetailBo> jobEntityToBo(List<LogRecordJobEntity> jobRecordList) {
        if(jobRecordList == null || jobRecordList.size() ==0){
            return Collections.emptyList();
        }
        List<JobDetailBo> resList = new ArrayList<JobDetailBo>(jobRecordList.size());
        for(LogRecordJobEntity entity : jobRecordList){
            String result = entity.getResult();
            resList.add(new JobDetailBo(entity.getId(),DateUtil.utcStrToLocalDate(entity.getTimestamp()),entity.getAction(),entity.getAppName(),entity.getHost(),
                    entity.getClassName(),(result==null||"".equals(result.trim()))?"UNKNOWN":result,entity.getParam(),entity.getJobId(),new Long(entity.getTime()).intValue()));
        }
        return resList;
    }

    public static void addResultToMap(Map<String,String> map, String k, String v){
        if(!StringUtil.isBlank(v)){
            if("UNKNOWN".equals(v)){
                map.put(k,"");
            }else{
                map.put(k,v);
            }

        }
    }


//    public static void fillInJob(List<JobDetailBo> resList, List<LogRecordJobEntity> outJobList) {
//        Map<String,LogRecordJobEntity> outMap = new HashMap<String, LogRecordJobEntity>(outJobList==null?0:outJobList.size());
//        if(outJobList!=null && outJobList.size()>0){
//            for(LogRecordJobEntity entity : outJobList){
//                outMap.put(entity.getJobId(),entity);
//            }
//        }
//
//        for(JobDetailBo tt : resList){
//            LogRecordJobEntity entity =outMap.get(tt.getJobId());
//            int time = -1;
//            String result = "UNKNOWN";
//            if(entity!=null){
//                time = new Long(entity.getTime()).intValue();
//                result = entity.getResult();
//            }
//            tt.setTime(time);
//            tt.setResult(result);
//        }
//    }
}
