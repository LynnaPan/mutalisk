package io.hybridtheory.mutalisk.executor.transport.mapper.template.aggregation;


import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;

public class NElasticProxySumAggTemplate implements NElasticProxyAggregationTemplate {
    String name;
    String key;

    public NElasticProxySumAggTemplate(String name, String key) {
        this.name = name;
        this.key = key;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public AggregationBuilder apply(Object[] args) {
        return AggregationBuilders.sum(name).field(key);
    }

    @Override
    public Object value(Aggregation aggregation) {
        return ((Sum) aggregation).getValue();
    }
}
