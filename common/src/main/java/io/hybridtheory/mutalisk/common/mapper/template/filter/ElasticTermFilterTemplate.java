package io.hybridtheory.mutalisk.common.mapper.template.filter;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;

public class ElasticTermFilterTemplate implements ElasticFilterTemplate {
    private String field;
    private String valueParamName;

    public ElasticTermFilterTemplate(String field, String valueParamName) {
        this.field = field;
        this.valueParamName = valueParamName;
    }

    @Override
    public ElasticFilter apply(ElasticExecutor executor, Object[] args) {
        return null;
    }
}
