package io.hybridtheory.mutalisk.mapper.template.dao;

import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.executor.ElasticExecutor;
import io.hybridtheory.mutalisk.mapper.ElasticSearchMapperTemplate;
import io.hybridtheory.mutalisk.executor.highlevel.ElasticHighLevelExecutor;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ElasticCreateDAOTemplate implements ElasticSearchMapperTemplate {
    public String index;
    public String mapping;

    public Class clz;


    public ElasticCreateDAOTemplate(String index, String mapping, Class clz) {
        this.index = index;
        this.mapping = mapping;
        this.clz = clz;
    }

    @Override
    public Object apply(ElasticExecutor executor,
                        Object[] args) throws IOException, ExecutionException, InterruptedException {
        return executor.impl().createIndex(clz);
    }
}
