package com.sinoservices.doppler2.service;

import com.sinoservices.doppler2.bo.WebBo;
import com.sinoservices.doppler2.bo.WebDetailBo;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public interface WebService {

    List<WebBo> queryWeb(Date startDate, Date endDate, String appName, String moduleName, String username);

    WebDetailBo queryWebDetail(String id);

}
