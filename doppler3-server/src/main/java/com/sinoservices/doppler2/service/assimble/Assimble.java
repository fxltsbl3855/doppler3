package com.sinoservices.doppler2.service.assimble;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.bo.*;
import com.sinoservices.doppler2.cache.CacheData;
import com.sinoservices.doppler2.es.DSLAssemble;
import com.sinoservices.doppler2.es.entity.*;
import com.sinoservices.util.DateUtil;
import com.sinoservices.util.JsonUtils;
import com.sinoservices.util.NumberUtil;
import com.sinoservices.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class Assimble {
    private static final Logger logger = LoggerFactory.getLogger(Assimble.class);

    public static MustRangeTimestamp getLast7DayMRT(){
        String startDateUTC = DateUtil.localDateToUTCStr(DateUtil.getNewDate(new Date(),-7),DSLAssemble.DATE_FORMAT_YYYY_MM_DD_HH);
        String endDateUTC = DateUtil.localDateToUTCStr(new Date(),DSLAssemble.DATE_FORMAT_YYYY_MM_DD_HH);
        MustRangeTimestamp mrt = new MustRangeTimestamp(Constant.ES_FIELD_TIMESTAMP,startDateUTC,endDateUTC, DSLAssemble.DATE_FORMAT_YYYY_MM_DD_HH,2);
        return mrt;
    }

    public static MustRangeTimestamp getMRTByDateStr(Date startDate,Date endDate,int intervalHours){
        String startDateUTC = DateUtil.localDateToUTCStr(startDate,DSLAssemble.DATE_FORMAT_YYYY_MM_DD_HH);
        String endDateUTC = DateUtil.localDateToUTCStr(endDate,DSLAssemble.DATE_FORMAT_YYYY_MM_DD_HH);
        MustRangeTimestamp mrt = new MustRangeTimestamp(Constant.ES_FIELD_TIMESTAMP,startDateUTC,endDateUTC, DSLAssemble.DATE_FORMAT_YYYY_MM_DD_HH,intervalHours);
        return mrt;
    }

    public static MustRangeTimestamp getMRTByCSTDate(String cstDateStr){
        Date startDate = DateUtil.stirng2Date(cstDateStr,"yyyy-MM-dd HH");
        Date endDate = new Date();endDate.setHours(endDate.getHours()+1);
        String startUtcDateStr = DateUtil.localDateToUTCStr(startDate,"yyyy-MM-dd HH");
        String endUtcDateStr = DateUtil.localDateToUTCStr(endDate,"yyyy-MM-dd HH");
        return new MustRangeTimestamp(Constant.FIELD_TIMESTAMP,startUtcDateStr,endUtcDateStr,"yyyy-MM-dd HH");
    }

    /**
     * 获取昨天0点到“现在”的时间
     * @return
     */
    @Deprecated
    public static MustRangeTimestamp getLast2DaysUTC(){
        Date yesterday = DateUtil.getNewDate(new Date(),-1);
        String startDateStr = DateUtil.Date2String(yesterday,"yyyy-MM-dd");
        Date startDate = DateUtil.stirng2Date(startDateStr,"yyyy-MM-dd");

        Date now = new Date();
        now.setHours(now.getHours()+1);

        String startUtcDateStr = DateUtil.localDateToUTCStr(startDate,"yyyy-MM-dd HH");
        String endUtcDateStr = DateUtil.localDateToUTCStr(now,"yyyy-MM-dd HH");
        return new MustRangeTimestamp(Constant.FIELD_TIMESTAMP,startUtcDateStr,endUtcDateStr,"yyyy-MM-dd HH");
    }

    public static MustRangeTimestamp getLast24HoursUTC(){
        Date beginDate = DateUtil.getNewDate(new Date(),-1);
        String startDateStr = DateUtil.Date2String(beginDate,"yyyy-MM-dd HH");
        Date startDate = DateUtil.stirng2Date(startDateStr,"yyyy-MM-dd HH");

        Date now = new Date();
        now.setHours(now.getHours()+1);

        String startUtcDateStr = DateUtil.localDateToUTCStr(startDate,"yyyy-MM-dd HH");
        String endUtcDateStr = DateUtil.localDateToUTCStr(now,"yyyy-MM-dd HH");
        return new MustRangeTimestamp(Constant.FIELD_TIMESTAMP,startUtcDateStr,endUtcDateStr,"yyyy-MM-dd HH");
    }

    public static MustRangeTimestamp getMRTByCSTDate(String cstStartDateStr,String cstEndDateStr){
        Date startDate = DateUtil.stirng2Date(cstStartDateStr,"yyyy-MM-dd HH:mm:ss");
        Date endDate = DateUtil.stirng2Date(cstEndDateStr,"yyyy-MM-dd HH:mm:ss");
        String startUtcDateStr = DateUtil.localDateToUTCStr(startDate,"yyyy-MM-dd HH:mm:ss");
        String endUtcDateStr = DateUtil.localDateToUTCStr(endDate,"yyyy-MM-dd HH:mm:ss");
        return new MustRangeTimestamp(Constant.FIELD_TIMESTAMP,startUtcDateStr,endUtcDateStr,"yyyy-MM-dd HH:mm:ss");
    }

    public static MustRangeTimestamp getMRTByCSTDate(Date startDate,Date endDate){
        String startUtcDateStr = DateUtil.localDateToUTCStr(startDate,"yyyy-MM-dd HH:mm:ss");
        String endUtcDateStr = DateUtil.localDateToUTCStr(endDate,"yyyy-MM-dd HH:mm:ss");
        return new MustRangeTimestamp(Constant.FIELD_TIMESTAMP,startUtcDateStr,endUtcDateStr,"yyyy-MM-dd HH:mm:ss");
    }

    public static MustRangeTimestamp getMRTByCSTDate(Date startDate,Date endDate,String format){
        String startUtcDateStr = DateUtil.localDateToUTCStr(startDate,format);
        String endUtcDateStr = DateUtil.localDateToUTCStr(endDate,format);
        return new MustRangeTimestamp(Constant.FIELD_TIMESTAMP,startUtcDateStr,endUtcDateStr,format);
    }

    public static Map<String,String> getNormalMap(String appName, String moduleName){
        Map<String,String> map = new HashMap<String,String>(2);
        if(!StringUtil.isBlank(appName)){
            map.put(Constant.FIELD_APP_NAME,appName);
        }
        if(!StringUtil.isBlank(moduleName)){
            map.put(Constant.FIELD_CLASS_NAME,moduleName);
        }
        return map;
    }

    public static void addToMap(Map<String,Long> map, String key, Long num){
        Long vv = map.get(key);
        if(vv == null){
            vv = 0L;
        }
        vv = vv+ (num == null?0:num);
        map.put(key,vv);
    }

    public static void addToMap(Map<String,String> map, String k, String v){
        if(!StringUtil.isBlank(v)){
            map.put(k,v);
        }
    }


    public static String[] eyToArrays(String appName,String host,String className,String result){
        List<String> list = new ArrayList<String> (4);
        if(appName!=null && !"".equals(appName.trim())){
            list.add(Constant.FIELD_APP_NAME);
        }
        if(host!=null && !"".equals(host.trim())){
            list.add(Constant.FIELD_HOST);
        }
        if(className!=null && !"".equals(className.trim())){
            list.add(Constant.FIELD_CLASS_NAME);
        }
        if(result!=null && !"".equals(result.trim())){
            list.add(Constant.FIELD_RESULT);
        }
        String[] tt = new String[list.size()];
        list.toArray(tt);
//        Arrays.sort(tt);
        return tt;
    }

    public static void fillError(DashboardBo dashboardBo, List<ErrorGroupByEntity> errorEntryList) {
        if(isEmpty(errorEntryList)){
            return;
        }
        String yesStr = DateUtil.Date2String(DateUtil.getNewDate(new Date(),-1),"yyyy-MM-dd");
//        long errorYes = 0;
        List<ErrorDetail> errorList = new ArrayList<ErrorDetail>();
        for(ErrorGroupByEntity tt : errorEntryList){
            errorList.add(new ErrorDetail(tt.getTimeStr(),new Long(tt.getNum()).intValue()));
//            if(tt.getTimeStr().startsWith(yesStr)){
//                errorYes += tt.getNum();
//            }
        }
//        dashboardBo.setErrorNum(errorYes);
        dashboardBo.setErrorList(errorList);
    }

    public static boolean isEmpty(List list){
        if(list == null || list.size()==0){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Map map){
        if(map == null || map.size()==0){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str,String isAlsoEmpty){
        if(str==null || "".equals(str.trim())){
            return true;
        }
        if(isAlsoEmpty != null && str.trim().equals(isAlsoEmpty.trim())){
            return true;
        }
        return false;
    }


    private static HostStatBo getHostStatBo(String host, Long reqNum, Long exNum,double maxTime,double minTime,double avgTime) {
        HostStatBo hostStatBo = new HostStatBo();
        hostStatBo.setHost(host);
        hostStatBo.setReqNum(reqNum==null?0:reqNum.longValue());
        hostStatBo.setErrorNum(exNum==null?0:exNum.intValue());
        hostStatBo.setQps(NumberUtil.devide(reqNum,86400L,2));
        hostStatBo.setErrorPercent(NumberUtil.devide(exNum,reqNum,2));

        hostStatBo.setMaxTime(NumberUtil.doubleToInt(maxTime));
        hostStatBo.setMinTime(NumberUtil.doubleToInt(minTime));
        hostStatBo.setAvgTime(NumberUtil.doubleToInt(avgTime));
        return hostStatBo;
    }

    private static AppStatBo getAppStatBo(String app, Long reqNum, Long exNum,double maxTime,double minTime,double avgTime) {
        AppStatBo appStatBo = new AppStatBo();
        appStatBo.setAppName(app);
        appStatBo.setReqNum(reqNum==null?0:reqNum.longValue());
        appStatBo.setErrorNum(exNum==null?0:exNum.intValue());
        appStatBo.setQps(NumberUtil.devide(reqNum,86400L,2));
        appStatBo.setErrorPercent(NumberUtil.devide(exNum,reqNum,2));
        appStatBo.setMaxTime(NumberUtil.doubleToInt(maxTime));
        appStatBo.setMinTime(NumberUtil.doubleToInt(minTime));
        appStatBo.setAvgTime(NumberUtil.doubleToInt(avgTime));
        return appStatBo;
    }



    public static String toIpStr(String ipObject) {
        if(ipObject.indexOf(".")>=0){
            return ipObject;
        }
        long ipaddress = NumberUtil.formatLong(ipObject,0);

        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((ipaddress >>> 24)));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x000000FF)));
        return sb.toString();
    }

    public static List<HostStatBo> hostStat(Map<String, AggsBucketsEntity> resMap) {
        if(isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<HostStatBo> resList = new ArrayList<HostStatBo>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            AggsBucketsEntity aggsBucketsEntity = entry.getValue();
            Long exNum = 0L;
            try {
                exNum = aggsBucketsEntity.getAggsBucketsEntityMap().get(Constant.STAT_RESULT_EXCEPTION).getValue();
            }catch (Exception e){
                logger.error("Assimble.hostStat error");
            }
            resList.add(getHostStatBo(toIpStr(aggsBucketsEntity.getKey()),aggsBucketsEntity.getValue(),exNum,aggsBucketsEntity.getMax(),aggsBucketsEntity.getMin(),aggsBucketsEntity.getAvg()));
        }
        return resList;
    }

    public static DashboardBo appModuleStat(Map<String, AggsBucketsEntity> resMap) {
        DashboardBo dashboardBo = new DashboardBo();
        if(isEmpty(resMap)){
            return dashboardBo;
        }

        long total=0;
        List<ReqDetail> appReqList = new ArrayList<ReqDetail>();
        List<ReqDetail> moduleReqList = new ArrayList<ReqDetail>();

        Map<String,Long> appStat = new HashMap<String,Long>();

        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            String key1AppName = entry.getKey();
            Map<String, AggsBucketsEntity> microMap = entry.getValue().getAggsBucketsEntityMap();

            for(Map.Entry<String, AggsBucketsEntity> entry2 : microMap.entrySet()){
                String key2ModuleName = entry2.getKey();
                long cModuleReqNum = entry2.getValue().getValue();
                total+=cModuleReqNum;
                addToMap(appStat,key1AppName,cModuleReqNum);

                moduleReqList.add(new ReqDetail(key2ModuleName,cModuleReqNum));
            }
        }

        for(Map.Entry<String,Long> entry : appStat.entrySet()){
            ReqDetail reqDetail = new ReqDetail();
            reqDetail.setName(entry.getKey());
            reqDetail.setNum(entry.getValue());
            appReqList.add(reqDetail);
        }

        dashboardBo.setAppNum(appStat.size());
        dashboardBo.setModuleNum(moduleReqList.size());
        dashboardBo.setAppReqNum(total);
        dashboardBo.setAppReqList(appReqList);
        dashboardBo.setModuleReqList(moduleReqList);

        return dashboardBo;
    }

    public static List<AppStatBo> appStat(Map<String, AggsBucketsEntity> resMap) {
        if(isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<AppStatBo> resList = new ArrayList<AppStatBo>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            AggsBucketsEntity aggsBucketsEntity = entry.getValue();
            Long exNum = 0L;
            try {
                exNum = aggsBucketsEntity.getAggsBucketsEntityMap().get(Constant.STAT_RESULT_EXCEPTION).getValue();
            }catch (Exception e){
                logger.error("Assimble.appStat error");
            }
            resList.add(getAppStatBo(aggsBucketsEntity.getKey(),aggsBucketsEntity.getValue(),exNum,aggsBucketsEntity.getMax(),aggsBucketsEntity.getMin(),aggsBucketsEntity.getAvg()));
        }
        return resList;
    }

    public static List<ModuleBo> aggsMapToModuleBo(Map<String, AggsBucketsEntity> resMap) {
        if(isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<ModuleBo> resList = new ArrayList<ModuleBo>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            if(StringUtil.isBlank(entry.getKey())){
                continue;
            }
            ModuleBo moduleBo = new ModuleBo(entry.getKey());
            resList.add(moduleBo);
        }
        return resList;
    }

    public static List<String> aggsMapToStr(Map<String, AggsBucketsEntity> resMap) {
        if(isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<String> resList = new ArrayList<String>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            if(StringUtil.isBlank(entry.getKey())){
                continue;
            }
            resList.add(entry.getKey());
        }
        return resList;
    }

    public static List<ModuleStatBo> aggsMapToModuleStatBoWith2Level(String moduleName, Map<String, AggsBucketsEntity> resMap) {
        if(isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<ModuleStatBo> resList = new ArrayList<ModuleStatBo>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            Map<String, AggsBucketsEntity> microMap = entry.getValue().getAggsBucketsEntityMap();
                Long exNum = 0L;
                if(microMap.get(Constant.STAT_RESULT_EXCEPTION) != null) {
                    exNum = microMap.get(Constant.STAT_RESULT_EXCEPTION).getValue();
                }
                ModuleStatBo ModuleStatBo= getModuleStatBo(moduleName,entry.getKey(),entry.getValue().getValue(),exNum,entry.getValue().getMax(),entry.getValue().getMin(),entry.getValue().getAvg());
                resList.add(ModuleStatBo);
        }
        return resList;
    }

    public static List<ModuleStatBo> aggsMapToModuleStatBoWith3Level( Map<String, AggsBucketsEntity> resMap) {
        if(isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<ModuleStatBo> resList = new ArrayList<ModuleStatBo>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            Map<String, AggsBucketsEntity> microMap = entry.getValue().getAggsBucketsEntityMap();
            for(Map.Entry<String, AggsBucketsEntity> microEntry : microMap.entrySet()){
                Map<String, AggsBucketsEntity> mmMap = microEntry.getValue().getAggsBucketsEntityMap();
                Long exNum = (mmMap==null||mmMap.size()==0||mmMap.get(Constant.STAT_RESULT_EXCEPTION)==null)?0L:(mmMap.get(Constant.STAT_RESULT_EXCEPTION).getValue());
                ModuleStatBo ModuleStatBo= getModuleStatBo(entry.getKey(),microEntry.getKey(),microEntry.getValue().getValue(),exNum,microEntry.getValue().getMax(),microEntry.getValue().getMin(),microEntry.getValue().getAvg());
                resList.add(ModuleStatBo);
            }
        }
        return resList;
    }

    private static ModuleStatBo getModuleStatBo(String moduleName, String methodName,Long reqNum, Long exNum,double maxTime,double minTime,double avgTime) {
        ModuleStatBo moduleStatBo = new ModuleStatBo();
        moduleStatBo.setModuleName(moduleName);
        moduleStatBo.setMethodName(methodName);
        moduleStatBo.setReqNum(reqNum==null?0:reqNum.longValue());
        moduleStatBo.setErrorNum(exNum==null?0:exNum.intValue());
        moduleStatBo.setQps(NumberUtil.devide(reqNum,86400L,2));
        moduleStatBo.setErrorPercent(NumberUtil.devide(exNum,reqNum,2));
        moduleStatBo.setMaxTime(NumberUtil.doubleToInt(maxTime));
        moduleStatBo.setMinTime(NumberUtil.doubleToInt(minTime));
        moduleStatBo.setAvgTime(NumberUtil.doubleToInt(avgTime));
        return moduleStatBo;
    }

    public static Map<String, String> getMap(String k1, String v1) {
        Map<String,String> map = new HashMap<String,String>(2);
        if(!StringUtil.isBlank(k1)){
            map.put(k1,v1);
        }
        return map;
    }

    public static Map<String, String> getMapValueNotNull(String k1, String v1) {
        Map<String,String> map = new HashMap<String,String>(2);
        if(!StringUtil.isBlank(k1) && !StringUtil.isBlank(v1)){
            map.put(k1,v1);
        }
        return map;
    }

    public static void putMapValueNotNull(Map<String,String> map,String k1, String v1) {
        if(!StringUtil.isBlank(k1) && !StringUtil.isBlank(v1)){
            map.put(k1,v1);
        }
    }

    public static Map<String, String> getMapValueNotNull(String k1, String v1,String k2, String v2) {
        Map<String,String> map = new HashMap<String,String>(2);
        if(!StringUtil.isBlank(k1) && !StringUtil.isBlank(v1)){
            map.put(k1,v1);
        }
        if(!StringUtil.isBlank(k2) && !StringUtil.isBlank(v2)){
            map.put(k2,v2);
        }
        return map;
    }

    public static List<MethodBo> transferToMethodBoList(List<ErrorGroupByEntity> esList) {
        if(isEmpty(esList)){
            return Collections.EMPTY_LIST;
        }
        List<MethodBo> resList = new ArrayList<MethodBo>(esList.size());
        for(ErrorGroupByEntity entity : esList){
            resList.add(new MethodBo(entity.getTimeStr(),entity.getNum()));
        }
        return resList;
    }

    public static List<AppInfoBo> toAppInfoBo(Map<String, AggsBucketsEntity> resMap) {
        if(isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<AppInfoBo> resList = new ArrayList<AppInfoBo>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            AggsBucketsEntity aggsBucketsEntity = entry.getValue();
            resList.add(new AppInfoBo(aggsBucketsEntity.getKey()));
        }
        return resList;
    }

    public static List<ErrorTypeBo> errorList(Map<String, AggsBucketsEntity> resMap) {
        if(isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<ErrorTypeBo> resList = new ArrayList<ErrorTypeBo>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            Map<String,AggsBucketsEntity> microMap = entry.getValue().getAggsBucketsEntityMap();
            for(Map.Entry<String, AggsBucketsEntity> microEntry : microMap.entrySet()){
                if(StringUtil.isBlank(microEntry.getKey())){
                    continue;
                }
                resList.add(new ErrorTypeBo(microEntry.getKey(),entry.getKey(),microEntry.getValue().getValue()));
            }
        }
        return resList;
    }

    public static List<ProblemBo> problemList(Map<String, AggsBucketsEntity> resMap) {
        if(isEmpty(resMap)){
            return Collections.EMPTY_LIST;
        }
        List<ProblemBo> resList = new ArrayList<ProblemBo>(resMap.size());
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            Map<String,AggsBucketsEntity> microMap2 = entry.getValue().getAggsBucketsEntityMap();
            for(Map.Entry<String, AggsBucketsEntity> microEntry2 : microMap2.entrySet()){
                if(StringUtil.isBlank(microEntry2.getKey())){
                    continue;
                }
                Map<String,AggsBucketsEntity> microMap3 = microEntry2.getValue().getAggsBucketsEntityMap();
                if(microMap3 == null){
                    logger.info("{} has no className and methodName ",microEntry2.getKey());
                    resList.add(new ProblemBo(entry.getKey(),microEntry2.getKey(),"unknown","unknown",
                            -1,microEntry2.getValue().getValue()));
                    continue;
                }
                for(Map.Entry<String, AggsBucketsEntity> microEntry3 : microMap3.entrySet()){
                    if(StringUtil.isBlank(microEntry3.getKey())){
                        continue;
                    }
                    Map<String,AggsBucketsEntity> microMap4 = microEntry3.getValue().getAggsBucketsEntityMap();
                    for(Map.Entry<String, AggsBucketsEntity> microEntry4 : microMap4.entrySet()){
                        if(StringUtil.isBlank(microEntry4.getKey())){
                            continue;
                        }
                        Map<String,AggsBucketsEntity> microMap5 = microEntry4.getValue().getAggsBucketsEntityMap();
                        for(Map.Entry<String, AggsBucketsEntity> microEntry5 : microMap5.entrySet()){
                            if(StringUtil.isBlank(microEntry5.getKey())){
                                continue;
                            }
                            resList.add(new ProblemBo(entry.getKey(),microEntry2.getKey(),microEntry3.getKey(),microEntry4.getKey(),
                                    NumberUtil.formatLong(microEntry5.getKey(),-1),microEntry5.getValue().getValue()));
                        }
                    }
                }
            }
        }
        return resList;
    }

    public static void  fillErrorList(DashboardBo dashboardBo ,Map<String, AggsBucketsEntity> resMap) {
        if(isEmpty(resMap)){
            return ;
        }
        List<ReqDetail> resList = new ArrayList<ReqDetail>(resMap.size());
        long errorNumYes = 0;
        for(Map.Entry<String, AggsBucketsEntity> entry : resMap.entrySet()){
            if(StringUtil.isBlank(entry.getKey())){
                continue;
            }
            resList.add(new ReqDetail(entry.getKey(),entry.getValue().getValue()));
            errorNumYes += entry.getValue().getValue();
        }
        dashboardBo.setErrorStat(resList);
        dashboardBo.setErrorNum(errorNumYes);
    }

    public static List<ErrorHostBo> logRecordEntityToErrorHostBo(String errorName,List<LogRecordEntity> queryList) {
        if(isEmpty(queryList)){
            return Collections.EMPTY_LIST;
        }
        List<ErrorHostBo> resList = new ArrayList<ErrorHostBo>(queryList.size());
        for(LogRecordEntity logRecordEntity :queryList  ){
            String timeStr = DateUtil.Date2String(DateUtil.utcStrToLocalDate(logRecordEntity.getTimestamp()),"yyyy-MM-dd HH:mm:ss");
            String id = logRecordEntity.getId();
            String ip = logRecordEntity.getHost();
            String appName = getAppNameFromId(id);
            resList.add(new ErrorHostBo(timeStr,id,errorName,ip,appName,1));
        }
        return resList;
    }

    public static String getAppNameFromId(String id){
        try {
            String fileName = id.substring(0, id.indexOf("."));
            return fileName ;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String getFileFromId(String id){
        try {
            String[] fileName = id.split("#");
            return fileName[0] ;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }


    public static long getLinePositionFromId(String id){
        try {
            String line = id.substring(id.lastIndexOf("#")+1);
            long position = NumberUtil.formatLong(line,-1L);
            return position ;
        }catch (Exception e){
            e.printStackTrace();
            return -1L;
        }
    }


    public static void main(String[] a){
        List<ErrorTypeBo> resList = new ArrayList<ErrorTypeBo>();
        ErrorTypeBo st = new ErrorTypeBo("NumberFormatException","ee",1);
        ErrorTypeBo st2 = new ErrorTypeBo("AddressException","ee",1);
        ErrorTypeBo st3 = new ErrorTypeBo("AAException","ee",1);
        resList.add(st);
        resList.add(st2);
        resList.add(st3);

        blackList(resList);


        System.out.println(JsonUtils.object2Json(resList));
    }

    private static final String findIP = "\\.(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
    private static final Pattern patternParam = Pattern.compile(findIP);

    public static void utctimeToBjtime(List<ErrorGroupByEntity> list) {
        for(ErrorGroupByEntity tt : list){
            Date temp = DateUtil.utcStrToLocalDate(tt.getTimeStr(),"yyyy-MM-dd HH");
            if(temp == null){
                tt.setTimeStr("");
            }else{
                tt.setTimeStr(DateUtil.Date2String(temp,"yyyy-MM-dd HH"));
            }
        }
    }

    public static void utctimeToBjtimeMethodBo(List<MethodBo> list) {
        if(list == null || list.size() ==0){
            return;
        }
        for(MethodBo tt : list){
            Date temp = DateUtil.utcStrToLocalDate(tt.getDate(),"yyyy-MM-dd HH");
            if(temp == null){
                tt.setDate("");
            }else{
                tt.setDate(DateUtil.Date2String(temp,"yyyy-MM-dd HH"));
            }
        }
    }

    public static List<TopTimeBo> toTopTimeBo(List<LogRecordStatEntity> queryList) {
        if(Assimble.isEmpty(queryList)){
            return Collections.EMPTY_LIST;
        }

        List<TopTimeBo> list = new ArrayList<TopTimeBo>(queryList.size());
        for(LogRecordStatEntity logRecordStatEntity : queryList){
            TopTimeBo topTimeBo = new TopTimeBo();
            topTimeBo.setHost(logRecordStatEntity.getHost());
            topTimeBo.setAppName(logRecordStatEntity.getAppName());
            topTimeBo.setModuleName(logRecordStatEntity.getClassName());
            topTimeBo.setMethodName(logRecordStatEntity.getMethod());
            topTimeBo.setParam(logRecordStatEntity.getParam());
            topTimeBo.setReqTime(DateUtil.utcStrToLocalDate(logRecordStatEntity.getTimestamp()));
            topTimeBo.setTime(logRecordStatEntity.getTime());
            list.add(topTimeBo);
        }

        return list;
    }

    public static List<ProblemDetailBo> problemDetailList(List<ExRecordEntity> entityList) {
        if(isEmpty(entityList)){
            return Collections.EMPTY_LIST;
        }
        List<ProblemDetailBo> resList = new ArrayList<ProblemDetailBo>(entityList.size());
        for(ExRecordEntity exRecordEntity :entityList){
            String timeStr = DateUtil.Date2String(DateUtil.utcStrToLocalDate(exRecordEntity.getTimestamp()),"yyyy-MM-dd HH:mm:ss");

            String id = exRecordEntity.getId();
            Date timestamp = DateUtil.stirng2Date(timeStr,"yyyy-MM-dd HH:mm:ss");
            String exName = exRecordEntity.getExName();
            String appName = exRecordEntity.getAppName();
            String host = exRecordEntity.getHost();
            String className = exRecordEntity.getClassName();
            String methodName = exRecordEntity.getMethod();
            long lineNum = exRecordEntity.getLineNum();
            String file = exRecordEntity.getFile();

            resList.add(new ProblemDetailBo(id,timestamp,appName,host,exName,className,methodName,lineNum));
        }
        return resList;
    }

    public static void blackList(List<ErrorTypeBo> resList) {
        if(resList == null || resList.size() ==0){
            return ;
        }
        List<String> errorBlackList = CacheData.getIns().getErrorBlackList();
        if(errorBlackList == null || errorBlackList.size() ==0){
            return;
        }
        for(int i = resList.size()-1;i>=0;i--){
             if(errorBlackList.contains(resList.get(i).getErrorName())){
                 resList.remove(i);
             }
        }
    }

    public static void blackListProblem(List<ProblemBo> resList) {
        if(resList == null || resList.size() ==0){
            return ;
        }
        List<String> errorBlackList = CacheData.getIns().getErrorBlackList();
        if(errorBlackList == null || errorBlackList.size() ==0){
            return;
        }
        for(int i = resList.size()-1;i>=0;i--){
            if(errorBlackList.contains(resList.get(i).getExName())){
                resList.remove(i);
            }
        }
    }
}
