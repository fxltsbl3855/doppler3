package com.sinoservices.parser.es;

import com.sinoservices.parser.es.entity.LogEntity;
import com.sinoservices.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ESQueue {
    private static final Logger logger = LoggerFactory.getLogger(ESQueue.class);

    private ArrayBlockingQueue<List<LogEntity>> queue = new ArrayBlockingQueue<List<LogEntity>>(1000);
    private static ESQueue instance =null;
    private ESQueue(){
        logger.info("ESQueue cons");
    }

    public static ESQueue getIns() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    private static synchronized void init(){
        if (instance == null) {
            instance = new ESQueue();
        }
    }

    public boolean offer(List<LogEntity> list){
        try {
            List<LogEntity> ll = new ArrayList<LogEntity>(list.size());
            ll.addAll(list);
            boolean res = queue.offer(ll,3, TimeUnit.SECONDS);
            if(!res){
                logger.error("queue offer fail,queue.size= "+queue.size() +" , data.size = "+list.size());
            }
            return res;
        } catch (Exception e) {
            logger.error("ESQueue offer error",e);
            return false;
        }
    }

    public List<LogEntity> poll(){
        if(queue.size() > 800){
            logger.error("current queue size = "+queue.size());
        }
        List<LogEntity> ll = null;
        try {
            ll = queue.poll(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("poll error",e);
        }
        return ll;
    }

    public int size(){
        return queue.size();
    }

}
