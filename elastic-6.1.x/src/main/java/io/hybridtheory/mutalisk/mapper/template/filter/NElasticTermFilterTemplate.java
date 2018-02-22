package io.hybridtheory.mutalisk.mapper.template.filter;

import io.hybridtheory.mutalisk.filter.ElasticFilter;

public class NElasticTermFilterTemplate implements NElasticFilterTemplate {
    public String key;
    public int valueIndex;

    public NElasticTermFilterTemplate(String key, int valueIndex) {
        this.key = key;
        this.valueIndex = valueIndex;
    }

    @Override
    public ElasticFilter apply(Object[] args) {
        return null;
    }
}
