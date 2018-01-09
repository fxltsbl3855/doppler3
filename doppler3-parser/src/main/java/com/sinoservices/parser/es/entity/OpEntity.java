package com.sinoservices.parser.es.entity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class OpEntity {

    private String id;

    private Date timestamp;
    private String action;
    private String appName;
    private String host;
    private String className;
    private String method;
    private String operType;
    private String operObj;
    private String result;
    private String param;
    private String busParam;
    private String original;
    private long time;
    private long position;
    private String file;

    public OpEntity() {}
    public OpEntity(String id,Date timestamp,String action,String appName, String host,String className,
                    String method,String operType,String operObj,String result,
                    String param,String busParam,String original,long time,long position,String file) {
        this.id = id;
        this.timestamp = timestamp;
        this.action = action;
        this.appName = appName;
        this.host = host;
        this.className = className;

        this.method = method;
        this.operType = operType;
        this.operObj = operObj;
        this.result = result;

        this.param = param;
        this.busParam = busParam;
        this.original = original;
        this.time = time;
        this.position = position;
        this.file = file;

    }

    public void setBase(String id,Date timestamp,String appName, String host,String original,long position,String file){
        this.id = id;
        this.timestamp = timestamp;
        this.appName = appName;
        this.host = host;
        this.original = original;
        this.position = position;
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getOperObj() {
        return operObj;
    }

    public void setOperObj(String operObj) {
        this.operObj = operObj;
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

    public String getBusParam() {
        return busParam;
    }

    public void setBusParam(String busParam) {
        this.busParam = busParam;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
