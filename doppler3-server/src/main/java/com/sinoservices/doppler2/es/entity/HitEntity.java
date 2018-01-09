package com.sinoservices.doppler2.es.entity;

import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.highlight.HighlightField;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class HitEntity {

    private String id;
    private Map<String, SearchHitField> valueMap;
    private Map<String, HighlightField> highlightValueMap;

    public HitEntity(String id,Map<String, SearchHitField> valueMap,Map<String, HighlightField> highlightValueMap){
        this.id = id;
        this.valueMap = valueMap;
        this.highlightValueMap = highlightValueMap;
    }


    public Map<String, HighlightField> getHighlightValueMap() {
        return highlightValueMap;
    }

    public void setHighlightValueMap(Map<String, HighlightField> highlightValueMap) {
        this.highlightValueMap = highlightValueMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, SearchHitField> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, SearchHitField> valueMap) {
        this.valueMap = valueMap;
    }
}
