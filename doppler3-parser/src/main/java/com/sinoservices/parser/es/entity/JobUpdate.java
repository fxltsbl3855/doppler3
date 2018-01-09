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
@Deprecated
public class JobUpdate {


    Map<String,Map<String,JobUpdateTerm[]>> query;

    Map<String,String> script;
    public Map<String, Map<String, JobUpdateTerm[]>> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Map<String, JobUpdateTerm[]>> query) {
        this.query = query;
    }

    public Map<String, String> getScript() {
        return script;
    }

    public void setScript(Map<String, String> script) {
        this.script = script;
    }

    public static JobUpdate fillData(String result,int time,String jobId){
        JobUpdate jobUpdate = new JobUpdate();
        Map<String,String> script = new HashMap<String, String>();
        String inline = "ctx._source.result=\""+result+"\" ; ctx._source.time="+time;
        script.put("inline",inline);
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
        JobUpdate jobUpdate = JobUpdate.fillData("SUCCESS2",33,"123456789");

        System.out.println(JsonUtils.object2Json(jobUpdate));

    }
}
