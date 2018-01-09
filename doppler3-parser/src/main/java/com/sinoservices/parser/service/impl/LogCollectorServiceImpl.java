package com.sinoservices.parser.service.impl;

import com.sinoservices.doppler2.bo.LogHttpResBo;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.facade.assemble.LogHttpAssemble;
import com.sinoservices.parser.kafka.MsgProcess;
import com.sinoservices.parser.service.LogCollectorService;
import com.sinoservices.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
@Service("logCollectorService")
public class LogCollectorServiceImpl implements LogCollectorService {
    private static final Logger logger = LoggerFactory.getLogger(LogCollectorServiceImpl.class);

    MsgProcess msgProcess = new MsgProcess();
    @Override
    public LogHttpResBo log(String host, String message, long offset, String fileName) {
        String opId = NumberUtil.getRandom(8)+"";
        try {
            String id = msgProcess.process(host, message, offset, fileName + "." + NumberUtil.getRandom(4));
            return LogHttpAssemble.getSucc(opId,id);
        }catch (Exception e){
            return LogHttpAssemble.getError(opId,e.getMessage());
        }finally {
            msgProcess.checkAndSaveDB(true);
        }
    }

    @Override
    public LogHttpResBo log(String host, String[] messages, long offset, String fileName) {
        String opId = NumberUtil.getRandom(8)+"";
        fileName = fileName+"."+ NumberUtil.getRandom(4);
        int successNum=0,errorNum=0;
        Map<String,Integer> errorMap = new HashMap<String,Integer>();
        StringBuilder success = new StringBuilder();
        for(String msg : messages){
            try {
                String id = msgProcess.process(host, msg, offset++, fileName);
                success.append(id+",");
                successNum++;
            }catch (Exception e){
                errorNum++;
                LogHttpAssemble.putErrorToMap(errorMap,e.getMessage());
            }
        }
        msgProcess.checkAndSaveDB(true);
        if(errorNum > 0 && successNum > 0){
            return LogHttpAssemble.getPartError(opId,LogHttpAssemble.getDesc(errorMap,errorNum,success.toString(),successNum));
        }else if(errorNum > 0){
            return LogHttpAssemble.getError(opId,LogHttpAssemble.getDesc(errorMap,errorNum,success.toString(),successNum));
        }else{
            return LogHttpAssemble.getSucc(opId,LogHttpAssemble.getDesc(null,0,success.toString(),successNum));
        }
    }

    public static void main(String[] args) {
        UpMessageConfig.es_addr="192.168.0.88:9300";
        UpMessageConfig.es_addr_http="192.168.0.88:9200";

        LogCollectorServiceImpl logCollectorServiceImpl = new LogCollectorServiceImpl();
        String msg = "15:56:38.093 [main] INFO  c.sinoservices.parser.redis.RedisMonitor[<init>] - RedisMonitor cons,log_collector=redis";
        String msg1 = "17:06:17.477 [org.springframework.scheduling.quartz.SchedulerFactoryBean#0_Worker-1] INFO  com.sinoservices.stat.aop.MonitorAop - [stat] @action=ACTION_JOB_OUT @className=com.ruite.demo.task.Task1 @method=doJob2 @result=SUCCESS @param= @jobId=148732237544666136 @time=2030ms";
        String[] msss = new String[2];
        msss[0] = msg;
        msss[1] = msg1;
        logCollectorServiceImpl.log("192.168.0.2",msss,System.currentTimeMillis(),"ruite-web.2017-02-21.192.169.0.2.8888");

    }
}
