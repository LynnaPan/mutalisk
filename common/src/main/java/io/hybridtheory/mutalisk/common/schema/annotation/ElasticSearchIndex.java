package io.hybridtheory.mutalisk.common.schema.annotation;

import io.hybridtheory.mutalisk.common.schema.ElasticSearchFieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchIndex {
    ElasticSearchFieldType type() default ElasticSearchFieldType.AUTO;
}
