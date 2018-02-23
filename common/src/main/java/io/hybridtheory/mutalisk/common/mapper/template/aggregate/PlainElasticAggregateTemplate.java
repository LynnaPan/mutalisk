package io.hybridtheory.mutalisk.common.mapper.template.aggregate;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.common.api.aggregate.PlainElasticAggregate;
import io.hybridtheory.mutalisk.common.mapper.annotation.aggregation.ElasticSearchAggregate;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;

public class PlainElasticAggregateTemplate implements ElasticAggregateTemplate {
    ElasticSearchAggregate aggregate;

    public PlainElasticAggregateTemplate(ElasticSearchAggregate aggregate) {
        this.aggregate = aggregate;
    }

    @Override
    public ElasticAggregate apply(ElasticExecutor executor, Object[] args) {
        return new PlainElasticAggregate(aggregate.type(), aggregate.name(), aggregate.key());
    }
}
