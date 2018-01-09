package com.sinoservices.parser.file;

import com.sinoservices.parser.Constant;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.es.ClientHelper;
import com.sinoservices.parser.es.ESTemplete;
import com.sinoservices.parser.es.ESWorker;
import com.sinoservices.parser.es.JsonUtil;
import com.sinoservices.parser.zip.UnzipFile;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.NumberUtil;
import com.sinoservices.util.StringUtil;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class FileMonitor extends Thread{
    private static final Logger LOG = LoggerFactory.getLogger(FileMonitor.class);
    public static boolean RUN_FLAG = true;
    public static List<String> bakFailFile= new ArrayList<String>();
    public static List<String> processFile= new ArrayList<String>();

    private int MAX_THREAD_NUM = 6;
    public static AtomicInteger currentThreadNum = new AtomicInteger(0);

    public FileMonitor(){
        LOG.info("FileMonitor cons,log_collector="+UpMessageConfig.log_collector);
        ESWorker.getIns();
    }

    long sleepTime = 60000;

    public void run(){
        if(!Constant.LOG_COLLECTOR_FILE.equals(UpMessageConfig.log_collector)) {
            LOG.info("log collector is configured :"+UpMessageConfig.log_collector +",FileMonitor set RUN_FLAG=false, and exit");
            RUN_FLAG = false;
            return;
        }

        int ycl = 1;
        if(ycl < 0 ){
            Client client = ClientHelper.getInstance().getClient();
            int num = ESTemplete.getClusterNodeNum(client);
            if(num > 1) {
                LOG.error("current es is cluster, this sysytem is not support. please upgrade to Enterprise Edition, otherwise system will be not work");
                return;
            }
            ClientHelper.getInstance().returnPool(client);
        }

        LOG.info("FileMonitor start running...........RUN_FLAG={},sourceDir={},bakDir="+ UpMessageConfig.bak_dir,Boolean.toString(RUN_FLAG),UpMessageConfig.source_dir);
        if(StringUtil.isBlank(UpMessageConfig.source_dir) || StringUtil.isBlank(UpMessageConfig.bak_dir)){
            LOG.error("properties file config error,sourceDir or bakDir is empty,monitor is quit");
            return;
        }
        while(RUN_FLAG) {
            try {
                monitorDir(UpMessageConfig.source_dir);
                Thread.sleep(sleepTime);
                if(LOG.isDebugEnabled()){
                    LOG.debug("FileMonitor is running...bakFailFile="+ JsonUtils.object2Json(bakFailFile)+",processFile="+JsonUtils.object2Json(processFile)+",sourceDir="+UpMessageConfig.source_dir);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("FileMonitor run error,e="+e.getMessage(),e);
            }
        }
        LOG.info("FileMonitor is stopped...........RUN_FLAG={}",RUN_FLAG);
    }

    public void monitorDir(String dir){
        File dirFile = new File(dir);
        File[] files = dirFile.listFiles();
        if(files == null || files.length ==0){
            LOG.info("files is empty");
            return;
        }
        for(File file : files){
            while(currentThreadNum.get() >= MAX_THREAD_NUM){
                LOG.warn("thread num is too many,currentThreadNum="+currentThreadNum);
                return;
            }

            if(file.isDirectory()){
                LOG.error(file.getName() +" is directory,continue");
                continue;
            }
            if(file.getName().indexOf(".zip")!=-1){
                UnzipFile.unzipWithShell(dir,file);
                LOG.error(file.getName() +" is zip file,unzip and deleted");
                continue;
            }
            if(bakFailFile.contains(file.getName())){
                LOG.error("this file have bean processed. And bak fail. please check the error manual.file={}",file.getName());
                continue;
            }
            if(processFile.contains(file.getName())){
                LOG.info("this file is processing.....file={}",file.getName());
                continue;
            }

            processFile.add(file.getName());
            FileReadTask fileReadTask = new FileReadTask(file);
            fileReadTask.start();
            LOG.info("add file to thread, fileName={}, and start task",file.getName());
        }
    }



    public void stopMonitor(){
        RUN_FLAG = false;
        LOG.info("FileMonitor will stop...");
    }

}
