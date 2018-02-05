package io.hybridtheory.mutalisk.executor.aggregation;


import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

public class ElasticSumSearchAPIAggregation implements ElasticSearchAPIAggregation {
    String name;
    String key;
    String field;


    public ElasticSumSearchAPIAggregation(String name, String key, String field) {
        this.name = name;
        this.key = key;
        this.field = field;
    }

    @Override
    public AggregationBuilder toAggregationBuilder() {
        return AggregationBuilders.sum(key).field(field);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Object value(Aggregation aggregation) {
        return null;
    }
}
