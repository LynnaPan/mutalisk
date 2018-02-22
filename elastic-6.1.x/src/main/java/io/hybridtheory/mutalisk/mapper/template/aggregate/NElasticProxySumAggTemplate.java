package io.hybridtheory.mutalisk.mapper.template.aggregate;


import io.hybridtheory.mutalisk.aggregate.ElasticAggregate;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;

public class NElasticProxySumAggTemplate implements NElasticAggregateTemplate {
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
    public ElasticAggregate apply(Object[] args) {
        return null;
    }

    @Override
    public Object value(Aggregation aggregation) {
        return ((Sum) aggregation).getValue();
    }
}
