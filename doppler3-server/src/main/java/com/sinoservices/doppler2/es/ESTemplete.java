package com.sinoservices.doppler2.es;

import com.sinoservices.doppler2.Constant;
import com.sinoservices.doppler2.DopplerConstants;
import com.sinoservices.doppler2.bo.JobBo;
import com.sinoservices.doppler2.bo.QueryBo;
import com.sinoservices.doppler2.config.UpMessageConfig;
import com.sinoservices.doppler2.es.entity.*;
import com.sinoservices.doppler2.service.assimble.Assimble;
import com.sinoservices.doppler2.service.assimble.JobAssimble;
import com.sinoservices.util.StringUtil;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.min.InternalMin;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ESTemplete {
    private static final Logger logger = LoggerFactory.getLogger(ESTemplete.class);

    private static ESTemplete esTemplete = new ESTemplete();
    public static ESTemplete getIns(){
        return esTemplete;
    }


    public static void main(String[] a){
        UpMessageConfig.es_addr = "192.168.0.88:9300";
        Client client = ClientHelper.getInstance().getClient();
        Map<String,List<String>> shouldPram = new HashMap<String, List<String>>();
        List<String> ll = new ArrayList<String>(2);
        ll.add("123456789");
        ll.add("98765");
        shouldPram.put("jobId",ll);

        Map<String,String> mustPram = new HashMap<String, String>();
        mustPram.put("action","ACTION_JOB_OUT");
        String[] groupByFields = Assimble.eyToArrays("aa","ss","sa",Constant.FIELD_HOST);
        int maxMinAvgPoint = groupByFields.length;

        Map<String,AggsBucketsEntity> ss = ESTemplete.getIns().queryAppReqStatAggs2(DopplerConstants.INDEX_STAT_JOB_NAME,mustPram,null,shouldPram,groupByFields,maxMinAvgPoint,"time",null);
        List<JobBo> resOutList  = JobAssimble.assimbleOut(ss);

        ClientHelper.getInstance().returnPool(client);
    }

    public Map<String,AggsBucketsEntity> queryAppReqStatAggs(String indexName, Map<String,String> mustParamMap, MustRangeTimestamp timestamp,String[] groupByField){
        return queryAppReqStatAggs2(indexName,mustParamMap,timestamp,null,groupByField,-1,null,null);
    }

    public Map<String,AggsBucketsEntity> queryAppReqStatAggsWildcard(String indexName, Map<String,String> mustParamMap, MustRangeTimestamp timestamp,String[] groupByField,Map<String,String> wildcard){
        return queryAppReqStatAggs2(indexName,mustParamMap,timestamp,null,groupByField,-1,null,wildcard);
    }

    @Deprecated
    public Map<String,AggsBucketsEntity> queryAppReqStatAggs(String indexName, Map<String,String> mustParamMap, MustRangeTimestamp timestamp,Map<String,String> shouldPram,String[] groupByField,int maxMinAvgPoint,String maxMinAvgField){
        return queryAppReqStatAggs2(indexName,mustParamMap,timestamp,DSLAssemble.toMapList(shouldPram),groupByField,maxMinAvgPoint,maxMinAvgField,null);
    }

    public Map<String,AggsBucketsEntity> queryAppReqStatAggs2(String indexName, Map<String,String> mustParamMap, MustRangeTimestamp timestamp,Map<String,List<String>> shouldPram,String[] groupByField,int maxMinAvgPoint,String maxMinAvgField,Map<String,String> wildcard){

        Client client = ClientHelper.getInstance().getClient();
        try {
            Map<String, AggsBucketsEntity> resMap = new HashMap<String, AggsBucketsEntity>();
            String tempAggsName = "aggs_count";
            SearchRequestBuilder srb = client.prepareSearch(indexName)
                    .setSearchType(SearchType.QUERY_AND_FETCH).addFields(new String[]{""})
                    .setQuery(DSLAssemble.booleanMustQuery(mustParamMap, timestamp,null, shouldPram, null,wildcard))
                    .addAggregation(DSLAssemble.getAggs(tempAggsName, groupByField,maxMinAvgPoint,maxMinAvgField))
                    .setFrom(0).setSize(0)
                    .setExplain(false);

            if (logger.isDebugEnabled()) {
                logger.debug("DSL=" + srb.toString());
            }
            SearchResponse response = srb.execute().actionGet();
            logger.debug("spend time=" + response.getTookInMillis());

            Map<String, Aggregation> aggMap = response.getAggregations().asMap();
            Iterator<Terms.Bucket> appBucketIt = DSLAssemble.getTermsBucket(tempAggsName + "0",aggMap);

            while (appBucketIt.hasNext()) {
                Terms.Bucket gradeBucket = appBucketIt.next();
                AggsBucketsEntity aggsBucketsEntity = new AggsBucketsEntity(gradeBucket.getKeyAsString(), gradeBucket.getDocCount());
                resMap.put(gradeBucket.getKeyAsString(), aggsBucketsEntity);
                if(maxMinAvgPoint == 1) {
                    ESResultAssimble.assimbleMaxMinAvg(gradeBucket,maxMinAvgField,aggsBucketsEntity);
                }
                if (groupByField.length <= 1) {
                    continue;
                }
                Map<String, Aggregation> level2Agggs = gradeBucket.getAggregations().asMap();
                Iterator<Terms.Bucket> appBucketIt2 = DSLAssemble.getTermsBucket(tempAggsName + "1",level2Agggs);
                while (appBucketIt2.hasNext()) {
                    Terms.Bucket gradeBucket2 = appBucketIt2.next();
                    AggsBucketsEntity aggsBucketsEntity2 = new AggsBucketsEntity(gradeBucket2.getKeyAsString(), gradeBucket2.getDocCount());
                    aggsBucketsEntity.addAggs(gradeBucket2.getKeyAsString(), aggsBucketsEntity2);
                    if(maxMinAvgPoint == 2) {
                        ESResultAssimble.assimbleMaxMinAvg(gradeBucket2,maxMinAvgField,aggsBucketsEntity2);
                    }
                    if (groupByField.length == 2) {
                        continue;
                    }

                    Map<String, Aggregation> level3Agggs = gradeBucket2.getAggregations().asMap();
                    Iterator<Terms.Bucket> appBucketIt3 = DSLAssemble.getTermsBucket(tempAggsName + "2",level3Agggs);
                    while (appBucketIt3.hasNext()) {
                        Terms.Bucket gradeBucket3 = appBucketIt3.next();
                        AggsBucketsEntity aggsBucketsEntity3 = new AggsBucketsEntity(gradeBucket3.getKeyAsString(), gradeBucket3.getDocCount());
                        aggsBucketsEntity2.addAggs(gradeBucket3.getKeyAsString(), aggsBucketsEntity3);
                        if(maxMinAvgPoint == 3) {
                            ESResultAssimble.assimbleMaxMinAvg(gradeBucket3,maxMinAvgField ,aggsBucketsEntity3);
                        }
                        if (groupByField.length == 3) {
                            continue;
                        }

                        Map<String, Aggregation> level4Agggs = gradeBucket3.getAggregations().asMap();
                        Iterator<Terms.Bucket> appBucketIt4 = DSLAssemble.getTermsBucket(tempAggsName + "3",level4Agggs);
                        while (appBucketIt4.hasNext()) {
                            Terms.Bucket gradeBucket4 = appBucketIt4.next();
                            AggsBucketsEntity aggsBucketsEntity4 = new AggsBucketsEntity(gradeBucket4.getKeyAsString(), gradeBucket4.getDocCount());
                            aggsBucketsEntity3.addAggs(gradeBucket4.getKeyAsString(), aggsBucketsEntity4);
                            if(maxMinAvgPoint == 4) {
                                ESResultAssimble.assimbleMaxMinAvg(gradeBucket4,maxMinAvgField ,aggsBucketsEntity4);
                            }
                            if (groupByField.length == 4) {
                                continue;
                            }

                            Map<String, Aggregation> level5Agggs = gradeBucket4.getAggregations().asMap();
                            Iterator<Terms.Bucket> appBucketIt5 = DSLAssemble.getTermsBucket(tempAggsName + "4",level5Agggs);
                            while (appBucketIt5.hasNext()) {
                                Terms.Bucket gradeBucket5 = appBucketIt5.next();
                                AggsBucketsEntity aggsBucketsEntity5 = new AggsBucketsEntity(gradeBucket5.getKeyAsString(), gradeBucket5.getDocCount());
                                aggsBucketsEntity4.addAggs(gradeBucket5.getKeyAsString(), aggsBucketsEntity5);
                                if(maxMinAvgPoint == 5) {
                                    ESResultAssimble.assimbleMaxMinAvg(gradeBucket5,maxMinAvgField ,aggsBucketsEntity5);
                                }
                                if (groupByField.length == 5) {
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            return resMap;
        }finally {
            ClientHelper.getInstance().returnPool(client);
        }
    }

    public List<ErrorGroupByEntity> queryByDateAggs(String indexName, Map<String,String> mustParamMap,Map<String,String> mustNotParamMap, MustRangeTimestamp timestamp, Map<String,String> shouldParamMap){
        String tempAggsName ="reqstat_over_2h" ;
        Client client = ClientHelper.getInstance().getClient();
        try {
            SearchRequestBuilder srb = client.prepareSearch(indexName)
                    .setSearchType(SearchType.QUERY_AND_FETCH).addFields(new String[]{""})
                    .setQuery(DSLAssemble.booleanMustNotQuery(mustParamMap, mustNotParamMap, timestamp, shouldParamMap, null))
                    .addAggregation(DSLAssemble.getDateHistogramBuilder(tempAggsName, timestamp))
                    .setExplain(false);
            if (logger.isDebugEnabled()) {
                logger.debug("DSL=" + srb.toString());
            }
            SearchResponse response = srb.execute().actionGet();
            logger.debug("spend time=" + response.getTookInMillis());

            Histogram histogram = response.getAggregations().get(tempAggsName);
            List<Histogram.Bucket> buckets = (List<Histogram.Bucket>) histogram.getBuckets();
            List<ErrorGroupByEntity> resList = new ArrayList<ErrorGroupByEntity>();
            for (Histogram.Bucket bucket : buckets) {
                resList.add(new ErrorGroupByEntity(bucket.getKeyAsString(), bucket.getDocCount()));
            }
            return resList;
        }finally {
            ClientHelper.getInstance().returnPool(client);
        }
    }

    public List<LogRecordEntity> queryLogRecord(String indexName, Map<String,String> mustParamMap,MustRangeTimestamp timestamp, Map<String,String> shouldMatchMap,int page,int pageSize){
        List<HitEntity> resList = queryByField2(indexName,mustParamMap,timestamp,null,null,shouldMatchMap,Constant.FIELD_TIMESTAMP,false,LogRecordEntity.getFields(),page,pageSize);
        List<LogRecordEntity> logRecordList = new ArrayList<LogRecordEntity>();
        for (HitEntity hitEntity :resList  ) {
            LogRecordEntity logRecord = new LogRecordEntity(hitEntity.getId(),hitEntity.getValueMap(),hitEntity.getHighlightValueMap());
            logRecordList.add(logRecord);
        }
        return logRecordList;
    }

    public List<LogRecordEntity> queryLogRecordByScore(String indexName, Map<String,String> mustParamMap,MustRangeTimestamp timestamp, Map<String,String> shouldMatchMap,int page,int pageSize){
        List<HitEntity> resList = queryByField2(indexName,mustParamMap,timestamp,null,null,shouldMatchMap,null,false,LogRecordEntity.getFields(),page,pageSize);
        List<LogRecordEntity> logRecordList = new ArrayList<LogRecordEntity>();
        for (HitEntity hitEntity :resList  ) {
            LogRecordEntity logRecord = new LogRecordEntity(hitEntity.getId(),hitEntity.getValueMap(),hitEntity.getHighlightValueMap());
            logRecordList.add(logRecord);
        }
        return logRecordList;
    }

    public List<OpEntity> queryOpStatRecordByScore(String indexName, Map<String,String> mustParamMap,MustRangeTimestamp timestamp, Map<String, String> shouldMatchMap,String orderField,boolean asc,int page,int pageSize){
        List<HitEntity> resList = queryByField2(indexName,mustParamMap,timestamp,null,null,shouldMatchMap,orderField,asc,OpEntity.getFields(),page,pageSize);
        List<OpEntity> opRecordList = new ArrayList<OpEntity>();
        for (HitEntity hitEntity :resList  ) {
            OpEntity opEntity = new OpEntity(hitEntity.getId(),hitEntity.getValueMap(),hitEntity.getHighlightValueMap());
            opRecordList.add(opEntity);
        }
        return opRecordList;
    }

    public List<LogRecordEntity> queryLogRecordSort(String indexName, Map<String,String> mustParamMap,MustRange range, Map<String,String> shouldMatchMap,String sortField,Boolean sortASC,int page,int pageSize){
        List<HitEntity> resList = queryByField2(indexName,mustParamMap,null,range,null,shouldMatchMap,sortField,sortASC,LogRecordEntity.getFields(),page,pageSize);
        List<LogRecordEntity> logRecordList = new ArrayList<LogRecordEntity>();
        for (HitEntity hitEntity :resList  ) {
            LogRecordEntity logRecord = new LogRecordEntity(hitEntity.getId(),hitEntity.getValueMap(),hitEntity.getHighlightValueMap());
            logRecordList.add(logRecord);
        }
        return logRecordList;
    }

    public List<LogRecordStatEntity> queryStatLogRecordSort(String indexName, Map<String,String> mustParamMap,MustRangeTimestamp range, Map<String,String> shouldMatchMap,String sortField,Boolean sortASC,String[] queryFields,int page,int pageSize){
        List<HitEntity> resList = queryByField2(indexName,mustParamMap,range,null,null,shouldMatchMap,sortField,sortASC,queryFields,page,pageSize);
        List<LogRecordStatEntity> logRecordStatList = new ArrayList<LogRecordStatEntity>();
        for (HitEntity hitEntity :resList  ) {
            LogRecordStatEntity logRecordStat = new LogRecordStatEntity(hitEntity.getId(),hitEntity.getValueMap(),hitEntity.getHighlightValueMap());
            logRecordStatList.add(logRecordStat);
        }
        return logRecordStatList;
    }

    public List<LogRecordJobEntity> queryJobLogRecordSort(String indexName, Map<String,String> mustParamMap,MustRangeTimestamp range ,Map<String,List<String>> shouldMap,String sortField,Boolean sortASC,int page,int pageSize){
        List<HitEntity> resList = queryByField2(indexName,mustParamMap,range,null,shouldMap,null,sortField,sortASC,LogRecordJobEntity.getFields(),page,pageSize);
        List<LogRecordJobEntity> jobList = new ArrayList<LogRecordJobEntity>();
        for (HitEntity hitEntity :resList  ) {
            LogRecordJobEntity jobEntity = new LogRecordJobEntity(hitEntity.getId(),hitEntity.getValueMap(),hitEntity.getHighlightValueMap());
            jobList.add(jobEntity);
        }
        return jobList;
    }

    public List<ExRecordEntity> queryExStatRecordSort(String indexName, Map<String,String> mustParamMap,MustRangeTimestamp range ,Map<String,List<String>> shouldMap,String sortField,Boolean sortASC,int page,int pageSize){
        List<HitEntity> resList = queryByField2(indexName,mustParamMap,range,null,shouldMap,null,sortField,sortASC,ExRecordEntity.getFields(),page,pageSize);
        List<ExRecordEntity> exStatList = new ArrayList<ExRecordEntity>();
        for (HitEntity hitEntity :resList) {
            ExRecordEntity jobEntity = new ExRecordEntity(hitEntity.getId(),hitEntity.getValueMap());
            exStatList.add(jobEntity);
        }
        return exStatList;
    }

    @Deprecated
    public List<HitEntity> queryByField(String indexName, Map<String,String> mustParamMap, MustRangeTimestamp timestamp,MustRange range, Map<String,String> shouldParamMap,Map<String,String> shouldMatchMap,
                                         String sortField,Boolean sortASC, String[] fileds, int page, int pageSize) {
        return queryByField2(indexName,mustParamMap,timestamp,range,DSLAssemble.toMapList(shouldParamMap),shouldMatchMap,sortField,sortASC,fileds,page,pageSize);
    }

    public List<HitEntity> queryByField2(String indexName, Map<String,String> mustParamMap, MustRangeTimestamp timestamp,MustRange range, Map<String,List<String>> shouldMap,Map<String,String> shouldMatchMap,String sortField,Boolean sortASC, String[] fileds, int page, int pageSize){
        if(page <= 0){
            page = 1;
        }
        Client client = ClientHelper.getInstance().getClient();
        try {
            SearchRequestBuilder srb = client.prepareSearch(indexName)
                    .setSearchType(SearchType.QUERY_AND_FETCH).addFields(fileds)
                    .setQuery(DSLAssemble.booleanMustQuery(mustParamMap, timestamp,range, shouldMap, shouldMatchMap,null))
                    .setFrom((page - 1) * pageSize).setSize(pageSize).setExplain(false);
            if(sortField != null && !"".equals(sortField.trim())) srb.addSort(DSLAssemble.sortField(sortField,sortASC));

            String hightlight = Constant.FIELD_ORIGINAL;
            if(shouldMatchMap!=null&&shouldMatchMap.size()==1){
                try {
                    hightlight = shouldMatchMap.keySet().iterator().next();
                }catch (Exception e){}
            }

            srb.addHighlightedField(hightlight).setHighlighterFragmentSize(500)
                    .setHighlighterEncoder("UTF-8")
                    .setHighlighterPreTags("<font color=red>")
                    .setHighlighterPostTags("</font>");
            if (logger.isDebugEnabled()) {
                logger.debug("DSL=" + srb.toString());
            }
            SearchResponse response = srb.execute().actionGet();
            logger.debug("spend time=" + response.getTookInMillis());

            SearchHits hits = response.getHits();
            List<HitEntity> hitList = new ArrayList<HitEntity>(hits.getHits().length);
            for (int i = 0; i < hits.getHits().length; i++) {
                hitList.add(new HitEntity(hits.getHits()[i].getId(), hits.getHits()[i].getFields(), hits.getHits()[i].getHighlightFields()));
            }
            return hitList;
        }finally {
            ClientHelper.getInstance().returnPool(client);
        }
    }

    public Map<String,Object> getById(String indexName, String type, String id){
        Client client = ClientHelper.getInstance().getClient();
        try {
            GetRequestBuilder mgb = client.prepareGet(indexName,type,id);
            GetResponse getResponse = mgb.get();
            return getResponse.getSourceAsMap();
        }finally {
            ClientHelper.getInstance().returnPool(client);
        }
    }

    public List<Map<String,Object>> mutiSearchByIds(String indexName, String type, String[] ids, String[] fields){
        Client client = ClientHelper.getInstance().getClient();
        try {
            MultiGetRequestBuilder mgb = client.prepareMultiGet();
            DSLAssemble.addItem(indexName,type,mgb,ids,fields);
            MultiGetResponse multiGetItemResponses = mgb.get();

            List<Map<String,Object>> resList = new ArrayList<Map<String,Object>>();
            for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
                GetResponse response = itemResponse.getResponse();
                if (!response.isExists()) {
                    continue;
                }
                Map<String,Object> microMap = ResultProcess.process(response.getFields());
                microMap.put("id",response.getId());
                resList.add(microMap);
                }
            return resList;
        }finally {
            ClientHelper.getInstance().returnPool(client);
        }
    }

    public void updateById(String indexName, String type,String id,String field1,Object value1,String field2,Object value2){
        Client client = ClientHelper.getInstance().getClient();
        try {
            UpdateRequest updateRequest = new UpdateRequest(indexName, type,id)
                    .doc(XContentFactory.jsonBuilder()
                            .startObject()
                            .field(field1, value1).field(field2, value2)
                            .endObject()
                    );
           UpdateResponse pp = client.update(updateRequest).get();
            pp.getShardInfo().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            ClientHelper.getInstance().returnPool(client);
        }
    }
}
