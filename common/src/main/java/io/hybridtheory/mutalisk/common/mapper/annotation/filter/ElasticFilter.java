package io.hybridtheory.mutalisk.common.mapper.annotation.filter;

import io.hybridtheory.mutalisk.common.type.ElasticFilterType;

public @interface ElasticFilter {
    ElasticFilterType type();
    String[] parameters() default {};
}
