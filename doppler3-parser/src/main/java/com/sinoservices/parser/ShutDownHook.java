package com.sinoservices.parser;


import com.sinoservices.parser.es.ClientHelper;
import com.sinoservices.parser.es.ESQueue;
import com.sinoservices.parser.es.ESWorker;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ShutDownHook implements Runnable {

    public void run() {
        ClientHelper.getInstance().closePool();
        ESWorker.getIns().stop();
        interrupt();
    }

    public void interrupt(){
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // 遍历线程组树，获取根线程组
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // 激活的线程数加倍
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slackList = new Thread[estimatedSize];
        // 获取根线程组的所有线程
        int actualSize = topGroup.enumerate(slackList);
        // copy into a list that is the exact size
        Thread[] list = new Thread[actualSize];
        System.arraycopy(slackList, 0, list, 0, actualSize);
        for (Thread thread : list) {
            String tname = thread.getName();
            if(tname.startsWith("JobUpdateThread")){
                thread.interrupt();
            }
        }
    }


}
