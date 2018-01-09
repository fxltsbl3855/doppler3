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
public interface ErrorService {
    List<QueryBo> queryErrorByKey(String startDate, String endDate, String appName, String errorLevel, String host, String keys);

    List<QueryBo> queryErrorById(String id, int forward,String host);

    List<ErrorTypeBo> getErrorList();

    List<ErrorTypeBo> getErrorList(Date startDate,Date endDate,String errorName);

    List<ErrorHostBo> getErrorHostList(String errorName,String appName);

    List<ErrorHostBo> getErrorHostList(String errorName,String appName,Date startDate,Date endDate);

    List<ProblemBo> getProblemList(Date startDate, Date endDate, String appName, String errorName);

    List<ProblemDetailBo> getProblemDetailList(Date startDate, Date endDate, String appName, String errorName, String className, String methodName, long lineNum);
}
