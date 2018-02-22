package io.hybridtheory.mutalisk.common.api.filter;

import io.hybridtheory.mutalisk.common.type.ElasticFilterType;

public class PlainElasticAPIFilter implements ElasticAPIFilter {
    private ElasticFilterType type;
    private Object[] parameters;

    public PlainElasticAPIFilter(ElasticFilterType type, Object[] parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    @Override
    public ElasticFilterType type() {
        return type;
    }

    @Override
    public Object[] parameters() {
        return parameters;
    }
}
