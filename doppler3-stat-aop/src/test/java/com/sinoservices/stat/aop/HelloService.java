package com.sinoservices.stat.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
@Service("helloService")
public class HelloService {

    @Autowired
    ThreadLocal<Map> globalThreadLocal;

    public String addOrderById(String type,Date now,int flag,double dd){

//        if(1==1){
//            throw new RuntimeException("sds");
//        }
        Map<String,String> map = new HashMap<String, String>();
        map.put("busProperty","test1=value1,test2=value2");
        map.put("processRes","SUCCESS");
        globalThreadLocal.set(map);

        return "hello world#";
    }

}
