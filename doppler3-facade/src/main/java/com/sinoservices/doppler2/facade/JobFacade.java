package com.sinoservices.doppler2.facade;

import com.sinoservices.doppler2.bo.*;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public interface JobFacade {

    List<AppInfoBo> getAppList();

    List<ModuleBo> getModuleList(String appName);

    List<JobBo> queryJob(String startDate, String endDate,String appName,String moduleName,String result);

    List<JobDetailBo> queryJobDetail(String startDate, String endDate,String appName,String host,String moduleName,String result);

}
