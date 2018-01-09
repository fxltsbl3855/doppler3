package com.sinoservices.parser.util;

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

    private static final String findDay = "\\.(\\d{4}-\\d{2}-\\d{2})";
    private static final Pattern patternDay = Pattern.compile(findDay);

    private static final String findIp = "\\.(\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}.\\d{0,3})";
    private static final Pattern patternIp = Pattern.compile(findIp);

    private static final String isIp = "\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}.\\d{0,3}";
    private static final Pattern patternIsIp = Pattern.compile(isIp);

    public static String getFileName(String source) {
        if(source == null || "".equals(source) ){
            return "";
        }
        int point1 = source.lastIndexOf("\\");
        int point2 = source.lastIndexOf("/");
        return source.substring(Math.max(point1,point2)+1);
    }

    public static String getAppName(String fileName){
        return  fileName.substring(0,fileName.indexOf(".")).trim();
    }

    public static String getDay(String fileName){
        Matcher matcher = patternDay.matcher(fileName);
        if(matcher.find()) {
            String day = matcher.group(1);
            if(day!=null)
                return day;
        }
        return "0000-00-00";
    }

    public static String getIp(String fileName){
        Matcher matcher = patternIp.matcher(fileName);
        if(matcher.find()) {
            String day = matcher.group(1);
            if(day!=null)
                return day;
        }
        return "0.0.0.0";
    }

    public static boolean isIp(String str){
        if(str == null || "".equals(str.trim())){
            return false;
        }
        Matcher matcher = patternIsIp.matcher(str.trim());
        return matcher.matches();
    }

    public static Map<String,Integer> getClusterAddr(String str,String defaultIp,int defaultPort){
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

    public static void main( String args[] ){
//        System.out.println(getClusterAddr("192.168.0.88:9300,192.168.0.89:9300"));
    }
}
