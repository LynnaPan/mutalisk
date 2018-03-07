package io.hybridtheory.mutalisk.rest.executor.sort;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.api.sort.ElasticFieldSort;
import io.hybridtheory.mutalisk.common.api.sort.ElasticSort;

import java.util.List;

public class ElasticRestSortParser {
    public static JsonArray parse(List<ElasticSort> sorts) {
        JsonArray sortArry = new JsonArray();

        for (ElasticSort sort : sorts) {
            JsonObject object = new JsonObject();


            switch (sort.type()) {
                case FIELD:
                    ElasticFieldSort fieldSort = (ElasticFieldSort) sort;
                    object.addProperty(fieldSort.field(), fieldSort.order().toString());
                    break;
            }

            sortArry.add(object);
        }

        return sortArry;
    }
}
