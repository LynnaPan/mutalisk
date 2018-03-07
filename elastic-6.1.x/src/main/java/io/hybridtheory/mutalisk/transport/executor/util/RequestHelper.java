package io.hybridtheory.mutalisk.transport.executor.util;


import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHelper {

    private static final Logger log = LoggerFactory.getLogger(RequestHelper.class);

    public static IndexRequest buildNoIdIndexRequest(Object object) {
        Class clz = object.getClass();
        return buildNoIdIndexRequest(clz, object);
    }

    public static IndexRequest buildNoIdIndexRequest(Class clz, Object object) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);
        return new IndexRequest(schema.index, schema.type).source(StorageUtil.gson.toJson(object));
    }

    public static IndexRequest buildIdIndexRequest(Class clz, Object object, String id) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return new IndexRequest(schema.index, schema.type, id).source(StorageUtil.gson.toJson(object));
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

    public static IndexRequest buildIndexRequest(Class clz, Object object) {
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
}
