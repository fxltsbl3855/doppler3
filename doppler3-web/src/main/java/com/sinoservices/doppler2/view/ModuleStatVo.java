package com.sinoservices.doppler2.view;

public class ModuleStatVo extends LinkView {

	private String moduleName;
    private String methodName;
    private long reqNum; //总请求量
    private double qps;

    private int maxTime;
    private int minTime;
    private int avgTime;

    private long errorNum;
    private double errorPercent;
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
