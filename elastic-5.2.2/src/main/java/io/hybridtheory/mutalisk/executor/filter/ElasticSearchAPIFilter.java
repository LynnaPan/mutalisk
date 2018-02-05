package io.hybridtheory.mutalisk.executor.filter;


import org.elasticsearch.index.query.QueryBuilder;

public interface ElasticSearchAPIFilter {
    QueryBuilder toQueryBuilder();
}
