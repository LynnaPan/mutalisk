package io.hybridtheory.mutalisk.common.mapper.template.mapping;

import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ElasticA2TSeachMapping implements ElasticA2TMapping {

    private static final Logger log = LoggerFactory.getLogger(ElasticA2TSeachMapping.class);

    @Override
    public ElasticTemplate apply(Method method) {
        return null;
    }
}
