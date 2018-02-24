package io.hybridtheory.mutalisk.common.mapper.template.mapping.filter;

import io.hybridtheory.mutalisk.common.mapper.template.filter.ElasticFilterTemplate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ElasticA2TFilterPipelineMapping implements ElasticA2TFilterMapping {
   private ElasticA2TFilterMapping[] filterMappings;

    public ElasticA2TFilterPipelineMapping(ElasticA2TFilterMapping[] filterMappings) {
        this.filterMappings = filterMappings;
    }

    @Override
    public List<ElasticFilterTemplate> apply(Method method) {
        List<ElasticFilterTemplate> list = new ArrayList<>();

        for (ElasticA2TFilterMapping mapping: filterMappings) {
            list.addAll(mapping.apply(method));
        }

        return list;
    }

    // static field
    private static ElasticA2TFilterPipelineMapping pipelineMapping =
            new ElasticA2TFilterPipelineMapping(new ElasticA2TFilterMapping[]{
               new ElasticA2TTermFilterMapping()
            });

    public static ElasticA2TFilterPipelineMapping builtin() {
        return pipelineMapping;
    }
}
