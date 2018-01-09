package com.sinoservices.parser.file;

import com.sinoservices.parser.es.entity.LogEntity;
import com.sinoservices.parser.es.entity.OpEntity;
import com.sinoservices.parser.es.entity.StatEntity;
import com.sinoservices.parser.es.entity.StatJobEntity;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class RegexMatches{
    private static final Logger LOG = LoggerFactory.getLogger(RegexMatches.class);

//    private static final String find = "(\\d{0,2}:\\d{0,2}:\\d{0,2}\\.\\d{0,3})|([a-zA-Z0-9]*)\\[([a-zA-Z0-9]*),[0-9]*\\]|@[a-zA-Z]*=([^@]*)";
//    private static final Pattern pattern = Pattern.compile(find);

    //匹配出 时间 和 logLevel
    private static final String findLogInfo = "(\\d{2}:\\d{2}:\\d{2}\\.\\d{3})|(INFO|DEBUG|ERROR|WARN|TRACE|FETAL)";
    private static final Pattern patternLogInfo = Pattern.compile(findLogInfo);

    //匹配@action ，@className，@method，@result，@time，@ex，@msg
    private static final String findParam = "(@action=ACTION_REQ_OUT)|(@className=[^@]*)|(@method=[^@]*)|(@result=[^@]*)|(@param=[^@]*)|(@time=\\d{0,9})*";
    private static final Pattern patternParam = Pattern.compile(findParam);

    //匹配@action ，@className，@method，@result，@param, @time
    private static final String findJobParam = "(@action=ACTION_JOB_(IN|OUT))|(@className=[^@]*)|(@method=[^@]*)|(@result=[^@]*)|(@param=[^@]*)|(@jobId=[^@]*)|(@time=\\d{0,9})*";
    private static final Pattern patternJobParam = Pattern.compile(findJobParam);

    //匹配op @action ，@className，@method，@result，@param, @time
    private static final String findOpParam = "(@action=ACTION_OPER_(IN|OUT))|(@className=[^@]*)|(@method=[^@]*)|(@opType=[^@]*)|(@operObj=[^@]*)|(@result=[^@]*)|(@param=[^@]*)|(@busParam=[^@]*)|(@time=\\d{0,9})*";
    private static final Pattern patternOpParam = Pattern.compile(findOpParam);

    //web系统特有字段;匹配@username ，@clientIp，@brower，@header
    private static final String findParamWeb = "(@username=[^@]*)|(@clientIp=[^@]*)|(@brower=[^@]*)|(@header=[^@]*)|(@code=[^@]*)*";
    private static final Pattern patternParamWeb = Pattern.compile(findParamWeb);

    //匹配出异常
    private static final String findEx = "[a-z]*\\.{1,20}([a-zA-Z]*Exception):";
    private static final Pattern patternEx = Pattern.compile(findEx);


    public static void main( String args[] ){
//        String ss = "Caused by: java.lang.IllegalArgumentException: failed to parse ip [?ˉ<86>??<81>], not a valid ip address";
//        String ss2 = "14:12:18.241 [DubboServerHandler-172.16.5.49:9020-thread-200] INFO  c.s.doppler2.facade.impl.ErrorFacadeImpl[queryErrorById,65] -  AsaException: @module= @action= @operator= @result= @host=172.16.5.49 @msg=queryErrorById invoked end,";
//        String ss3 = "13:49:31.766 [http-8088-1] INFO  c.sinoservices.stat.filter.MonitorFilter - [stat] @action=ACTION_REQ_OUT @className=/test/go.shtml @method= @result=SUCCESS @param=s=3#a=18# @username=admin @clientIp=192.168.0.254 @brower=Chrome 52.0.2743.82 @header=host=192.168.0.88:8088#connection=keep-alive#cache-control=max-age=0#upgrade-insecure-requests=1#user-agent=Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36#accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8#accept-encoding=gzip, deflate, sdch#accept-language=zh-CN,zh;q=0.8,en;q=0.6#cookie=JSESSIONID=F6AF2041F965D9897FADB7DE8C63B85E# @code=404 @time=63ms";
////        StatEntity aa = new StatEntity();
////        boolean res = parseStatWebParam(ss3,aa);
//
//        StatJobEntity aa = parseJobParam(ss3);
//        LogEntity logEntity = new LogEntity();
//        logEntity.setOriginal(ss3);
//        StatEntity statJobEntity = RegexMatches.parseStatLineMsg("ruite-web.2011-1-1.log",123,logEntity);
//        System.out.print("res="+","+JsonUtils.object2Json(statJobEntity));

        List<String> matchesList = new ArrayList<String>(2);
        matchesList.add("bb");
        matchesList.add(0,"aa");

        System.out.println(matchesList.get(1));


    }

    /**
     * 解析一行日志记录
     * @param fileName  日志记录从哪个文件收集过来的；此字段作为es日志id的一部分，便于追踪记录来源
     * @param lineNumber    日志行号，或者日志在文件中的offset
     * @param appName   是哪个应用产生的日志；
     * @param host      是在哪个IP产生的日志
     * @param lineMsg   日志记录字符串
     * @param day       日志是哪一天产生的；日志记录中只有时分秒
     * @param proviousDate  前一行日志的时间，（异常行没有时间信息，用这个)
     * @return
     */
    public static LogEntity parseLineMsg(String fileName, long lineNumber, String appName, String host, String lineMsg,String day,Date proviousDate){
        LogEntity logEntity = new LogEntity(fileName,lineNumber,appName,host,lineMsg);
        boolean infoMatched = RegexMatches.parseTimeAndLoglevel(day,lineMsg,logEntity);

        //if(!infoMatched){
            //RegexMatches.parseEx(lineMsg,logEntity,proviousDate);
        //}
        return logEntity;
    }


    public static OpEntity parseOpStatLineMsg(String fileName, long offset, LogEntity logEntity) {
        OpEntity opEntity = RegexMatches.parseOpParam(logEntity.getOriginal());
        opEntity.setBase(logEntity.getId(),logEntity.getTimestamp(),logEntity.getAppName(),logEntity.getHost(),
                logEntity.getOriginal(),logEntity.getPosition(),logEntity.getFile());
        return opEntity;
    }

    /**
     * 解析统计日志记录
     * @param fileName 日志记录从哪个文件收集过来的；此字段作为es日志id的一部分，便于追踪记录来源
     * @param lineNumber 日志行号，或者日志在文件中的offset
     * @param logEntity 普通日志对象
     * @return
     */
    public static StatEntity parseStatLineMsg(String fileName, long lineNumber, LogEntity logEntity){
        StatEntity statEntity = RegexMatches.parseStatParam(logEntity.getOriginal());
        if(statEntity == null){
            return null;
        }
        //statEntity != null 表示当前行是统计日志
        boolean isWebApp = RegexMatches.parseStatWebParam(logEntity.getOriginal(), statEntity); //如果是统计日志，再匹配是否是web应用
        statEntity.setBase(fileName,lineNumber,logEntity.getAppName(),logEntity.getHost(),logEntity.getTimestamp(),isWebApp);
        return statEntity;
    }

    public static StatJobEntity parseJobLineMsg(String fileName, long lineNumber, LogEntity logEntity){
        StatJobEntity jobEntity = RegexMatches.parseJobParam(logEntity.getOriginal());
        if(jobEntity == null){
            return null;
        }
        //statEntity != null 表示当前行是统计Job
        jobEntity.setBase(fileName,lineNumber,logEntity.getAppName(),logEntity.getHost(),logEntity.getTimestamp());
        return jobEntity;
    }

    public static StatJobEntity parseJobParam(String line){
        List<String> matchesList = new ArrayList<String>(6);
        Matcher matcher = patternJobParam.matcher(line);
        boolean jobLog = false;
        while(matcher.find()) {
            for(int i=1;i<=matcher.groupCount();i++){
                if( matcher.group(i)==null) {
                    continue;
                }
                if("@action=ACTION_JOB_IN".equals(matcher.group(i)) || "@action=ACTION_JOB_OUT".equals(matcher.group(i))){
                    jobLog = true;
                }
                matchesList.add(matcher.group(i));
            }
        }
        if(jobLog) {
            StatJobEntity statJobEntity = new StatJobEntity();
            fillStatParam(statJobEntity, matchesList);
            return statJobEntity;
        }
        return null;
    }

    public static OpEntity parseOpParam(String line){
        List<String> matchesList = new ArrayList<String>(6);
        Matcher matcher = patternOpParam.matcher(line);
        boolean opLog = false;
        while(matcher.find()) {
            for(int i=1;i<=matcher.groupCount();i++){
                if( matcher.group(i)==null) {
                    continue;
                }
                if("@action=ACTION_OPER_IN".equals(matcher.group(i)) || "@action=ACTION_OPER_OUT".equals(matcher.group(i))){
                    opLog = true;
                }
                matchesList.add(matcher.group(i));
            }
        }
        if(opLog) {
            OpEntity opEntity = new OpEntity();
            fillOpStatParam(opEntity, matchesList);
            return opEntity;
        }
        return null;
    }

    public static StatEntity parseStatParam(String line){
        List<String> matchesList = null;
        Matcher matcher = patternParam.matcher(line);
        boolean statLog = false;
        while(matcher.find()) {
            for(int i=1;i<=matcher.groupCount();i++){
                if( matcher.group(i)==null) {
                    continue;
                }
                if("@action=ACTION_REQ_OUT".equals(matcher.group(i))){
                    statLog = true;
                }
                if(matchesList == null) matchesList = new ArrayList<String>(6);
                matchesList.add(matcher.group(i));
            }
        }
        if(statLog) {
            StatEntity statEntity = new StatEntity();
            fillStatParam(statEntity, matchesList);
            return statEntity;
        }
        return null;
    }

    public static boolean parseStatWebParam(String line,StatEntity statEntity){
        List<String> matchesList = null;
        Matcher matcher = patternParamWeb.matcher(line);
        boolean statWebLog = false;
        while(matcher.find()) {
            for(int i=1;i<=matcher.groupCount();i++){
                if( matcher.group(i)==null) {
                    continue;
                }
                statWebLog = true;
                if(matchesList==null) matchesList = new ArrayList<String>(5);
                matchesList.add(matcher.group(i));
            }
        }
        if(statWebLog) {
            fillStatWebParam(statEntity, matchesList);
            return true;
        }
        return false;
    }

    private static void fillStatWebParam(StatEntity statEntity,List<String> matches){
        if(matches == null || matches.size() ==0){
            return;
        }
        for(String str : matches){
            if(str.startsWith("@username")){
                statEntity.setUsername(str.substring(str.indexOf("@username")+10).trim());
            }else if(str.startsWith("@clientIp")){
                statEntity.setClientIp(str.substring(str.indexOf("@clientIp")+10).trim());
            }else if(str.startsWith("@brower")){
                statEntity.setBrower(str.substring(str.indexOf("@brower")+8).trim());
            }else if(str.startsWith("@header")){
                statEntity.setHeader(str.substring(str.indexOf("@header")+8).trim());
            }else if(str.startsWith("@code")){
                statEntity.setCode(NumberUtil.formatNumber(str.substring(str.indexOf("@code")+6).trim(),-1));
            }
        }
        matches.clear();
        matches = null;
    }

    private static void fillStatParam(StatJobEntity jobEntity,List<String> matches){
        if(matches == null || matches.size() ==0){
            return;
        }
        for(String str : matches){
            if(str.startsWith("@action")){
                jobEntity.setAction(str.substring(str.indexOf("@action")+8).trim());
            }else if(str.startsWith("@className")){
                jobEntity.setClassName(str.substring(str.indexOf("@className")+11).trim());
            }else if(str.startsWith("@method")){
                jobEntity.setMethod(str.substring(str.indexOf("@method")+8).trim());
            }else if(str.startsWith("@result")){
                jobEntity.setResult(str.substring(str.indexOf("@result")+8).trim());
            }else if(str.startsWith("@param")){
                jobEntity.setParam(str.substring(str.indexOf("@param")+7).trim());
            }else if(str.startsWith("@jobId")){
                jobEntity.setJobId(str.substring(str.indexOf("@jobId")+7).trim());
            }else if(str.startsWith("@time")){
                jobEntity.setTime(NumberUtil.formatLong(str.substring(str.indexOf("@time")+6).trim(),0L));
            }
        }
        matches.clear();
        matches = null;
    }

    private static void fillOpStatParam(OpEntity opEntity,List<String> matches){
        if(matches == null || matches.size() ==0){
            return;
        }
        for(String str : matches){
            if(str.startsWith("@action")){
                opEntity.setAction(str.substring(str.indexOf("@action")+8).trim());
            }else if(str.startsWith("@className")){
                opEntity.setClassName(str.substring(str.indexOf("@className")+11).trim());
            }else if(str.startsWith("@method")){
                opEntity.setMethod(str.substring(str.indexOf("@method")+8).trim());
            }else if(str.startsWith("@opType")){
                opEntity.setOperType(str.substring(str.indexOf("@opType")+8).trim());
            }else if(str.startsWith("@opObj")){
                opEntity.setOperObj(str.substring(str.indexOf("@opObj")+7).trim());
            }else if(str.startsWith("@result")){
                opEntity.setResult(str.substring(str.indexOf("@result")+8).trim());
            }else if(str.startsWith("@param")){
                opEntity.setParam(str.substring(str.indexOf("@param")+7).trim());
            }else if(str.startsWith("@busParam")){
                opEntity.setBusParam(str.substring(str.indexOf("@busParam")+10).trim());
            }else if(str.startsWith("@time")){
                opEntity.setTime(NumberUtil.formatLong(str.substring(str.indexOf("@time")+6).trim(),0L));
            }
        }
        matches.clear();
        matches = null;
    }

    private static void fillStatParam(StatEntity statEntity,List<String> matches){
        if(matches == null || matches.size() ==0){
            return;
        }
        for(String str : matches){
            if(str.startsWith("@action")){
//                statEntity.seta(str.substring(str.indexOf("@module")+8).trim());
            }else if(str.startsWith("@className")){
                statEntity.setClassName(str.substring(str.indexOf("@className")+11).trim());
            }else if(str.startsWith("@method")){
                statEntity.setMethod(str.substring(str.indexOf("@method")+8).trim());
            }else if(str.startsWith("@result")){
                statEntity.setResult(str.substring(str.indexOf("@result")+8).trim());
            }else if(str.startsWith("@time")){
                statEntity.setTime(NumberUtil.formatLong(str.substring(str.indexOf("@time")+6).trim(),0L));
            }else if(str.startsWith("@param")){
                statEntity.setParam(str.substring(str.indexOf("@param")+7).trim());
            }
        }
        matches.clear();
        matches = null;
    }

    public static boolean parseEx(String line,LogEntity logEntity,Date proviousDate){
        List<String> matchesList = new ArrayList<String>(1);
        Matcher matcher = patternEx.matcher(line);
        while(matcher.find()) {
            for(int i=1;i<=matcher.groupCount();i++){
                if( matcher.group(i)==null) {
                    continue;
                }
                matchesList.add(matcher.group(i));
            }
        }
        if(matchesList.size() > 0) {
            logEntity.setEx(matchesList.get(0));
        }
        logEntity.setTimestamp(proviousDate);
        return matchesList.size()!=0;
    }

    public static String matchEx(String line){
        Matcher matcher = patternEx.matcher(line);
        while(matcher.find()) {
            for(int i=1;i<=matcher.groupCount();i++){
                if( matcher.group(i)==null) {
                    continue;
                }
               return matcher.group(i);
            }
        }
        return null;
    }

    public static boolean parseTimeAndLoglevel(String day,String line,LogEntity logEntity){
        List<String> matchesList = new ArrayList<String>(2);
        if(line.indexOf(" INFO ",16) > 0){
            matchesList.add("INFO");
        }else if(line.indexOf(" DEBUG ",16) > 0){
            matchesList.add("DEBUG");
        }else if(line.indexOf(" ERROR ",16) > 0){
            matchesList.add("ERROR");
        }else if(line.indexOf(" WARN ",16) > 0){
            matchesList.add("WARN");
        }else if(line.indexOf(" TRACE ",16) > 0){
            matchesList.add("TRACE");
        }else if(line.indexOf(" FETAL ",16) > 0){
            matchesList.add("FETAL");
        }else{
            return false;
        }
        matchesList.add(0,line.substring(0,12));
        fillTimeAndLoglevel(day, logEntity, matchesList);
        return true;
    }

    /**
     * 获取时间和logLevel
     * @param day
     * @param logEntity
     * @param matches
     */
    private static void fillTimeAndLoglevel(String day,LogEntity logEntity,List<String> matches){
        int index = 0;
        logEntity.setTimestamp(DateUtil.stirng2Date(day+" "+getValueFromList(matches,index),"yyyy-MM-dd HH:mm:ss.SSS"));index++;
        logEntity.setLogLevel(getValueFromList(matches,index));index++;
    }

    private static String getValueFromList(List<String> matches,int index){
        try {
            return matches.get(index).trim();
        }catch (IndexOutOfBoundsException e){
            return "";
        }
    }


}
