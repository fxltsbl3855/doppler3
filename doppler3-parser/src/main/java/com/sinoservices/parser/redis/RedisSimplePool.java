package com.sinoservices.parser.redis;

import java.util.concurrent.LinkedBlockingDeque;

import java.util.concurrent.TimeUnit;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.util.StringProcess;
import com.sinoservices.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class RedisSimplePool {

    private static final Logger logger = LoggerFactory.getLogger(RedisSimplePool.class);

    private LinkedBlockingDeque<Jedis> queue = new LinkedBlockingDeque<Jedis>(20);

    private Integer maxActive;
    private String host;
    private Integer port;
    private Integer timeout;

    public RedisSimplePool() {
        logger.info("RedisSimplePool cons start...");
        maxActive = NumberUtil.formatNumber(UpMessageConfig.redis_conns,5);
        if(!StringProcess.isIp(UpMessageConfig.redis_ip)){
            logger.error("RedisSimplePool host is invalid,host="+UpMessageConfig.redis_ip+",use default ip : 127.0.0.1");
            host = "127.0.0.1";
        }else{
            host = UpMessageConfig.redis_ip.trim();
        }
        port = NumberUtil.formatNumber(UpMessageConfig.redis_port,6379);
        timeout = NumberUtil.formatNumber(UpMessageConfig.redis_timeout,2000);
        logger.info("RedisSimplePool cons over,maxActive="+maxActive+",host="+host+",port="+port+",timeout="+timeout);
    }

    private synchronized void fillRedis() {
        logger.debug("fillRedis...maxActive is {}",maxActive );
        for (int i = 0; i < maxActive; i++) {
            try {
                Jedis jedis = getNew();
                queue.offerFirst(jedis,3, TimeUnit.SECONDS);
            } catch (Exception e) {
                logger.error("RedisPool error:" + e.getMessage(),e);
            }
        }
    }

    public synchronized Jedis getResource() {
//        logger.debug("RedisSimplePool>>>>>>>>> getResource,queue.size={}",queue.size());
        if (this.queue.size() == 0) {
            fillRedis();
        }
        Jedis sss = null;
        try {
            sss = this.queue.pollLast(3,TimeUnit.SECONDS);
            if(isConnected(sss)){
//                logger.debug("RedisSimplePool>>>>>>>>> isConnected = true,hashcode={}",sss.hashCode());
               return sss;
            }else{
                logger.debug("RedisSimplePool>>>>>>>>> isConnected = false,hashcode={}",sss.hashCode());
                returnBrokenResource(sss);
                sss = getNew();
            }
        } catch (InterruptedException e) {
            logger.error("RedisSimplePool>>>>>>>>> "+e.getMessage(),e);
            sss = getNew();
        }
        return sss;
    }

    public Jedis getNew() {
        try {
            Jedis jedis = new Jedis(host, port, timeout);
            jedis.connect();
            logger.debug("RedisSimplePool>>>>>>>>> new ok,"+jedis.isConnected()+","+jedis.hashCode());
            return jedis;
        } catch (Exception e) {
            logger.error("RedisSimplePool>>>>>>>>> new error");
            return null;
        }
    }

    public synchronized void returnResource(Jedis sss) {
//        logger.debug("RedisSimplePool>>>>>>>>> returnResource,queue.size={}",queue.size());
        try {
            this.queue.putFirst(sss);
        } catch (InterruptedException e) {
            logger.error("returnResource putFirst error",e);
        }
    }

    public void returnBrokenResource(Jedis sss) {
        logger.debug("RedisSimplePool>>>>>>>>> returnBrokenResource,queue.size={}",queue.size());
        if (sss != null) {
            try {
                sss.quit();
            }catch (Exception e){
                logger.error("returnBrokenResource error,quit",e);
            }
            try {
                sss.disconnect();
            }catch (Exception e){
                logger.error("returnBrokenResource error,disconnect",e);
            }
            sss = null;
        }
    }

    public boolean isConnected(Jedis jedis) {
        try {
            return jedis.isConnected();
        } catch (Exception e) {
            logger.error("judge isConnected exception ",e);
            return false;
        }
    }
}
