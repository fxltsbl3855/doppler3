package com.sinoservices.doppler2.bo;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class OpInfoBo implements Serializable{
    private List<String> opTypeList;
    private List<String> opObjList;
    private List<String> appList;

    public OpInfoBo(){
    }

    public List<String> getAppList() {
        return appList;
    }

    public void setAppList(List<String> appList) {
        this.appList = appList;
    }

    public List<String> getOpTypeList() {
        return opTypeList;
    }

    public void setOpTypeList(List<String> opTypeList) {
        this.opTypeList = opTypeList;
    }

    public List<String> getOpObjList() {
        return opObjList;
    }

    public void setOpObjList(List<String> opObjList) {
        this.opObjList = opObjList;
    }
}
