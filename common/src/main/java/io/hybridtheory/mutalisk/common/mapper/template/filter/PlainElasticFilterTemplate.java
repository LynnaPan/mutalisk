package io.hybridtheory.mutalisk.common.mapper.template.filter;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.api.filter.PlainElasticFilter;
import io.hybridtheory.mutalisk.common.mapper.annotation.filter.ElasticSearchFilter;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;

public class PlainElasticFilterTemplate implements ElasticTemplate {
    ElasticSearchFilter filter;

    public PlainElasticFilterTemplate(ElasticSearchFilter filter) {
        this.filter = filter;
    }

    @Override
    public ElasticFilter apply(ElasticExecutor executor, Object[] args) {
        return new PlainElasticFilter(filter.type(), filter.parameters());
    }
}
