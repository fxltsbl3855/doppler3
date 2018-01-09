package com.sinoservices.doppler2.es.entity;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.NumberUtil;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.highlight.HighlightField;

import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class OpEntity {

    private String id;

    private Date timestamp;
    private String action;
    private String appName;
    private String host;
    private String className;
    private String method;
    private String operType;
    private String operObj;
    private String result;
    private String param;
    private String busParam;
    private String original;
    private long time;
    private long position;
    private String file;

    public OpEntity() {}

    public OpEntity(String id, Map<String, SearchHitField> fields, Map<String, HighlightField> highlightfields) {
        this.id = id;
        this.timestamp = fields.get(Constant.OpStat.FIELD_TIMESTAMP)==null?null: DateUtil.utcStrToLocalDate(fields.get(Constant.FIELD_TIMESTAMP).getValue()+"");

        this.action =  fields.get(Constant.OpStat.FIELD_ACTION)==null?"":fields.get(Constant.OpStat.FIELD_ACTION).getValue()+"";
        this.appName = fields.get(Constant.OpStat.FIELD_APP_NAME)==null?"":fields.get(Constant.OpStat.FIELD_APP_NAME).getValue()+"";
        this.host = fields.get(Constant.OpStat.FIELD_HOST)==null?"":fields.get(Constant.OpStat.FIELD_HOST).getValue()+"";
        this.operType = fields.get(Constant.OpStat.FIELD_OP_TYPE)==null?"":fields.get(Constant.OpStat.FIELD_OP_TYPE).getValue()+"";
        this.operObj = fields.get(Constant.OpStat.FIELD_OP_OBJ)==null?"":fields.get(Constant.OpStat.FIELD_OP_OBJ).getValue()+"";
        this.result = fields.get(Constant.OpStat.FIELD_RESULT)==null?"":fields.get(Constant.OpStat.FIELD_RESULT).getValue()+"";
        this.param = fields.get(Constant.OpStat.FIELD_PARAM)==null?"":fields.get(Constant.OpStat.FIELD_PARAM).getValue()+"";

        try {
            this.busParam = highlightfields.get(Constant.OpStat.FIELD_BUS_PARAM).fragments()[0].toString();
        }catch (Exception e){
            this.busParam =fields.get(Constant.OpStat.FIELD_BUS_PARAM)==null?"":fields.get(Constant.OpStat.FIELD_BUS_PARAM).getValue()+"";
        }
        this.time = NumberUtil.formatLong(fields.get(Constant.OpStat.FIELD_TIME)==null?"-1":fields.get(Constant.OpStat.FIELD_TIME).getValue().toString(),-1L);
        this.position = NumberUtil.formatLong(fields.get(Constant.FIELD_POSITION)==null?"-1":fields.get(Constant.FIELD_POSITION).getValue().toString(),-1L);
    }

    public OpEntity(String id, Date timestamp, String action, String appName, String host, String className,
                    String method, String operType, String operObj, String result,
                    String param, String busParam, String original, long time, long position, String file) {
        this.id = id;
        this.timestamp = timestamp;
        this.action = action;
        this.appName = appName;
        this.host = host;
        this.className = className;

        this.method = method;
        this.operType = operType;
        this.operObj = operObj;
        this.result = result;

        this.param = param;
        this.busParam = busParam;
        this.original = original;
        this.time = time;
        this.position = position;
        this.file = file;

    }

    public void setBase(String id,Date timestamp,String appName, String host,String original,long position,String file){
        this.id = id;
        this.timestamp = timestamp;
        this.appName = appName;
        this.host = host;
        this.original = original;
        this.position = position;
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getOperObj() {
        return operObj;
    }

    public void setOperObj(String operObj) {
        this.operObj = operObj;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getBusParam() {
        return busParam;
    }

    public void setBusParam(String busParam) {
        this.busParam = busParam;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public static String[] getFields() {
        return new String[]{Constant.OpStat.FIELD_TIMESTAMP,Constant.OpStat.FIELD_APP_NAME,Constant.OpStat.FIELD_HOST,Constant.OpStat.FIELD_OP_TYPE,
                Constant.OpStat.FIELD_OP_OBJ,Constant.OpStat.FIELD_RESULT,Constant.OpStat.FIELD_PARAM,Constant.OpStat.FIELD_BUS_PARAM,
                Constant.OpStat.FIELD_TIME};
    }
}




















