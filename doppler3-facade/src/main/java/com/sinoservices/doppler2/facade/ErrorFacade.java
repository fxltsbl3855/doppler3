package com.sinoservices.doppler2.facade;

import com.sinoservices.doppler2.bo.*;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public interface ErrorFacade {

    List<QueryBo> queryErrorByKey(String startDate, String endDate, String appName , String moduleName,
                                  String action, String result, String errorLevel,
                                  String host, String keys);

    List<QueryBo> queryErrorById(String id,int forward,String host);

    List<ErrorTypeBo> getErrorList();

    List<ErrorTypeBo> getErrorList(String startDateStr,String endDateStr, String errorName);

    List<ErrorHostBo> getErrorHostList(String errorName,String appName);

    List<ErrorHostBo> getErrorHostList(String errorName,String appName,String startDateStr,String endDateStr);

    List<ProblemBo> getProblemList(String startDateStr,String endDateStr,String appName,String errorName);

    List<ProblemDetailBo> getProblemDetailList(String startDateStr,String endDateStr,String appName,String errorName,String className,String methodName,long lineNum);

}
