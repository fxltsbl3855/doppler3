package com.sinoservices.parser.es;

import java.io.IOException;

import com.sinoservices.parser.es.entity.*;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    /**
     * 实现将实体对象转换成json对象
     * @param logEntity    LogEntity
     * @return
     */
    public static String obj2JsonData(LogEntity logEntity){
        String jsonData = null;
        try {
            //使用XContentBuilder创建json数据
            XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
            jsonBuild.startObject()
                    .field("appName",logEntity.getAppName())
                    .field("host",logEntity.getHost())
                    .field("timestamp",logEntity.getTimestamp())
                    .field("logLevel",logEntity.getLogLevel())
                    .field("ex",logEntity.getEx())
                    .field("original",logEntity.getOriginal())
                    .field("position",logEntity.getPosition())
                    .field("file",logEntity.getFile())
                    .endObject();
            jsonData = jsonBuild.string();
        } catch (IOException e) {
            logger.error("JsonUtil.obj2JsonData error",e);
        }
        return jsonData;
    }

    public static String obj2JsonData(StatEntity statEntity) {
        String jsonData = null;
        try {
            //使用XContentBuilder创建json数据
            XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
            jsonBuild.startObject()
                    .field("appName",statEntity.getAppName())
                    .field("host",statEntity.getHost())
                    .field("timestamp",statEntity.getTimestamp())
                    .field("className",statEntity.getClassName())
                    .field("method",statEntity.getMethod())
                    .field("result",statEntity.getResult())
                    .field("param",statEntity.getParam())
                    .field("time",statEntity.getTime())
                    .field("appType",statEntity.getAppType())
                    .field("username",statEntity.getUsername())
                    .field("clientIp",statEntity.getClientIp())
                    .field("brower",statEntity.getBrower())
                    .field("header",statEntity.getHeader())
                    .field("code",statEntity.getCode())
                    .endObject();
            jsonData = jsonBuild.string();
        } catch (IOException e) {
            logger.error("JsonUtil.obj2JsonData2 error",e);
        }
        return jsonData;
    }

    public static String obj2JsonData(StatJobEntity statJobEntity) {
        String jsonData = null;
        try {
            //使用XContentBuilder创建json数据
            XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
            jsonBuild.startObject()
                    .field("action",statJobEntity.getAction())
                    .field("appName",statJobEntity.getAppName())
                    .field("host",statJobEntity.getHost())
                    .field("timestamp",statJobEntity.getTimestamp())
                    .field("className",statJobEntity.getClassName())
                    .field("method",statJobEntity.getMethod())
                    .field("result",statJobEntity.getResult())
                    .field("param",statJobEntity.getParam())
                    .field("jobId",statJobEntity.getJobId())
                    .field("time",statJobEntity.getTime())
                    .endObject();
            jsonData = jsonBuild.string();
        } catch (IOException e) {
            logger.error("JsonUtil.obj2JsonData3 error",e);
        }
        return jsonData;
    }

    public static String obj2JsonData(ExEntity exEntity) {
        String jsonData = null;
        try {
            //使用XContentBuilder创建json数据
            XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
            jsonBuild.startObject()
                    .field("timestamp",exEntity.getTimestamp())
                    .field("appName",exEntity.getAppName())
                    .field("host",exEntity.getHost())
                    .field("exName",exEntity.getExName())
                    .field("className",exEntity.getClassName())
                    .field("method",exEntity.getMethod())
                    .field("lineNum",exEntity.getLineNum())
                    .field("original",exEntity.getOriginal())
                    .field("position",exEntity.getPosition())
                    .field("file",exEntity.getFile())
                    .endObject();
            jsonData = jsonBuild.string();
        } catch (IOException e) {
            logger.error("JsonUtil.obj2JsonData4 error",e);
        }
        return jsonData;
    }

    public static String obj2JsonData(OpEntity opEntity) {
        String jsonData = null;
        try {
            //使用XContentBuilder创建json数据
            XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
            jsonBuild.startObject()
                    .field("timestamp",opEntity.getTimestamp())
                    .field("action",opEntity.getAction())
                    .field("appName",opEntity.getAppName())
                    .field("host",opEntity.getHost())
                    .field("className",opEntity.getClassName())
                    .field("method",opEntity.getMethod())
                    .field("opType",opEntity.getOperType())
                    .field("opObj",opEntity.getOperObj())
                    .field("result",opEntity.getResult())
                    .field("param",opEntity.getParam())
                    .field("busParam",opEntity.getBusParam())
                    .field("time",opEntity.getTime())
                    .field("original",opEntity.getOriginal())
                    .field("position",opEntity.getPosition())
                    .field("file",opEntity.getFile())
                    .endObject();
            jsonData = jsonBuild.string();
        } catch (IOException e) {
            logger.error("JsonUtil.obj2JsonData5 error",e);
        }
        return jsonData;
    }
}
