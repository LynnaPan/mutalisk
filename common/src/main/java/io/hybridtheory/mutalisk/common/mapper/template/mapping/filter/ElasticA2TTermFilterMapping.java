package io.hybridtheory.mutalisk.common.mapper.template.mapping.filter;

import io.hybridtheory.mutalisk.common.mapper.annotation.filter.ElasticSearchFilter;
import io.hybridtheory.mutalisk.common.mapper.template.filter.ElasticFilterTemplate;
import io.hybridtheory.mutalisk.common.mapper.template.filter.ElasticTermFilterTemplate;
import io.hybridtheory.mutalisk.common.type.ElasticFilterType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ElasticA2TTermFilterMapping implements ElasticA2TFilterMapping {
    @Override
    public List<ElasticFilterTemplate> apply(Method method) {
        List<ElasticFilterTemplate> list = new ArrayList<>();

        Annotation[] annotations = method.getDeclaredAnnotations();

        for (Annotation annotation: annotations) {
            if (annotation.annotationType().equals(ElasticSearchFilter.class)) {
                ElasticSearchFilter filter = (ElasticSearchFilter)annotation;

                if (filter.type() == ElasticFilterType.TERM) {
                    String[] params = filter.parameters();
                    list.add(new ElasticTermFilterTemplate(params[0], params[1]));
                }
            }
        }

        return list;
    }
}
