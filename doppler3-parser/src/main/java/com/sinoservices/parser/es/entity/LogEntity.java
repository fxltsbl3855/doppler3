package com.sinoservices.parser.es.entity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class LogEntity {

    //es字段
    private String id;

    //系统追加字段
    private String appName;
    private String host;
    private String logLevel;
    private Date timestamp;
    private String ex= "";
    private String original;
    private long position;
    private String file;

    public LogEntity() {}

    public LogEntity(String fileName,long position, String appName, String host, String original) {
        this.id = fileName + "#" +position;
        this.position = position;
        this.appName = appName;
        this.host = host;
        this.original =original;
        this.file =fileName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
