package com.sinoservices.doppler2.service;

import com.sinoservices.doppler2.bo.OpBo;
import com.sinoservices.doppler2.bo.OpDetailBo;
import com.sinoservices.doppler2.bo.OpInfoBo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public interface OpService {
    OpInfoBo queryOpTypeObj();

    List<OpBo> queryOp(String startDate, String endDate,String appName, String opType, String opObj, String busField);

    OpDetailBo queryOpDetail(String id);
}
