package com.sinoservices.doppler2.bo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/17.
 */
public class ErrorHostBo implements Serializable{


    private String timeStr;
    private String errorId;
    private String errorName;
    private String host;
    private String fileInfo;
    private long num;

    public ErrorHostBo(String timeStr,String errorId,String errorName, String host,String fileInfo, long num){
        this.timeStr = timeStr;
        this.errorId = errorId;
        this.errorName = errorName;
        this.host = host;
        this.fileInfo = fileInfo;
        this.num = num;
    }

    public ErrorHostBo(String errorId,String errorName, String host,String fileInfo, long num){
        this.errorId = errorId;
        this.errorName = errorName;
        this.host = host;
        this.fileInfo = fileInfo;
        this.num = num;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
