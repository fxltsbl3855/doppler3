package com.sinoservices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class TestESQueue {

    public static void main(String[] args) {

        List<ESQueueProducer> list = new ArrayList<ESQueueProducer>();
        for (int i = 0; i <30 ; i++) {
            ESQueueProducer ss = new ESQueueProducer(10);
            list.add(ss);
        }
        for (int i = 0; i <30 ; i++) {
            ESQueueProducer ss = list.get(i);
            ss.start();
        }

        for (int i = 0; i <8 ; i++) {
            ESQueueConsumer ss = new ESQueueConsumer();
        }



    }

}
