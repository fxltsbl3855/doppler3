package com.sinoservices.doppler2.bo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/20.
 */
public class ModuleStatBo implements Serializable {

    private String moduleName;
    private String methodName;
    private long reqNum; //总请求量
    private double qps;

    private int maxTime;
    private int minTime;
    private int avgTime;

    private long errorNum;
    private double errorPercent;

    public ModuleStatBo(){

    }
    public ModuleStatBo(String moduleName,String methodName,Long reqNum,Long exNum){
        this.moduleName = moduleName;
        this.methodName = methodName;
        this.reqNum = reqNum==null?0L:reqNum;
        this.errorNum = exNum==null?0L:exNum;;
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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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

    public long getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(long errorNum) {
        this.errorNum = errorNum;
    }

    public double getErrorPercent() {
        return errorPercent;
    }

    public void setErrorPercent(double errorPercent) {
        this.errorPercent = errorPercent;
    }
}
