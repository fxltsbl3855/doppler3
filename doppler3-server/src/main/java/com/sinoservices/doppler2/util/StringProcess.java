package com.sinoservices.doppler2.util;

import com.sinoservices.util.NumberUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class StringProcess {
    private static final String isIp = "\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}.\\d{0,3}";
    private static final Pattern patternIsIp = Pattern.compile(isIp);

    public static boolean isIp(String str){
        if(str == null || "".equals(str.trim())){
            return false;
        }
        Matcher matcher = patternIsIp.matcher(str.trim());
        return matcher.matches();
    }

    public static Map<String,Integer> getClusterAddr(String str, String defaultIp, int defaultPort){
        Map<String,Integer> map = new HashMap<String, Integer>(1);
        if(str == null || "".equals(str.trim()) ||"null".equals(str.trim())){
            map.put(defaultIp,defaultPort);
            return map;
        }
        StringTokenizer st = new StringTokenizer(str,",");
        while(st.hasMoreTokens()){
            String addr = st.nextToken();
            if(addr != null && !"".equals(addr.trim()) && !"null".equals(addr.trim())){
                String[] elment = addr.split(":");
                if(elment == null || elment.length!=2){
                    continue;
                }
                if(elment[0] == null || !isIp(elment[0].trim())){
                    continue;
                }
                if(elment[1] == null ){
                    continue;
                }
                int port = NumberUtil.formatNumber(elment[1].trim(),-1);
                if( port <= 0 || port > 65535 ){
                    continue;
                }
                map.put(elment[0].trim(),port);
            }
        }
        if(map.size() ==0){
            map.put(defaultIp,defaultPort);
        }
        return map;
    }

}
