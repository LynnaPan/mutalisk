package io.hybridtheory.mutalisk.common.api.filter;

import io.hybridtheory.mutalisk.common.type.ElasticFilterType;

public class ElasticTermFilter implements ElasticFilter {
    private ElasticFilterType type;
    private String field;
    private Object value;

    public ElasticTermFilter(ElasticFilterType type, String field, Object value) {
        this.type = type;
        this.field = field;
        this.value = value;
    }

    @Override
    public ElasticFilterType type() {
        return null;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}
