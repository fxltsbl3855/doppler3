package com.sinoservices.parser.kafka;

import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.parser.Constant;
import com.sinoservices.parser.es.ClientHelper;
import com.sinoservices.parser.es.ESQueue;
import com.sinoservices.parser.es.ESTemplete;
import com.sinoservices.parser.es.ESWorker;
import com.sinoservices.parser.es.entity.*;
import com.sinoservices.parser.file.ExProcess;
import com.sinoservices.parser.file.RegexMatches;
import com.sinoservices.parser.util.FastJsonUtil;
import com.sinoservices.parser.util.HttpRequest;
import com.sinoservices.parser.util.StringProcess;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class MsgProcess {
    private static final Logger logger = LoggerFactory.getLogger(MsgProcess.class);

    Date proviousDate = null;
    private final int batchSize = 8000;
    private List<LogEntity> logEntityList = new ArrayList<LogEntity>(batchSize);
    private List<StatEntity> statEntityList = new ArrayList<StatEntity>(batchSize);
    private List<OpEntity> opStatEntityList = new ArrayList<OpEntity>(batchSize);
    private List<StatJobEntity> statJobEntityList = new ArrayList<StatJobEntity>(batchSize);
    private List<ExEntity> exEntityList = new ArrayList<ExEntity>(batchSize);
    private List<StatJobEntity> statJobOutUpdateList = new ArrayList<StatJobEntity>(100);

    public MsgProcess(){
        ESWorker.getIns();
    }

    public boolean checkAndSaveDB(boolean enforce){
        boolean canSaveLog = logEntityList.size() > 0 && (logEntityList.size() >= batchSize || enforce);
        boolean canSaveStatLog = statEntityList.size() > 0 && (statEntityList.size() >= batchSize || enforce);
        boolean canSaveOpStatLog = opStatEntityList.size() > 0 && (opStatEntityList.size() >= batchSize || enforce);
        boolean canSaveStatJobLog = statJobEntityList.size() > 0 && (statJobEntityList.size() >= batchSize || enforce);
        boolean canSaveExLog = exEntityList.size() > 0 && (exEntityList.size() >= batchSize || enforce);

        boolean saveRes = false;
        if(canSaveLog){
            boolean offerRes = ESQueue.getIns().offer(logEntityList);
            logger.info("batch save es log, offer res is {} , list size is {}",offerRes,logEntityList.size());
            if(offerRes) {
                logEntityList.clear();
                saveRes = true;
            }
        }

        if(canSaveStatLog){
            Client client = null;
            try {
                client = ClientHelper.getInstance().getClient();
                ESTemplete.createStatBatch(client, DopplerConstants.INDEX_STAT_NAME, DopplerConstants.INDEX_TYPE, statEntityList);
                logger.info("batch save es log stat, list size is "+statEntityList.size());
            }catch (Exception e){
                logger.error("batch insert into stat es exception",e);
            }finally {
                if(client != null)
                    ClientHelper.getInstance().returnPool(client);
                statEntityList.clear();
            }
            saveRes = true;
        }

        if(canSaveOpStatLog){
            Client client = null;
            try {
                client = ClientHelper.getInstance().getClient();
                ESTemplete.createOpStatBatch(client, DopplerConstants.INDEX_OP_NAME, DopplerConstants.INDEX_TYPE, opStatEntityList);
                logger.info("batch save es log op stat, list size is "+opStatEntityList.size());
            }catch (Exception e){
                logger.error("batch insert into op stat es exception",e);
            }finally {
                if(client != null)
                    ClientHelper.getInstance().returnPool(client);
                opStatEntityList.clear();
            }
            saveRes = true;
        }

        if(canSaveStatJobLog){
            Client client = null;
            try {
                client = ClientHelper.getInstance().getClient();
                ESTemplete.createStatJobBatch(client, DopplerConstants.INDEX_STAT_JOB_NAME, DopplerConstants.INDEX_TYPE, statJobEntityList);
                logger.info("batch save es log stat job, list size is "+statJobEntityList.size());
            }catch (Exception e){
                logger.error("batch insert into stat job es exception",e);
            }finally {
                if(client != null)
                    ClientHelper.getInstance().returnPool(client);
                statJobEntityList.clear();
            }
            saveRes = true;
            //job日志存储es后，就执行更新
            if(statJobOutUpdateList.size() > 0){
                new JobUpdateThread(statJobOutUpdateList,1).start();
                statJobOutUpdateList.clear();
            }
        }

        if(canSaveExLog){
            Client client = null;
            try {
                client = ClientHelper.getInstance().getClient();
                ESTemplete.createExBatch(client, DopplerConstants.INDEX_EX_NAME, DopplerConstants.INDEX_TYPE, exEntityList);
                logger.info("batch save es log exstat, list size is "+exEntityList.size());
            }catch (Exception e){
                logger.error("batch insert into exstat es exception",e);
            }finally {
                if(client != null)
                    ClientHelper.getInstance().returnPool(client);
                exEntityList.clear();
            }
            saveRes = true;
        }
        return saveRes;
    }

    public String process(String msg){
        MsgEntity msgEntity = FastJsonUtil.json2Object(msg,MsgEntity.class);
        String host = msgEntity.getFields().get(Constant.FILEBEAT_FILEDS_HOST);
        String message = msgEntity.getMessage();
        long offset = msgEntity.getOffset();
        String fileName = StringProcess.getFileName(msgEntity.getSource());
        return process(host,message,offset,fileName);
    }

    /**
     * 处理queue中获取到的消息,解析，插入es，需要批量插入，不能逐条插入
     */
    public String process(String host,String message,long offset,String fileName){
        String appName = StringProcess.getAppName(fileName);
        String day = StringProcess.getDay(fileName);

        if(proviousDate == null) proviousDate = DateUtil.stirng2Date(day,"yyyy-MM-dd");
        //解析日志记录为日志对象，需要传入文件名相关的参数以及日志记录
        LogEntity logEntity = RegexMatches.parseLineMsg(fileName,offset,appName,host,message,day,proviousDate);
        logEntityList.add(logEntity);

        //解析异常
        ExEntity exEntity = ExProcess.parserEx(logEntity);
        if(exEntity != null){
            exEntityList.add(exEntity);
            return logEntity.getId();
        }


        boolean isJobOut = logEntity.getOriginal().indexOf(Constant.ACTION_JOB_OUT_MATCH) > 0;
        boolean isJobIn = logEntity.getOriginal().indexOf(Constant.ACTION_JOB_IN_MATCH) > 0;
        boolean isJob = isJobOut || isJobIn;
        boolean isStat = logEntity.getOriginal().indexOf(Constant.ACTION_REQ_OUT_MATCH) > 0;
        boolean isOpStat = logEntity.getOriginal().indexOf(Constant.ACTION_OPER_OUT_MATCH) > 0;

        if(isJob) {
            matchJob(fileName,offset,logEntity);
        }

        //解析统计日志记录,依赖于解析后的LogEntity对象
        if(isStat) {
            matchStat(fileName,offset,logEntity);
        }

        if(isOpStat){
            matchOpStat(fileName,offset,logEntity);
        }
        proviousDate = logEntity.getTimestamp();
        return logEntity.getId();
    }

    public void matchOpStat(String fileName,long offset,LogEntity logEntity){
        OpEntity opEntity = RegexMatches.parseOpStatLineMsg(fileName, offset, logEntity);
        if (opEntity == null) {
            logger.warn("matchOpStat error,please check the log,fileName={},offset={},log="+opEntity.getOriginal(),fileName,offset);
            return;
        }
        opStatEntityList.add(opEntity);
    }

    public void matchStat(String fileName,long offset,LogEntity logEntity){
        StatEntity statEntity = RegexMatches.parseStatLineMsg(fileName, offset, logEntity);
        if (statEntity == null) {
            logger.warn("matchStat error,please check the log,fileName={},offset={},log="+logEntity.getOriginal(),fileName,offset);
            return;
        }
        statEntityList.add(statEntity);
    }

    public void matchJob(String fileName,long offset,LogEntity logEntity){
        StatJobEntity statJobEntity = RegexMatches.parseJobLineMsg(fileName, offset, logEntity);
        if (statJobEntity == null) {
            logger.warn("matchJob error,please check the log,fileName={},offset={},log="+logEntity.getOriginal(),fileName,offset);
            return;
        }
        statJobEntityList.add(statJobEntity);
        if (Constant.ACTION_JOB_OUT.equals(statJobEntity.getAction())) {
            statJobOutUpdateList.add(statJobEntity);
        }
    }

    public static void main(String[] a){
//        String str = " {\"@timestamp\":\"2017-01-04T07:15:54.187Z\",\"beat\":{\"hostname\":\"TRD-Charlie-MP06JNBT\",\"name\":\"TRD-Charlie-MP06JNBT\",\"version\":\"5.1.1\"},\"fields\":{\"ip\":\"192.168.0.86\"},\"input_type\":\"log\",\"message\":\"17:06:17.477 [org.springframework.scheduling.quartz.SchedulerFactoryBean#0_Worker-1] INFO  com.sinoservices.stat.aop.MonitorAop - [stat] @action=ACTION_JOB_OUT @className=com.ruite.demo.task.Task1 @method=doJob2 @result=SUCCESS @param= @jobId=148732237544666136 @time=2030ms\",\"offset\":542,\"source\":\"D:\\\\mnt\\\\logs\\\\ruite-web.2017-01-04.log\",\"type\":\"log\"}";
        String str = " {\"@timestamp\":\"2017-01-04T07:15:54.187Z\",\"beat\":{\"hostname\":\"TRD-Charlie-MP06JNBT\",\"name\":\"TRD-Charlie-MP06JNBT\",\"version\":\"5.1.1\"},\"fields\":{\"ip\":\"192.168.0.86\"},\"input_type\":\"log\",\"message\":\"09:34:05.002 [main] INFO  com.sinoservices.stat.aop.OpAop[invoke] - [stat] @action=ACTION_OPER_OUT @className=com.sinoservices.stat.aop.HelloService @method=addOrderById @operType=add @operObj=Order @result=SUCCESS @param=aa#Tue Aug 15 09:34:04 CST 2017#3#3.45# @busParam=test1=value1,test2=value2 @time=98ms\",\"offset\":542,\"source\":\"D:\\\\mnt\\\\logs\\\\ruite-web.2017-01-04.log\",\"type\":\"log\"}";
//        String str2 = "09:34:05.002 [main] INFO  com.sinoservices.stat.aop.OpAop[invoke] - [stat] @action=ACTION_OPER_OUT @className=com.sinoservices.stat.aop.HelloService @method=addOrderById @operType=add @operObj=Order @result=SUCCESS @param=aa#Tue Aug 15 09:34:04 CST 2017#3#3.45# @busParam=test1=value1,test2=value2 @time=98ms";

//        String fileName = "";
//        long offset = 2132324;
//        String appName = "";
//        String host = "";
//        String day = "";
//        Date proviousDate = new Date();
//
//
//        LogEntity logEntity = RegexMatches.parseLineMsg(fileName,offset,appName,host,message,day,proviousDate);


        MsgProcess msgProcess = new MsgProcess();
        msgProcess.process(str);
        System.out.println(JsonUtils.object2Json(msgProcess.opStatEntityList));
//        msgProcess.process(str2);
//        long start = System.currentTimeMillis();
//        for (int i = 0; i <200000 ; i++) {
//            msgProcess.process(str);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("log:"+time1+",json:"+time2+",getday:"+time3+",other="+time4+",time5="+time5+ ",totle:"+(end-start));

    }



}
