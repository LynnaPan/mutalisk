package io.hybridtheory.mutalisk.common.mapper.template.aggregate;

import io.hybridtheory.mutalisk.common.api.ElasticAPIExecutor;
import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAPIAggregate;
import io.hybridtheory.mutalisk.common.api.aggregate.PlainElasticAPIAggregate;
import io.hybridtheory.mutalisk.common.mapper.annotation.aggregation.ElasticAggregate;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;

public class PlainElasticAggregateTemplate implements ElasticTemplate {
    ElasticAggregate aggregate;

    public PlainElasticAggregateTemplate(ElasticAggregate aggregate) {
        this.aggregate = aggregate;
    }

    @Override
    public ElasticAPIAggregate apply(ElasticAPIExecutor executor, Object[] args) {
        return new PlainElasticAPIAggregate(aggregate.type(), aggregate.name(), aggregate.key());
    }
}
