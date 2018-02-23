package io.hybridtheory.mutalisk.common.mapper.template.mapping;

import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;

import java.lang.reflect.Method;

public interface ElasticA2TMapping {
    ElasticTemplate apply(Method method);
}
