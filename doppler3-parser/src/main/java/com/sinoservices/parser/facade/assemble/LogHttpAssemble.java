package com.sinoservices.parser.facade.assemble;

import com.sinoservices.doppler2.bo.LogHttpResBo;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class LogHttpAssemble {
    public static LogHttpResBo getSucc(String opId,String desc) {
        LogHttpResBo logHttpResBo = new LogHttpResBo();
        logHttpResBo.setOpId(opId);
        logHttpResBo.setRes(1);
        logHttpResBo.setDesc(desc);
        return logHttpResBo;
    }

    public static LogHttpResBo getError(String opId,String errorInfo) {
        LogHttpResBo logHttpResBo = new LogHttpResBo();
        logHttpResBo.setOpId(opId);
        logHttpResBo.setRes(-1);
        logHttpResBo.setDesc(errorInfo);
        return logHttpResBo;
    }

    public static LogHttpResBo getPartError(String opId,String errorInfo) {
        LogHttpResBo logHttpResBo = new LogHttpResBo();
        logHttpResBo.setOpId(opId);
        logHttpResBo.setRes(-2);
        logHttpResBo.setDesc(errorInfo);
        return logHttpResBo;
    }

    public static String getDesc(Map<String,Integer> resMap,int errorNum,String succ,int succNum) {
        StringBuilder sb = new StringBuilder();
        sb.append("fail size = ");
        sb.append(errorNum);
        sb.append(" , succ size = ");
        sb.append(succNum);
        sb.append(" , succ detail : ");
        sb.append(succ);
        if(resMap != null && resMap.size() > 0){
            sb.append(",fail detail : ");
            for(Map.Entry<String,Integer> entry : resMap.entrySet()){
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue()+",");
            }
        }
        return sb.toString();
    }

    public static void putErrorToMap(Map<String,Integer> resMap,String message) {
        Integer ss = resMap.get(message);
        if(ss == null){
            ss = 0;
        }else{
            ss++;
        }
        resMap.put(message,ss);
    }
}
