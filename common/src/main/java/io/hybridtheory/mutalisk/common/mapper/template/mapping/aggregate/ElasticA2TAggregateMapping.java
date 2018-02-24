package io.hybridtheory.mutalisk.common.mapper.template.mapping.aggregate;

import io.hybridtheory.mutalisk.common.mapper.template.aggregate.ElasticAggregateTemplate;
import io.hybridtheory.mutalisk.common.mapper.template.mapping.ElasticA2TMapping;

import java.lang.reflect.Method;
import java.util.List;

public interface ElasticA2TAggregateMapping {
    List<ElasticAggregateTemplate> apply(Method method);
}
