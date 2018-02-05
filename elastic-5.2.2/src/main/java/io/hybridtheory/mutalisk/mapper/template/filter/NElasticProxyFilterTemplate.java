package io.hybridtheory.mutalisk.mapper.template.filter;


import org.elasticsearch.index.query.QueryBuilder;

public interface NElasticProxyFilterTemplate {
    QueryBuilder apply(Object[] args);
}
