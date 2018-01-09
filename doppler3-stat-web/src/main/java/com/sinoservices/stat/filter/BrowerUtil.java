package com.sinoservices.stat.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrowerUtil {
	private static final Logger logger = LoggerFactory.getLogger(BrowerUtil.class);
	
	public static final String chrome = "Chrome/[^ ]*";
	public static final Pattern chrome_pattern = Pattern.compile(chrome);
	
	public static final String ie = "MSIE \\d\\.\\d";
	public static final Pattern ie_pattern = Pattern.compile(ie);
    
	public static final String safari = "Safari/[\\d\\.]{1,}";
	public static final Pattern safari_pattern = Pattern.compile(safari);
	
	public static final String firefox = "Firefox/[\\d\\.]{1,}";
	public static final Pattern firefox_pattern = Pattern.compile(firefox);
	
	public static final String opera = "(Opera [\\d\\.]{1,})|(Opera/[\\d\\.]{1,})";
	public static final Pattern opera_pattern = Pattern.compile(opera);
	
	public static final String navigator = "Navigator/[\\d\\.]{1,}";
	public static final Pattern navigator_pattern = Pattern.compile(navigator);
	
    
    public static void main(String[] args){
    	 String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.12) Gecko/20080219 Firefox/2.0.0.12 Navigator/9.0.0.6";
         
    	 System.out.println(getBrowerAndVersion(userAgent));
    }
    
    public static String getBrowerAndVersion(String userAgent){
		if(userAgent == null || "".equals(userAgent.trim())){
			return "unknow";
		}
    	String res = getMatch(userAgent,chrome_pattern);

    	if(res != null){
    		res = res.replaceFirst("/", " ");
    		return res;
    	}
    	
    	res = getMatch(userAgent,ie_pattern);
    	if(res != null){
    		res = res.replaceFirst("MSIE", "IE");
    		return res;
    	}
    	
    	res = getMatch(userAgent,safari_pattern);
    	if(res != null){
    		res = res.replaceFirst("/", " ");
    		return res;
    	}
    	
    	res = getMatch(userAgent,firefox_pattern);
    	if(res != null){
    		res = res.replaceFirst("/", " ");
    		return res;
    	}
    	
    	res = getMatch(userAgent,opera_pattern);
    	if(res != null){
    		res = res.replaceFirst("/", " ");
    		return res;
    	}
    	
    	res = getMatch(userAgent,navigator_pattern);
    	if(res != null){
    		res = res.replaceFirst("/", " ");
    		return res;
    	}
		logger.warn("userAgent={}",userAgent);
    	return "unknow";
    	
    }
    
    private static String getMatch(String userAgent,Pattern pattern){
        Matcher matcher = pattern.matcher(userAgent);
        while(matcher.find()) {
            String res = matcher.group();
            return res;
            
        }
        return null;
    }
}