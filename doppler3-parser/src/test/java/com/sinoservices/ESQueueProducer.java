package com.sinoservices;

import com.sinoservices.parser.es.ESQueue;
import com.sinoservices.parser.es.entity.LogEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ESQueueProducer extends Thread {

    private int a;
    public ESQueueProducer(int a){
        this.a = a;
    }

    public void run(){

        for (int i = 0; i <a ; i++) {
            List<LogEntity> list = new ArrayList<LogEntity>();
            for (int j = 0; j <=i ; j++) {
                LogEntity log = new LogEntity();log.setId(a+"_"+i+"_"+j);
                list.add(log);
            }
            ESQueue.getIns().offer(list);
        }




    }

}
