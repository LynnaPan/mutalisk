package io.hybridtheory.mutalisk.mapper.template.aggregate;


import io.hybridtheory.mutalisk.aggregate.ElasticAggregate;
import org.elasticsearch.search.aggregations.Aggregation;

public interface NElasticAggregateTemplate {
    String name();

    ElasticAggregate apply(Object[] args);

    Object value(Aggregation aggregation);
}
