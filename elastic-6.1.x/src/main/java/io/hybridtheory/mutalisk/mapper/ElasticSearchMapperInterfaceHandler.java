package io.hybridtheory.mutalisk.mapper;


import io.hybridtheory.mutalisk.common.mapper.annotation.*;
import io.hybridtheory.mutalisk.common.mapper.annotation.aggregation.ElasticSearchSumAggregation;
import io.hybridtheory.mutalisk.common.mapper.annotation.filter.ElasticSearchTermFilter;
import io.hybridtheory.mutalisk.executor.highlevel.ElasticHighLevelExecutor;
import io.hybridtheory.mutalisk.mapper.template.aggregate.NElasticProxySumAggTemplate;
import io.hybridtheory.mutalisk.mapper.template.dao.*;
import io.hybridtheory.mutalisk.mapper.template.filter.NElasticFilterTemplate;
import io.hybridtheory.mutalisk.mapper.template.filter.NElasticTermFilterTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class ElasticSearchMapperInterfaceHandler<T> implements InvocationHandler {
    Class<T> clz;
    ElasticHighLevelExecutor executor;
    Map<String, ElasticSearchMapperTemplate> daoTemplateMap = new HashMap<>();

    public ElasticSearchMapperInterfaceHandler(Class<T> clz, ElasticHighLevelExecutor executor) {
        this.clz = clz;
        this.executor = executor;

        buildTemplates();
    }

    public static List<NElasticFilterTemplate> buildFilterTemplate(Method method) {
        List<NElasticFilterTemplate> results = new ArrayList<>();

        ElasticSearchTermFilter termFilter = method.getAnnotation(ElasticSearchTermFilter.class);

        if (termFilter != null) {
            int valueIndex = termFilter.valueIndex();
            String value = termFilter.value();

            if (valueIndex < 0 || valueIndex >= method.getParameterCount()) {
                if (value.equals("")) {
                    value = termFilter.key();
                }

                // if you enabled -g in javac, you may directly call getDirectlyParameterIndex to fetch value index
                // otherwise need to parse ElasticSearchParam annotation
                // see more in ElasticSearchParam.java
                valueIndex = getParameterIndex(method, value);
            }

            if (valueIndex == -1) {
                throw new NoSuchElementException("Unable to find " + value);
            }

            results.add(new NElasticTermFilterTemplate(termFilter.key(), valueIndex));
        }

        return results;
    }

    public static List<io.hybridtheory.mutalisk.mapper.template.aggregate.NElasticAggregateTemplate> buildAggregationTemplate(Method method) {
        List<io.hybridtheory.mutalisk.mapper.template.aggregate.NElasticAggregateTemplate> results = new ArrayList<>();

        ElasticSearchSumAggregation sumAggregation = method.getAnnotation(ElasticSearchSumAggregation.class);

        if (sumAggregation != null) {
            results.add(new NElasticProxySumAggTemplate(sumAggregation.name(), sumAggregation.key()));
        }

        return results;
    }

    // return -1 if not found
    public static int getParameterIndex(Method method, String paramName) {


        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            ElasticSearchParam param = parameter.getAnnotation(ElasticSearchParam.class);

            if (param.name().equals(paramName)) {
                return i;
            }
        }

        return -1;
    }

    // return -1 if not found
    public static int getDirectlyParameterIndex(Method method, String paramName) {


        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.getName().equals(paramName)) {
                return i;
            }
        }

        return -1;
    }

    public void buildTemplates() {
        if (!clz.isInterface()) {
            return;
        }

        // parse methods
        for (Method m : clz.getDeclaredMethods()) {
            ElasticSearchCreate createAnno = m.getAnnotation(ElasticSearchCreate.class);

            if (createAnno != null) {
                daoTemplateMap.put(m.getName(),
                        new ElasticCreateDAOTemplate(createAnno.index(), createAnno.mapping(), createAnno.clz()));
                continue;
            }

            ElasticSearchInsert insertAnno = m.getAnnotation(ElasticSearchInsert.class);
            if (insertAnno != null) {
                // parameter number should be one
                Class clz = m.getParameterTypes()[0];
                daoTemplateMap.put(m.getName(),
                        new ElasticInsertDAOTemplate(
                                insertAnno.index(),
                                insertAnno.mapping(),
                                insertAnno.primary(),
                                clz));
                continue;
            }

            ElasticSearchBulkInsert bulkInsertAnno = m.getAnnotation(ElasticSearchBulkInsert.class);
            if (bulkInsertAnno != null) {
                Class clz = m.getParameterTypes()[0];
                daoTemplateMap.put(m.getName(),
                        new ElasticBulkInsertDAOTemplate(
                                bulkInsertAnno.index(),
                                bulkInsertAnno.mapping(),
                                bulkInsertAnno.primary(),
                                clz));
                continue;
            }

            ElasticSearchSearch searchAnno = m.getAnnotation(ElasticSearchSearch.class);
            if (searchAnno != null) {
                Class arrayClz = m.getReturnType();
                // get term/range annotation

                daoTemplateMap.put(m.getName(), new ElasticSearchDAOTemplate(
                        searchAnno.index(),
                        searchAnno.mapping(),
                        buildFilterTemplate(m),
                        arrayClz
                ));

                continue;
            }

            ElasticSearchAggregate aggregationAnno = m.getAnnotation(ElasticSearchAggregate.class);
            if (aggregationAnno != null) {
                daoTemplateMap.put(m.getName(), new ElasticAggregateDAOTemplate(
                        aggregationAnno.index(),
                        aggregationAnno.mapping(),
                        buildFilterTemplate(m),
                        buildAggregationTemplate(m)
                ));

                continue;
            }

            // @TODO tell un-recognized method
            System.out.println("Unrecognized Method : " + m.getName());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        if (daoTemplateMap.containsKey(methodName)) {
            return daoTemplateMap.get(methodName).apply(executor, args);
        }

        throw new UnsupportedOperationException("Unsupported method: " + methodName);
    }
}
