package com.sinoservices.dubbo.filter;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class StatUtil {
    private static final Logger LOG = LoggerFactory.getLogger(StatUtil.class);

    public static String toStr(Object[] params) {
        if (params == null || params.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : params) {
            if(obj == null) continue;
            sb.append(object2Json(obj));
            sb.append("#");
        }
        return sb.toString();
    }

    public static String object2Json(Object objectClass) {
        return JSON.toJSONString(objectClass);
    }

}
