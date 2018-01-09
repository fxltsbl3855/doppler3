package com.sinoservices.doppler2.es.entity;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class MustRangeTimestamp {

    private String field;

    /**
     * from 和 to都必须是utc时间
     */
    private String from;
    private String to;
    private String format;
    private int intervalHours; //只在按照时间group by的时候有效

    public MustRangeTimestamp(String field,String from,String to,String format,int intervalHours){
        this.field = field;
        this.from = from;
        this.to = to;
        this.format = format;
        this.intervalHours = intervalHours;
    }

    public MustRangeTimestamp(String field,String from,String to,String format){
        this.field = field;
        this.from = from;
        this.to = to;
        this.format = format;
        this.intervalHours = 1;
    }

    public int getIntervalHours() {
        return intervalHours;
    }

    public void setIntervalHours(int intervalHours) {
        this.intervalHours = intervalHours;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
