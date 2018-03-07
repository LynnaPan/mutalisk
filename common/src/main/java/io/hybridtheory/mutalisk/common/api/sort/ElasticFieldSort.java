package io.hybridtheory.mutalisk.common.api.sort;


import io.hybridtheory.mutalisk.common.type.ElasticSortMode;
import io.hybridtheory.mutalisk.common.type.ElasticSortOrder;
import io.hybridtheory.mutalisk.common.type.ElasticSortType;

public class ElasticFieldSort implements ElasticSort {

    private String field;
    private ElasticSortOrder order;
    private ElasticSortMode mode = ElasticSortMode.NON;

    public ElasticFieldSort(String field, ElasticSortOrder order) {
        this.field = field;
        this.order = order;
    }

    public ElasticFieldSort(String field, ElasticSortOrder order, ElasticSortMode mode) {
        this.field = field;
        this.order = order;
        this.mode = mode;
    }

    public String field() {
        return field;
    }

    @Override
    public ElasticSortType type() {
        return ElasticSortType.FIELD;
    }

    @Override
    public ElasticSortOrder order() {
        return this.order;
    }

    @Override
    public ElasticSortMode mode() {
        return mode;
    }
}
