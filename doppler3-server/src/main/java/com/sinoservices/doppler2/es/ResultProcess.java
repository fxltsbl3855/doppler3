package com.sinoservices.doppler2.es;

import org.elasticsearch.index.get.GetField;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ResultProcess {

    public static Map<String, Object> process(Map<String, GetField> fields) {
        if(fields == null ||fields.size() ==0){
            return Collections.emptyMap();
        }
        Map<String, Object> resMap = new HashMap<String, Object>(fields.size());
        for(Map.Entry<String, GetField> entry : fields.entrySet()){
            resMap.put(entry.getKey(),entry.getValue().getValue());
        }
        return resMap;
    }

}
