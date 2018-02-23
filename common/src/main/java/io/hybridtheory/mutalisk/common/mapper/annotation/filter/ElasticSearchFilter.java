package io.hybridtheory.mutalisk.common.mapper.annotation.filter;

import io.hybridtheory.mutalisk.common.type.ElasticFilterType;

public @interface ElasticSearchFilter {
    ElasticFilterType type();
    String[] parameters() default {};
}
