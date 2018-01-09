package com.sinoservices.doppler2.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinoservices.doppler2.bo.ErrorHostBo;
import com.sinoservices.doppler2.bo.ErrorTypeBo;
import com.sinoservices.doppler2.bo.ProblemBo;
import com.sinoservices.doppler2.bo.ProblemDetailBo;
import com.sinoservices.doppler2.bo.QueryBo;
import com.sinoservices.doppler2.facade.ErrorFacade;
import com.sinoservices.doppler2.model.QueryModel;

@Controller
@RequestMapping("/${module-name}/exception")
@Scope(value = "prototype")
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    private static final String YELLOW = "yellow";
    private static final String YELLOW_GREEN = "yellowgreen";

    @Autowired
    private ErrorFacade errorFacade;

    /**
     * 异常监控列表
     * 
     * @return List<QueryBo>
     */
    @RequestMapping(value = "/queryErrorByKey")
    @ResponseBody
    public List<QueryBo> queryErrorByKey(@RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate, @RequestParam(value = "appName") String appName, @RequestParam(
                    value = "errorLevel") String errorLevel, @RequestParam(value = "addr") String addr,
            @RequestParam(value = "keys") String keys) {
        logger.info("start queryErrorByKey process...");
        List<QueryBo> queryBoList = new ArrayList<QueryBo>();
        try {
            queryBoList = errorFacade.queryErrorByKey(startDate, endDate, appName, null, null, null, errorLevel, addr, keys);
        } catch (Exception e) {
            logger.error("queryErrorByKey Exception : " + e.getMessage(), e);
            return null;
        }

        return queryBoList;
    }

    /**
     * 前后15条异常查询
     * 
     * @param id
     * @param forward
     * @return List<QueryBo>
     */
    @RequestMapping(value = "/queryErrorById")
    @ResponseBody
    public List<QueryModel> queryErrorById(@RequestParam(value = "id") String id, @RequestParam(value = "forward") int forward,
            @RequestParam(value = "index") int index, @RequestParam(value = "host") String host) {
        logger.info("start queryErrorById process...");
        List<QueryBo> queryBoList = new ArrayList<QueryBo>();
        try {
            queryBoList = errorFacade.queryErrorById(id, forward, host);
        } catch (Exception e) {
            logger.error("queryErrorById Exception : " + e.getMessage(), e);
            return null;
        }

        List<QueryModel> queryModels = new ArrayList<QueryModel>();
        String color = getColor(forward, index);

        if (queryBoList != null && queryBoList.size() > 0) {
            for (QueryBo queryBo : queryBoList) {
                QueryModel model =
                        new QueryModel(queryBo.getId(), queryBo.getLogInfo().replaceFirst("\t",
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"), color);
                queryModels.add(model);
            }
        }
        return queryModels;

    }


    /**
     * 异常列表查询
     * 
     * @return List<ErrorTypeBo>
     */
    @RequestMapping(value = "/getErrorList")
    @ResponseBody
    public List<ErrorTypeBo> getErrorList(@RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate, @RequestParam(value = "errorName") String errorName) {
        List<ErrorTypeBo> errorTypeBoList = new ArrayList<ErrorTypeBo>();
        try {
            errorTypeBoList = errorFacade.getErrorList(startDate, endDate, errorName);
        } catch (Exception e) {
            logger.error("getErrorList Exception : " + e.getMessage(), e);
            return null;
        }
        return errorTypeBoList;
    }

    /**
     * 异常明细查询
     * 
     * @return List< ErrorHostBo>
     */
    @RequestMapping(value = "/getErrorHostList")
    @ResponseBody
    public List<ErrorHostBo> getErrorHostList(@RequestParam(value = "errorName") String errorName,
            @RequestParam(value = "appName") String appName, @RequestParam(value = "startDate") String startDate, @RequestParam(
                    value = "endDate") String endDate) {
        List<ErrorHostBo> errorHostBoList = new ArrayList<ErrorHostBo>();
        try {
            errorHostBoList = errorFacade.getErrorHostList(errorName, appName, startDate, endDate);
        } catch (Exception e) {
            logger.error("getErrorList Exception : " + e.getMessage(), e);
            return null;
        }
        return errorHostBoList;
    }

    /**
     * 问题汇总查询
     * 
     * @return List< ProblemBo>
     */
    @RequestMapping(value = "/getProblemList")
    @ResponseBody
    public List<ProblemBo> getProblemList(@RequestParam(value = "exName") String errorName,
            @RequestParam(value = "appName") String appName, @RequestParam(value = "startDate") String startDate, @RequestParam(
                    value = "endDate") String endDate) {
        List<ProblemBo> problemBoList = new ArrayList<ProblemBo>();
        try {
            problemBoList = errorFacade.getProblemList(startDate, endDate, appName, errorName);
        } catch (Exception e) {
            logger.error("getProblemList Exception : " + e.getMessage(), e);
            return null;
        }
        return problemBoList;
    }

    /**
     * 问题详情查询
     * 
     * @return List< ProblemDetailBo>
     */
    @RequestMapping(value = "/getProblemDetailList")
    @ResponseBody
    public List<ProblemDetailBo> getProblemDetailList(@RequestParam(value = "exName") String exName,
            @RequestParam(value = "appName") String appName, @RequestParam(value = "startDate") String startDate, @RequestParam(
                    value = "endDate") String endDate, @RequestParam(value = "className") String className, @RequestParam(
                    value = "methodName") String methodName, @RequestParam(value = "lineNum") long lineNum) {
        List<ProblemDetailBo> problemDetailBoList = new ArrayList<ProblemDetailBo>();
        try {
            problemDetailBoList = errorFacade.getProblemDetailList(startDate, endDate, appName, exName, className, methodName, lineNum);
        } catch (Exception e) {
            logger.error("getProblemDetailList Exception : " + e.getMessage(), e);
            return null;
        }
        return problemDetailBoList;
    }

    /**
     * 设置颜色
     * 
     * @param forward
     * @param index
     * @return String
     */
    public static String getColor(int forward, int index) {
        String color = null;
        // 前后15条颜色设置
        if (forward == 0) {
            color = YELLOW;
        }
        // 前15条颜色设置
        if (forward == -1) {
            if (index % 2 == 0) {
                color = YELLOW;
            } else {
                color = YELLOW_GREEN;
            }
        }
        // 后15条颜色设置
        if (forward == 1) {
            if (index % 2 == 0) {
                color = YELLOW;
            } else {
                color = YELLOW_GREEN;
            }
        }
        return color;
    }
}
