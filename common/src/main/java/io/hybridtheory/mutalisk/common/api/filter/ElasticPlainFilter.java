package io.hybridtheory.mutalisk.common.api.filter;

import io.hybridtheory.mutalisk.common.type.ElasticFilterType;

public class ElasticPlainFilter implements ElasticFilter {
    private ElasticFilterType type;
    private Object[] parameters;

    public ElasticPlainFilter(ElasticFilterType type, Object[] parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    @Override
    public ElasticFilterType type() {
        return type;
    }

    public Object[] parameters() {
        return parameters;
    }
}
