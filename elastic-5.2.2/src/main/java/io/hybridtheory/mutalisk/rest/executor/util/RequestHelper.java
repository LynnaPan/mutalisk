package io.hybridtheory.mutalisk.rest.executor.util;

import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RequestHelper {

    private final static Logger log = LoggerFactory.getLogger(RequestHelper.class);

    public static <T> String buildBulkInsert(List<T> objects) {
        StringBuffer buffer = new StringBuffer();

        for(T object: objects) {
            Class clz = object.getClass();
            ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

            // need to read id from object
            JsonObject jobj = StorageUtil.gson.toJsonTree(object).getAsJsonObject();
            String createdHeader = null;

            if (schema.id.length == 0) {
                createdHeader = String.format("{\"created\": {\"_index\": \"%s\", \"_type\": \"%s\"}",
                    schema.index, schema.type);
            } else {
                createdHeader = String.format("{\"created\": {\"_index\": \"%s\", \"_type\": \"%s\", \"_id\": \"%s\"}",
                    schema.index, schema.type, schema.getId(jobj));
            }

            buffer.append(createdHeader);
            buffer.append(jobj.toString());
        }

        return buffer.toString();
    }

    public static <T> String buildBulkInsert(List<T> objects, Class<T> clz) {
        StringBuffer buffer = new StringBuffer();

        for(T object: objects) {
            ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

            // need to read id from object
            JsonObject jobj = StorageUtil.gson.toJsonTree(object).getAsJsonObject();
            String createdHeader = null;

            if (schema.id.length == 0) {
                createdHeader = String.format("{\"created\": {\"_index\": \"%s\", \"_type\": \"%s\"}",
                    schema.index, schema.type);
            } else {
                createdHeader = String.format("{\"created\": {\"_index\": \"%s\", \"_type\": \"%s\", \"_id\": \"%s\"}",
                    schema.index, schema.type, schema.getId(jobj));
            }

            buffer.append(createdHeader);
            buffer.append(jobj.toString());
        }

        return buffer.toString();
    }
}
