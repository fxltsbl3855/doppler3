package com.sinoservices.doppler2.falcon;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.es.ESTemplete;
import com.sinoservices.doppler2.es.entity.AggsBucketsEntity;
import com.sinoservices.doppler2.es.entity.MustRangeTimestamp;
import com.sinoservices.doppler2.service.assimble.Assimble;
import com.sinoservices.doppler2.util.HttpRequest;
import com.sinoservices.doppler2.util.MetricsBo;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class AppMonitorTask extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(AppMonitorTask.class);
    public static boolean running = true;
    public static int stepSecs = 30;

    private List<String> targetList;

    Map<String, List<String>> shouldMap;
    String[] groupByFields;
    long valueDataTime = System.currentTimeMillis() - 10*60*1000;
    List<MonitorBo> cacheValueData = null;

    public AppMonitorTask(){
        logger.info("AppMonitorTask cons");
    }

    public void stopThread(){
        running = false;
    }

    @Override
    public void run() {
        targetList = AppMonitorAssimble.getFalconMonAppValue(UpMessageConfig.falcon_mon_app);
        if(targetList ==null || targetList.size() ==0){
            logger.warn("no configured appName for falcon monitor, task stop..");
            return;
        }
        if(UpMessageConfig.falcon_data_url == null || "".equals(UpMessageConfig.falcon_data_url.trim())){
            logger.warn("no configured falcon monitor addr, task stop..");
            return;
        }
        stepSecs = NumberUtil.formatNumber(UpMessageConfig.falcon_mon_step_sec,30);
        logger.info("falcon stepSecs is {},UpMessageConfig.falcon_mon_step_sec={}",stepSecs,UpMessageConfig.falcon_mon_step_sec);

        shouldMap = AppMonitorAssimble.shouldMap(targetList);
        groupByFields = AppMonitorAssimble.groupByFields();
        logger.info("AppMonitorTask ready to run,UpMessageConfig.falcon_mon_app={},targetList={},shouldMap="+
                JsonUtils.object2Json(shouldMap)+",groupByFields="+JsonUtils.object2Json(groupByFields),
                UpMessageConfig.falcon_mon_app,JsonUtils.object2Json(targetList));

        while (running) {
            try {
                Date date = new Date(new Date().getTime() - 10000);
                process(date);
                logger.info("AppMonitorTask is running,date="+ DateUtil.Date2String(date,"yyyy-MM-dd HH:mm:ss"));
            }catch (Exception e){
                logger.error("AppMonitorTask run error,"+e.getMessage(),e);
            }
            try {
                Thread.currentThread().sleep(stepSecs*1000);
            } catch (Exception e) {
                logger.error("AppMonitorTask sleep error,"+e.getMessage(),e);
            }
        }
        logger.info("AppMonitorTask is stopped");
    }



    public void process(Date endDate){
        Date startTime = new Date(endDate.getTime() - stepSecs*1000);
        MustRangeTimestamp mrt = Assimble.getMRTByCSTDate(startTime,endDate);
        //1 查询
        Map<String,AggsBucketsEntity> logData = ESTemplete.getIns().queryAppReqStatAggs2(DopplerConstants.INDEX_STAT_NAME,null,mrt,shouldMap,groupByFields,4,Constant.FIELD_TIME,null);
        List<MonitorBo> list = AppMonitorAssimble.assimbleMonitor(logData);
        List<MonitorBo> resList = AppMonitorAssimble.merge(getValueData(),list);

        //2 转换
        MetricsBo[] mbs = AppMonitorAssimble.transfer(resList,endDate.getTime()/1000,stepSecs);

        //3 发送
        try {
            String res = HttpRequest.restPost(UpMessageConfig.falcon_data_url,mbs);
            if(logger.isDebugEnabled()){
                logger.debug("send to falcon,data={}",JsonUtils.object2Json(mbs));
            }
            logger.info("send to falcon,res={}",res);
        } catch (Exception e) {
            logger.error("send to falcon error , e="+e.getMessage(),e);
        }
    }

    public List<MonitorBo> getValueData(){
        if(cacheValueData != null && System.currentTimeMillis()-valueDataTime <= 5*60*1000){
            logger.info("getValueData use cache");
            return cacheValueData;
        }
        Map<String,AggsBucketsEntity> valueData = ESTemplete.getIns().queryAppReqStatAggs2(DopplerConstants.INDEX_STAT_NAME,null,null,shouldMap,groupByFields,4,Constant.FIELD_TIME,null);
        List<MonitorBo> valuelist = AppMonitorAssimble.assimbleMonitor(valueData);

        cacheValueData = valuelist;
        valueDataTime = System.currentTimeMillis();
        logger.info("getValueData not use cache, query es");
        return valuelist;
    }
}
