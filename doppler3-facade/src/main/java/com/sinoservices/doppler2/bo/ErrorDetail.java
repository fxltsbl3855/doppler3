package com.sinoservices.doppler2.bo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/24.
 */
public class ErrorDetail implements Serializable {

    private String dateStr;
    private long num;

    public ErrorDetail(){ }

    public ErrorDetail(String dateStr, int num){
        this.dateStr = dateStr;
        this.num = num;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
