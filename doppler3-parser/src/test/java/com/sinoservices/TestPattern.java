package com.sinoservices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class TestPattern {

    public static void main(String[] args) {
        String msg = "13:44:54.539 [http-apr-8080-exec-4] ERROR c.s.f.email.controller.WebController - error2  ";
        String findLogInfo = "^(\\d{2}:\\d{2}:\\d{2}\\.\\d{3})";
        Pattern patternLogInfo = Pattern.compile(findLogInfo);
        Matcher m = patternLogInfo.matcher(msg);
        boolean res = m.find();
//        while(m.find()) {
//            for(int i=1;i<=m.groupCount();i++){
//                if( m.group(i)==null) {
//                    continue;
//                }
//                System.out.println(m.group(i));
//            }
//        }
        System.out.println(res);
//        String msg = "13:44:54.539 [http-apr-8080-exec-4] ERROR c.s.f.email.controller.WebController - error2";
//        boolean isMatch = Pattern.matches(findLogInfo, msg);
        //System.out.println(isMatch);


    }
}
