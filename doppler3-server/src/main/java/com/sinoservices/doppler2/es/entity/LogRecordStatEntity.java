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
public class LogRecordStatEntity {
    private String id;

    private String host;
    private String appName;
    private String timestamp;
    private String className;
    private String method;
    private String param;

    private long time;

    private String brower;
    private String username;
    private String clientIp;
    private String header;
    private int code;

    public LogRecordStatEntity(){}


    public LogRecordStatEntity(String id, Map<String, SearchHitField> fields, Map<String, HighlightField> highlightfields) {
        this.id = id;
        this.timestamp = fields.get(Constant.FIELD_TIMESTAMP)==null?"":fields.get(Constant.FIELD_TIMESTAMP).getValue()+"";
        this.host = fields.get(Constant.FIELD_HOST).getValue();
        this.appName = get(fields.get(Constant.FIELD_APP_NAME));
        this.className = get(fields.get(Constant.FIELD_CLASS_NAME));
        this.method = get(fields.get(Constant.FIELD_METHOD));
        this.param = get(fields.get(Constant.FIELD_PARAM));
        this.time = NumberUtil.formatLong(fields.get(Constant.FIELD_TIME).getValue().toString(),-1L);

        this.brower = get(fields.get(Constant.FIELD_BROWER));
        this.username = get(fields.get(Constant.FIELD_USERNAME));
        this.clientIp = get(fields.get(Constant.FIELD_CLIENTIP));
        this.header = get(fields.get(Constant.FIELD_HEADER));
        this.code = getInt(fields.get(Constant.FIELD_CODE));
    }

    public static String[] getFields(){
        return new String[]{Constant.FIELD_HOST,Constant.FIELD_APP_NAME,Constant.FIELD_CLASS_NAME,Constant.FIELD_METHOD,Constant.FIELD_TIMESTAMP,Constant.FIELD_PARAM,Constant.FIELD_TIME};
    }

    public static String[] webStatFields(){
        return new String[]{Constant.FIELD_HOST,Constant.FIELD_APP_NAME,Constant.FIELD_CLASS_NAME,Constant.FIELD_METHOD,Constant.FIELD_TIMESTAMP,Constant.FIELD_TIME,
                Constant.FIELD_BROWER,Constant.FIELD_USERNAME,Constant.FIELD_CLIENTIP,Constant.FIELD_CODE};
    }

    public static String[] webDetailFields(){
        return new String[]{Constant.FIELD_HOST,Constant.FIELD_APP_NAME,Constant.FIELD_CLASS_NAME,Constant.FIELD_METHOD,Constant.FIELD_TIMESTAMP,Constant.FIELD_PARAM,Constant.FIELD_TIME,
                Constant.FIELD_BROWER,Constant.FIELD_USERNAME,Constant.FIELD_CLIENTIP,Constant.FIELD_HEADER,Constant.FIELD_CODE};
    }

    public String get(SearchHitField sh){
        if(sh == null){
            return "";
        }
        return sh.getValue();

    }

    public int getInt(SearchHitField sh){
        if(sh == null){
            return 0;
        }
        return sh.getValue();

    }

    public String getBrower() {
        return brower;
    }

    public void setBrower(String brower) {
        this.brower = brower;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
