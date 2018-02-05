package io.hybridtheory.mutalisk.common.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// createIndex index
// - no function parameters
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchCreate {
    String index();

    String mapping() default "data";

    Class clz();
}
