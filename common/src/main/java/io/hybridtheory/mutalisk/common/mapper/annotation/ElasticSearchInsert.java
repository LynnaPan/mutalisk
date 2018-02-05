package io.hybridtheory.mutalisk.common.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchInsert {
    String index();

    String mapping() default "data";

    String primary() default "";
}
