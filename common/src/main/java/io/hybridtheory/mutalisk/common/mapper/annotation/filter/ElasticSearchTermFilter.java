package io.hybridtheory.mutalisk.common.mapper.annotation.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchTermFilter {
    String key();

    int valueIndex() default -1;

    String value() default "";
}
