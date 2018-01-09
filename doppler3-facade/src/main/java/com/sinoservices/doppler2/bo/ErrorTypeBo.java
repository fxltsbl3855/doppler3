package com.sinoservices.doppler2.bo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/17.
 */
public class ErrorTypeBo implements Serializable,Comparable{


    private String errorName;
    private String appName;
    private long num;

    public ErrorTypeBo(String errorName, String appName, long num){
        this.errorName = errorName;
        this.appName = appName;
        this.num = num;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public int compareTo(Object o) {
        ErrorTypeBo sdto = (ErrorTypeBo)o;
        return this.num > sdto.getNum()?-1:1;
    }
}
