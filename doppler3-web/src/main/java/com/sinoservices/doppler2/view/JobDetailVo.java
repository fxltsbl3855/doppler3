package com.sinoservices.doppler2.view;

import java.util.Date;

import com.sinoservices.util.DateUtil;

public class JobDetailVo {

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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExecuteTime() {
		if (executeTime == null){
    		return "";
    	}
		return DateUtil.Date2String(executeTime, "yyyy-MM-dd HH:mm:ss");
	}
	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
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
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
    
}
