package com.sinoservices.doppler2.service.assimble;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.bo.JobBo;
import com.sinoservices.doppler2.bo.JobDetailBo;
import com.sinoservices.doppler2.bo.WebBo;
import com.sinoservices.doppler2.bo.WebDetailBo;
import com.sinoservices.doppler2.es.entity.AggsBucketsEntity;
import com.sinoservices.doppler2.es.entity.LogRecordJobEntity;
import com.sinoservices.doppler2.es.entity.LogRecordStatEntity;
import com.sinoservices.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class WebAssimble {
    private static final Logger logger = LoggerFactory.getLogger(WebAssimble.class);

    public static void main(String[] a){

    }

    public static List<WebBo> webEntityToBo(List<LogRecordStatEntity> webRecordList) {
        if(Assimble.isEmpty(webRecordList)){
            return Collections.EMPTY_LIST;
        }
        List<WebBo> resList = new ArrayList<WebBo>(webRecordList.size());
        for(LogRecordStatEntity entity : webRecordList){
            resList.add(getWebBo(entity.getId(), DateUtil.utcStrToLocalDate(entity.getTimestamp()),entity.getAppName(),entity.getHost(),entity.getClassName(),
                    entity.getBrower(),entity.getUsername(),new Long(entity.getTime()).intValue(),entity.getCode()));
        }
        return resList;
    }

    private static WebBo getWebBo(String id, Date reqTime, String appName, String host, String className, String brower, String username, int time, int code) {
        WebBo tmp = new WebBo();
        tmp.setId(id);
        tmp.setReqTime(reqTime);
        tmp.setAppName(appName);
        tmp.setClassName(className);
        tmp.setHost(host);
        tmp.setBrower(brower);
        tmp.setUsername(username);
        tmp.setTime(time);
        tmp.setCode(code+"");
        return tmp;
    }

    public static WebDetailBo toWebDetailBo(Map<String, Object> res,String id) {
        String param = res.get("param")==null?"":res.get("param").toString();
        String header = res.get("header")==null?"":res.get("header").toString();
        return new WebDetailBo(id,param,header);
    }
}
