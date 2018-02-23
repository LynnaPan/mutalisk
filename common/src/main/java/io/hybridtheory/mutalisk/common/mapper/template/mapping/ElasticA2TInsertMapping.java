package io.hybridtheory.mutalisk.common.mapper.template.mapping;

import io.hybridtheory.mutalisk.common.mapper.annotation.ElasticSearchInsert;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;
import io.hybridtheory.mutalisk.common.mapper.template.action.ElasticInsertTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ElasticA2TInsertMapping implements ElasticA2TMapping {
    private static final Logger log = LoggerFactory.getLogger(ElasticA2TInsertMapping.class);

    @Override
    public ElasticTemplate apply(Method method) {

        // check parameters, the args should length = 1
        if (method.getParameterCount() != 1) {
            log.info("{} : Parameter count should be exactly 1, ignore mapping", method);
            return null;
        }

        ElasticSearchInsert insertAnno = method.getAnnotation(ElasticSearchInsert.class);

        if (insertAnno == null) {
            log.info("{} : There is no ElasticSearchInsert annotation attached, ignore mapping", method);
            return null;
        }

        Class clz = insertAnno.clz();

        if (clz.equals(Object.class)) {
            log.info("Use parameters[0] class as target class");
            clz = method.getParameterTypes()[0];
        }

        // re-check
        if (clz.equals(Object.class)) {
            log.info("{} : parameters[0] class is Object.class, insert without specified class", method);
            clz = null;
        }

        return new ElasticInsertTemplate(clz);
    }
}
