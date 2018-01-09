package com.sinoservices;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
class TestSpeed {

    public static void main(String[] a){
        TestSpeed testSpeed = new TestSpeed();
        long start = System.currentTimeMillis();
//        for (int i = 0; i <100000 ; i++) {
//            testSpeed.testReg();
//        }
        testSpeed.testIndex();
        long end = System.currentTimeMillis();
        System.out.println("end-start = " + (end - start));
    }

    public void testIndex(){
        String line = "13:49:31.766 [http-8088-1] INFO  c.sinoservices.stat.filter.MonitorFilter - [stat] @action=ACTION_REQ_OUT @className=/test/go.shtml @method= @result=SUCCESS @param=s=3#a=18# @username=admin @clientIp=192.168.0.254 @brower=Chrome 52.0.2743.82 @header=host=192.168.0.88:8088#connection=keep-alive#cache-control=max-age=0#upgrade-insecure-requests=1#user-agent=Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36#accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8#accept-encoding=gzip, deflate, sdch#accept-language=zh-CN,zh;q=0.8,en;q=0.6#cookie=JSESSIONID=F6AF2041F965D9897FADB7DE8C63B85E# @code=404 @time=63ms";
        for(int i=0;i<100000;i++){
            line.indexOf("@action=ACTION_JOB_OUT");
        }
    }

    private static final String findJobParam = "(@action=ACTION_JOB_(IN|OUT))|(@className=[^@]*)|(@method=[^@]*)|(@result=[^@]*)|(@param=[^@]*)|(@jobId=[^@]*)|(@time=\\d{0,9})*";
    private static final Pattern patternJobParam = Pattern.compile(findJobParam);

    public void testReg(){
        String line = "13:49:31.766 [http-8088-1] INFO  c.sinoservices.stat.filter.MonitorFilter - [stat] @action=ACTION_REQ_OUT @className=/test/go.shtml @method= @result=SUCCESS @param=s=3#a=18# @username=admin @clientIp=192.168.0.254 @brower=Chrome 52.0.2743.82 @header=host=192.168.0.88:8088#connection=keep-alive#cache-control=max-age=0#upgrade-insecure-requests=1#user-agent=Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36#accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8#accept-encoding=gzip, deflate, sdch#accept-language=zh-CN,zh;q=0.8,en;q=0.6#cookie=JSESSIONID=F6AF2041F965D9897FADB7DE8C63B85E# @code=404 @time=63ms";
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
    }


}
