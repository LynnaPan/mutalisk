package io.hybridtheory.mutalisk.executor.aggregation;


import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

public class ElasticTermSearchAPIAggregation implements ElasticSearchAPIAggregation {
    String name;
    String key;
    String field;


    public ElasticTermSearchAPIAggregation(String name, String key, String field) {
        this.name = name;
        this.key = key;
        this.field = field;
    }

    @Override
    public AggregationBuilder toAggregationBuilder() {
        return AggregationBuilders.terms(key).field(field);
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public Object value(Aggregation aggregation) {
        return null;
    }
}
