package io.hybridtheory.mutalisk.executor;


import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.executor.aggregation.ElasticSearchAPIAggregation;
import io.hybridtheory.mutalisk.executor.exception.BulkDeleteException;
import io.hybridtheory.mutalisk.executor.filter.ElasticSearchAPIFilter;
import io.hybridtheory.mutalisk.executor.util.RequestHelper;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.reindex.BulkIndexByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

class ElasticTransportExecutorImpl {
    private static final Logger log = LoggerFactory.getLogger(ElasticTransportExecutorImpl.class);
    ElasticTransportExecutor executor;

    public ElasticTransportExecutorImpl(ElasticTransportExecutor executor) {
        this.executor = executor;
    }

    // create action
    // https://www.elastic.co/guide/en/elasticsearch/client/java-api/5.2/java-admin-indices.html
    public boolean createIndex(Class clz) {
        return createIndex(clz, null);
    }

    public boolean createIndex(Class clz, TimeValue timeout) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        log.info("Create Index {}:{} for Class {}", schema.index, schema.type, clz.getName());

        return createIndex(schema.index, schema.type, schema.toTypeMapping(), timeout);
    }

    public boolean createIndex(String index, String type, String mappingSource) {
        return createIndex(index, type, mappingSource, null);
    }

    public boolean createIndex(String index, String type, String mappingSource, TimeValue timeout) {
        log.info("Create Index {}:{}", index, type);

        try {
            CreateIndexRequestBuilder builder = this.executor.client().admin().indices()
                    .prepareCreate(index).addMapping(type, mappingSource);

            CreateIndexResponse response;
            if (timeout != null) {
                response = builder.get(timeout);
            } else {
                response = builder.get();
            }

            return response.isShardsAcked();
        } catch (Throwable t) {
            log.error("Unable to createIndex", t);
            return false;
        }
    }

    // remove index
    public boolean deleteIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        log.info("Delete Index {} for Class {}", schema.index, clz.getName());

        return deleteIndex(schema.index);
    }

    public boolean deleteIndex(String index) {
        try {
            DeleteIndexResponse response = this.executor.client().admin().indices().prepareDelete(index).get();

            return response.isAcknowledged();
        } catch (Throwable t) {
            log.error("Delete to createIndex", t);
            return false;
        }
    }

    public boolean existedIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        log.info("Test Existed Index {} for Class {}", schema.index, clz.getName());

        return existedIndex(schema.index);
    }

    public boolean existedIndex(String index) {
        try {
            IndicesExistsResponse response = this.executor.client().admin().indices().prepareExists(index).get();

            return response.isExists();
        } catch (Throwable t) {
            log.error("Delete to createIndex", t);
            return false;
        }
    }

    public long countIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return countIndex(schema.index, schema.type);
    }

    public long countIndex(String index, String type) {
        SearchResponse response = this.executor.client().prepareSearch(index).setTypes(type)
                .setSize(0).setRequestCache(false).get();

        return response.getHits().getTotalHits();
    }

    public boolean clearIndexType(Class clz) throws BulkDeleteException {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return clearIndexType(schema.index, schema.type);
    }

    public boolean clearIndexType(String index, String type) throws BulkDeleteException {
        BulkIndexByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(this.executor.client())
                .source(index).get();

        if (response.getBulkFailures().size() > 0) {
            throw new BulkDeleteException(response.getBulkFailures());
        }

        return true;
    }

    // insert with id
    public boolean insertById(Object object, String id) {
        return insertById(object, id, null);
    }

    public boolean insertById(Object object, String id, TimeValue timeout) {
        IndexRequest indexRequest = RequestHelper.buildIdIndexRequest(object, id);

        IndexResponse response;
        if (timeout != null) {
            response = this.executor.client().index(indexRequest).actionGet(timeout);
        } else {
            response = this.executor.client().index(indexRequest).actionGet();
        }

        return RequestHelper.assertCreatedOrUpdated(response.status());
    }

    public boolean insertByNoId(Object object) {
        return insertByNoId(object, null);
    }

    public boolean insertByNoId(Object object, TimeValue timeout) {
        IndexRequest indexRequest = RequestHelper.buildNoIdIndexRequest(object);

        IndexResponse response;
        if (timeout != null) {
            response = this.executor.client().index(indexRequest).actionGet(timeout);
        } else {
            response = this.executor.client().index(indexRequest).actionGet();
        }

        return RequestHelper.assertCreatedOrUpdated(response.status());
    }

    // normal insert, id is judged by @ElasticSearchMeta
    public boolean insert(Object object) throws ExecutionException, InterruptedException {
        return insert(object, null);
    }

    public boolean insert(Object object, TimeValue timeout) throws ExecutionException, InterruptedException {
        IndexRequest indexRequest = RequestHelper.buildIndexRequest(object);

        IndexResponse response;
        if (timeout != null) {
            response = this.executor.client().index(indexRequest).actionGet(timeout);
        } else {
            response = this.executor.client().index(indexRequest).actionGet();
        }

        return RequestHelper.assertCreatedOrUpdated(response.status());
    }

    //delete by id
    public boolean delete(Class clz, String id) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        DeleteResponse response = this.executor.client().prepareDelete()
                .setIndex(schema.index).setType(schema.type).setId(id)
                .get();

        return response.status().equals(RestStatus.ACCEPTED);
    }

    // get one specifed object with id
    public <T extends Object> T get(Class<T> clz, String id) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        GetResponse response = this.executor.client().prepareGet()
                .setIndex(schema.index).setType(schema.type).setId(id)
                .get();

        if (response.isExists()) {
            return StorageUtil.gson.fromJson(response.getSourceAsString(), clz);
        } else {
            return null;
        }
    }

    // bulkInsert multi values, values may not in same type
    public <T> boolean bulkInsert(T[] objects) throws ExecutionException, InterruptedException {
        BulkRequest bulkRequest = new BulkRequest();

        for (Object object : objects) {
            bulkRequest.add(RequestHelper.buildIndexRequest(object));
        }

        log.info("bulkInsert insert into elastic search with size = {}", objects.length);
        BulkResponse responses = this.executor.client().bulk(bulkRequest).get();

        if (responses.hasFailures()) {
            throw new ExecutionException(new Exception(responses.buildFailureMessage()));
        }

        return true;
    }

    public <T> boolean bulkInsert(List<T> objects) throws ExecutionException, InterruptedException {

        BulkRequest bulkRequest = new BulkRequest();

        for (Object object : objects) {
            bulkRequest.add(RequestHelper.buildIndexRequest(object));
        }

        log.info("bulkInsert insert into elastic search with size = {}", objects.size());
        BulkResponse responses = this.executor.client().bulk(bulkRequest).get();

        if (responses.hasFailures()) {
            throw new ExecutionException(new Exception(responses.buildFailureMessage()));
        }

        return true;
    }

    public <T extends Object> T[] search(Class<T[]> arrayClz,
                                         List<ElasticSearchAPIFilter> filters) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        SearchRequest request = new SearchRequest(schema.index);
        request.types(schema.type);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (ElasticSearchAPIFilter filter : filters) {
            sourceBuilder.query(filter.toQueryBuilder());
        }


        request.source(sourceBuilder);

        SearchHits hits = this.executor.client().search(request).actionGet().getHits();

        Object[] results = new Object[hits.hits().length];

        for (int i = 0; i < results.length; i++) {
            results[i] = StorageUtil.gson.fromJson(hits.getAt(i).sourceAsString(), clz);
        }

        return Arrays.copyOf(results, results.length, arrayClz);
    }

    public <T extends Object> T[] search(Class<T[]> arrayClz,
                                         ElasticSearchAPIFilter[] filters) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        SearchRequest request = new SearchRequest(schema.index);
        request.types(schema.type);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (ElasticSearchAPIFilter filter : filters) {
            sourceBuilder.query(filter.toQueryBuilder());
        }


        request.source(sourceBuilder);

        SearchHits hits = this.executor.client().search(request).actionGet().getHits();

        Object[] results = new Object[hits.hits().length];

        for (int i = 0; i < results.length; i++) {
            results[i] = StorageUtil.gson.fromJson(hits.getAt(i).sourceAsString(), clz);
        }

        return Arrays.copyOf(results, results.length, arrayClz);
    }

    public <T extends Object> Map<String, Object> aggregate(Class<T[]> arrayClz,
                                                            List<ElasticSearchAPIFilter> filters,
                                                            List<ElasticSearchAPIAggregation> aggregations) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        SearchRequest request = new SearchRequest(schema.index);
        request.types(schema.type);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (ElasticSearchAPIFilter filter : filters) {
            sourceBuilder.query(filter.toQueryBuilder());
        }

        Map<String, ElasticSearchAPIAggregation> aggregationMap = new HashMap<>();

        for (ElasticSearchAPIAggregation aggregation : aggregations) {
            sourceBuilder.aggregation(aggregation.toAggregationBuilder());
            aggregationMap.put(aggregation.name(), aggregation);
        }

        request.source(sourceBuilder);

        Aggregations aggResults = this.executor.client().search(request).actionGet().getAggregations();

        Map<String, Object> results = new HashMap<>();

        for (Map.Entry<String, Aggregation> entry : aggResults.asMap().entrySet()) {
            ElasticSearchAPIAggregation xtempl = aggregationMap.get(entry.getValue().getName());

            // @TODO could use response.getAggregations().get("text").getProperty("value") to replace
            // results.put(entry.getKey(), entry.getValue().getProperty("value"));
            results.put(entry.getKey(), xtempl.value(entry.getValue()));
        }

        return results;
    }
}
