package com.sinoservices.parser.es;

import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.parser.es.entity.LogEntity;
import com.sinoservices.parser.redis.RedisConsumerTask;
import com.sinoservices.parser.util.FastJsonUtil;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ESWorker {
    private static final Logger logger = LoggerFactory.getLogger(ESWorker.class);

    private static final int WORKER_NUM = 6;
    private static final long SLEEP_TIME = 2000;
    private static ESWorker instance =null;
    private static boolean running = true;


    private ExecutorService fixedThreadPool;

    public ESWorker() {
        fixedThreadPool = Executors.newFixedThreadPool(WORKER_NUM);
        for (int i = 0; i < WORKER_NUM; i++) {
            fixedThreadPool.execute(new Runnable() {
                public void run() {
                    long pollCount = 0;
                    while (running) {
                        try {
                            List<LogEntity> list = ESQueue.getIns().poll();
                            pollCount++;
                            if (pollCount % 120 == 0) {
                                logger.info("ESWork is working, pollCount = " + pollCount);
                            }
                            boolean executeRes = true;
                            if (list != null) {
                                executeRes = insertESBatch(list);
                            }
                            if(list == null || !executeRes){
                                try {
                                    Thread.currentThread().sleep(SLEEP_TIME);
                                } catch (InterruptedException e) {
                                    logger.error("sleep error", e);
                                }
                            }
                        }catch (Exception e){
                            logger.error("process queue data error,e="+e.getMessage(),e);
                        }
                    }
                    logger.info("ESWorker is exit ");
                }
            });
        }
        logger.info("init ESWorker over,thread num is "+WORKER_NUM);
    }

    public static ESWorker getIns() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    private static synchronized void init(){
        if (instance == null) {
            instance = new ESWorker();
        }
    }

    private boolean insertESBatch(List<LogEntity> logEntityList){
        Client client = ClientHelper.getInstance().getClient();

        if(client == null){
            logger.warn("first get es client is null,try again");
            client = ClientHelper.getInstance().getClient();
            if(client == null) {
                logger.error("es client is null,this log is lost, logEntityList.size={},logEntityList={}", logEntityList.size(), FastJsonUtil.object2Json(logEntityList));
                return false;
            }
        }
        try {
            return ESTemplete.createDataBatch(client, DopplerConstants.INDEX_NAME, DopplerConstants.INDEX_TYPE, logEntityList);
        }catch (Exception e){
            logger.error("batch insert into es exception",e);
            return false;
        }finally {
            ClientHelper.getInstance().returnPool(client);
        }
    }

    public void stop(){
        RedisConsumerTask.running = false;
        int i=0;
        while(ESQueue.getIns().size() != 0 && i< 180){
            logger.info("waitting for processing data, left data number is {}",ESQueue.getIns().size());
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                logger.error("jvm stop error");
            }
            i++;
        }
        if(ESQueue.getIns().size() != 0){
            logger.error("JVM will close, data is not processed over. size is "+ESQueue.getIns().size());
        }else{
            logger.info("perfect close");
            logger.info("perfect close");
            logger.info("perfect close");
            logger.info("perfect close JVM will close, data processeing is completed, no data lost");
        }

        running = false;
        if(fixedThreadPool!=null){
            fixedThreadPool.shutdown();
        }
        logger.info("ESWorker pool is shutdown");
    }

    public static void main(String[] args) {
        ESWorker ss = new ESWorker();
        try {
            Thread.currentThread().sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main : exit2");
        ss.stop();
    }
}
