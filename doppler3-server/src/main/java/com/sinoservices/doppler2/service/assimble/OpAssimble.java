package com.sinoservices.doppler2.service.assimble;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.bo.OpBo;
import com.sinoservices.doppler2.bo.OpDetailBo;
import com.sinoservices.doppler2.bo.QueryBo;
import com.sinoservices.doppler2.es.entity.LogRecordEntity;
import com.sinoservices.doppler2.es.entity.OpEntity;
import com.sinoservices.util.DateUtil;
import org.elasticsearch.index.get.GetField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class OpAssimble {
    public static OpDetailBo assimbleOpDetail(Map<String, Object> resMap) {
        OpDetailBo opDetailBo = new OpDetailBo();
        opDetailBo.setId(resMap.get("id").toString());
        opDetailBo.setTimestamp(DateUtil.utcStrToLocalDate(resMap.get(Constant.OpStat.FIELD_TIMESTAMP).toString()));
        opDetailBo.setAppName(resMap.get(Constant.OpStat.FIELD_APP_NAME).toString());
        opDetailBo.setHost(resMap.get(Constant.OpStat.FIELD_HOST).toString());
        opDetailBo.setOpType(resMap.get(Constant.OpStat.FIELD_OP_TYPE).toString());
        opDetailBo.setOpObj(resMap.get(Constant.OpStat.FIELD_OP_OBJ).toString());
        opDetailBo.setParam(resMap.get(Constant.OpStat.FIELD_PARAM).toString());
        opDetailBo.setBusParam(resMap.get(Constant.OpStat.FIELD_BUS_PARAM).toString());
        opDetailBo.setOpRes(resMap.get(Constant.OpStat.FIELD_RESULT).toString());
        return opDetailBo;
    }

    public static String reGroupBusParam(String busField) {
        if(Assimble.isEmpty(busField,null)){
            return null;
        }
        busField = busField.replaceAll("=","_");
        return busField;
    }

    public static List<OpBo> translate(List<OpEntity> opList) {
        if(Assimble.isEmpty(opList)){
            return Collections.EMPTY_LIST;
        }
        List<OpBo> resList = new ArrayList<OpBo>(opList.size());
        for(OpEntity opEntity : opList){
            resList.add(new OpBo(opEntity.getId(),opEntity.getTimestamp(),opEntity.getAppName(),opEntity.getHost(),
                    opEntity.getOperType(),opEntity.getOperObj(),opEntity.getParam(),busParamToDisplay(opEntity.getBusParam()),
                    opEntity.getResult(),opEntity.getTime()));
        }
        return resList;
    }

    public static String busParamToDisplay(String busParam){
        if(busParam==null || "".equals(busParam.trim())){
            return busParam;
        }
        return busParam.replaceAll("_","=");
    }
}
