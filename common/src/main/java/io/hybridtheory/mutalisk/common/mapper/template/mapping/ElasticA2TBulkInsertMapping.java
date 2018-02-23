package io.hybridtheory.mutalisk.common.mapper.template.mapping;

import io.hybridtheory.mutalisk.common.mapper.annotation.ElasticSearchBulkInsert;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;
import io.hybridtheory.mutalisk.common.mapper.template.action.ElasticBulkInsertTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

public class ElasticA2TBulkInsertMapping implements ElasticA2TMapping {
    private static final Logger log = LoggerFactory.getLogger(ElasticA2TBulkInsertMapping.class);

    @Override
    public ElasticTemplate apply(Method method) {
        // check parameters, the args should length = 1
        if (method.getParameterCount() != 1) {
            log.info("{} : Parameter count should be exactly 1, ignore mapping", method);
            return null;
        }

        ElasticSearchBulkInsert bulkInsertAnno = method.getAnnotation(ElasticSearchBulkInsert.class);

        if (bulkInsertAnno == null) {
            log.info("{} : There is no ElasticSearchBulkInsert annotation attached, ignore mapping", method);
            return null;
        }

        Class arrayClz = bulkInsertAnno.arrayClz();
        boolean isList = false;

        if (arrayClz.equals(Object[].class)) {
            log.info("Use parameters[0] class as target class");
            arrayClz = method.getParameterTypes()[0];
        }

        // re-check
        if (arrayClz.equals(Object[].class)) {
            log.info("{} : parameters[0] class is Object.class, insert without specified class", method);
            arrayClz = null;
        } else if (List.class.isAssignableFrom(arrayClz)) {
            // if List<T> version
            isList = true;
        }

        return new ElasticBulkInsertTemplate(arrayClz, isList);
    }
}
