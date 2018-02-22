package io.hybridtheory.mutalisk.common.mapper.template.filter;

import io.hybridtheory.mutalisk.common.api.ElasticAPIExecutor;
import io.hybridtheory.mutalisk.common.api.filter.ElasticAPIFilter;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;

public interface ElasticFilterTemplate extends ElasticTemplate {
    @Override
    ElasticAPIFilter apply(ElasticAPIExecutor executor, Object[] args);

}
