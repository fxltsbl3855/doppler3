package com.sinoservices.doppler2.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/17.
 */
public class ProblemDetailBo implements Serializable,Comparable{


    private String id;
    private Date timestamp;
    private String appName;
    private String host;
    private String exName;
    private String className;
    private String classNameSim;
    private String methodName;
    private long lineNum;

    public ProblemDetailBo(){

    }

    public ProblemDetailBo(String id,Date timestamp,String appName,String host, String exName, String className, String methodName, long lineNum) {
        this.id = id;
        this.timestamp = timestamp;
        this.appName = appName;
        this.host = host;
        this.exName = exName;
        this.className = className;
        this.classNameSim = getClassNameSim();
        this.methodName = methodName;
        this.lineNum = lineNum;
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

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
        this.classNameSim = getClassNameSim();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getLineNum() {
        return lineNum;
    }

    public void setLineNum(long lineNum) {
        this.lineNum = lineNum;
    }

    public int compareTo(Object o) {
        ProblemDetailBo sdto = (ProblemDetailBo)o;
        return this.getTimestamp().compareTo(sdto.getTimestamp()) > 0?1:-1;
    }

    public String getClassNameSim(){
        if(className==null){
            return null;
        }
        if(className.startsWith("com.sinoservices.")){
            String sim = className.replaceFirst("com.sinoservices.","c.s.");
            return sim;
        }
        return className;
    }
}
