package io.hybridtheory.mutalisk.common.api.sort;


import io.hybridtheory.mutalisk.common.type.ElasticSortOrder;

public interface ElasticSort {
    ElasticSort type();
    ElasticSortOrder order();
}
