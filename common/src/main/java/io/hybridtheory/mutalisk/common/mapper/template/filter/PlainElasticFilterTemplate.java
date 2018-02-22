package io.hybridtheory.mutalisk.common.mapper.template.filter;

import io.hybridtheory.mutalisk.common.api.ElasticAPIExecutor;
import io.hybridtheory.mutalisk.common.api.filter.ElasticAPIFilter;
import io.hybridtheory.mutalisk.common.api.filter.PlainElasticAPIFilter;
import io.hybridtheory.mutalisk.common.mapper.annotation.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;

public class PlainElasticFilterTemplate implements ElasticTemplate {
    ElasticFilter filter;

    public PlainElasticFilterTemplate(ElasticFilter filter) {
        this.filter = filter;
    }

    @Override
    public ElasticAPIFilter apply(ElasticAPIExecutor executor, Object[] args) {
        return new PlainElasticAPIFilter(filter.type(), filter.parameters());
    }
}
