package io.hybridtheory.mutalisk.rest.executor.aggregation;


import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;

import java.util.List;

public class ElasitcRestAggregateParser {
    public static JsonObject parse(List<ElasticAggregate> aggregates) {
        JsonObject aggObj = new JsonObject();

        for (ElasticAggregate aggregate: aggregates) {
            JsonObject object = new JsonObject();
            JsonObject innerAgg = new JsonObject();

            switch (aggregate.type()) {
                case SUM:
                    object = new JsonObject();
                    object.addProperty("field", aggregate.field());

                    innerAgg = new JsonObject();
                    innerAgg.add("sum", object);
                    aggObj.add(aggregate.name(), innerAgg);
                    break;

                case AVERAGE:
                    object = new JsonObject();
                    object.addProperty("field", aggregate.field());

                    innerAgg = new JsonObject();
                    innerAgg.add("avg", object);
                    aggObj.add(aggregate.name(), innerAgg);
                    break;
            }
        }

        return aggObj;
    }
}
