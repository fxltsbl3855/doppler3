package com.sinoservices.parser.kafka;

import com.sinoservices.parser.Constant;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.es.ESWorker;
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
public class KafkaMonitor {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMonitor.class);


    private final int WORKER_NUM = 6;
    private List<KafkaConsumerTask> workerList = new ArrayList<KafkaConsumerTask>(WORKER_NUM);

    public KafkaMonitor(){
        logger.info("KafkaWorkers cons,work num="+WORKER_NUM+",host="+UpMessageConfig.kafka_ip+",port="+UpMessageConfig.kafka_port+",topic="+UpMessageConfig.kafka_topic+",log_collector="+UpMessageConfig.log_collector);
        if(!Constant.LOG_COLLECTOR_KAFKA.equals(UpMessageConfig.log_collector)) {
            logger.error("log collector is configured :"+UpMessageConfig.log_collector +",KafkaConsumerTask is not init");
            return;
        }

        for (int i = 0; i < WORKER_NUM; i++) {
            KafkaConsumerTask temp = new KafkaConsumerTask(UpMessageConfig.kafka_ip, UpMessageConfig.kafka_port, UpMessageConfig.kafka_topic);
            workerList.add(temp);
        }
    }

    public void start(){
        for(KafkaConsumerTask work : workerList){
            work.start();
        }
    }

    public void stop(){
        for(KafkaConsumerTask work : workerList){
            work.stopThread();
        }
        logger.info("KafkaMonitor stop...");
    }

    public static void main(String[] a){
        UpMessageConfig.log_collector = "kafka";
        UpMessageConfig.kafka_ip = "192.168.0.88";
        UpMessageConfig.kafka_port = "9092";
        UpMessageConfig.kafka_topic = "beats";
        KafkaMonitor kafkaWorker = new KafkaMonitor();
        kafkaWorker.start();
        logger.info("KafkaConsumerTask started...");
        System.out.println("KafkaConsumerTask started...");
    }
}
