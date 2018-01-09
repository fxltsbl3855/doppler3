package com.sinoservices.parser.file;

import com.sinoservices.parser.config.UpMessageConfig;
import com.sinoservices.parser.es.JsonUtil;
import com.sinoservices.parser.es.entity.ExEntity;
import com.sinoservices.parser.es.entity.LogEntity;
import com.sinoservices.util.JsonUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ExProcess {

    //private static final String findProblem = "at (([a-z]+\\.)+[A-Z]{1}[a-zA-Z]*)\\.([a-zA-Z]+)\\S+java:([0-9]+)";
    private static final String findProblem = "at (([a-z]+\\.)+[A-Z]{1}[a-zA-Z]*)\\.([a-zA-Z]+)\\S+:([0-9]+)";
    private static final Pattern patternProblem = Pattern.compile(findProblem);

    private static final String CLASS_NAME = "class_name";
    private static final String METHOD_NAME = "method_name";
    private static final String LINE_NUM = "line_num";

    private static String exMatchWhiteList = null;

    private static void initWhitelist(){
        if(exMatchWhiteList != null){
            return;
        }
        if(UpMessageConfig.ex_match_whitelist == null || "".equals(UpMessageConfig.ex_match_whitelist.trim())){
            exMatchWhiteList = "com.sinoservices";
            return;
        }
        exMatchWhiteList = UpMessageConfig.ex_match_whitelist.trim();
    }

    public static ExEntity parserEx(LogEntity logEntity) {
        if(logEntity.getOriginal().indexOf('\n') == 0){
            return null;
        }
        initWhitelist();

        StringTokenizer st = new StringTokenizer(logEntity.getOriginal(),"\n");
        String exName = null;
        String problemLine = null;
        String firstLine = "";
        while(st.hasMoreTokens()){
            String tmp = st.nextToken();
            if(exName == null || "".equals(exName.trim())) {
                exName = RegexMatches.matchEx(tmp);
            }
            if(firstLine == null || "".equals(firstLine)){
                if(tmp.startsWith("\tat ")){
                    firstLine = tmp;
                }
            }
            if(exName != null && !"".equals(exName.trim())){
                if(tmp.startsWith("\tat " + exMatchWhiteList)){
                    problemLine = tmp;
                    break;
                }
            }
        }
        if(exName == null || "".equals(exName.trim())) {
            return null;
        }
        logEntity.setEx(exName);
        problemLine = problemLine!=null?problemLine:firstLine;
        Map<String,String> pInfo = getProblemInfo(problemLine);
        ExEntity exEntity = fillProperty(logEntity,exName,pInfo);
        return exEntity;
    }

    private static ExEntity fillProperty(LogEntity logEntity,String exName, Map<String, String> pInfo) {
        ExEntity exEntity = new ExEntity(exName,pInfo.get(CLASS_NAME),pInfo.get(METHOD_NAME),pInfo.get(LINE_NUM));
        exEntity.setTimestamp(logEntity.getTimestamp());
        exEntity.setAppName(logEntity.getAppName());
        exEntity.setId(logEntity.getId());
        exEntity.setHost(logEntity.getHost());
        exEntity.setOriginal(logEntity.getOriginal());
        exEntity.setPosition(logEntity.getPosition());
        exEntity.setFile(logEntity.getFile());
        return exEntity;
    }

    public static Map<String,String> getProblemInfo(String problemLine){
        Matcher matcher = patternProblem.matcher(problemLine);
        List<String> matchesList = new ArrayList<String>(1);
        while(matcher.find()) {
            for(int i=1;i<=matcher.groupCount();i++){
                if( matcher.group(i)==null) {
                    continue;
                }
                matchesList.add(matcher.group(i));
            }
        }

        Map<String,String> map = new HashMap<String, String>(4);
        if(matchesList.size() == 0){
            return map;
        }
        map.put(CLASS_NAME,get(matchesList,0));
        map.put(METHOD_NAME,get(matchesList,2));
        map.put(LINE_NUM,get(matchesList,3));
        return map;
    }

    public static String get(List<String> matchesList,int index){
        try {
            return matchesList.get(index);
        }catch (Exception e){
            return "";
        }
    }

    public static void main(String[] args) {
//        String problemLine = "org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.NumberFormatException\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:973) ~[spring-webmvc-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:863) ~[spring-webmvc-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:648) ~[servlet-api.jar:na]\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:837) ~[spring-webmvc-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:729) ~[servlet-api.jar:na]\n\tat com.sinoservices.xframework.core.web.dispatches.SpringDispatcher.dispatch(SourceFile:50) ~[tangram-core-5.0.22.jar:na]\n\tat com.sinoservices.xframework.core.web.WebFilter.dispatchModule(SourceFile:256) ~[tangram-core-5.0.22.jar:na]\n\tat com.sinoservices.xframework.core.web.WebFilter.intercept(SourceFile:229) ~[tangram-core-5.0.22.jar:na]\n\tat com.sinoservices.xframework.core.web.c.intercept(SourceFile:317) ~[tangram-core-5.0.22.jar:na]\n\tat com.sinoservices.xframework.core.web.interceptors.a.doFilter(SourceFile:69) ~[tangram-core-5.0.22.jar:na]\n\tat com.sinoservices.xframework.core.web.security.SecurityFilter.doFilter(SourceFile:99) ~[tangram-core-5.0.22.jar:na]\n\tat com.sinoservices.xframework.core.web.interceptors.FilterWebInterceptor.intercept(SourceFile:55) ~[tangram-core-5.0.22.jar:na]\n\tat com.sinoservices.xframework.core.web.c.intercept(SourceFile:317) ~[tangram-core-5.0.22.jar:na]\n\tat com.sinoservices.xframework.core.web.WebFilter.doFilter(SourceFile:207) ~[tangram-core-5.0.22.jar:na]\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:240) [catalina.jar:8.0.39]\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:207) [catalina.jar:8.0.39]\n\tat com.sinoservices.stat.filter.MonitorFilter.doFilter(MonitorFilter.java:76) [doppler3-stat-web-3.1.jar:na]\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:240) [catalina.jar:8.0.39]\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:207) [catalina.jar:8.0.39]\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:88) [spring-web-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:108) [spring-web-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:240) [catalina.jar:8.0.39]\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:207) [catalina.jar:8.0.39]\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:212) [catalina.jar:8.0.39]\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:106) [catalina.jar:8.0.39]\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:614) [catalina.jar:8.0.39]\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:141) [catalina.jar:8.0.39]\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:79) [catalina.jar:8.0.39]\n\tat org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:616) [catalina.jar:8.0.39]\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:88) [catalina.jar:8.0.39]\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:509) [catalina.jar:8.0.39]\n\tat org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1104) [tomcat-coyote.jar:8.0.39]\n\tat org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:684) [tomcat-coyote.jar:8.0.39]\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1520) [tomcat-coyote.jar:8.0.39]\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.run(NioEndpoint.java:1476) [tomcat-coyote.jar:8.0.39]\n\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [na:1.8.0_121]\n\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [na:1.8.0_121]\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-util.jar:8.0.39]\n\tat java.lang.Thread.run(Thread.java:745) [na:1.8.0_121]\\nCaused by: java.lang.NumberFormatException: null\n\tat java.math.BigDecimal.(BigDecimal.java:498) ~[na:1.8.0_121]\n\tat java.math.BigDecimal.(BigDecimal.java:383) ~[na:1.8.0_121]\n\tat java.math.BigDecimal.(BigDecimal.java:806) ~[na:1.8.0_121]\n\tat com.sinoservices.monweb.gps.controllers.GpsController.getDampnessList(GpsController.java:1015) ~[classes/:na]\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_121]\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_121]\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_121]\n\tat java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_121]\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:215) ~[spring-web-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:132) ~[spring-web-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:104) ~[spring-webmvc-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandleMethod(RequestMappingHandlerAdapter.java:749) ~[spring-webmvc-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:690) ~[spring-webmvc-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:83) ~[spring-webmvc-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:945) ~[spring-webmvc-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:876) ~[spring-webmvc-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:961) ~[spring-webmvc-4.0.2.RELEASE.jar:4.0.2.RELEASE]\n        ... 38 common frames omitted";
//        LogEntity logEntity = new LogEntity();
//        logEntity.setId("1");
//        logEntity.setTimestamp(new Date());
//        logEntity.setOriginal(problemLine);
//        logEntity.setPosition(2);
//        logEntity.setFile("a.txt");
//        logEntity.setAppName("app1");
//        logEntity.setHost("192.168.0.1");
//        logEntity.setLogLevel("INFO");
//
//        ExEntity ee = parserEx(logEntity);

        String ss = "at com.sinoservices.bms.facade.mobile.impl.CarriageMobileFacadeImpl$$EnhancerBySpringCGLIB$$c9202dc8.listScanForSignDetail(<generated>)";
        Map<String,String> cc = getProblemInfo(ss);
        System.out.println(JsonUtils.object2Json(cc));

//         String aa = "dsadsa\ndsa\nsd";
//
//        System.out.println(aa.indexOf('\n'));

    }
}
