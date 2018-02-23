package io.hybridtheory.mutalisk.common.mapper.template.mapping.filter;

import io.hybridtheory.mutalisk.common.mapper.template.filter.ElasticFilterTemplate;
import io.hybridtheory.mutalisk.common.mapper.template.mapping.ElasticA2TMapping;

import java.lang.reflect.Method;

public interface ElasticA2TFilterMapping extends ElasticA2TMapping {
    @Override
    ElasticFilterTemplate apply(Method method);
}
