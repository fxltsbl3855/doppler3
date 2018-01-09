package com.sinoservices.doppler2.service.impl;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.doppler2.bo.AppStatBo;
import com.sinoservices.doppler2.bo.MethodBo;
import com.sinoservices.doppler2.bo.ModuleBo;
import com.sinoservices.doppler2.bo.ModuleStatBo;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.es.ESTemplete;
import com.sinoservices.doppler2.es.entity.AggsBucketsEntity;
import com.sinoservices.doppler2.es.entity.ErrorGroupByEntity;
import com.sinoservices.doppler2.es.entity.MustRangeTimestamp;
import com.sinoservices.doppler2.service.ServiceService;
import com.sinoservices.doppler2.service.assimble.Assimble;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.StringUtil;
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
@Service("serviceService")
public class ServiceServiceImpl implements ServiceService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceServiceImpl.class);


    @Override
    public List<ModuleBo> getServiceList(String appName) {
        Map<String,AggsBucketsEntity> resMap =  ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_STAT_NAME, Assimble.getNormalMap(appName,null),null,new String[]{Constant.FIELD_CLASS_NAME});
        List<ModuleBo> resList = Assimble.aggsMapToModuleBo(resMap);
        return resList;
    }

    @Override
    public List<ModuleStatBo> getServiceDetailList(Date startDate,Date endDate,String appName, String moduleName) {
        Map<String,String> boolMap = Assimble.getNormalMap(appName, moduleName);
        String[] groupByFields;
        int maxMinAvgPoint;
        if(StringUtil.isBlank(moduleName)) {
            groupByFields = new String[]{Constant.FIELD_CLASS_NAME,Constant.FIELD_METHOD,Constant.FIELD_RESULT};
            maxMinAvgPoint = 2;
        }else{
            groupByFields = new String[]{Constant.FIELD_METHOD,Constant.FIELD_RESULT};
            maxMinAvgPoint = 1;
        }
        Map<String,AggsBucketsEntity> resMap =  ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_STAT_NAME,boolMap ,
                Assimble.getMRTByCSTDate(startDate,endDate),null,groupByFields,maxMinAvgPoint,Constant.FIELD_TIME);
        List<ModuleStatBo> resList;
        if(StringUtil.isBlank(moduleName)){
            resList = Assimble.aggsMapToModuleStatBoWith3Level(resMap);
        }else{
            resList = Assimble.aggsMapToModuleStatBoWith2Level(moduleName,resMap);
        }
        return resList;
    }

    @Override
    public List<MethodBo> getModuleChart(String appName, String moduleName, String method, Date startDate,Date endDate) {
        Map<String,String> boolMap = Assimble.getNormalMap(appName, moduleName);boolMap.put(Constant.FIELD_METHOD,method);

        List<ErrorGroupByEntity> esList =  ESTemplete.getIns().queryByDateAggs(DopplerConstants.INDEX_STAT_NAME,boolMap ,null,
                Assimble.getMRTByDateStr(startDate,endDate,1),null);

        List<MethodBo>  resList = Assimble.transferToMethodBoList(esList);
        Assimble.utctimeToBjtimeMethodBo(resList);
        return resList;
    }

    public static void main(String[] a){
//        UpMessageConfig.es_ip = "192.168.0.88";
//        ServiceServiceImpl serviceServiceImpl = new ServiceServiceImpl();
//        List<ModuleStatBo>  list = serviceServiceImpl.getServiceDetailList("2017-1-18","ruite-web","");
//        logger.info(JsonUtils.object2Json(list));
    }
}
