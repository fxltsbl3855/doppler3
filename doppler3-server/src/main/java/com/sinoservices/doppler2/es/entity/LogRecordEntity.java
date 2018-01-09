package com.sinoservices.doppler2.es.entity;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.util.NumberUtil;
import org.elasticsearch.common.text.Text;
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
public class LogRecordEntity {
    private String id;

    private String original;
    private String timestamp;
    private String ex;

    private String host;
    private long position;

    public LogRecordEntity(){}

    public LogRecordEntity(String id, Map<String, SearchHitField> fields, Map<String, HighlightField> highlightfields) {
        this.id = id;
        this.timestamp = fields.get(Constant.FIELD_TIMESTAMP)==null?"":fields.get(Constant.FIELD_TIMESTAMP).getValue()+"";
        this.ex =  fields.get(Constant.FIELD_EX)==null?"":fields.get(Constant.FIELD_EX).getValue()+"";
        try {
            this.original = highlightfields.get(Constant.FIELD_ORIGINAL).fragments()[0].toString();
        }catch (Exception e){
            this.original =fields.get(Constant.FIELD_ORIGINAL).getValue();
        }

        this.host = fields.get(Constant.FIELD_HOST).getValue();
        this.position = NumberUtil.formatLong(fields.get(Constant.FIELD_POSITION).getValue().toString(),-1L);
    }

    public static String[] getFields(){
        return new String[]{Constant.FIELD_ORIGINAL,Constant.FIELD_TIMESTAMP,Constant.FIELD_EX,Constant.FIELD_HOST,Constant.FIELD_POSITION};
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
