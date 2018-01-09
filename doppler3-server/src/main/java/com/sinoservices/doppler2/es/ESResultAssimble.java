package com.sinoservices.doppler2.es;

import com.sinoservices.doppler2.es.entity.AggsBucketsEntity;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.min.InternalMin;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class ESResultAssimble {
    public static void assimbleMaxMinAvg(Terms.Bucket bucket, String time, AggsBucketsEntity aggsBucketsEntity) {
        Map<String, Aggregation> maxMinAvgAgggs = bucket.getAggregations().asMap();
        double maxValue = ((InternalMax)maxMinAvgAgggs.get("max"+time)).getValue();
        double minValue = ((InternalMin)maxMinAvgAgggs.get("min"+time)).getValue();
        double avgValue = ((InternalAvg)maxMinAvgAgggs.get("avg"+time)).getValue();
        aggsBucketsEntity.setMax(maxValue);
        aggsBucketsEntity.setMin(minValue);
        aggsBucketsEntity.setAvg(avgValue);
    }
}
