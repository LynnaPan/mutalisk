package io.hybridtheory.mutalisk.executor.filter;


import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class ElasticTermSearchAPIFilter implements ElasticSearchAPIFilter {
    String key;
    Object value;

    public ElasticTermSearchAPIFilter(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public QueryBuilder toQueryBuilder() {
        return QueryBuilders.termQuery(key, value);
    }
}
