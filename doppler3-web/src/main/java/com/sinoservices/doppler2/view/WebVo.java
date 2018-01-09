package com.sinoservices.doppler2.view;

import java.util.Date;

import com.sinoservices.util.DateUtil;

public class WebVo extends LinkView{
 	private String id;
    private Date reqTime;
    private String appName ;
    private String host;
    private String brower;
    private String username;
    private String className;
    private int time;
    private String code;
    
    public String getReqTime() {
    	if (reqTime == null){
    		return "";
    	}
		return DateUtil.Date2String(reqTime, "yyyy-MM-dd HH:mm:ss");
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
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

    public String getBrower() {
        return brower;
    }

    public void setBrower(String brower) {
        this.brower = brower;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
	
}
