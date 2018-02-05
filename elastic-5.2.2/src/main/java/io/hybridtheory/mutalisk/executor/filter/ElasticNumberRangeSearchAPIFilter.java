package io.hybridtheory.mutalisk.executor.filter;


import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class ElasticNumberRangeSearchAPIFilter implements ElasticSearchAPIFilter {
    String key;
    int min;
    int max;

    public ElasticNumberRangeSearchAPIFilter(String key, int min, int max) {
        this.key = key;
        this.min = min;
        this.max = max;
    }

    @Override
    public QueryBuilder toQueryBuilder() {
        return QueryBuilders.rangeQuery(key).from(min).to(max);
    }
}
