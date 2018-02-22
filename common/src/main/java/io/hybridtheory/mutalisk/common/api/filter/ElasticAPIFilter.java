package io.hybridtheory.mutalisk.common.api.filter;

import io.hybridtheory.mutalisk.common.type.ElasticFilterType;

public interface ElasticAPIFilter {
    ElasticFilterType type();

    Object[] parameters();
}
