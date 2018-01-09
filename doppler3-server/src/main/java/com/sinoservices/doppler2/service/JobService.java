package com.sinoservices.doppler2.service;

import com.sinoservices.doppler2.bo.AppInfoBo;
import com.sinoservices.doppler2.bo.JobBo;
import com.sinoservices.doppler2.bo.JobDetailBo;
import com.sinoservices.doppler2.bo.ModuleBo;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public interface JobService {

    List<AppInfoBo> getAppList();

    List<ModuleBo> getModuleList(String appName);

    List<JobBo> queryJob(Date startDate, Date endDate, String appName, String moduleName, String result);

    List<JobDetailBo> queryJobDetail(Date startDate, Date endDate, String appName,String host, String moduleName, String result);


}
