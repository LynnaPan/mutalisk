package io.hybridtheory.mutalisk.executor.transport.mapper.template.dao;

import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.executor.transport.ElasticTransportExecutor;
import io.hybridtheory.mutalisk.executor.transport.mapper.ElasticSearchMapperTemplate;

public class NElasticProxyCreateTemplate implements ElasticSearchMapperTemplate {
    public String index;
    public String mapping;

    public Class clz;


    public NElasticProxyCreateTemplate(String index, String mapping, Class clz) {
        this.index = index;
        this.mapping = mapping;
        this.clz = clz;
    }

    @Override
    public Object apply(ElasticTransportExecutor executor, Object[] args) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        try {
            executor.client().admin().indices().prepareCreate(schema.index).addMapping(schema.type,
                schema.toTypeMapping()).get();
        } catch (Throwable t) {
            return false;
        }

        return true;
    }
}
