package io.hybridtheory.mutalisk.executor.filter;


import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.api.filter.ElasticTermFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.ArrayList;
import java.util.List;

public class ElasticSearchParser {
    public static List<QueryBuilder> parse(List<ElasticFilter> filters) {
        List<QueryBuilder> list = new ArrayList<>();

        for (ElasticFilter filter: filters) {
            switch (filter.type()) {
                case TERM:
                    ElasticTermFilter f = (ElasticTermFilter) filter;
                    list.add(QueryBuilders.termQuery(f.getField(), f.getValue()));
                    break;
            }
        }

        return list;
    }
}
