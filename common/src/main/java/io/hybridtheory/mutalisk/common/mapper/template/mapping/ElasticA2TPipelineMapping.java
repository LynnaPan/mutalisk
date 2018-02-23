package io.hybridtheory.mutalisk.common.mapper.template.mapping;

import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;

import java.lang.reflect.Method;

public class ElasticA2TPipelineMapping implements ElasticA2TMapping {
    private ElasticA2TMapping[] pipelines;

    public ElasticA2TPipelineMapping(ElasticA2TMapping[] pipelines) {
        this.pipelines = pipelines;
    }

    @Override
    public ElasticTemplate apply(Method method) {
        for (ElasticA2TMapping mapping : pipelines) {
            ElasticTemplate template = mapping.apply(method);

            if (template != null) {
                return template;
            }
        }

        return null;
    }

}
