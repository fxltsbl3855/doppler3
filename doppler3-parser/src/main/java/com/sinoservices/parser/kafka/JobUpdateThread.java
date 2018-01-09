package com.sinoservices.parser.kafka;

import com.sinoservices.parser.es.entity.JobUpdateGroovy;
import com.sinoservices.parser.es.entity.StatJobEntity;
import com.sinoservices.parser.util.FastJsonUtil;
import com.sinoservices.parser.util.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class JobUpdateThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(JobUpdateThread.class);

    private int batchNum;
    private long sleepTime = 5000;
    List<StatJobEntity> list;

    public JobUpdateThread(List<StatJobEntity> list,int batchNum){
        this.list = new ArrayList<StatJobEntity>(list.size());
        this.list.addAll(list);
        this.batchNum = batchNum;
        if(batchNum == 2){
            sleepTime = 20000;
        }else if(batchNum == 3){
            sleepTime = 60000;
        }else if(batchNum == 4 ){
            sleepTime = 300000;
        }else if(batchNum >= 5){
            sleepTime = 600000;
        }
        this.setName("JobUpdateThread-"+batchNum+"-"+sleepTime+"-"+list.size());
    }
    public void run(){
        boolean isInterrupted = false;
        try {
            Thread.currentThread().sleep(sleepTime);
        } catch (InterruptedException e) {
            logger.warn("JobUpdateThread sleep interrupted");
            isInterrupted = true;
        }

        List<StatJobEntity> failList = new ArrayList<StatJobEntity>(list.size());
        for(StatJobEntity statJobEntity : list){
            String res = HttpRequest.restPostUpdateJob(JobUpdateGroovy.fillData(statJobEntity.getResult(),new Long(statJobEntity.getTime()).intValue(),statJobEntity.getJobId()));
            if(res == null || res.indexOf("\"updated\":0") > 0){
                failList.add(statJobEntity);
            }
            logger.debug("batch num ="+batchNum+",sleepTime= "+sleepTime+",job update,id="+statJobEntity.getId()+",jobId={},res={}",statJobEntity.getJobId(),res);
        }
        list.clear();

        if(failList.size()==0){
            return;
        }

        //最多重试1次
        if(batchNum <= 2 && !isInterrupted) {
            JobUpdateThread jobUpdateThread = new JobUpdateThread(failList, ++batchNum);
            jobUpdateThread.start();
        }else{
            logger.error("JobUpdate task fail,try num is {},failList= {},isInterrupted="+isInterrupted,batchNum, FastJsonUtil.object2Json(failList));
            failList.clear();
        }
    }


}
