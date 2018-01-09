package com.sinoservices.parser.service;

import com.sinoservices.doppler2.bo.LogHttpResBo;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public interface LogCollectorService {

    LogHttpResBo log(String host, String message, long offset, String fileName);

    LogHttpResBo log(String host,String message[],long offset,String fileName);

}
