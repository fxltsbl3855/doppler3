package com.sinoservices.doppler2.falcon;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/20.
 */
public class MonitorBo implements Serializable {
    private String appName;
    private String host;
    private String className;
    private String method;
    private int executeNum;
    private int maxTime;
    private int minTime;
    private int avgTime;



    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getExecuteNum() {
        return executeNum;
    }

    public void setExecuteNum(int executeNum) {
        this.executeNum = executeNum;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(int avgTime) {
        this.avgTime = avgTime;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("appName=");
        sb.append(appName);
        sb.append(",className=");
        sb.append(className);
        sb.append(",method=");
        sb.append(method);
        sb.append(",executeNum=");
        sb.append(executeNum);
        sb.append(",maxTime=");
        sb.append(maxTime);
        sb.append(",minTime=");
        sb.append(minTime);
        sb.append(",avgTime=");
        sb.append(avgTime);
        return sb.toString();
    }
}
