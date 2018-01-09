package com.sinoservices.doppler2.service;

import com.sinoservices.doppler2.bo.*;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public interface AppService {
    DashboardBo getDashboard();

    List<AppStatBo> getAppDetailList(Date startDate,Date endDate);

    List<HostStatBo> getHostDetailList(Date startDate, Date endDate);

    List<AppInfoBo> getAppList();

    List<AppInfoBo> getWebAppList();

    List<TopTimeBo> topReq(Date startDate,Date endDate, String host,String appName,String moduleName,String method,int size);
}
