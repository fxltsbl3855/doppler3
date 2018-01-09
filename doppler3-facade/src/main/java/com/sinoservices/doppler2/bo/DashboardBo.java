package com.sinoservices.doppler2.bo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/24.
 */
public class DashboardBo implements Serializable {

    private int appNum;
    private int moduleNum;
    private long appReqNum;
    private long moduleReqNum;
    private long errorNum;


    List<ReqDetail> appReqList;
    List<ReqDetail> moduleReqList;
    List<ErrorDetail> errorList;
    List<ReqDetail> errorStat;

    public DashboardBo(){
    }

    public List<ReqDetail> getErrorStat() {
        return errorStat;
    }

    public void setErrorStat(List<ReqDetail> errorStat) {
        this.errorStat = errorStat;
    }

    public int getAppNum() {
        return appNum;
    }

    public void setAppNum(int appNum) {
        this.appNum = appNum;
    }

    public int getModuleNum() {
        return moduleNum;
    }

    public void setModuleNum(int moduleNum) {
        this.moduleNum = moduleNum;
    }

    public long getAppReqNum() {
        return appReqNum;
    }

    public void setAppReqNum(long appReqNum) {
        this.appReqNum = appReqNum;
    }

    public long getModuleReqNum() {
        return moduleReqNum;
    }

    public void setModuleReqNum(long moduleReqNum) {
        this.moduleReqNum = moduleReqNum;
    }

    public long getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(long errorNum) {
        this.errorNum = errorNum;
    }

    public List<ReqDetail> getAppReqList() {
        return appReqList;
    }

    public void setAppReqList(List<ReqDetail> appReqList) {
        this.appReqList = appReqList;
    }

    public List<ReqDetail> getModuleReqList() {
        return moduleReqList;
    }

    public void setModuleReqList(List<ReqDetail> moduleReqList) {
        this.moduleReqList = moduleReqList;
    }

    public List<ErrorDetail> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ErrorDetail> errorList) {
        this.errorList = errorList;
    }

}
