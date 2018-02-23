package io.hybridtheory.mutalisk.common.mapper.template.action;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;
import io.hybridtheory.mutalisk.common.mapper.template.filter.ElasticFilterTemplate;

import java.util.ArrayList;
import java.util.List;

public class ElasticSearchTemplate implements ElasticTemplate {
    private List<ElasticFilterTemplate> filterTemplates;
    private Class arrayClz;

    public ElasticSearchTemplate(List<ElasticFilterTemplate> filterTemplates, Class arrayClz) {
        this.filterTemplates = filterTemplates;
        this.arrayClz = arrayClz;
    }

    @Override
    public Object apply(ElasticExecutor executor, Object[] args) {
        List<ElasticFilter> filters = new ArrayList<>();

        for (ElasticFilterTemplate filterTemplate: filterTemplates) {
            filters.add(filterTemplate.apply(executor, args));
        }

        return executor.search(arrayClz, filters);
    }
}
