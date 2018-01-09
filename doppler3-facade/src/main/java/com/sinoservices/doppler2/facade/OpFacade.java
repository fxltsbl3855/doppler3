package com.sinoservices.doppler2.facade;

import com.sinoservices.doppler2.bo.*;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public interface OpFacade {

    OpInfoBo queryOpTypeObj();

    List<OpBo> queryOp(String startDate, String endDate,String appName, String opType, String opObj, String busField);

    OpDetailBo queryOpDetail(String id);

}
