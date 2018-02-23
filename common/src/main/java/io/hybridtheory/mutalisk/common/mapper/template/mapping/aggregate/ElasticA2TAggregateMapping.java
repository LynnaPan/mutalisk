package io.hybridtheory.mutalisk.common.mapper.template.mapping.aggregate;

import io.hybridtheory.mutalisk.common.mapper.template.aggregate.ElasticAggregateTemplate;
import io.hybridtheory.mutalisk.common.mapper.template.mapping.ElasticA2TMapping;

import java.lang.reflect.Method;

public interface ElasticA2TAggregateMapping extends ElasticA2TMapping {
    @Override
    ElasticAggregateTemplate apply(Method method);
}
