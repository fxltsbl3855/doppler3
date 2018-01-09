package com.sinoservices.doppler2.falcon;

import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.util.JsonUtils;
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
public class AppMonitor {
    private static final Logger logger = LoggerFactory.getLogger(AppMonitor.class);
    AppMonitorTask appMonitorTask;

    public AppMonitor(){
        logger.info("AppMonitor cons");
    }

    public void start(){
        appMonitorTask = new AppMonitorTask();
        appMonitorTask.start();
        logger.info("AppMonitor started");
    }

    public void stop(){
        appMonitorTask.stopThread();
        logger.info("AppMonitor stopped");
    }

    public static void main(String[] a){
//        UpMessageConfig.redis_ip = "192.168.0.88";
//        UpMessageConfig.redis_port = "6379";
//        UpMessageConfig.falcon_data_url = "http://192.168.0.90:6060/api/push";
//
//        UpMessageConfig.es_addr = "192.168.0.89:9300";
//
////        RedisMonitor kafkaWorker = new RedisMonitor();
////        kafkaWorker.start();
//
//        AppMonitor appMonitor = new AppMonitor();
//        List<String> list = new ArrayList<String>();
//        list.add("doppler3-server");
//        appMonitor.setTargetList(list);
//        appMonitor.start();

        System.out.println(new Date(1490667334000L));

    }
}
