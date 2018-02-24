package io.hybridtheory.mutalisk.common.mapper.template.action;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;
import io.hybridtheory.mutalisk.common.mapper.template.aggregate.ElasticAggregateTemplate;
import io.hybridtheory.mutalisk.common.mapper.template.filter.ElasticFilterTemplate;

import java.util.ArrayList;
import java.util.List;

public class ElasticAggregationTemplate implements ElasticTemplate {
    public List<ElasticFilterTemplate> filterTemplates;
    public List<ElasticAggregateTemplate> aggregateTemplates;
    public Class arrayClz;

    public ElasticAggregationTemplate(List<ElasticFilterTemplate> filterTemplates,
                                      List<ElasticAggregateTemplate> aggregateTemplates,
                                      Class arrayClz) {
        this.filterTemplates = filterTemplates;
        this.aggregateTemplates = aggregateTemplates;
        this.arrayClz = arrayClz;
    }

    @Override
    public Object apply(ElasticExecutor executor, Object[] args) {
        List<ElasticFilter> filters = new ArrayList<>();

        for (ElasticFilterTemplate filterTemplate: filterTemplates) {
            filters.add(filterTemplate.apply(executor, args));
        }

        List<ElasticAggregate> aggregates = new ArrayList<>();

        for (ElasticAggregateTemplate aggregateTemplate: aggregateTemplates) {
            aggregates.add(aggregateTemplate.apply(executor, args));
        }

        return executor.aggregate(arrayClz, filters, aggregates);
    }
}
