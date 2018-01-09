package com.sinoservices.doppler2.facade;

import com.sinoservices.doppler2.bo.*;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public interface ServiceFacade {


    List<ModuleBo> getServiceList(String appName);


    List<ModuleStatBo> getServiceDetailList(String startDate,String endDate, String appName,String moduleName);


    List<MethodBo> getModuleChart(String appName,String moduleName,String method, String startDate,String endDate);


}
