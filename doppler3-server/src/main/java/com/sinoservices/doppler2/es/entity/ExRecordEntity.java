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
public class ExRecordEntity {

    private String id;
    private String timestamp;
    private String appName;
    private String host;
    private String exName;
    private String className;
    private String method;
    private long lineNum;
    private String file;

    public ExRecordEntity(){}

    public ExRecordEntity(String id, Map<String, SearchHitField> fields) {
        this.id = id;
        this.timestamp = fields.get(Constant.ExStat.FIELD_TIMESTAMP)==null?"":fields.get(Constant.ExStat.FIELD_TIMESTAMP).getValue()+"";
        this.exName =  fields.get(Constant.ExStat.FIELD_EX_NAME)==null?"":fields.get(Constant.ExStat.FIELD_EX_NAME).getValue()+"";
        this.host =  fields.get(Constant.ExStat.FIELD_HOST)==null?"":fields.get(Constant.ExStat.FIELD_HOST).getValue()+"";
        this.appName =  fields.get(Constant.ExStat.FIELD_APP_NAME)==null?"":fields.get(Constant.ExStat.FIELD_APP_NAME).getValue()+"";
        this.className =  fields.get(Constant.ExStat.FIELD_CLASS_NAME)==null?"":fields.get(Constant.ExStat.FIELD_CLASS_NAME).getValue()+"";
        this.method =  fields.get(Constant.ExStat.FIELD_METHOD_NAME)==null?"":fields.get(Constant.ExStat.FIELD_METHOD_NAME).getValue()+"";
        this.lineNum = NumberUtil.formatLong(fields.get(Constant.ExStat.FIELD_LINE_NUM).getValue().toString(),-1L);
        this.file =  fields.get(Constant.ExStat.FIELD_FILE)==null?"":fields.get(Constant.ExStat.FIELD_FILE).getValue()+"";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
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

    public long getLineNum() {
        return lineNum;
    }

    public void setLineNum(long lineNum) {
        this.lineNum = lineNum;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public static String[] getFields(){
        return new String[]{Constant.FIELD_TIMESTAMP,Constant.ExStat.FIELD_EX_NAME,Constant.ExStat.FIELD_APP_NAME,Constant.ExStat.FIELD_HOST,
                Constant.ExStat.FIELD_CLASS_NAME ,Constant.ExStat.FIELD_METHOD_NAME,Constant.ExStat.FIELD_LINE_NUM,Constant.ExStat.FIELD_FILE};
    }


}
