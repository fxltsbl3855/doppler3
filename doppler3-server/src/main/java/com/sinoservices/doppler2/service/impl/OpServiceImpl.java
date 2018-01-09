package com.sinoservices.doppler2.service.impl;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.doppler2.bo.ModuleBo;
import com.sinoservices.doppler2.bo.OpBo;
import com.sinoservices.doppler2.bo.OpDetailBo;
import com.sinoservices.doppler2.bo.OpInfoBo;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.es.ESTemplete;
import com.sinoservices.doppler2.es.entity.AggsBucketsEntity;
import com.sinoservices.doppler2.es.entity.LogRecordEntity;
import com.sinoservices.doppler2.es.entity.MustRangeTimestamp;
import com.sinoservices.doppler2.es.entity.OpEntity;
import com.sinoservices.doppler2.service.OpService;
import com.sinoservices.doppler2.service.assimble.Assimble;
import com.sinoservices.doppler2.service.assimble.ErrorAssimble;
import com.sinoservices.doppler2.service.assimble.OpAssimble;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.StringUtil;
import org.elasticsearch.index.get.GetField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
@Service("opService")
public class OpServiceImpl implements OpService {
    private static final Logger logger = LoggerFactory.getLogger(OpServiceImpl.class);
    @Override
    public OpInfoBo queryOpTypeObj() {
        Map<String,AggsBucketsEntity> resTypeMap =  ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_OP_NAME, null,null,new String[]{Constant.OpStat.FIELD_OP_TYPE});
        List<String> resTypeList = Assimble.aggsMapToStr(resTypeMap);

        Map<String,AggsBucketsEntity> resObjMap =  ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_OP_NAME, null,null,new String[]{Constant.OpStat.FIELD_OP_OBJ});
        List<String> resObjList = Assimble.aggsMapToStr(resObjMap);

        Map<String,AggsBucketsEntity> appMap =  ESTemplete.getIns().queryAppReqStatAggs(DopplerConstants.INDEX_OP_NAME, null,null,new String[]{Constant.OpStat.FIELD_APP_NAME});
        List<String> appList = Assimble.aggsMapToStr(appMap);

        OpInfoBo opInfoBo = new OpInfoBo();
        opInfoBo.setOpTypeList(resTypeList);
        opInfoBo.setOpObjList(resObjList);
        opInfoBo.setAppList(appList);
        return opInfoBo;
    }

    @Override
    public List<OpBo> queryOp(String startTime, String endTime,String appName, String opType, String opObj, String busField) {

        Map<String,String> musuMap = new HashMap<String,String>(2);
        musuMap.put(Constant.OpStat.FIELD_ACTION,Constant.OpStat.FIELD_ACTION_OPOUT);
        if(!Assimble.isEmpty(appName,"-1")) {
            musuMap.put(Constant.OpStat.FIELD_APP_NAME, appName);
        }
        if(!Assimble.isEmpty(opType,"-1")) {
            musuMap.put(Constant.OpStat.FIELD_OP_TYPE, opType);
        }
        if(!Assimble.isEmpty(opObj,"-1")) {
            musuMap.put(Constant.OpStat.FIELD_OP_OBJ, opObj);
        }

        MustRangeTimestamp timestamp =  Assimble.getMRTByCSTDate(startTime,endTime);
        Map<String, String> shouldMatchMap = Assimble.getMapValueNotNull(Constant.OpStat.FIELD_BUS_PARAM,OpAssimble.reGroupBusParam(busField));
        List<OpEntity> opList = ESTemplete.getIns().queryOpStatRecordByScore(DopplerConstants.INDEX_OP_NAME,musuMap,timestamp,shouldMatchMap,Constant.OpStat.FIELD_TIMESTAMP,false, 1,30 );
        return  OpAssimble.translate(opList);
    }

    @Override
    public OpDetailBo queryOpDetail(String id) {
        String[] fields = {Constant.OpStat.FIELD_TIMESTAMP,Constant.OpStat.FIELD_APP_NAME,Constant.OpStat.FIELD_HOST,Constant.OpStat.FIELD_OP_OBJ,
                Constant.OpStat.FIELD_OP_TYPE,Constant.OpStat.FIELD_PARAM,Constant.OpStat.FIELD_BUS_PARAM,Constant.OpStat.FIELD_RESULT,Constant.OpStat.FIELD_TIME};

        List<Map<String,Object>> resList = ESTemplete.getIns().mutiSearchByIds(DopplerConstants.INDEX_OP_NAME,DopplerConstants.INDEX_TYPE,new String[]{id},fields);
        if(resList == null || resList.size()!=1){
            logger.error("queryOpDetail query by id,result size is empty or more than 1 ");
            return null;
        }
        return OpAssimble.assimbleOpDetail(resList.get(0));
    }

    public static void main(String[] args) {
        UpMessageConfig.es_addr = "192.168.0.89:9300";

        OpServiceImpl opServiceImpl = new OpServiceImpl();
        System.out.println(JsonUtils.object2Json(opServiceImpl.queryOpTypeObj()));


    }
}
