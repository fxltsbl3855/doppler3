package com.sinoservices.doppler2.facade.impl;

import com.sinoservices.doppler2.bo.WebBo;
import com.sinoservices.doppler2.bo.WebDetailBo;
import com.sinoservices.doppler2.exception.ESParamException;
import com.sinoservices.doppler2.facade.WebFacade;
import com.sinoservices.doppler2.facade.check.ParamCheck;
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
@Service("webFacade")
public class WebFacadeImpl implements WebFacade {
    private static final Logger logger = LoggerFactory.getLogger(WebFacadeImpl.class);

    @Autowired
    WebService webService;



    @Override
    public List<WebBo> queryWeb(String startDate, String endDate, String appName, String moduleName, String username) {
        logger.info("queryWeb invoke....startDate="+startDate+",endDate="+endDate+",appName="+appName+",moduleName="+moduleName+",username="+username);
        if(!ParamCheck.checkTime(startDate)){
            throw new ESParamException("startDate is invalid,format should be 'yyyy-MM-dd HH:mm:ss',startDate="+startDate);
        }
        if(!ParamCheck.checkTime(endDate)){
            throw new ESParamException("endDate is invalid,format should be 'yyyy-MM-dd HH:mm:ss',endDate="+endDate);
        }

        List<WebBo> resList = webService.queryWeb(DateUtil.stirng2Date(startDate,"yyyy-MM-dd HH:mm:ss"),DateUtil.stirng2Date(endDate,"yyyy-MM-dd HH:mm:ss"),appName,moduleName,username);
        logger.info("queryWeb invoke end....,resList = {}", JsonUtils.object2Json(resList));
        return resList;
    }

    @Override
    public WebDetailBo queryWebDetail(String id) {
        logger.info("queryWebDetail invoke....id="+id);
        if(StringUtil.isBlank(id)){
            throw new ESParamException("id is empty, id="+id);
        }
        WebDetailBo res = webService.queryWebDetail(id);
        logger.info("queryWebDetail invoke end....,res = {}", JsonUtils.object2Json(res));
        return res;
    }
}
