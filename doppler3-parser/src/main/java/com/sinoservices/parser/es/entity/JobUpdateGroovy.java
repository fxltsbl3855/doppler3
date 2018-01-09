package com.sinoservices.parser.es.entity;

import com.sinoservices.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */

public class JobUpdateGroovy {

    GroovyScript script;

    Map<String,Map<String,JobUpdateTerm[]>> query;

    public Map<String, Map<String, JobUpdateTerm[]>> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Map<String, JobUpdateTerm[]>> query) {
        this.query = query;
    }

    public GroovyScript getScript() {
        return script;
    }

    public void setScript(GroovyScript script) {
        this.script = script;
    }

    public static JobUpdateGroovy fillData(String result, int time, String jobId){
        JobUpdateGroovy jobUpdate = new JobUpdateGroovy();
        GroovyScript script = new GroovyScript(result,time);
        //String inline = "ctx._source.result=\""+result+"\" ; ctx._source.time="+time;
       // script.put("inline",inline);
        jobUpdate.setScript(script);

        JobUpdateTerm[] jobUpdateTerm = new JobUpdateTerm[2];
        jobUpdateTerm[0] = new JobUpdateTerm();
        Map<String,String> t1 = new HashMap<String, String>();
        t1.put("action","ACTION_JOB_IN");
        jobUpdateTerm[0].setTerm(t1);
        jobUpdateTerm[1] = new JobUpdateTerm();
        Map<String,String> t2 = new HashMap<String, String>();
        t2.put("jobId",jobId);
        jobUpdateTerm[1].setTerm(t2);

        Map<String,Map<String,JobUpdateTerm[]>> q2 = new HashMap<String, Map<String, JobUpdateTerm[]>>();
        Map<String,JobUpdateTerm[]> q3 = new HashMap<String, JobUpdateTerm[]>();
        q3.put("must",jobUpdateTerm);
        q2.put("bool",q3);
        jobUpdate.setQuery(q2);
        return jobUpdate;
    }

    public static void main(String[] a){
        JobUpdateGroovy jobUpdate = JobUpdateGroovy.fillData("SUCCESS2",33,"123456789");

        System.out.println(JsonUtils.object2Json(jobUpdate));

    }
}
