package com.sinoservices.doppler2.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/20.
 */
public class JobDetailBo implements Serializable {
    private String id;
    private Date executeTime;
    private String action;
    private String appName;
    private String host;
    private String className;
    private String result;
    private String param;
    private String jobId;
    private int time;

    public JobDetailBo(String id,Date timestamp, String action, String appName, String host, String className, String result,String param,String jobId,int time) {
        this.id = id;
        this.executeTime = timestamp;
        this.action = action;
        this.appName = appName;
        this.host = host;
        this.className = className;
        this.result = result;
        this.param = param;
        this.jobId = jobId;
        this.time = time;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
