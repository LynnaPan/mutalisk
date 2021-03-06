package io.hybridtheory.mutalisk.highlevel.executor.aggregation;

import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lynna on 2018/3/8.
 */
public class ElasticAggregateParser {
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
