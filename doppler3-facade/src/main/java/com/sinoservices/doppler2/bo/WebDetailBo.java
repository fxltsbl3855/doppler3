package com.sinoservices.doppler2.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/20.
 */
public class WebDetailBo implements Serializable {
    private String id;
    private String param;
    private String header;

    public WebDetailBo(String id,String param,String header){
        this.id = id;
        this.param = param;
        this.header = header;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
