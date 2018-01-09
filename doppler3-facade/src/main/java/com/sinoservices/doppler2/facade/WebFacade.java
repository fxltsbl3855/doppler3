package com.sinoservices.doppler2.facade;

import com.sinoservices.doppler2.bo.*;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public interface WebFacade {

    List<WebBo> queryWeb(String startDate, String endDate, String appName, String moduleName, String username);

    WebDetailBo queryWebDetail(String id);

}
