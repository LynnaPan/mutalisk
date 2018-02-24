package io.hybridtheory.mutalisk.common.mapper.template;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.mapper.template.mapping.ElasticA2TPipelineMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ElasticMapperInterfaceHandler<T> implements InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(ElasticMapperInterfaceHandler.class);

    private Class<T> clz;
    private ElasticExecutor executor;

    private Map<String, ElasticTemplate> daoTemplateMap = new HashMap<>();

    public ElasticMapperInterfaceHandler(Class<T> clz, ElasticExecutor executor) {
        this.clz = clz;
        this.executor = executor;

        this.buildTemplates();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        if (daoTemplateMap.containsKey(methodName)) {
            return daoTemplateMap.get(methodName).apply(executor, args);
        }

        throw new UnsupportedOperationException("Unsupported method: " + methodName);

    }

    // --- help functions
    public void buildTemplates() {
        if (!clz.isInterface()) {
            log.error("{} should be one interface, otherwise unable to create mapper", clz);
            return;
        }

        // parse methods
        //@NOTE we have preference here in template with following priority:
        // - create index action
        // - insert document action
        // - bulk insert document action
        // - delete document action
        // - search action
        // - aggregation action
        // - other will report error
        for (Method m : clz.getDeclaredMethods()) {
            ElasticTemplate template = ElasticA2TPipelineMapping.getActionMappings().apply(m);

            if (template != null) {
                daoTemplateMap.put(m.getName(), template);
            }
        }
    }
}
