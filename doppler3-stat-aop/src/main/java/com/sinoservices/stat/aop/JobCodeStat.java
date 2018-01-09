package com.sinoservices.stat.aop;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * User: charlie
 * Time: 18:34
 * Timers
 */
public class JobCodeStat {

    private static final ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<Map<String,Object>>();
    private static final String START_TIME = "START_TIME";
    private static final String JOB_ID = "JOB_ID";
    private static final String JOB_PARAM = "JOB_PARAM";

    public static String start(String param){
        Map<String,Object> map = new HashMap<String, Object>(2);
        long startTime = System.currentTimeMillis();
        map.put(START_TIME,startTime);
        map.put(JOB_ID, AOPUtil.getRandom(startTime));
        map.put(JOB_PARAM, param);
        threadLocal.set(map);
        return getStr(AOPUtil.ACTION_TYPE_JOB_IN,"");
    }

    public static String endOk(){
        return getStr(AOPUtil.ACTION_TYPE_JOB_OUT,"SUCCESS");
    }

    public static String endError(){
        return getStr(AOPUtil.ACTION_TYPE_JOB_OUT,"EXCEPTION");
    }

    private static String getStr(String action ,String result){
        Map<String,Object> map = threadLocal.get();
        long startTime = -1L;
        String jobId = "";
        String param = "";
        try {
            startTime = (Long) map.get(START_TIME);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            jobId = (String) map.get(JOB_ID);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            param = (String) map.get(JOB_PARAM);
        }catch (Exception e){
            e.printStackTrace();
        }
        String[] msg = getStackFrame(-3);
        String className = msg[0];
        String method = msg[1];
        long time = System.currentTimeMillis()-startTime;

        StringBuilder sb = new StringBuilder();
        sb.append("[stat] @action=");
        sb.append(action);
        sb.append(" @className=");
        sb.append(className);
        sb.append(" @method=");
        sb.append(method);
        sb.append(" @result=");
        sb.append(result);
        sb.append(" @param=");
        sb.append(param);
        sb.append(" @jobId=");
        sb.append(jobId);
        sb.append(" @time=");
        sb.append(time);
        sb.append("ms");
        return sb.toString();
    }

    private static String[] getStackFrame(int index){
        index = Math.abs(index);
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        String[] str = new String[2];

        if(index >= stackElements.length){
            str[0]="null";
            str[1]="null";
            return str;
        }
        if (stackElements != null) {
            str[0]=stackElements[index].getClassName();
            str[1]=stackElements[index].getMethodName();
        }
        return str;
    }


}
