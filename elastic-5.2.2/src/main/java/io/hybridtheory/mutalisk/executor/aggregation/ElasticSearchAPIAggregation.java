package io.hybridtheory.mutalisk.executor.aggregation;


import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;

public interface ElasticSearchAPIAggregation {
    AggregationBuilder toAggregationBuilder();

    String name();

    Object value(Aggregation aggregation);
}
