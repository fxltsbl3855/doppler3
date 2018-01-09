package com.sinoservices.doppler2.facade.impl;

import com.sinoservices.doppler2.bo.*;
import com.sinoservices.doppler2.exception.ESParamException;
import com.sinoservices.doppler2.facade.OpFacade;
import com.sinoservices.doppler2.facade.WebFacade;
import com.sinoservices.doppler2.facade.check.ParamCheck;
import com.sinoservices.doppler2.service.OpService;
import com.sinoservices.doppler2.service.WebService;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
@Service("opFacade")
public class OpFacadeImpl implements OpFacade {
    private static final Logger logger = LoggerFactory.getLogger(OpFacadeImpl.class);

    @Autowired
    OpService opService;


    @Override
    public OpInfoBo queryOpTypeObj() {
        logger.info("queryOpTypeObj invoke...");
        OpInfoBo opInfoBo = opService.queryOpTypeObj();
        logger.info("queryOpTypeObj invoke end,opInfoBo={}",JsonUtils.object2Json(opInfoBo));
        return opInfoBo;
    }

    @Override
    public List<OpBo> queryOp(String startDate, String endDate,String appName, String opType, String opObj, String busField) {
        logger.info("queryOp invoke...startDate="+startDate+",endDate="+endDate+",appName="+appName+",opType="+opType+",opObj="+opObj+",busField="+busField);
        List<OpBo> resList =  opService.queryOp(startDate,endDate,appName,opType,opObj,busField);
        logger.info("resList.size={}",(resList==null?"0":resList.size()));
        return resList;
    }

    @Override
    public OpDetailBo queryOpDetail(String id) {
        logger.info("queryOpDetail invoke...id={}",id);
        OpDetailBo opDetailBo = opService.queryOpDetail(id);
        logger.info("opDetailBo={}",JsonUtils.object2Json(opDetailBo));
        return opDetailBo;
    }
}
