package com.sinoservices.parser.zip;

import com.sinoservices.parser.util.StringProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFile {
    private static final Logger logger = LoggerFactory.getLogger(UnzipFile.class);
    private static final String ZIP_ERROR_STR = "error: Nothing to do!";
    public static void getIpByFileName(String fileName){

    }

    public static void zipWithShellAndDelTextFile(File textFile) {
        long startTime = System.currentTimeMillis();
        String fileName = textFile.getAbsolutePath();
        String cmd = "zip "+fileName + ".zip "+fileName;
        if(logger.isInfoEnabled()) {
            logger.info("zipWithShellAndDelTextFile start execute...fileName=" + fileName + ", cmd=" + cmd);
        }

        logger.info("start zip....");
        StringBuilder sb = new StringBuilder();
        boolean cmdRes = true;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                sb.append(line);
                if(line.indexOf(ZIP_ERROR_STR)>0){
                    cmdRes = false;
                }
            }
            input.close();
        } catch (Exception e) {
            logger.error("zip file error,e="+e.getMessage(),e);
        }
        logger.info("zip over, cmdRes = "+cmdRes+",msg="+sb.toString());
        long endTime = System.currentTimeMillis();

        if(cmdRes) {
            boolean delete = textFile.delete();
            logger.info("delete text file , file=" + textFile.getName() + ",res=" + delete);
        }
        long endTime2 = System.currentTimeMillis();
        logger.info("zipWithShellAndDelTextFile end,  zip res="+cmdRes+",zip time="+(endTime-startTime) +",delete time="+(endTime2 - endTime));
    }

    public static void unzipWithShell(String targetPath, File zipFile) {
        long startTime = System.currentTimeMillis();
        logger.info("unzipWithShell start execute...targetPath="+targetPath+",zipFile="+zipFile.getName());
        String ip = StringProcess.getIp(zipFile.getName());
        //unzip -j -n /opt/es_log_source/ruite-web.2016-09-19.log.zip.192.168.0.90 -d /opt/es_log_source/
        String cmd = "unzip -j -n "+targetPath+"/"+zipFile.getName()+" -d "+targetPath;
        String proviousFileName = "";
        try {
            InputStream is = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                proviousFileName = getFileName(entry.getName());
                break;
            }
            zis.close();
            is.close();
        }catch (Exception e){
            logger.error("get zip file name error",e);
        }
        logger.info(" ip= "+ip +",cmd="+cmd+",proviousFileName="+proviousFileName);

        logger.info("start unzip....");
        Process process = null;
        StringBuilder sb = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                sb.append(line);
            }
            input.close();
        } catch (IOException e) {
            logger.error("zip file error,e="+e.getMessage(),e);
        }
        logger.info("unzip over, msg="+sb.toString());

        //rename
        File file =new File(targetPath+"/"+proviousFileName);
        boolean reNameRes = file.renameTo(new File(file.getAbsolutePath()+"."+ip));
        logger.info(" rename over, filePath="+file.getAbsolutePath()+",fileName="+file.getName()+",res="+reNameRes);

        boolean delete = zipFile.delete();
        logger.info("delete zip file , file=" +zipFile.getName()+",res="+delete);
        long endTime = System.currentTimeMillis();
        logger.info("unzipWithShell end,  time="+(endTime-startTime));
    }


    public static void unzipFile(String targetPath, File zipFile) {
        logger.info("unzipFile start ................targetPath="+targetPath+", zipFile="+zipFile.getName());
        try {
            String ip = StringProcess.getIp(zipFile.getName());

            InputStream is = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry = null;
            boolean isUnzip = false;
            logger.info("start unzip " + zipFile.getName() + "...");
            while ((entry = zis.getNextEntry()) != null) {
                String zipPath = entry.getName();
                logger.info("zipPath="+zipPath);
                try {
                    if (entry.isDirectory()) {
                        logger.info("entry is directory");
                        File zipFolder = new File(targetPath + File.separator
                                + zipPath);
                        if (!zipFolder.exists()) {
                            zipFolder.mkdirs();
                        }
                    } else {
                        logger.info("entry is file");
                        zipPath = getFileName(zipPath);
                        logger.info("zipPath fileName = " + zipPath);
                        File file = new File(targetPath + File.separator
                                + zipPath);
                        if (!file.exists()) {
                            logger.info("file is not exist,file="+file.getName());
                            File pathDir = file.getParentFile();
                            logger.info("pathDir="+pathDir.getName());
//                            pathDir.mkdirs();
                            file.createNewFile();
                            FileOutputStream fos = new FileOutputStream(file);
                            int bread;
                            while ((bread = zis.read()) != -1) {
                                fos.write(bread);
                            }
                            fos.close();
                            logger.info("unzip success ,file="+file.getName());
                            isUnzip = true;
                            file.renameTo(new File(file.getAbsolutePath()+"."+ip));
                        }else{
                            logger.info("unzip stop ,the file is exist,file="+file.getName());
                        }
                    }
                } catch (Exception e) {
                    logger.error("unzip error",e);
                    continue;
                }
            }
            zis.close();
            is.close();
            logger.info("unzip over");

            if(isUnzip){
                boolean delete = zipFile.delete();
                logger.info("delete zip file , file=" +zipFile.getName()+",res="+delete);
            }
        } catch (Exception e) {
            logger.error("unzip error2",e);
        }
        logger.info("unzipFile end ................");
    }

        /**
         *
         * @param targetPath
         * @param zipFilePath
         */
    public static void unzipFile(String targetPath, String zipFilePath) {
        File zipFile = new File(zipFilePath);
        unzipFile(targetPath,zipFile);
    }

    public static String getFileName(String dir) {
        return dir.substring(dir.lastIndexOf("/")+1);
    }

    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
//        String targetPath = "E:\\dd\\";
//        String zipFile = "E:\\dd\\ZipTestCompressing.zip.192.168.0.8";
//        UnzipFile.unzipFile(targetPath, zipFile);

        String zipPath = "ruite-web.2016-09-19.log";
        System.out.print(getFileName(zipPath));
    }  
  
}  
