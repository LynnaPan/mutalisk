package io.hybridtheory.mutalisk.filter;


import org.elasticsearch.index.query.QueryBuilder;

public interface ElasticFilter {
    QueryBuilder builder();
}
