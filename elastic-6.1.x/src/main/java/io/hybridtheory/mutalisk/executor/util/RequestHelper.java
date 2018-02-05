package io.hybridtheory.mutalisk.executor.util;


import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class RequestHelper {

    private static final Logger log = LoggerFactory.getLogger(RequestHelper.class);

    public static IndexRequest buildNoIdIndexRequest(Object object) {
        Class clz = object.getClass();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return new IndexRequest(schema.index, schema.type).source(StorageUtil.gson.toJson(object));
    }

    public static IndexRequest buildIdIndexRequest(Object object, String id) {
        Class clz = object.getClass();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return new IndexRequest(schema.index, schema.type, id).source(StorageUtil.gson.toJson(object));
    }

    public static IndexRequest buildIndexRequest(Object object) {
        Class clz = object.getClass();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        if (schema.id.length == 0) {
            return new IndexRequest(schema.index, schema.type).source(StorageUtil.gson.toJson(object));
        }

        // need to read id from object
        JsonObject jobj = (JsonObject) StorageUtil.gson.toJsonTree(object);
        IndexRequest request = new IndexRequest(schema.index, schema.type, schema.getId(jobj));
        request.source(jobj.toString());

        return request;
    }

    public static boolean assertCreatedOrUpdated(RestStatus status) {
        return status == RestStatus.CREATED || status == RestStatus.OK;
    }


    // handle search response
    public static <T extends Object> T[] handleSearchResponse(SearchResponse response, Class<T[]> arrayClz) {
        Class clz = arrayClz.getComponentType();

        SearchHits hits = response.getHits();

        Object[] results = new Object[hits.getHits().length];

        for (int i = 0; i < results.length; i++) {
            results[i] = StorageUtil.gson.fromJson(hits.getAt(i).getSourceAsString(), clz);
        }

        return Arrays.copyOf(results, results.length, arrayClz);
    }
}
