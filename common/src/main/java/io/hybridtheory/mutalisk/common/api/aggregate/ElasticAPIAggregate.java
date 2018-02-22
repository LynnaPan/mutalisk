package io.hybridtheory.mutalisk.common.api.aggregate;

import io.hybridtheory.mutalisk.common.type.ElasticAggregateType;

public interface ElasticAPIAggregate {
    ElasticAggregateType type();

    String name();

    String field();
}
