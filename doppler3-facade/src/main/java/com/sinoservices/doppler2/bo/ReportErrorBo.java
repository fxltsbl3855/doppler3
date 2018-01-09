package com.sinoservices.doppler2.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ReportErrorBo implements Serializable,Comparable{

    private Date date;
    private String errorName;
    private String appName;
    private String ip;
    private int num;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int compareTo(Object o) {
        if(o == null){
            return 0;
        }
        ReportErrorBo sdto = (ReportErrorBo)o;
        if(this.date.compareTo(sdto.getDate())>0){
            return 1;
        }else if(this.date.compareTo(sdto.getDate())<0){
            return -1;
        }else{
            if(this.errorName.compareTo(sdto.getErrorName())>0){
                return 1;
            }else if(this.errorName.compareTo(sdto.getErrorName())<0){
                return -1;
            }else{
                return 0;
            }
        }
    }
}
