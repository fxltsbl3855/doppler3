package com.sinoservices.parser.kafka;

import com.sinoservices.parser.es.ESQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class KafkaConsumerTask extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerTask.class);

    private boolean running = true;
    private int MIN_SAVEDB_INTERVAL_SECS = 1; //至少多长时间存一次db，单位是秒；影响到系统的实时性

    MsgProcess kafkaMsgProcess = new MsgProcess();
    long currentTime = System.currentTimeMillis();

    private KafkaConsumer<String, String> consumer;
    public KafkaConsumerTask(String host,String port,String topic){
        logger.info("Kafka worker "+Thread.currentThread().getName()+" cons start....");
        Properties props = new Properties();
        props.put("bootstrap.servers", host+":"+port);
        props.put("group.id", "parsegroup");
        props.put("auto.offset.reset", "earliest");
        props.put("enable.auto.commit", "true");
        props.put("fetch.max.bytes", "5008000");
        props.put("max.poll.records", "5000");
        props.put("auto.commit.interval.ms","1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topic));
        logger.info("Kafka worker "+Thread.currentThread().getName()+" cons end...........");
    }

    public void stopThread(){
        running = false;
    }

    @Override
    public void run() {
        long times =  0;
        while (running) {
            times++;
            if(times % 60 ==0) {
                logger.info("kafka consumer is working.....,current get data times={}",times);
            }
            try {
                ConsumerRecords<String, String> records = consumer.poll(500);
                for (ConsumerRecord<String, String> record : records) {
                    kafkaMsgProcess.process(record.value());
                    logger.debug("get kafka msg, offset = {}, v = {} ", record.offset(), record.value());
                    checkCache();
                }
                logger.debug("batch size = {}", records.count());
                checkCache();
                if (records.isEmpty()) {
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error("sleep error,msg=" + e.getMessage(), e);
                    }
                }
            }catch (Exception e){
                logger.error("KafkaConsumerTask run error,"+e.getMessage(),e);
            }
        }
        if (consumer != null) consumer.close();
        kafkaMsgProcess.checkAndSaveDB(true);
        logger.info("kafka consumer is stopped");
    }

    public void checkCache(){
        reValueInterval();
        //存储db策略：满足任一条件即可    1： MIN_SAVEDB_INTERVAL_SECS秒至少存一次db； 2：批量的话，够条数就保存
        if (System.currentTimeMillis() - currentTime > MIN_SAVEDB_INTERVAL_SECS * 1000) {
            kafkaMsgProcess.checkAndSaveDB(true); //条件1
            currentTime = System.currentTimeMillis();
        } else {
            boolean res = kafkaMsgProcess.checkAndSaveDB(false);//条件2
            if (res) currentTime = System.currentTimeMillis();
        }
    }

    public void reValueInterval(){
        int queueSize = ESQueue.getIns().size();
        if(queueSize < 10){
            if(MIN_SAVEDB_INTERVAL_SECS != 1){
                logger.info("interval changed from {} to {}",MIN_SAVEDB_INTERVAL_SECS,1);
            }
            MIN_SAVEDB_INTERVAL_SECS = 1;
        }else if(queueSize < 50){
            if(MIN_SAVEDB_INTERVAL_SECS != 2){
                logger.info("interval changed from {} to {}",MIN_SAVEDB_INTERVAL_SECS,2);
            }
            MIN_SAVEDB_INTERVAL_SECS = 2;
        } else if(queueSize < 65){
            if(MIN_SAVEDB_INTERVAL_SECS != 3){
                logger.info("interval changed from {} to {}",MIN_SAVEDB_INTERVAL_SECS,3);
            }
            MIN_SAVEDB_INTERVAL_SECS = 3;
        } else if(queueSize < 80){
            if(MIN_SAVEDB_INTERVAL_SECS != 4){
                logger.info("interval changed from {} to {}",MIN_SAVEDB_INTERVAL_SECS,4);
            }
            MIN_SAVEDB_INTERVAL_SECS = 4;
        } else if(queueSize < 120){
            if(MIN_SAVEDB_INTERVAL_SECS != 6){
                logger.info("interval changed from {} to {}",MIN_SAVEDB_INTERVAL_SECS,6);
            }
            MIN_SAVEDB_INTERVAL_SECS = 6;
        }else{
            if(MIN_SAVEDB_INTERVAL_SECS != 10){
                logger.info("interval changed from {} to {}",MIN_SAVEDB_INTERVAL_SECS,10);
            }
            MIN_SAVEDB_INTERVAL_SECS  = 10;
        }
        //消费太慢，减小获取速度,
        int sleepTime = 0;
        if(queueSize > 200){
            sleepTime = 3000;
        }else if(queueSize > 120){
            sleepTime = 1000;
        }else if(queueSize > 80){
            sleepTime = 800;
        }else if(queueSize > 65){
            sleepTime = 500;
        }else if(queueSize > 50){
            sleepTime = 200;
        }
        if(sleepTime > 0){
            try {
                logger.warn("queue size is {},stop comsume data for {} milliseconds",queueSize,sleepTime);
                Thread.currentThread().sleep(sleepTime);
            } catch (Exception e) {
                logger.error("stop comsume sleep error",e);
            }
        }
    }
}
