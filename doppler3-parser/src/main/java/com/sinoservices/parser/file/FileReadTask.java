package com.sinoservices.parser.file;

import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.es.ClientHelper;
import com.sinoservices.parser.es.ESQueue;
import com.sinoservices.parser.es.ESTemplete;
import com.sinoservices.parser.es.entity.LogEntity;
import com.sinoservices.parser.es.entity.StatEntity;
import com.sinoservices.parser.util.StringProcess;
import com.sinoservices.parser.zip.UnzipFile;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.NumberUtil;
import com.sinoservices.util.StringUtil;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class FileReadTask extends Thread{
    private static final Logger LOG = LoggerFactory.getLogger(FileReadTask.class);

    File file = null;

    public FileReadTask(File file){
        this.file = file;
    }

    public void run(){
        FileMonitor.currentThreadNum.incrementAndGet();

        LOG.info("start process file, fileName={},current thread num={}",file.getName(),FileMonitor.currentThreadNum);
        long start = System.currentTimeMillis();
        String bakFileName = file.getName()+"."+ DateUtil.Date2String(new Date(),"HHmmss")+"_"+ NumberUtil.getRandom(4);
        Long lineNum = readFile(file,bakFileName);
        if(lineNum == -1){
            LOG.info("file name invalid");
            bakFileName = file.getName()+"."+ DateUtil.Date2String(new Date(),"HHmmss") +"_error";
        }
        long end = System.currentTimeMillis();
        LOG.info("process file end, read file spend time="+(end-start)+", line number="+lineNum+",bakFileName="+bakFileName);

        String proviousName = file.getName();
        boolean res = bakFileAndZip(file,bakFileName);
        if(res){
            FileMonitor.processFile.remove(proviousName);
            LOG.info("proviousName is remove from processFile list, proviousName={}",proviousName);
        }
        LOG.info("bak file end, fileName={},bak result={}",file.getName(),res);

        FileMonitor.currentThreadNum.decrementAndGet();
        LOG.info("current thread num={}",FileMonitor.currentThreadNum);
    }

    public Long readFile(File file,String bakFileName){
        String appName = StringProcess.getAppName(file.getName());
        String day = StringProcess.getDay(file.getName());
        String host = StringProcess.getIp(file.getName());

        if("0000-00-00".equals(day) || "0.0.0.0".equals(host)){
            LOG.error("file name is invalid, file name hasnot contains date or host ,file="+file.getName());
            return -1L;
        }

        LOG.info("file processing,appName={},day={}",appName,day);
        BufferedReader reader = null;
        long lineNumber = 1;
        try {
            reader = new BufferedReader(new FileReader(file));
            String lineMsg = null;
            List<LogEntity> logEntityList = new ArrayList<LogEntity>(8000);
            List<StatEntity> statEntityList = new ArrayList<StatEntity>(8000);
            Date proviousDate = DateUtil.stirng2Date(day,"yyyy-MM-dd");
            if(proviousDate == null){
                proviousDate = DateUtil.toYesterday(new Date());
            }
            while ((lineMsg = reader.readLine()) != null) {

                //解析日志记录为日志对象，需要传入文件名相关的参数以及日志记录
                LogEntity logEntity = RegexMatches.parseLineMsg(bakFileName,lineNumber,appName,host,lineMsg,day,proviousDate);

                proviousDate = logEntity.getTimestamp();
                logEntityList.add(logEntity);

                if(StringUtil.isBlank(logEntity.getEx())) {
                    //解析统计日志记录,依赖于解析后的LogEntity对象
                    StatEntity statEntity = RegexMatches.parseStatLineMsg(bakFileName,lineNumber,logEntity);

                    if (statEntity != null) {
                        statEntityList.add(statEntity);
                    }
                    if(statEntityList.size() > 0 && statEntityList.size() % 8000 == 0){
                        insertEStatBatch(statEntityList);
                        statEntityList.clear();
                    }
                }
                if(logEntityList.size() > 0 && logEntityList.size() % 8000 == 0){
                    ESQueue.getIns().offer(logEntityList);
                    LOG.info("batch process line , current line number is "+lineNumber);
                    logEntityList.clear();
                }
                lineNumber++;
            }
            LOG.info( "read file while exit,file={}",file.getName());
            if(statEntityList.size()>0){
                insertEStatBatch(statEntityList);
                statEntityList.clear();
            }
            if(logEntityList.size()>0){
                ESQueue.getIns().offer(logEntityList);
                LOG.info( "batch process line , current line number is "+lineNumber);
                logEntityList.clear();
            }
            reader.close();
        } catch (Exception e) {
            LOG.error("read file exception",e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e1) {
                    LOG.error("close file exception",e1);
                }
            }
            LOG.info( "read file over,file={},line={}",file.getName(),lineNumber);
        }
        return lineNumber;
    }

    public boolean bakFileAndZip(File file,String bakFileName){
        File bak = new File(UpMessageConfig.bak_dir +bakFileName);
        boolean res = file.renameTo(bak);
        if(!res){
            LOG.error("bak fail, fileName={},bak result={}",file.getName(),res);
            FileMonitor.bakFailFile.add(file.getName());
        }else{//zip
            UnzipFile.zipWithShellAndDelTextFile(bak);
        }
        return res;
    }

    private void insertEStatBatch(List<StatEntity> statEntityList){
        Client client = null;
        try {
            client = ClientHelper.getInstance().getClient();
            ESTemplete.createStatBatch(client,DopplerConstants.INDEX_STAT_NAME, DopplerConstants.INDEX_TYPE, statEntityList);
        }catch (Exception e){
            LOG.error("batch insert into stat es exception",e);
        }finally {
            if(client != null)
                ClientHelper.getInstance().returnPool(client);
        }
    }








}
