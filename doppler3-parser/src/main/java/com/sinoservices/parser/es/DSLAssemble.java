package com.sinoservices.parser.es;

import com.sinoservices.parser.es.entity.StatEntity;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class DSLAssemble {

    public static QueryBuilder booleanMustQuery(Map<String,String> mustPram) {
        BoolQueryBuilder bq = QueryBuilders.boolQuery();

        if(mustPram!=null && mustPram.size() > 0) {
            for (Map.Entry<String, String> entry : mustPram.entrySet()) {
                bq = bq.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
            }
        }
        return bq;
    }

    public static Map<String, String> getMustMap(StatEntity statEntity) {
        Map<String, String> map = new HashMap<String, String>(2);
        map.put("appName",statEntity.getAppName());
        map.put("className",statEntity.getClassName());
        return map;
    }
}
