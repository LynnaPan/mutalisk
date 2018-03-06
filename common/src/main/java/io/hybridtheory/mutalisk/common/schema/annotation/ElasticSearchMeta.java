package io.hybridtheory.mutalisk.common.schema.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// however, if you use Mapper mode, this annotation could be overwritten or ignored

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchMeta {
    String index();

    String type() default "data";

    String[] id() default {};

    int shards() default 5;

    int replicas() default 2;
}
