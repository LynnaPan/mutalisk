package io.hybridtheory.mutalisk.common.api.aggregate;

import io.hybridtheory.mutalisk.common.type.ElasticAggregateType;

public class PlainElasticAggregate implements ElasticAggregate {
    private ElasticAggregateType type;
    private String name;
    private String field;

    public PlainElasticAggregate(ElasticAggregateType type, String name, String field) {
        this.type = type;
        this.name = name;
        this.field = field;
    }

    @Override
    public ElasticAggregateType type() {
        return type;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String field() {
        return field;
    }
}
