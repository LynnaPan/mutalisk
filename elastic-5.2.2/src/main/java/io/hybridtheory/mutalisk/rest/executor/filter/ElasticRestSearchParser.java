package io.hybridtheory.mutalisk.rest.executor.filter;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.api.filter.ElasticTermFilter;


import java.util.List;

public class ElasticRestSearchParser {
    public static JsonObject parse(List<ElasticFilter> filters) {
        JsonObject queryObj = new JsonObject();

        for (ElasticFilter filter: filters) {
            switch (filter.type()) {
                case TERM:
                    ElasticTermFilter f = (ElasticTermFilter) filter;
                    JsonObject object = new JsonObject();

                    // new JsonPrimitive will auto detect the value type even if you just put one string value
                    // into it.
                    object.add(f.getField(), new JsonPrimitive(f.getStringValue()));
                    queryObj.add("term", object);
                    break;
            }
        }

        return queryObj;
    }
}
