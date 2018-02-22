package io.hybridtheory.mutalisk.common.api.filter;

import io.hybridtheory.mutalisk.common.type.ElasticFilterType;

public class PlainElasticFilter implements ElasticFilter {
    private ElasticFilterType type;
    private Object[] parameters;

    public PlainElasticFilter(ElasticFilterType type, Object[] parameters) {
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
