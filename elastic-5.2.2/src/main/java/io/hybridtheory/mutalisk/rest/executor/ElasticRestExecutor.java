package io.hybridtheory.mutalisk.rest.executor;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.common.api.exception.BulkDeleteException;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.api.sort.ElasticSort;
import io.hybridtheory.mutalisk.common.conf.ElasticClientConf;
import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.rest.executor.aggregation.ElasitcRestAggregateParser;
import io.hybridtheory.mutalisk.rest.executor.filter.ElasticRestSearchParser;
import io.hybridtheory.mutalisk.rest.executor.sort.ElasticRestSortParser;
import io.hybridtheory.mutalisk.rest.executor.util.RequestHelper;
import io.hybridtheory.mutalisk.rest.executor.util.ResponseHelper;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ElasticRestExecutor implements ElasticExecutor {
    private static final Logger log = LoggerFactory.getLogger(ElasticRestExecutor.class);

    private final RestClient client;

    public ElasticRestExecutor(ElasticClientConf conf) {
        this.client = RestClient.builder(conf.hostPorts).build();
    }

    @Override
    public boolean createIndex(Class clz) {
        return createIndex(clz, 0);
    }

    @Override
    public boolean createIndex(Class clz, long timeout) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        Response response = null;
        try {
            response = this.client.performRequest("PUT", "/" + schema.index + "/" + schema.type, null, new NStringEntity(
                schema.toIndexCreate(), ContentType.APPLICATION_JSON));
        } catch (IOException e) {
            log.error("Unable to performance index create request : {} - {}", schema.index, schema.type, e);

            return false;
        }

        return ResponseHelper.responseAckCheck(response);
    }

    @Override
    public boolean createIndex(String index, String type, String settingSource, String mappingSource) {
        return createIndex(index, type, settingSource, mappingSource, 0);
    }

    @Override
    public boolean createIndex(String index, String type, String settingSource, String mappingSource, long timeout) {
        Response response = null;
        try {
            response = this.client.performRequest("PUT", "/" + index + "/" + type, null, new NStringEntity(
                "{" +
                    "settings : " + settingSource + "," +
                    "properties : " + mappingSource +
                    "}", ContentType.APPLICATION_JSON));
        } catch (IOException e) {
            log.error("Unable to performance index create request : {} - {}", index, type, e);

            return false;
        }

        return ResponseHelper.responseAckCheck(response);
    }


    @Override
    public boolean deleteIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        log.info("Delete Index {} for Class {}", schema.index, clz.getName());

        return deleteIndex(schema.index);
    }

    @Override
    public boolean deleteIndex(String index) {
        Response response = null;
        try {
            response = this.client.performRequest("DELETE", "/" + index);
        } catch (IOException e) {
            log.error("Unable to delete index : {}", index, e);
            return false;
        }

        return ResponseHelper.responseAckCheck(response);
    }

    @Override
    public boolean openIndex(String index) {
        Response response = null;
        try {
            response = this.client.performRequest("POST", "/" + index + "/_open");
        } catch (IOException e) {
            log.error("Unable to open index : {}", index, e);
            return false;
        }

        return ResponseHelper.responseAckCheck(response);
    }

    @Override
    public boolean openIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        log.info("Open Index {} for Class {}", schema.index, clz.getName());

        return openIndex(schema.index);
    }

    @Override
    public boolean closeIndex(String index) {
        Response response = null;
        try {
            response = this.client.performRequest("POST", "/" + index + "/_close");
        } catch (IOException e) {
            log.error("Unable to open index : {}", index, e);
            return false;
        }

        return ResponseHelper.responseAckCheck(response);
    }

    @Override
    public boolean closeIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        log.info("Open Index {} for Class {}", schema.index, clz.getName());

        return closeIndex(schema.index);
    }

    @Override
    public boolean existedIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        log.info("Exist Index {} for Class {}", schema.index, clz.getName());

        return existedIndex(schema.index);
    }

    @Override
    public boolean existedIndex(String index) {
        Response response = null;
        try {
            response = this.client.performRequest("HEAD", "/" + index);
        } catch (IOException e) {
            log.error("Unable to check existed index : {}", index, e);
            return false;
        }

        return ResponseHelper.response200Check(response);
    }

    //@Use https://www.elastic.co/guide/en/elasticsearch/reference/current/search-count.html
    @Override
    public long countIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);
        return countIndex(schema.index, schema.type);
    }

    @Override
    public long countIndex(String index, String type) {
        Response response = null;
        try {
            response = this.client.performRequest("GET", "/" + index + "/" + type + "?size=0");
        } catch (IOException e) {
            log.error("Unable to check count index : {}", index, e);
            return -1;
        }

        if (ResponseHelper.response200Check(response)) return -1;

        return ResponseHelper.searchHitsFetch(response);
    }

    @Override
    public boolean clearIndexType(Class clz) throws BulkDeleteException {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);
        return clearIndexType(schema.index, schema.type);
    }

    @Override
    public boolean clearIndexType(String index, String type) throws BulkDeleteException {
        Response response = null;
        try {
            response = this.client.performRequest("POST", index + "/_delete_by_query");
        } catch (IOException e) {
            log.error("Unable to check clear index : {}", index, e);
            return false;
        }

        return ResponseHelper.response200Check(response) && ResponseHelper.responseTimeoutCheck(response);
    }

    @Override
    public boolean insertById(Object object, String id) {
        return insertById(object, id, 0);
    }

    @Override
    public boolean insertById(Object object, String id, long timeout) {
        Class clz = object.getClass();
        return insertById(object, clz, id);
    }

    @Override
    public boolean insertById(Object object, Class clz, String id) {
        return insertById(object, clz, id, 0);
    }

    @Override
    public boolean insertById(Object object, Class clz, String id, long timeout) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        try {
            Response response = this.client.performRequest("PUT", schema.index + "/" + schema.type + "/" + id,
                null, new NStringEntity(StorageUtil.gson.toJson(object), ContentType.APPLICATION_JSON));
            return ResponseHelper.documentCreatedCheck(response);
        } catch (IOException e) {
            log.error("Unable to insert into index : {} - {} id = {}", schema.index, schema.type, id, e);
            return false;
        }
    }

    @Override
    public boolean insertByNoId(Object object) {
        return insertByNoId(object, 0);
    }

    @Override
    public boolean insertByNoId(Object object, long timeout) {
        Class clz = object.getClass();
        return insertByNoId(object, clz);
    }

    @Override
    public boolean insertByNoId(Object object, Class clz) {
        return insertByNoId(object, clz, 0);
    }

    @Override
    public boolean insertByNoId(Object object, Class clz, long timeout) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        try {
            Response response = this.client.performRequest("PUT", schema.index + "/" + schema.type,
                null, new NStringEntity(StorageUtil.gson.toJson(object), ContentType.APPLICATION_JSON));
            return ResponseHelper.documentCreatedCheck(response);
        } catch (IOException e) {
            log.error("Unable to insert into index : {} - {}", schema.index, schema.type, e);
            return false;
        }
    }

    @Override
    public boolean insert(Object object) throws ExecutionException, InterruptedException {
        return insert(object, 0);
    }

    @Override
    public boolean insert(Object object, long timeout) throws ExecutionException, InterruptedException {
        Class clz = object.getClass();
        return insert(object, clz, timeout);
    }

    @Override
    public boolean insert(Object object, Class clz) throws ExecutionException, InterruptedException {
        return insert(object, clz, 0);
    }

    @Override
    public boolean insert(Object object, Class clz, long timeout) throws ExecutionException, InterruptedException {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        if (schema.id.length == 0) {
            return insertByNoId(object, clz, timeout);
        }

        JsonObject jobj = StorageUtil.gson.toJsonTree(object).getAsJsonObject();
        String id = schema.getId(jobj);

        try {
            Response response = this.client.performRequest("PUT", schema.index + "/" + schema.type + "/" + id,
                null, new NStringEntity(StorageUtil.gson.toJson(object), ContentType.APPLICATION_JSON));
            return ResponseHelper.documentCreatedCheck(response);
        } catch (IOException e) {
            log.error("Unable to insert into index : {} - {} id = {}", schema.index, schema.type, id, e);
            return false;
        }
    }

    @Override
    public boolean delete(Class clz, String id) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        try {
            Response response = this.client.performRequest("DELETE", "/" + schema.index + "/" + schema.type + "/" + id);
            return ResponseHelper.documentDeletedCheck(response);
        } catch (IOException e) {
            log.error("Unable to delete index : {} - {} id = {}", schema.index, schema.type, id, e);
            return false;
        }
    }

    @Override
    public <T> T get(Class<T> clz, String id) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        try {
            Response response = this.client.performRequest("GET", "/" + schema.index + "/" + schema.type + "/" + id);
            JsonObject res = ResponseHelper.getJson(response);

            if (!res.get("found").getAsBoolean()) {
                return null;
            }

            JsonObject data = res.get("_source").getAsJsonObject();

            return StorageUtil.gson.fromJson(data, clz);
        } catch (IOException e) {
            log.error("Unable to get index : {} - {} id = {}", schema.index, schema.type, id, e);
            return null;
        }
    }

    @Override
    public <T> boolean bulkInsert(T[] objects, Class<T> clz) throws ExecutionException, InterruptedException {
        return bulkInsert(Arrays.asList(objects));
    }

    @Override
    public <T> boolean bulkInsert(List<T> objects, Class<T> clz) throws ExecutionException, InterruptedException {
        try {
            Response response = this.client.performRequest("POST", "_bulk", null,
                new NStringEntity(RequestHelper.buildBulkInsert(objects, clz), ContentType.APPLICATION_JSON));
            return ResponseHelper.bulkInsertCheck(response);
        } catch (IOException e) {
            log.error("Unable to bulk insert", e);
            return false;
        }
    }

    @Override
    public <T> boolean bulkInsert(T[] objects) throws ExecutionException, InterruptedException {
        return bulkInsert(Arrays.asList(objects));
    }

    @Override
    public <T> boolean bulkInsert(List<T> objects) throws ExecutionException, InterruptedException {
        try {
            Response response = this.client.performRequest("POST", "_bulk", null,
                new NStringEntity(RequestHelper.buildBulkInsert(objects), ContentType.APPLICATION_JSON));
            return ResponseHelper.bulkInsertCheck(response);
        } catch (IOException e) {
            log.error("Unable to bulk insert", e);
            return false;
        }
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters) {
        return search(arrayClz, filters, 0, null);
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters) {
        return search(arrayClz, Arrays.asList(filters));
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters, int size) {
        return search(arrayClz, filters, size, null);
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters, int size) {
        return search(arrayClz, Arrays.asList(filters), size);
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters, int size, List<ElasticSort> sorts) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        String link = "/" + schema.index + "/_search?size=0";

        if (size > 0) {
            link += "?size=" + size;
        }

        JsonObject searchObj = new JsonObject();
        searchObj.add("query", ElasticRestSearchParser.parse(filters));

        if (sorts != null && sorts.size() > 0) {
            searchObj.add("sort", ElasticRestSortParser.parse(sorts));
        }

        try {
            Response response = this.client.performRequest("POST", link, null, new NStringEntity(
                searchObj.toString(), ContentType.APPLICATION_JSON
            ));
            JsonObject res = ResponseHelper.getJson(response);

            if (res.get("timed_out").getAsBoolean()) {
                return null;
            }

            JsonArray hits = res.get("hits").getAsJsonObject().get("hits").getAsJsonArray();

            Object[] results = new Object[hits.size()];

            for (int i = 0; i < results.length; i++) {
                results[i] = StorageUtil.gson.fromJson(hits.get(i), clz);
            }

            return Arrays.copyOf(results, results.length, arrayClz);
        } catch (IOException e) {
            log.error("Unable to search index : {} - {}", schema.index, schema.type, e);
            return null;
        }
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters, int size, List<ElasticSort> sorts) {
        return search(arrayClz, Arrays.asList(filters), size, sorts);
    }

    @Override
    public <T> Map<String, Object> aggregate(Class<T[]> arrayClz, List<ElasticFilter> filters, List<ElasticAggregate> aggregates) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        String link = "/" + schema.index + "/_search";

        JsonObject searchObj = new JsonObject();
        searchObj.add("query", ElasticRestSearchParser.parse(filters));
        searchObj.add("aggs", ElasitcRestAggregateParser.parse(aggregates));

        try {
            Response response = this.client.performRequest("POST", link, null, new NStringEntity(
                searchObj.toString(), ContentType.APPLICATION_JSON
            ));
            JsonObject res = ResponseHelper.getJson(response);

            if (res.get("timed_out").getAsBoolean()) {
                return null;
            }

            JsonObject aggregations = res.get("aggregations").getAsJsonObject();

            Map<String, Object> results = new HashMap<>();

            for (Map.Entry<String, JsonElement> entry : aggregations.entrySet()) {
                double value = entry.getValue().getAsJsonObject().get("value").getAsNumber().doubleValue();
                results.put(entry.getKey(), value);
            }

            return results;
        } catch (IOException e) {
            log.error("Unable to search index : {} - {}", schema.index, schema.type, e);
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        this.client.close();
    }
}
