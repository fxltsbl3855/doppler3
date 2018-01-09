package com.sinoservices.stat.aop;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class AOPUtil {

    public static final String ACTION_TYPE_REQ = "REQ";
    public static final String ACTION_TYPE_JOB = "JOB";

    public static final String ACTION_TYPE_JOB_IN = "ACTION_JOB_IN";
    public static final String ACTION_TYPE_JOB_OUT = "ACTION_JOB_OUT";

    public static final String PROCESS_RES = "processRes";
    public static final String BUS_PROPERTY = "busProperty";


    /**
     * 7位随机数
     * @return
     */
    public static String getRandom(long startTime) {
        int ss = (int)((Math.random()*9+1)*10000);
        return startTime+""+ss;

    }

    public static void main(String[] a){
        String[] ss = parserOper("SER");
        System.out.println(ss[0]+"_"+ss[1]);
    }

    public static String[] parserOper(String method) {
        String[] res = {"",""};
        if(method == null || "".equals(method.trim()) || method.length()<4){
            return res;
        }
        int upper1st = -1;
        int upper2nd = -1;
        int i = 0;
        while(i < method.length()){
            char chr = method.charAt(i);
            if(Character.isUpperCase(chr)&&upper1st==-1){
                upper1st = i;
            }else if(Character.isUpperCase(chr)&&upper2nd==-1){
                upper2nd = i;
                break;
            }
            i++;
        }
        upper1st = upper1st ==-1?method.length():upper1st;
        upper2nd = upper2nd ==-1?method.length():upper2nd;
        res[0] = method.substring(0,upper1st);
        res[1] = method.substring(upper1st,upper2nd);
        if(res[0].length()<2 || res[1].length()<2){
            return new String[]{"",""};
        }
        return res;
    }

    public static String getValueFromThreadlocal(ThreadLocal<Map> threadLocal,String key) {
        Map map = threadLocal.get();
        return map.get(key) ==null?"":map.get(key).toString();
    }
}
