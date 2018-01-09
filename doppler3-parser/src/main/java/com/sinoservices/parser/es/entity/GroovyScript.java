package com.sinoservices.parser.es.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class GroovyScript {
    private String lang = "groovy";
    private String file = "update_by_jobid";
    private Map params = new HashMap();

    public GroovyScript(String newResult , int newTime){
        params.put("new_result",newResult);
        params.put("new_time",newTime);
    }

}
