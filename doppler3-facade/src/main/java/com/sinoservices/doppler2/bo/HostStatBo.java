package com.sinoservices.doppler2.bo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/20.
 */
public class HostStatBo implements Serializable {
    private String host;
    private long reqNum;
    private double qps;
    private int errorNum;
    private double errorPercent;

    private int maxTime;
    private int minTime;
    private int avgTime;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getReqNum() {
        return reqNum;
    }

    public void setReqNum(long reqNum) {
        this.reqNum = reqNum;
    }

    public double getQps() {
        return qps;
    }

    public void setQps(double qps) {
        this.qps = qps;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }

    public double getErrorPercent() {
        return errorPercent;
    }

    public void setErrorPercent(double errorPercent) {
        this.errorPercent = errorPercent;
    }
}
