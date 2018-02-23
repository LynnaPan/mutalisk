package io.hybridtheory.mutalisk.common.mapper.template.action;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.mapper.template.ElasticTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticCreateTemplate implements ElasticTemplate {
    private static final Logger log = LoggerFactory.getLogger(ElasticCreateTemplate.class);
    public Class clz;

    public ElasticCreateTemplate(Class clz) {
        this.clz = clz;
    }

    @Override
    public Object apply(ElasticExecutor executor, Object[] args) {

        try {
            executor.createIndex(clz);
        } catch (Throwable t) {
            log.error("Apply to ElasticCreateTemplate fails with {}", this.clz, t);
            return false;
        }

        return true;
    }
}
