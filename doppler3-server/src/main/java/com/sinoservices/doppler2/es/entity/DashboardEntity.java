package com.sinoservices.doppler2.es.entity;

import com.sinoservices.util.NumberUtil;
import org.elasticsearch.search.SearchHitField;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class DashboardEntity {

    private String id;

    private String appName;
    private String moduleName;
    private long reqStat;

    public DashboardEntity(String id, Map<String, SearchHitField> fields) {
        this.id = id;
        this.appName = fields.get("appName").getValue();
        this.moduleName = fields.get("moduleName").getValue();
        this.reqStat = NumberUtil.formatLong(fields.get("reqStat").getValue().toString(),0);
    }

    public static String[] getFields(){
        return new String[]{"appName","moduleName","reqStat"};
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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public long getReqStat() {
        return reqStat;
    }

    public void setReqStat(long reqStat) {
        this.reqStat = reqStat;
    }
}
