package io.hybridtheory.mutalisk.mapper.template.dao;

import io.hybridtheory.mutalisk.executor.ElasticTransportExecutor;
import io.hybridtheory.mutalisk.mapper.ElasticSearchMapperTemplate;
import io.hybridtheory.mutalisk.mapper.template.aggregation.NElasticProxyAggregationTemplate;
import io.hybridtheory.mutalisk.mapper.template.filter.NElasticProxyFilterTemplate;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NElasticProxyAggregateTemplate implements ElasticSearchMapperTemplate {
    public String index;
    public String mapping;

    public List<NElasticProxyFilterTemplate> filters;
    public List<NElasticProxyAggregationTemplate> aggregations;


    private Map<String, NElasticProxyAggregationTemplate> aggregationMap = new HashMap<>();

    public NElasticProxyAggregateTemplate(String index, String mapping,
                                          List<NElasticProxyFilterTemplate> filters,
                                          List<NElasticProxyAggregationTemplate> aggregations) {
        this.index = index;
        this.mapping = mapping;
        this.filters = filters;
        this.aggregations = aggregations;

        for(NElasticProxyAggregationTemplate template : aggregations) {
            aggregationMap.put(template.name(), template);
        }
    }

    @Override
    public Object apply(ElasticTransportExecutor executor, Object[] args) {
        SearchRequest request = new SearchRequest(index);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (NElasticProxyFilterTemplate filter : filters) {
            sourceBuilder.query(filter.apply(args));
        }

        for (NElasticProxyAggregationTemplate aggregation : aggregations) {
            sourceBuilder.aggregation(aggregation.apply(args));
        }

        request.source(sourceBuilder);

        Aggregations aggregations = executor.client().search(request).actionGet().getAggregations();

        Map<String, Object> results = new HashMap<>();

        for (Map.Entry<String, Aggregation> entry: aggregations.asMap().entrySet()) {
            NElasticProxyAggregationTemplate xtempl = aggregationMap.get(entry.getValue().getName());
            results.put(entry.getKey(), xtempl.value(entry.getValue()));
        }

        return results;
    }
}
