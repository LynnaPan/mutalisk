package io.hybridtheory.mutalisk.mapper.template.aggregation;


import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;

public interface NElasticProxyAggregationTemplate {
    String name();
    AggregationBuilder apply(Object[] args);
    Object value(Aggregation aggregation);
}
