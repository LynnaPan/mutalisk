package io.hybridtheory.mutalisk.mapper.template.dao;

import io.hybridtheory.mutalisk.executor.ElasticExecutor;
import io.hybridtheory.mutalisk.filter.ElasticFilter;
import io.hybridtheory.mutalisk.mapper.ElasticSearchMapperTemplate;
import io.hybridtheory.mutalisk.mapper.template.filter.NElasticFilterTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchDAOTemplate implements ElasticSearchMapperTemplate {
    public String index;
    public String mapping;

    public List<NElasticFilterTemplate> filters;
    public Class arrayClz;

    public ElasticSearchDAOTemplate(String index, String mapping,
                                    List<NElasticFilterTemplate> filters, Class arrayClz) {
        this.index = index;
        this.mapping = mapping;
        this.filters = filters;
        this.arrayClz = arrayClz;
    }

    @Override
    public Object apply(ElasticExecutor executor, Object[] args) throws IOException {

        List<ElasticFilter> xfilters = new ArrayList<>();

        for (NElasticFilterTemplate filter : filters) {
            xfilters.add(filter.apply(args));
        }

        return executor.impl().search(index, mapping, arrayClz, xfilters);
    }
}
