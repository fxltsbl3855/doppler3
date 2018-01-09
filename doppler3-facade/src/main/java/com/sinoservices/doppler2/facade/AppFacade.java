package com.sinoservices.doppler2.facade;

import com.sinoservices.doppler2.bo.*;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public interface AppFacade {

    String test();

    DashboardBo getDashboard();

    List<AppStatBo> getAppDetailList(String startDate,String endDate);

    List<HostStatBo> getHostDetailList(String startDate,String endDate);

    List<AppInfoBo> getAppList();

    List<AppInfoBo> getAppList(int appType);

    List<TopTimeBo> topReq(String startDate,String endDate,String host,String appName,String moduleName,String method,int size);

}
