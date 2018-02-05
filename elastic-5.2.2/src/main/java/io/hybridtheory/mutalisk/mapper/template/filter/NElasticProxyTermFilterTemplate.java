package io.hybridtheory.mutalisk.mapper.template.filter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class NElasticProxyTermFilterTemplate implements NElasticProxyFilterTemplate {
    public String key;
    public int valueIndex;

    public NElasticProxyTermFilterTemplate(String key, int valueIndex) {
        this.key = key;
        this.valueIndex = valueIndex;
    }

    @Override
    public QueryBuilder apply(Object[] args) {
        return QueryBuilders.termQuery(key, args[valueIndex]);
    }
}
