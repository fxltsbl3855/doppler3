package com.sinoservices.doppler2.facade.check;

import com.sinoservices.doppler2.es.entity.MustRangeTimestamp;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.StringUtil;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ParamCheck {

    public static void main(String[] a){
//        System.out.print(checkTime("2016-8-8"));
    }

    public static boolean checkTime(String str){
        if(DateUtil.stirng2Date(str,"yyyy-MM-dd HH:mm:ss")==null){
            return false;
        }
        return true;
    }

    public static boolean checkDate(String str){
        if(DateUtil.stirng2Date(str,"yyyy-MM-dd")==null){
            return false;
        }
        return true;
    }

    public static boolean checkDate(String str,String format){
        if(DateUtil.stirng2Date(str,format)==null){
            return false;
        }
        return true;
    }

    public static String change(String str){
        if(StringUtil.isBlank(str)){
            return str;
        }
        if("-1".equals(str.trim())){
            return "";
        }
        return str;
    }
}
