package io.hybridtheory.mutalisk.aggregate;


import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;

public interface ElasticAggregate {
    AggregationBuilder builder();

    String name();

    Object value(Aggregation aggregation);
}
