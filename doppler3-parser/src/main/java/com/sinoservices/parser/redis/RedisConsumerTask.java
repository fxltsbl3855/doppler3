package com.sinoservices.parser.redis;

import com.sinoservices.parser.Constant;
import com.sinoservices.parser.es.ESQueue;
import com.sinoservices.parser.kafka.MsgProcess;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class RedisConsumerTask extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(RedisConsumerTask.class);

    public static boolean running = true;
    private volatile int MIN_SAVEDB_INTERVAL_SECS = 1; //至少多长时间存一次db，单位是秒；影响到系统的实时性
    MsgProcess msgProcess = new MsgProcess();
    public RedisConsumerTask(){ }

    public void stopThread(){
        running = false;
    }

    @Override
    public void run() {
        long times =  0;
        long currentTime = System.currentTimeMillis();
        long currentTimeForLog = System.currentTimeMillis();

        while (running) {
            try {
                times++;
                if(times == Long.MAX_VALUE) times = 0;
                if (System.currentTimeMillis() - currentTimeForLog >= 20000) {
                    logger.info("redis consumer is working.....,current get data times=" + times);
                    currentTimeForLog = System.currentTimeMillis();
                }
                String msg = RedisTemplete.getIns().lpop(Constant.REDIS_QUEUE_KEY);
                if (msg != null) {
                    logger.debug("get data from redis,msg : {}" , msg);
                    msgProcess.process(msg);
                } else {
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error("sleep error,msg=" + e.getMessage(), e);
                    }
                }

                reValueInterval();

                //存储db策略：满足任一条件即可    1： MIN_SAVEDB_INTERVAL_SECS秒至少存一次db； 2：批量的话，够条数就保存
                if (System.currentTimeMillis() - currentTime > MIN_SAVEDB_INTERVAL_SECS * 1000) {
                    msgProcess.checkAndSaveDB(true); //条件1
                    currentTime = System.currentTimeMillis();
                } else {
                    boolean res = msgProcess.checkAndSaveDB(false);//条件2
                    if (res) currentTime = System.currentTimeMillis();
                }
            }catch (Exception e){
                logger.error("RedisConsumerTask run error,"+e.getMessage(),e);
            }
        }
        msgProcess.checkAndSaveDB(true);
        logger.info("redis consumer is stopped");
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
        } else{
            if(MIN_SAVEDB_INTERVAL_SECS != 10){
                logger.info("interval changed from {} to {}",MIN_SAVEDB_INTERVAL_SECS,10);
            }
            MIN_SAVEDB_INTERVAL_SECS  = 10;
        }
        //消费太慢，减小获取速度,
        if(queueSize > 500){
            try {
                logger.warn("queue size is more than 500,stop comsuming data for 3 seconds");
                Thread.currentThread().sleep(3000);
            } catch (Exception e) {
                logger.error("stop comsume sleep error",e);
            }
        }
    }

}
