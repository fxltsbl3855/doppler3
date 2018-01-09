package com.sinoservices.doppler2.bo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/17.
 */
public class ProblemBo implements Serializable,Comparable{


    private String exName;
    private String appName;
    private String className;
    private String classNameSim;
    private String methodName;
    private long lineNum;
    private long num;

    public ProblemBo(){
    }

    public ProblemBo(String appName, String exName, String className, String methodName,  long lineNum,Long num) {
        this.appName = appName;
        this.exName = exName;
        this.className = className;
        this.classNameSim = getClassNameSim();
        this.methodName = methodName;
        this.lineNum = lineNum;
        this.num = num;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public int compareTo(Object o) {
        ProblemBo sdto = (ProblemBo)o;
        return this.num > sdto.getNum()?-1:1;
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
