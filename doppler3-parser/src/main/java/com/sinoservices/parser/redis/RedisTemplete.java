package com.sinoservices.parser.redis;

import com.sinoservices.parser.Constant;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.kafka.MsgEntity;
import com.sinoservices.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class RedisTemplete {
    private static final Logger logger = LoggerFactory.getLogger(RedisTemplete.class);

    private static RedisTemplete ins;
    private RedisSimplePool pool = new RedisSimplePool();
    private RedisTemplete(){}

    public static RedisTemplete getIns() {
         if (ins == null) {
             init();
         }
         return ins;
    }

    private static synchronized void init(){
        if (ins == null) {
            ins = new RedisTemplete();
        }
    }

    public Long rpush(String key,String... values){
        Jedis jedis = pool.getResource();
        try {
            return jedis.rpush(key, values);
        }finally {
            pool.returnResource(jedis);
        }
    }

    public String lpop(String key){
        Jedis jedis = pool.getResource();
        boolean jedisConnStatus = true;
        try {
            String res =  jedis.lpop(key);
            return res;
        }catch(JedisConnectionException e) {
            jedisConnStatus = false;
            logger.error("redis lpop JedisConnectionException,"+e.getMessage());
            return null;
        }catch (Exception e){
            logger.error("redis lpop data error,"+e.getMessage(),e);
            return null;
        }finally {
            if(jedisConnStatus) {
                pool.returnResource(jedis);
            }else{
                pool.returnBrokenResource(jedis);
            }
        }
    }

    public Long llen(String key){
        Jedis jedis = pool.getResource();
        try {
            return jedis.llen(key);
        }finally {
            pool.returnResource(jedis);
        }

    }

    public static void main(String[] a){
//        UpMessageConfig.redis_conns = "1";
//        UpMessageConfig.redis_ip = "192.168.0.88";
//        UpMessageConfig.redis_port = "6379";
//        UpMessageConfig.redis_timeout = "2000";
//
////        String[] ss = {"a","b","c"};
////        System.out.println(RedisTemplete.getIns().rpush("test",ss));
//        long length = RedisTemplete.getIns().llen(Constant.REDIS_QUEUE_KEY);
//        for(int i=0;i<length;i++) {
//            String msg = RedisTemplete.getIns().lpop(Constant.REDIS_QUEUE_KEY);
//            if(msg == null){
//                continue;
//            }
//            MsgEntity msgEntity = JsonUtils.json2Object(msg,MsgEntity.class);
//            System.out.println(msgEntity.getMessage());
//        }

        Jedis jedis = new Jedis("192.168.0.88", 6379, 2000);
        jedis.connect();
        System.out.println(jedis.isConnected());
        System.out.println(jedis.llen("test"));
        System.out.println(jedis.isConnected());
        jedis.quit();

    }

}
