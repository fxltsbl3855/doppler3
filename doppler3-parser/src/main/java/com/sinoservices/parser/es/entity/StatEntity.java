package com.sinoservices.parser.es.entity;

import com.sinoservices.parser.Constant;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class StatEntity {

    private String id;
    private String appName;
    private String host;
    private Date timestamp;
    private String className;
    private String method;
    private String result;
    private long time;
    private String param;

    //app类型；1 web； 2 server
    private String appType;

    //web app addtional column
    private String username;
    private String clientIp;
    private String brower;
    private String header;
    private int code;

    public StatEntity() {}

    public StatEntity(String id,String appName, String host,Date timestamp) {
        this.id = id;
        this.appName = appName;
        this.host = host;
        this.timestamp = timestamp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getBrower() {
        return brower;
    }

    public void setBrower(String brower) {
        this.brower = brower;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getParam() {
        return param;
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

    public void setBase(String fileName, long lineNumber, String appName, String host, Date timestamp,boolean isWebApp) {
        this.id = fileName+"#"+lineNumber;
        this.appName = appName;
        this.host = host;
        this.timestamp = timestamp;
        this.appType = isWebApp ? Constant.APP_TYPE_WEB:Constant.APP_TYPE_SERVER;
    }
}
