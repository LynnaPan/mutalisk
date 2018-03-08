package io.hybridtheory.mutalisk.highlevel.executor.sort;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.api.sort.ElasticFieldSort;
import io.hybridtheory.mutalisk.common.api.sort.ElasticSort;
import io.hybridtheory.mutalisk.common.type.ElasticSortOrder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;

public class ElasticSortParser {
    public static List<SortBuilder> parse(List<ElasticSort> sorts) {
        List<SortBuilder> builders = new ArrayList<>();

        for (ElasticSort sort : sorts) {

            switch (sort.type()) {
                case FIELD:
                    ElasticFieldSort fieldSort = (ElasticFieldSort) sort;
                    FieldSortBuilder builder = new FieldSortBuilder(fieldSort.field()).order(switchType(fieldSort.order()));
                    builders.add(builder);
                    break;
            }

        }

        return builders;
    }

    private static SortOrder switchType (ElasticSortOrder order){
        SortOrder result = null;
        switch (order){
            case ASC:
                result = SortOrder.ASC;
                break;
            case DESC:
                result = SortOrder.DESC;
        }
        return result;
    }

}
