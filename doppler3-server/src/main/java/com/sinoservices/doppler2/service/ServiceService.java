package com.sinoservices.doppler2.service;

import com.sinoservices.doppler2.bo.MethodBo;
import com.sinoservices.doppler2.bo.ModuleBo;
import com.sinoservices.doppler2.bo.ModuleStatBo;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public interface ServiceService {
    List<ModuleBo> getServiceList(String appName);

    List<ModuleStatBo> getServiceDetailList(Date startDate,Date endDate, String appName, String moduleName);

    List<MethodBo> getModuleChart(String appName, String moduleName, String method, Date startDate,Date endDate);
}
