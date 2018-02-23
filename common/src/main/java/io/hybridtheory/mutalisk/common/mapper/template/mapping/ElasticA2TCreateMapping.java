package io.hybridtheory.mutalisk.common.mapper.template.mapping;

import io.hybridtheory.mutalisk.common.mapper.annotation.ElasticSearchCreate;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;
import io.hybridtheory.mutalisk.common.mapper.template.action.ElasticCreateTemplate;

import java.lang.reflect.Method;

public class ElasticA2TCreateMapping implements ElasticA2TMapping {
    @Override
    public ElasticTemplate apply(Method method) {
        ElasticSearchCreate createAnno = method.getAnnotation(ElasticSearchCreate.class);

        if (createAnno != null) {
           return new ElasticCreateTemplate(createAnno.clz());
        }

        return null;
    }
}
