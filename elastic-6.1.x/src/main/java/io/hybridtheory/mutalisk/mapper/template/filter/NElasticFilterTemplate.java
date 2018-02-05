package io.hybridtheory.mutalisk.mapper.template.filter;


import io.hybridtheory.mutalisk.filter.ElasticFilter;

public interface NElasticFilterTemplate {
    ElasticFilter apply(Object[] args);
}
