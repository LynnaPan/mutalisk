package io.hybridtheory.mutalisk.common.api.sort;


import io.hybridtheory.mutalisk.common.type.ElasticSortOrder;
import io.hybridtheory.mutalisk.common.type.ElasticSortType;

public interface ElasticSort {
    ElasticSortType type();
    ElasticSortOrder order();
}
