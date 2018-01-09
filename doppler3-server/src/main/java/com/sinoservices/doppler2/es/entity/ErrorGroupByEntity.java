package com.sinoservices.doppler2.es.entity;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ErrorGroupByEntity {

    private String timeStr;
    private long num;

    public ErrorGroupByEntity(String timeStr,long num){
        this.timeStr = timeStr;
        this.num = num;
    }
    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
