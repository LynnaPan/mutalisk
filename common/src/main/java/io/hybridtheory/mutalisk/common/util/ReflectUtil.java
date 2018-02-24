package io.hybridtheory.mutalisk.common.util;

import io.hybridtheory.mutalisk.common.mapper.annotation.ElasticSearchParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectUtil {

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
}
