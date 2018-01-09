/**
 * 
 */
package com.sinoservices.doppler2.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinoservices.doppler2.bo.OpBo;
import com.sinoservices.doppler2.bo.OpDetailBo;
import com.sinoservices.doppler2.bo.OpInfoBo;
import com.sinoservices.doppler2.facade.OpFacade;

/**
 * 〈一句话功能简述〉 〈功能详细描述〉 版本：1.0 作者：Bill 修改日期：2017-8-15 修改内容：
 */
@Controller
@RequestMapping({"/${module-name}/log"})
public class LogController {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private OpFacade opFacade;

    @ResponseBody
    @RequestMapping(value = "/getOpBoList")
    public List<OpBo> getOpBoList(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "opType") String opType, @RequestParam(value = "appName") String appName,
            @RequestParam(value = "opObj") String opObj, @RequestParam(value = "busField") String busField) {
        logger.info("start getOpBoList process...");
        List<OpBo> opBoList = new ArrayList<OpBo>();
        try {
            opBoList = opFacade.queryOp(startDate, endDate, appName, opType, opObj, busField);
        } catch (Exception e) {
            logger.error("getOpBoList Exception : " + e.getMessage(), e);
            return null;
        }
        return opBoList;
    }

    @ResponseBody
    @RequestMapping(value = "/queryOpDetail")
    public OpDetailBo queryOpDetail(@RequestParam(value = "id") String id) {
        logger.info("start queryOpDetail process...");
        OpDetailBo opDetailBo = null;
        try {
            opDetailBo = opFacade.queryOpDetail(id);
        } catch (Exception e) {
            logger.error("queryOpDetail Exception : " + e.getMessage(), e);
            return null;
        }
        return opDetailBo;
    }

    @ResponseBody
    @RequestMapping(value = "/queryOpTypeObj")
    public OpInfoBo queryOpTypeObj() {
        logger.info("start queryOpTypeObj process...");
        OpInfoBo opInfoBo = null;
        try {
            opInfoBo = opFacade.queryOpTypeObj();
        } catch (Exception e) {
            logger.error("queryOpTypeObj Exception : " + e.getMessage(), e);
            return null;
        }
        return opInfoBo;
    }



}
