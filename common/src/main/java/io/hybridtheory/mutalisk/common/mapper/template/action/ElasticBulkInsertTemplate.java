package io.hybridtheory.mutalisk.common.mapper.template.action;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ElasticBulkInsertTemplate implements ElasticTemplate {
    private static final Logger log = LoggerFactory.getLogger(ElasticCreateTemplate.class);

    private Class arrayClz;
    private boolean isList;

    public ElasticBulkInsertTemplate(Class arrayClz, boolean isList) {
        this.arrayClz = arrayClz;
        this.isList = isList;
    }

    @Override
    public Object apply(ElasticExecutor executor, Object[] args) {

        try {
            if (this.arrayClz == null) {
                if (!isList) {
                    executor.bulkInsert((Object[]) args[0]);
                } else {
                    executor.bulkInsert((List)args[0]);
                }
            } else {
                if (!isList) {
                    executor.bulkInsert((Object[]) args[0], arrayClz);
                } else {
                    executor.bulkInsert((List)args[0], arrayClz);
                }
            }
        } catch (Throwable t) {
            log.error("Apply to ElasticBulkInsertTemplate fails with arrayClz = {}", this.arrayClz, t);
            return false;
        }

        return true;
    }
}
