package com.sinoservices.doppler2.service.assimble;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.bo.*;
import com.sinoservices.doppler2.es.DSLAssemble;
import com.sinoservices.doppler2.es.entity.*;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
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
public class ErrorAssimble {
    private static final Logger logger = LoggerFactory.getLogger(ErrorAssimble.class);

    public static void main(String[] a){
        List<LogRecordEntity> queryList = new ArrayList<LogRecordEntity>();

        int currentPosition = 0;
        for(int i=0;i < 70;i++){
            LogRecordEntity logRecordEntity = new LogRecordEntity();
            logRecordEntity.setId(""+i);
            currentPosition += (150+50);
            logRecordEntity.setPosition(currentPosition);
            queryList.add(logRecordEntity);
        }
//        for(LogRecordEntity logRecordEntity : queryList){
//            System.out.println(logRecordEntity.getId()+"_"+logRecordEntity.getPosition());
//        }

        List<QueryBo> temp = translate(queryList,0,13600);
        System.out.println();
        for(QueryBo ss : temp) {
            System.out.print(ss.getId() + ",");
        }


//        List<String> sss = new ArrayList<String>();
//        sss.add("q1");
//        sss.add("q2");
//        sss.add("q3");
//        sss.add("q4");
//        for(String ss : sss.subList(0,5))
//            System.out.println(ss);
    }


    public static List<QueryBo> translate(List<LogRecordEntity> recordList) {
        if(Assimble.isEmpty(recordList)){
            return Collections.EMPTY_LIST;
        }
        List<QueryBo> resList = new ArrayList<QueryBo>(recordList.size());
        for(LogRecordEntity logRecordEntity : recordList){
            resList.add(new QueryBo(logRecordEntity.getId(),logRecordEntity.getOriginal(),logRecordEntity.getHost(),
                    ErrorAssimble.getTitleByIdHost(logRecordEntity.getId(),logRecordEntity.getHost())));
        }
        return resList;
    }

    public static List<String> getIds(String id, int forward) {
        List<String> resList = new ArrayList<String>();
        if(StringUtil.isBlank(id)){
            return resList;
        }
        int dIndex = id.lastIndexOf(Constant.ES_ID_DELIMETER);
        if(dIndex == -1){
            logger.error("es docId is not contain #, please check it.");
            return resList;
        }
        String idProfix = id.substring(0,dIndex);
        String idSuffixNumStr = id.substring(dIndex+1);
        int idSuffixNum = NumberUtil.formatNumber(idSuffixNumStr,0) ;
        int startIdSuffixNum=0,endIdSuffixNum=0;
        if(forward == 0){
            startIdSuffixNum = idSuffixNum - Constant.LOG_DETAIL_PAGE_SIZE;
            endIdSuffixNum = idSuffixNum + Constant.LOG_DETAIL_PAGE_SIZE;
        }else if(forward==1){
            startIdSuffixNum = idSuffixNum+1;
            endIdSuffixNum = idSuffixNum + Constant.LOG_DETAIL_PAGE_SIZE;
        }else if(forward == -1){
            startIdSuffixNum = idSuffixNum - Constant.LOG_DETAIL_PAGE_SIZE;
            endIdSuffixNum = idSuffixNum-1;
        }

        if(startIdSuffixNum < 0){
            startIdSuffixNum = 0;
        }

        for(int i=startIdSuffixNum;i<=endIdSuffixNum;i++){
            resList.add(idProfix+Constant.ES_ID_DELIMETER+i);
        }
        return resList;
    }

    public static List<QueryBo> translate(List<LogRecordEntity> queryList, int forward,long currentOffset) {
        if(Assimble.isEmpty(queryList)){
            return Collections.EMPTY_LIST;
        }

        int pointIndex = -1,retrieveIndex=-1;long retrieveInterval=-1;
        List<QueryBo> resList = new ArrayList<QueryBo>(Constant.LOG_DETAIL_PAGE_SIZE);
        for(int i=0;i<queryList.size();i++){
            LogRecordEntity logRecordEntity = queryList.get(i);
            resList.add(new QueryBo(logRecordEntity.getId(),logRecordEntity.getOriginal(),logRecordEntity.getHost(),
                    ErrorAssimble.getTitleByIdHost(logRecordEntity.getId(),logRecordEntity.getHost())));
            if(logRecordEntity.getPosition() == currentOffset){
                pointIndex = i ;
            }
            //记录position值最接近currentOffset的retrieveIndex
            if(retrieveInterval ==-1 || Math.abs(currentOffset-logRecordEntity.getPosition()) < retrieveInterval){
                retrieveIndex = i; retrieveInterval = Math.abs(currentOffset-logRecordEntity.getPosition());
            }
        }
        if(pointIndex == -1) pointIndex = retrieveIndex;

        if(resList.size() <= Constant.LOG_DETAIL_PAGE_SIZE){
            return resList;
        }

        int startIndex=0,endIndex=resList.size();
        if(forward < 0){
            startIndex = resList.size() - Constant.LOG_DETAIL_PAGE_SIZE ;
            endIndex = resList.size();
        }else if(forward > 0){
            startIndex = 0;
            endIndex = Constant.LOG_DETAIL_PAGE_SIZE;
        }else{
            if(pointIndex+1 > Constant.LOG_DETAIL_PAGE_SIZE){
                startIndex = pointIndex - Constant.LOG_DETAIL_PAGE_SIZE;
            }
            if(resList.size() - (pointIndex+1) >  Constant.LOG_DETAIL_PAGE_SIZE) {
                endIndex = pointIndex + Constant.LOG_DETAIL_PAGE_SIZE + 1;
            }
        }
        return resList.subList(startIndex,endIndex);
    }

    public static String getTitleByIdHost(String id,String host) {
        try {
            String[] tmp = id.split("#");
            return "file="+tmp[0]+", host="+host;
        }catch (Exception e){
            logger.error("id is invalid",e);
            return id+","+host;
        }
    }

    public static int getOffsetById(String id) {
        try {
            String[] tmp = id.split("#");
            int offset = NumberUtil.formatNumber(tmp[1], 0);
            return offset;
        }catch (Exception e){
            logger.error("id is invalid",e);
            return 0;
        }
    }
}
