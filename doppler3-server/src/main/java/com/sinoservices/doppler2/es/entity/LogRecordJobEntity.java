package com.sinoservices.doppler2.es.entity;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.util.NumberUtil;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.highlight.HighlightField;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class LogRecordJobEntity {
    private String id;
    private String action;
    private String host;
    private String appName;
    private String timestamp;
    private String className;
    private String method;
    private String result;
    private String param;
    private String jobId;

    private long time;

    public LogRecordJobEntity(){}

    public LogRecordJobEntity(String id, Map<String, SearchHitField> fields, Map<String, HighlightField> highlightfields) {
        this.id = id;
        this.timestamp = fields.get(Constant.FIELD_TIMESTAMP)==null?"":fields.get(Constant.FIELD_TIMESTAMP).getValue()+"";
        this.action = fields.get(Constant.FIELD_ACTION).getValue();
        this.host = fields.get(Constant.FIELD_HOST).getValue();
        this.appName = fields.get(Constant.FIELD_APP_NAME).getValue();
        this.className = fields.get(Constant.FIELD_CLASS_NAME).getValue();
        this.method = fields.get(Constant.FIELD_METHOD).getValue();
        this.param = fields.get(Constant.FIELD_PARAM).getValue();
        this.jobId = fields.get(Constant.FIELD_JOBID).getValue();
        this.result = fields.get(Constant.FIELD_RESULT).getValue();
        this.time = NumberUtil.formatLong(fields.get(Constant.FIELD_TIME).getValue().toString(),-1L);
    }

    public static String[] getFields(){
        return new String[]{Constant.FIELD_ACTION,Constant.FIELD_HOST,Constant.FIELD_APP_NAME,Constant.FIELD_CLASS_NAME,Constant.FIELD_METHOD,
                Constant.FIELD_TIMESTAMP,Constant.FIELD_PARAM,Constant.FIELD_RESULT,Constant.FIELD_JOBID,Constant.FIELD_TIME};
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
