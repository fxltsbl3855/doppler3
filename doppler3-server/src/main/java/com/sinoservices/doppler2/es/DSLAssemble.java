package com.sinoservices.doppler2.es;

import com.sinoservices.doppler2.es.entity.MustRange;
import com.sinoservices.doppler2.es.entity.MustRangeTimestamp;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.ValuesSourceMetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxBuilder;
import org.elasticsearch.search.aggregations.metrics.min.MinBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class DSLAssemble {

    public static String DATE_FORMAT_YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public static void addItem(String indexName, String type,MultiGetRequestBuilder mgb,String[] ids,String[] fields){
        for(int i = 0; i < ids.length; i++){
            MultiGetRequest.Item item = new MultiGetRequest.Item(indexName,type,ids[i]);
            item.fields(fields);
            mgb.add(item);
        }
    }

    public static void setBSToBuilder(TermsBuilder temp, String field){
        MaxBuilder maxBuilder = new MaxBuilder("max"+field);maxBuilder.field(field);
        MinBuilder minBuilder = new MinBuilder("min"+field);minBuilder.field(field);
        AvgBuilder avgBuilder = new AvgBuilder("avg"+field);avgBuilder.field(field);

        temp.subAggregation(maxBuilder);
        temp.subAggregation(minBuilder);
        temp.subAggregation(avgBuilder);
    }

    public static TermsBuilder getAggs(String tempAggsName,String[] groupByField,int maxMinAvgPoint,String maxMinAvgField){
        if(groupByField.length==0 || groupByField.length>5){
            throw new RuntimeException("DSLAssemble.getAggsCount.param error,groupByField array length should be [1,5],now length is "+groupByField.length);
        }
        TermsBuilder termsBuilder = AggregationBuilders.terms(tempAggsName+"0").field(groupByField[0]).size(100);
        if(maxMinAvgPoint == 1){
            setBSToBuilder(termsBuilder,maxMinAvgField);
        }
        if(groupByField.length==1){
            return termsBuilder;
        }

        TermsBuilder temp = AggregationBuilders.terms(tempAggsName+"1").field(groupByField[1]).size(100);
        if(maxMinAvgPoint == 2) {
            setBSToBuilder(temp,maxMinAvgField);
        }
        termsBuilder.subAggregation(temp);
        if(groupByField.length==2){
            return termsBuilder;
        }

        TermsBuilder temp2 = AggregationBuilders.terms(tempAggsName+"2").field(groupByField[2]).size(100);
        if(maxMinAvgPoint == 3) {
            setBSToBuilder(temp2,maxMinAvgField);
        }
        temp.subAggregation(temp2);
        if(groupByField.length ==3){
            return termsBuilder;
        }

        TermsBuilder temp3 = AggregationBuilders.terms(tempAggsName+"3").field(groupByField[3]).size(100);
        if(maxMinAvgPoint == 4) {
            setBSToBuilder(temp3,maxMinAvgField);
        }
        temp2.subAggregation(temp3);
        if(groupByField.length ==4){
            return termsBuilder;
        }

        TermsBuilder temp4 = AggregationBuilders.terms(tempAggsName+"4").field(groupByField[4]).size(100);
        if(maxMinAvgPoint == 5) {
            setBSToBuilder(temp4,maxMinAvgField);
        }
        temp3.subAggregation(temp4);

        return termsBuilder;
    }

    public static DateHistogramBuilder getDateHistogramBuilder(String tempAggsName,MustRangeTimestamp timestamp){
        DateHistogramBuilder dateAgg = AggregationBuilders.dateHistogram(tempAggsName);
        dateAgg.field("timestamp");
        dateAgg.interval(DateHistogramInterval.hours(timestamp.getIntervalHours()));
        dateAgg.format(DSLAssemble.DATE_FORMAT_YYYY_MM_DD_HH);
        dateAgg.minDocCount(0L);
        dateAgg.extendedBounds(timestamp.getFrom(),timestamp.getTo());
        return dateAgg;
    }

    @Deprecated
    public static QueryBuilder booleanMustQuery(Map<String,String> mustPram, MustRangeTimestamp timestamp, MustRange range, Map<String,List<String>> shouldPram, Map<String,String> shouldMatch,Map<String,String> wildcard) {
        BoolQueryBuilder bq = QueryBuilders.boolQuery();
        if(mustPram!=null && mustPram.size() > 0) {
            for (Map.Entry<String, String> entry : mustPram.entrySet()) {
                bq = bq.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
            }
        }

        if(timestamp!=null ) {
            bq = bq.must(QueryBuilders.rangeQuery(timestamp.getField()).from(timestamp.getFrom()).to(timestamp.getTo()).format(timestamp.getFormat()));
        }

        if(range != null ) {
            bq = bq.must(QueryBuilders.rangeQuery(range.getField()).from(range.getFrom()).to(range.getTo()));
        }

        if(wildcard != null && wildcard.size() > 0) {
            for (Map.Entry<String,String> entry : wildcard.entrySet()) {
                bq = bq.must(new WildcardQueryBuilder(entry.getKey(), "*"+entry.getValue()+"*"));
            }
        }

        if(shouldPram!=null && shouldPram.size() > 0) {
            for (Map.Entry<String, List<String>> entry : shouldPram.entrySet()) {
                bq = bq.should(QueryBuilders.termsQuery(entry.getKey(), entry.getValue()));
            }
            bq.minimumNumberShouldMatch(1);
        }

        if(shouldMatch!=null && shouldMatch.size() > 0) {
            for (Map.Entry<String, String> entry : shouldMatch.entrySet()) {
                bq = bq.should(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
            }
            bq.minimumNumberShouldMatch(shouldMatch.size());
        }
        return bq;
    }

    public static QueryBuilder booleanMustNotQuery(Map<String,String> mustPram,Map<String,String> mustNotPram, MustRangeTimestamp timestamp, Map<String,String> shouldPram,Map<String,String> shouldMatch) {
        BoolQueryBuilder bq = QueryBuilders.boolQuery();
        if(mustPram!=null && mustPram.size() > 0) {
            for (Map.Entry<String, String> entry : mustPram.entrySet()) {
                bq = bq.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
            }
        }

        if(mustNotPram!=null && mustNotPram.size() > 0) {
            for (Map.Entry<String, String> entry : mustNotPram.entrySet()) {
                bq = bq.mustNot(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
            }
        }

        if(timestamp!=null ) {
            bq = bq.must(QueryBuilders.rangeQuery(timestamp.getField()).from(timestamp.getFrom()).to(timestamp.getTo()).format(timestamp.getFormat()));
        }

        if(shouldPram!=null && shouldPram.size() > 0) {
            for (Map.Entry<String, String> entry : shouldPram.entrySet()) {
                bq = bq.should(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
            }
            bq.minimumNumberShouldMatch(shouldPram.size());
        }

        if(shouldMatch!=null && shouldMatch.size() > 0) {
            for (Map.Entry<String, String> entry : shouldMatch.entrySet()) {
                bq = bq.should(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
            }
            bq.minimumNumberShouldMatch(shouldMatch.size());
        }
        return bq;
    }

    public static SortBuilder sortField(String sortField, Boolean sortASC) {
        SortBuilder sort = SortBuilders.fieldSort(sortField);
        if(sortASC == null || sortASC.booleanValue()) {
            sort.order(SortOrder.ASC);
        }else{
            sort.order(SortOrder.DESC);
        }
        return sort;
    }

    public static Iterator<Terms.Bucket> getTermsBucket(String tempAggsName, Map<String, Aggregation> aggMap) {
        Object terms = aggMap.get(tempAggsName);
        Iterator<Terms.Bucket> appBucketIt;
        if (terms instanceof LongTerms) {
            LongTerms longTerms = (LongTerms) aggMap.get(tempAggsName);
            appBucketIt = longTerms.getBuckets().iterator();
        } else {
            StringTerms appTerms = (StringTerms) aggMap.get(tempAggsName);
            appBucketIt = appTerms.getBuckets().iterator();
        }
        return appBucketIt;
    }

    public static Map<String, List<String>> toMapList(Map<String, String> shouldPram) {
        if(shouldPram == null) {
            return null;
        }
        Map<String,List<String>> newShouldPram = new HashMap<String, List<String>>(shouldPram.size());
        for(Map.Entry<String,String> entry : shouldPram.entrySet()){
            List<String> tt = new ArrayList<String>(1);
            newShouldPram.put(entry.getKey(),tt);
        }

        return newShouldPram;
    }


//    public static void query(){
//        SearchResponse response = ClientHelper.getInstance().getClient().prepareSearch("test5")
//                .setTypes("SYSLOG")
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).addFields("timestamp","original")
//                .setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("moduleName", "module1")).must(QueryBuilders.termQuery("className", "className1")))             // Query
//                // .setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
//                .setFrom(0).setSize(10).setExplain(false)
//                .execute()
//                .actionGet();
////        SearchResponse response = ClientHelper.getInstance().getClient().prepareSearch().execute().actionGet();
//
//        SearchHits hits = response.getHits();
//        for (int i = 0; i < hits.getHits().length; i++) {
//            Map<String, SearchHitField> resMap = hits.getHits()[i].getFields();
//        }
////        System.out.println(response.toString());
//    }
}
