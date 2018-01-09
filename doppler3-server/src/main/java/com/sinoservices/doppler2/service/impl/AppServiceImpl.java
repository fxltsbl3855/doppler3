package com.sinoservices.doppler2.service.impl;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.doppler2.bo.*;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.es.DSLAssemble;
import com.sinoservices.doppler2.es.ESTemplete;
import com.sinoservices.doppler2.es.entity.*;
import com.sinoservices.doppler2.service.AppService;
import com.sinoservices.doppler2.service.assimble.Assimble;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import org.apache.zookeeper.server.LogFormatter;
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
@Service("appService")
public class AppServiceImpl implements AppService {
    private static final Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);

    @Override
    public DashboardBo getDashboard() {

        Map<String,AggsBucketsEntity> groupByAppNameMap = ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_STAT_NAME,null,null,new String[]{Constant.FIELD_APP_NAME,Constant.FIELD_CLASS_NAME});
        DashboardBo dashboardBo = Assimble.appModuleStat(groupByAppNameMap);

        List<ErrorGroupByEntity> errorList = ESTemplete.getIns().queryByDateAggs(DopplerConstants.INDEX_NAME,null, Assimble.getMap(Constant.FIELD_EX, ""),Assimble.getLast7DayMRT(),null);
        Assimble.utctimeToBjtime(errorList);
        Assimble.fillError(dashboardBo,errorList);

        Map<String,AggsBucketsEntity> resMap = ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_NAME,null,
                Assimble.getLast24HoursUTC(),new String[]{Constant.FIELD_EX,});
        Assimble.fillErrorList(dashboardBo,resMap);
        return dashboardBo;
    }

    @Override
    public List<AppStatBo> getAppDetailList(Date startDate,Date endDate) {
        Map<String,AggsBucketsEntity> resMap = ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_STAT_NAME,null,
                Assimble.getMRTByCSTDate(startDate,endDate),null,new String[]{Constant.FIELD_APP_NAME,Constant.FIELD_RESULT},1,Constant.FIELD_TIME);
        List<AppStatBo> resList = Assimble.appStat(resMap);

        return resList;
    }

    @Override
    public List<HostStatBo> getHostDetailList(Date startDate,Date endDate) {
        Map<String,AggsBucketsEntity> resMap = ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_STAT_NAME,null,
                Assimble.getMRTByCSTDate(startDate,endDate),null,new String[]{Constant.FIELD_HOST,Constant.FIELD_RESULT},1,Constant.FIELD_TIME);
        List<HostStatBo> resList = Assimble.hostStat(resMap);
        return resList;
    }

    @Override
    public List<AppInfoBo> getAppList() {
        Map<String,AggsBucketsEntity>  resMap = ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_NAME,null,null,new String[]{Constant.FIELD_APP_NAME});
        List<AppInfoBo> resList = Assimble.toAppInfoBo(resMap);
        return resList;
    }

    @Override
    public List<AppInfoBo> getWebAppList() {
        Map<String, String> mustparam = Assimble.getMapValueNotNull(Constant.FIELD_APP_TYPE, String.valueOf(1));
        Map<String,AggsBucketsEntity>  resMap = ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_STAT_NAME,mustparam,null,new String[]{Constant.FIELD_APP_NAME});
        List<AppInfoBo> resList = Assimble.toAppInfoBo(resMap);
        return resList;
    }

    @Override
    public List<TopTimeBo> topReq(Date startDate,Date endDate, String host,String appName, String moduleName, String method, int size) {
        if(size > 50) size = 50;
        Map<String, String> mustparam = Assimble.getMapValueNotNull(Constant.FIELD_APP_NAME,appName,Constant.FIELD_CLASS_NAME,moduleName);
        Assimble.putMapValueNotNull(mustparam,Constant.FIELD_METHOD,method);
        Assimble.putMapValueNotNull(mustparam,Constant.FIELD_HOST,host);
        String utcFrom = DateUtil.localDateToUTCStr(startDate,"yyyy-MM-dd HH:mm:ss");
        String toFrom = DateUtil.localDateToUTCStr(DateUtil.getNewDate(endDate,1),"yyyy-MM-dd HH:mm:ss");
        MustRangeTimestamp mustRange = new MustRangeTimestamp(Constant.FIELD_TIMESTAMP,utcFrom,toFrom,"yyyy-MM-dd HH:mm:ss");

        List<LogRecordStatEntity> queryList = ESTemplete.getIns().queryStatLogRecordSort(DopplerConstants.INDEX_STAT_NAME,mustparam,mustRange,null,Constant.FIELD_TIME,false,LogRecordStatEntity.getFields(),1,size);
        List<TopTimeBo> list = Assimble.toTopTimeBo(queryList);
        return list;
    }


    public static void main(String[] a){
        UpMessageConfig.es_addr = "192.168.0.89:9300";
        AppServiceImpl appServiceImpl = new AppServiceImpl();
//        List<TopTimeBo> resList = appServiceImpl.topReq("2017-1-22",null,"ruite-web2","com.class1","method1",5);
//        logger.info(resList.size()+"===================");
//        for(TopTimeBo rr : resList){
//            logger.info(rr.getTime()+"===================");
//        }

        System.out.println(JsonUtils.object2Json(appServiceImpl.getWebAppList()));

    }

}
