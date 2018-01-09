package com.sinoservices.parser.es.entity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class StatJobEntity {

    private String id;
    private String action;
    private String appName;
    private String host;
    private Date timestamp;
    private String className;
    private String method;
    private String result;
    private String param;
    private String jobId;
    private long time;


    public StatJobEntity() {}

    public StatJobEntity(String id, String appName, String host, Date timestamp) {
        this.id = id;
        this.appName = appName;
        this.host = host;
        this.timestamp = timestamp;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getParam() {
        return param;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setBase(String fileName, long lineNumber, String appName, String host, Date timestamp) {
        this.id = fileName+"#"+lineNumber;
        this.appName = appName;
        this.host = host;
        this.timestamp = timestamp;
    }
}
