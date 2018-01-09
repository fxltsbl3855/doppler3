package com.sinoservices.doppler2.es.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class AggsBucketsEntity {

    private String key;
    private Long value;

    private double max;
    private double min;
    private double avg;

    private Map<String,AggsBucketsEntity> aggsBucketsEntityMap;

    public AggsBucketsEntity (String key,Long value){
        this.key = key;
        this.value = value;
    }

    public Map<String, AggsBucketsEntity> getAggsBucketsEntityMap() {
        return aggsBucketsEntityMap;
    }

    public void setAggsBucketsEntityMap(Map<String, AggsBucketsEntity> aggsBucketsEntityMap) {
        this.aggsBucketsEntityMap = aggsBucketsEntityMap;
    }

    public void addAggs(String key, AggsBucketsEntity aggsBucketsEntity){
        if(aggsBucketsEntityMap ==null){
            aggsBucketsEntityMap = new HashMap<String, AggsBucketsEntity>();
        }
        aggsBucketsEntityMap.put(key,aggsBucketsEntity);
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
