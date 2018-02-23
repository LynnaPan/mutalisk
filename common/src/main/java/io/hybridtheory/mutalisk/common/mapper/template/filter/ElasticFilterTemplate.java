package io.hybridtheory.mutalisk.common.mapper.template.filter;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;

public interface ElasticFilterTemplate extends ElasticTemplate {
    @Override
    ElasticFilter apply(ElasticExecutor executor, Object[] args);

}
