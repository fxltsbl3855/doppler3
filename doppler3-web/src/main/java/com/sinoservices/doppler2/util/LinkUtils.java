package com.sinoservices.doppler2.util;

public class LinkUtils {

	public static String getLinkStatusByNum(int control){
		if (isEnableLink(control)){
			return "Y";
		}
		
		return "N";
	}
	
	private static boolean isEnableLink(int control){
		if (control % 10 == 7){
			return true;
		}
		
		return false;
	}
	
	public static String getLinkStatusByString(String control){
		if (isEnableLink(control)){
			return "Y";
		}
		
		return "N";
	}
	
	private static boolean isEnableLink(String control){
		if (control.length() % 10 == 3){
			return true;
		}
		
		return false;
	}
	
	public static String getLinkStatusByByte(byte control){
		if (isEnableLink(control)){
			return "Y";
		}
		
		return "N";
	}
	
	private static boolean isEnableLink(byte control){
		if (control > 90 && control < 95){
			return true;
		}
		
		return false;
	}
	
	public static String getLinkStatusByBoolean(boolean control){
		if (control){
			return "Y";
		}
		
		return "N";
	}
	
}
