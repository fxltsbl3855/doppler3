package com.sinoservices.doppler2.bo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/17.
 */
public class QueryBo implements Serializable{


    private String id;
    private String title;
    private String logInfo;
    private String host;


    public QueryBo(String id,String logInfo){
        this.id = id;
        this.logInfo = logInfo;
    }

    public QueryBo(String id,String logInfo,String host,String title){
        this.id = id;
        this.logInfo = logInfo;
        this.host = host;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }
}
