package io.hybridtheory.mutalisk.common.mapper.template.mapping.filter;

import io.hybridtheory.mutalisk.common.mapper.template.filter.ElasticFilterTemplate;

import java.lang.reflect.Method;
import java.util.List;

public interface ElasticA2TFilterMapping {
    List<ElasticFilterTemplate> apply(Method method);
}
