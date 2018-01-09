package com.sinoservices.doppler2.facade;

import com.sinoservices.doppler2.bo.LogHttpResBo;
import com.sinoservices.doppler2.bo.WebBo;
import com.sinoservices.doppler2.bo.WebDetailBo;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public interface LogCollectorFacade {

    LogHttpResBo log(String host,String message,long offset,String fileName);

    LogHttpResBo log(String host,String message[],long offset,String fileName);

    String deleteAllIndex();

}
