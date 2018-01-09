package com.sinoservices.doppler2.bo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/24.
 */
public class ReqDetail implements Serializable,Comparable {

    private String name;
    private long num;

    public ReqDetail(){ }

    public ReqDetail(String name, long num){
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public int compareTo(Object o) {
        if(o == null){
            return 0;
        }
        ReqDetail sdto = (ReqDetail)o;
        if(this.num > sdto.getNum()){
            return 1;
        }else if(this.num < sdto.getNum()){
            return -1;
        }else{
            return 0;
        }
    }
}
