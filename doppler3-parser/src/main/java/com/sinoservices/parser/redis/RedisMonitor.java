package com.sinoservices.parser.redis;

import com.sinoservices.parser.Constant;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.es.ClientHelper;
import com.sinoservices.parser.es.ESQueue;
import com.sinoservices.parser.es.ESTemplete;
import com.sinoservices.parser.es.ESWorker;
import com.sinoservices.parser.kafka.KafkaConsumerTask;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class RedisMonitor {
    private static final Logger logger = LoggerFactory.getLogger(RedisMonitor.class);


    private final int WORKER_NUM = 8;
    private List<RedisConsumerTask> workerList = new ArrayList<RedisConsumerTask>(WORKER_NUM);

    public RedisMonitor(){
        logger.info("RedisMonitor cons,log_collector="+UpMessageConfig.log_collector);
        if(!Constant.LOG_COLLECTOR_REDIS.equals(UpMessageConfig.log_collector)) {
            logger.info("log collector is configured :"+UpMessageConfig.log_collector +",RedisConsumerTask is not init");
            return;
        }

        for (int i = 0; i < WORKER_NUM; i++) {
            RedisConsumerTask temp = new RedisConsumerTask();
            workerList.add(temp);
        }

    }

    public void start(){
        int ycl = 1;
        if(ycl < 0 ){
            Client client = ClientHelper.getInstance().getClient();
            int num = ESTemplete.getClusterNodeNum(client);
            if(num > 1) {
                logger.error("current es is cluster, this sysytem is not support. please upgrade to Enterprise Edition, otherwise system will be not work");
                return;
            }
            ClientHelper.getInstance().returnPool(client);
        }

        for(RedisConsumerTask work : workerList){
            work.start();
        }
    }

    public void stop(){
        for(RedisConsumerTask work : workerList){
            work.stopThread();
        }
        logger.info("RedisMonitor stopped");
    }

    public static void main(String[] a){
        UpMessageConfig.redis_ip = "192.168.0.89";
        UpMessageConfig.redis_port = "6379";
        UpMessageConfig.redis_timeout = "2000";

        UpMessageConfig.es_addr = "192.168.0.89:9300";
        UpMessageConfig.log_collector = "redis";

        RedisMonitor kafkaWorker = new RedisMonitor();
        kafkaWorker.start();
        logger.info("RedisConsumerTask started...");
        System.out.println("RedisConsumerTask started...");
    }
}
