package com.sinoservices.doppler2.bo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/20.
 */
public class LogHttpResBo implements Serializable {

    private String opId;  //操作ID
    private int res; //操作结果，1:成功； -1：错误； -2 ：部分失败（批量情况下）
    private String desc;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }
}
