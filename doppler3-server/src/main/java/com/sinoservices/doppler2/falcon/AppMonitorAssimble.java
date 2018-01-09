package com.sinoservices.doppler2.falcon;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.es.entity.AggsBucketsEntity;
import com.sinoservices.doppler2.service.assimble.Assimble;
import com.sinoservices.doppler2.util.MetricsBo;
import com.sinoservices.util.NumberUtil;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class AppMonitorAssimble {
    public static Map<String, List<String>> shouldMap(List<String> targetList) {
        if(targetList == null || targetList.size() ==0){
            return Collections.emptyMap();
        }
        Map<String,List<String>> shouldPram = new HashMap<String, List<String>>(targetList.size());
        List<String> ss = new ArrayList<String>();
        for(String appName : targetList){
            ss.add(appName);
        }
        shouldPram.put(Constant.FIELD_APP_NAME,ss);
        return shouldPram;

    }

    public static String[] groupByFields() {
        List<String> list = new ArrayList<String> (3);
        list.add(Constant.FIELD_APP_NAME);
        list.add(Constant.FIELD_HOST);
        list.add(Constant.FIELD_CLASS_NAME);
        list.add(Constant.FIELD_METHOD);
        String[] groupByFields = new String[list.size()];
        list.toArray(groupByFields);
        return groupByFields;
    }

    public static List<MonitorBo> assimbleMonitor(Map<String, AggsBucketsEntity> resMap) {
        if(Assimble.isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<MonitorBo> resList = new ArrayList<MonitorBo>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            AggsBucketsEntity aggsBucketsEntity = entry.getValue();
            String appName = aggsBucketsEntity.getKey();
            Map<String, AggsBucketsEntity> map2 = aggsBucketsEntity.getAggsBucketsEntityMap();
            for(Map.Entry<String, AggsBucketsEntity> entry2 : map2.entrySet()){
                String host = entry2.getKey();
                Map<String, AggsBucketsEntity> map3 = entry2.getValue().getAggsBucketsEntityMap();
                for(Map.Entry<String, AggsBucketsEntity> entry3 : map3.entrySet()){
                    String moduleName = entry3.getKey();
                    Map<String, AggsBucketsEntity> map4 = entry3.getValue().getAggsBucketsEntityMap();
                    if(map4 == null || map4.size() ==0) continue;
                    for (Map.Entry<String, AggsBucketsEntity> entry4 : map4.entrySet()) {
                        String method = entry4.getKey();
                        long count = entry4.getValue().getValue();
                        double max = entry4.getValue().getMax();
                        double min = entry4.getValue().getMin();
                        double avg = entry4.getValue().getAvg();
                        resList.add(getMonitorBo(appName, moduleName, strToIp(host), method, count, max, min, avg));
                    }
                }
            }
        }
        return resList;
    }

    public static String strToIp(String ipStr) {
        long ip = NumberUtil.formatLong(ipStr,-1);
        StringBuilder result = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            result.insert(0,Long.toString(ip & 0xff));
            if (i < 3) {
                result.insert(0,'.');
            }
            ip = ip >> 8;
        }
        return result.toString();
    }

    private static MonitorBo getMonitorBo(String appName, String moduleName,String host, String method, long count, double max, double min, double avg) {
        MonitorBo monitorBo = new MonitorBo();
        monitorBo.setAppName(appName);
        monitorBo.setHost(host);
        monitorBo.setClassName(moduleName);
        monitorBo.setMethod(method);
        monitorBo.setExecuteNum(new Long(count).intValue());
        monitorBo.setMaxTime(NumberUtil.doubleToInt(max));
        monitorBo.setMinTime(NumberUtil.doubleToInt(min));
        monitorBo.setAvgTime(NumberUtil.doubleToInt(avg));
        return monitorBo;
    }

    public static MetricsBo getMetricsBo(String appName, String host,String interfaceName, long time, int value, int step){
        MetricsBo mb = new MetricsBo();
        mb.setMetric("url_time");
        mb.setEndpoint(host);
        mb.setTimestamp(time);
        mb.setStep(step);
        mb.setValue(value);
        mb.setCounterType("GAUGE");
        mb.setTags("a="+appName+",p="+interfaceName);
        return mb;
    }

    public static MetricsBo[] transfer(List<MonitorBo> list,long time,int step) {
        if(list == null || list.size()==0){
            return null;
        }
        MetricsBo[] mss = new MetricsBo[list.size()];
        for(int i=0;i<list.size();i++){
            mss[i] = getMetricsBo(list.get(i).getAppName(),list.get(i).getHost(),list.get(i).getClassName()+"."+list.get(i).getMethod(),time,list.get(i).getMaxTime(),step);

        }
        return mss;
    }


    public static List<MonitorBo> merge(List<MonitorBo> valuelist, List<MonitorBo> list) {
        if(valuelist == null || valuelist.size()==0){
            return Collections.EMPTY_LIST;
        }
        Map<String,MonitorBo> resMap = new HashMap<String, MonitorBo>();
        for(MonitorBo value : valuelist){
            value.setMaxTime(0);
            resMap.put(value.getAppName()+"_"+value.getHost()+"_"+value.getClassName()+"_"+value.getMethod(),value);
        }

        if(list == null || list.size()==0){
            return new ArrayList<MonitorBo>(resMap.values());
        }
        for(MonitorBo mb : list){
            MonitorBo value = resMap.get(mb.getAppName()+"_"+mb.getHost()+"_"+mb.getClassName()+"_"+mb.getMethod());
            if(value!=null){
                value.setMaxTime(mb.getMaxTime());
            }
        }
        return new ArrayList<MonitorBo>(resMap.values());
    }

    public static List<String> getFalconMonAppValue(String value) {
        if(value == null || "".equals(value.trim())){
            return Collections.EMPTY_LIST;
        }
        List<String> resList = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(value,",");
        while(st.hasMoreTokens()){
            String c = st.nextToken();
            if(c != null && !"".equals(c.trim())) {
                resList.add(c.trim());
            }
        }
        return resList;
    }

    public static void main(String[] args) {
        System.out.println(getFalconMonAppValue("ad,fd-s,vv ,fdsad,doppler-server"));
    }
}
