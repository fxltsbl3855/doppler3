package com.sinoservices.doppler2.cache;

import com.sinoservices.doppler2.config.UpMessageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class CacheData {
    private static final Logger logger = LoggerFactory.getLogger(CacheData.class);

    private static CacheData ourInstance = null;
    private static List<String> errorBlackList = null;

    private CacheData() {
        errorBlackList = new ArrayList<String>();
        if(UpMessageConfig.error_black_list != null && !"".equals(UpMessageConfig.error_black_list.trim())){
            StringTokenizer st = new StringTokenizer(UpMessageConfig.error_black_list.trim(),",");
            while(st.hasMoreTokens()){
                errorBlackList.add(st.nextToken().trim());
            }
        }
        logger.info("error black list init ok,errorBlackList=" + errorBlackList);
    }

    public static CacheData getIns() {
        if(ourInstance == null){
            init();
        }
        return ourInstance;
    }

    private static synchronized void init() {
        if(ourInstance == null){
            ourInstance = new CacheData();
        }
    }

    public  List<String> getErrorBlackList(){
        return errorBlackList;
    }




}
