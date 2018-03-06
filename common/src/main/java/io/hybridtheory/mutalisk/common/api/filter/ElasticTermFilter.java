package io.hybridtheory.mutalisk.common.api.filter;

import io.hybridtheory.mutalisk.common.type.ElasticFilterType;


public class ElasticTermFilter implements ElasticFilter {
    private String field;
    private Object value;

    public ElasticTermFilter(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public ElasticFilterType type() {
        return ElasticFilterType.TERM;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}