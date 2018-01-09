package com.sinoservices.doppler2.service.impl;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.doppler2.bo.WebBo;
import com.sinoservices.doppler2.bo.WebDetailBo;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.es.ESTemplete;
import com.sinoservices.doppler2.es.entity.LogRecordJobEntity;
import com.sinoservices.doppler2.es.entity.LogRecordStatEntity;
import com.sinoservices.doppler2.service.WebService;
import com.sinoservices.doppler2.service.assimble.Assimble;
import com.sinoservices.doppler2.service.assimble.WebAssimble;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
@Service("webService")
public class WebServiceImpl implements WebService {
    private static final Logger logger = LoggerFactory.getLogger(WebServiceImpl.class);

    @Override
    public List<WebBo> queryWeb(Date startDate, Date endDate, String appName, String moduleName, String username) {
        Map<String,String> mustMap = Assimble.getNormalMap(appName, moduleName);
        Assimble.addToMap(mustMap,Constant.FIELD_APP_TYPE,"1");
        Assimble.addToMap(mustMap,Constant.FIELD_USERNAME,username);

        List<LogRecordStatEntity> webRecordList = ESTemplete.getIns().queryStatLogRecordSort(DopplerConstants.INDEX_STAT_NAME,mustMap,
                Assimble.getMRTByCSTDate(startDate,endDate),null, Constant.FIELD_TIMESTAMP,false,LogRecordStatEntity.webStatFields(),1,60);
        List<WebBo> resList = WebAssimble.webEntityToBo(webRecordList);
        return resList;
    }

    @Override
    public WebDetailBo queryWebDetail(String id) {
        Map<String,Object> res = ESTemplete.getIns().getById(DopplerConstants.INDEX_STAT_NAME,DopplerConstants.INDEX_TYPE,id);
        WebDetailBo webDetailBo = WebAssimble.toWebDetailBo(res,id);
        return webDetailBo;
    }

    public static void main(String[] a){
        UpMessageConfig.es_addr = "192.168.0.88:9300";

        WebServiceImpl webServiceImpl = new WebServiceImpl();
//        List ll = webServiceImpl.queryWeb(DateUtil.stirng2Date("2017-2-13","yyyy-MM-dd"),DateUtil.stirng2Date("2017-3-15","yyyy-MM-dd"),"ruite-web","com.class1","u1");
        logger.info(JsonUtils.object2Json(webServiceImpl.queryWebDetail("q1")));
    }
}
