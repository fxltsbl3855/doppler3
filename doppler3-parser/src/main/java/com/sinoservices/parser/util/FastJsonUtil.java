package com.sinoservices.parser.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class FastJsonUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FastJsonUtil.class);

    public static <T> T json2Object(String json, Class<T> objectClass) {
        try {
            return JSON.parseObject(json,objectClass);
        } catch (Exception e) {
            LOG.error("erorr json: " + json, e);
            return null;
        }
    }

    public static String object2Json(Object objectClass) {
        return JSON.toJSONString(objectClass);
    }

}
