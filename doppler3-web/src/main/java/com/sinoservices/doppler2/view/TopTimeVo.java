package com.sinoservices.doppler2.view;

import java.util.Date;

import com.sinoservices.util.DateUtil;

public class TopTimeVo extends LinkView{
	private Date reqTime;
    private String host;
    private String appName;
    private String moduleName;
    private String methodName;
    private String param;
    private long time;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getReqTime() {
    	if (reqTime == null){
    		return "";
    	}
		return DateUtil.Date2String(reqTime, "yyyy-MM-dd HH:mm:ss");
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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
