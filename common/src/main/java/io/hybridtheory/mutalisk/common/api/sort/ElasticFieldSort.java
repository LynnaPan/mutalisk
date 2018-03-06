package io.hybridtheory.mutalisk.common.api.sort;


import io.hybridtheory.mutalisk.common.type.ElasticSortOrder;
import io.hybridtheory.mutalisk.common.type.ElasticSortType;

public class ElasticFieldSort implements ElasticSort {

    private String field;
    private ElasticSortOrder order;

    public ElasticFieldSort(String field, ElasticSortOrder order) {
        this.field = field;
        this.order = order;
    }

    @Override
    public ElasticSortType type() {
        return ElasticSortType.FIELD;
    }

    @Override
    public ElasticSortOrder order() {
        return this.order;
    }
}
