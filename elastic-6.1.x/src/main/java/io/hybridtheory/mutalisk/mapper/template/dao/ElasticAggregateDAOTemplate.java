package io.hybridtheory.mutalisk.mapper.template.dao;

import io.hybridtheory.mutalisk.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.executor.ElasticExecutor;
import io.hybridtheory.mutalisk.filter.ElasticFilter;
import io.hybridtheory.mutalisk.mapper.ElasticSearchMapperTemplate;
import io.hybridtheory.mutalisk.mapper.template.aggregate.NElasticAggregateTemplate;
import io.hybridtheory.mutalisk.mapper.template.filter.NElasticFilterTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ElasticAggregateDAOTemplate implements ElasticSearchMapperTemplate {
    public String index;
    public String mapping;

    public List<NElasticFilterTemplate> filters;
    public List<NElasticAggregateTemplate> aggregates;


    private Map<String, NElasticAggregateTemplate> aggregationMap = new HashMap<>();

    public ElasticAggregateDAOTemplate(String index, String mapping,
                                       List<NElasticFilterTemplate> filters,
                                       List<NElasticAggregateTemplate> aggregates) {
        this.index = index;
        this.mapping = mapping;
        this.filters = filters;
        this.aggregates = aggregates;

        for(NElasticAggregateTemplate template : aggregates) {
            aggregationMap.put(template.name(), template);
        }
    }

    @Override
    public Object apply(ElasticExecutor executor,
                        Object[] args) throws IOException, ExecutionException, InterruptedException {
        List<ElasticFilter> xfilters = new ArrayList<>();
        List<ElasticAggregate> xaggregates = new ArrayList<>();

        for (NElasticFilterTemplate filter : filters) {
            xfilters.add(filter.apply(args));
        }

        for (NElasticAggregateTemplate aggregation : aggregates) {
            xaggregates.add(aggregation.apply(args));
        }

        return executor.impl().aggregate(index, mapping, xfilters, xaggregates);
    }
}
