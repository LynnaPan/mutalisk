package io.hybridtheory.mutalisk.common.mapper.annotation.aggregation;

import io.hybridtheory.mutalisk.common.type.ElasticAggregateType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticAggregate {
    String name();

    String key();

    ElasticAggregateType type();
}
