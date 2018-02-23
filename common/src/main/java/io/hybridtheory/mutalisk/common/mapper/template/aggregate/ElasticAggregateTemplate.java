package io.hybridtheory.mutalisk.common.mapper.template.aggregate;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;

public interface ElasticAggregateTemplate extends ElasticTemplate {
    @Override
    public ElasticAggregate apply(ElasticExecutor executor, Object[] args);
}
