package io.hybridtheory.mutalisk.transport.executor.aggregation;


import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import java.util.ArrayList;
import java.util.List;

public class ElasticTransportAggregateParser {
    public static List<AggregationBuilder> parse(List<ElasticAggregate> aggregates) {
        List<AggregationBuilder> ags = new ArrayList<>();

        for (ElasticAggregate aggregate: aggregates) {
            switch (aggregate.type()) {
                case SUM:
                    ags.add(AggregationBuilders.sum(aggregate.name()).field(aggregate.field()));
                    break;

                case AVERAGE:
                    ags.add(AggregationBuilders.avg(aggregate.name()).field(aggregate.field()));
                    break;
            }
        }

        return ags;
    }
}
