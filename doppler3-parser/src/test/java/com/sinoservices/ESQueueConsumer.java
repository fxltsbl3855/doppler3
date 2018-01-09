package com.sinoservices;

import com.sinoservices.parser.es.ESQueue;
import com.sinoservices.parser.es.entity.LogEntity;
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
public class ESQueueConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ESQueueConsumer.class);

    private static final int WORKER_NUM = 6;
    private ExecutorService fixedThreadPool;
    private static boolean running = true;

    public ESQueueConsumer(){
        fixedThreadPool = Executors.newFixedThreadPool(WORKER_NUM);
        for (int i = 0; i < WORKER_NUM; i++) {
            fixedThreadPool.execute(new Runnable() {
                public void run() {
                    long pollCount = 0;
                    while (running){
                        List<LogEntity> list = ESQueue.getIns().poll();
                        if(list!=null)
                            logger.info("ESQueueConsumer is working, list = "+list.size());
                        else {
                            try {
                                Thread.currentThread().sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    logger.info("ESQueueConsumer is exit ");
                }
            });
        }
    }



}
