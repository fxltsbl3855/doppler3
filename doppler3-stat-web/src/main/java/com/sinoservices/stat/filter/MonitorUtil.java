package com.sinoservices.stat.filter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class MonitorUtil {

    static List<String> usernameList = new ArrayList<String>(4);


    public static String getHeaderInfo(HttpServletRequest request){
        Enumeration<String> headerName = request.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        for(Enumeration e = headerName;e.hasMoreElements();){
            String thisName=e.nextElement().toString();
            sb.append(thisName);
            sb.append("=");
            sb.append(request.getHeader(thisName));
            sb.append("#");
        }
        return sb.toString();
    }

    /**
     * 获取路径的app name
     * //D:\bak\study_bak\Tomcat-6.0.37-32bit\Tomcat-6.0.37-32bit\webapps\WebRoot\ 为   WebRoot
     * D:\bak\study_bak\Tomcat-6.0.37-32bit\Tomcat-6.0.37-32bit\webapps\doppler-web-1.0 为    doppler-web-1.0
     *
     * @param str
     * @return
     */
    public static String getDeployPath(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        if (str.endsWith("\\") || str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        if (str.indexOf("\\") >= 0) {
            int from = str.lastIndexOf("\\");
            str = str.substring(from + 1);
        } else if (str.indexOf("/") >= 0) {
            int from = str.lastIndexOf("/");
            str = str.substring(from + 1);
        }
        return str;
    }

    /**
     * 去掉相对url后的/，去掉应用名
     *
     * @param reqPath
     * @param appName
     * @return
     */
    public static String getBrifePath(String reqPath, String appName) {
        if (reqPath == null || "".equals(reqPath.trim())) {
            return "";
        }
        if (reqPath.indexOf("？") > 0) {
            reqPath = reqPath.substring(0, reqPath.indexOf("？"));
        }
        if (reqPath.indexOf("?") > 0) {
            reqPath = reqPath.substring(0, reqPath.indexOf("?"));
        }
        if (reqPath.startsWith(appName)) {
            reqPath = reqPath.replaceFirst(appName, "");
        }
        if (reqPath.startsWith("/" + appName)) {
            reqPath = reqPath.replaceFirst("/" + appName, "");
        }
        if (reqPath.endsWith("/") && reqPath.length() > 1) {
            reqPath = reqPath.substring(0, reqPath.length() - 1);
        }
        return reqPath;
    }

    public static String toStr(Map<String, String[]> paramMap) {
        if (paramMap == null || paramMap.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> temp : paramMap.entrySet()) {
            sb.append(temp.getKey());
            sb.append("=");
            sb.append(temp.getValue()[0]);
            sb.append("#");
        }
        return sb.toString();
    }

    public static void main(String[] a){
        String userAgent = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)";
        String findLogInfo = "(MSIE \\d\\.\\d)";
        Pattern patternLogInfo = Pattern.compile(findLogInfo);
        Matcher matcher = patternLogInfo.matcher(userAgent);
        while(matcher.find()) {
            String res = matcher.group();
            System.out.println(res);
        }
    }

    public static String getUserIp(HttpServletRequest request) {
        String ip = request.getRemoteHost();
        //TODO 完善获取真实client ip
        return ip;
    }

    public static String getUsername(HttpSession session) {
        if(session == null){
            return "";
        }
        for(String username : usernameList){
            Object tmp = session.getAttribute(username);
            if(tmp != null){
                return tmp.toString();
            }
            continue;
        }
        return "";
    }

    static {
        usernameList.add("username");
        usernameList.add("user");
        usernameList.add("userName");
        usernameList.add("user_name");
    }
}