package com.sinoservices;

import com.sinoservices.parser.kafka.MsgEntity;
import com.sinoservices.parser.util.FastJsonUtil;
import com.sinoservices.util.JsonUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class TestJson {

    public static void main(String[] args) {
        TestJson testJson = new TestJson();
        String str = " {\"@timestamp\":\"2017-01-04T07:15:54.187Z\",\"beat\":{\"hostname\":\"TRD-Charlie-MP06JNBT\",\"name\":\"TRD-Charlie-MP06JNBT\",\"version\":\"5.1.1\"},\"fields\":{\"ip\":\"192.168.0.86\"},\"input_type\":\"log\",\"message\":\"17:06:17.477 [org.springframework.scheduling.quartz.SchedulerFactoryBean#0_Worker-1] INFO  com.sinoservices.stat.aop.MonitorAop - msg dsa dsa dsa dsa dsa dsa dsa\",\"offset\":542,\"source\":\"D:\\\\mnt\\\\logs\\\\ruite-web.2017-01-04.log\",\"type\":\"log\"}";
        long ss = System.currentTimeMillis();
        for (int i = 0; i < 300000 ; i++) {
            testJson.testFastJson(str);
        }
        long ee = System.currentTimeMillis();
        System.out.println(ee-ss);
    }

    public void testFastJson(String msg){
        MsgEntity msgEntity = FastJsonUtil.json2Object(msg,MsgEntity.class);


    }

    public void testGson(String msg){
        MsgEntity msgEntity = JsonUtils.json2Object(msg,MsgEntity.class);
    }

}
