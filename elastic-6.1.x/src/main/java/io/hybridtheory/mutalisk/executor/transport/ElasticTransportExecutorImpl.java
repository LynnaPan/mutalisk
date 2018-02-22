package io.hybridtheory.mutalisk.executor.transport;


import io.hybridtheory.mutalisk.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.executor.ElasticExecutorImpl;
import io.hybridtheory.mutalisk.executor.exception.BulkDeleteException;
import io.hybridtheory.mutalisk.executor.util.RequestHelper;
import io.hybridtheory.mutalisk.filter.ElasticFilter;
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
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

class ElasticTransportExecutorImpl implements ElasticExecutorImpl {
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
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(this.executor.client())
                .source(index).get();

        if (response.getBulkFailures().size() > 0) {
            throw new BulkDeleteException(response.getBulkFailures());
        }

        return true;
    }

    // insertById with id
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

    @Override
    public boolean insertById(String index, String type, String id, String source) throws ExecutionException, InterruptedException {
        return insertById(index, type, id, null);
    }

    @Override
    public boolean insert(String index, String type, String id, String source, TimeValue timeout) throws ExecutionException, InterruptedException {
        IndexRequest indexRequest = new IndexRequest(index, type, id).source(source);

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

    // normal insertById, id is judged by @ElasticSearchMeta
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

        log.info("bulkInsert insertById into elastic search with size = {}", objects.length);
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

        log.info("bulkInsert insertById into elastic search with size = {}", objects.size());
        BulkResponse responses = this.executor.client().bulk(bulkRequest).get();

        if (responses.hasFailures()) {
            throw new ExecutionException(new Exception(responses.buildFailureMessage()));
        }

        return true;
    }

    @Override
    public boolean bulkInsert(String index, String type, List<String> ids, List<String> objects) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public <T extends Object> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters) {
        SearchResponse response = rawSearch(arrayClz, filters);
        return RequestHelper.handleSearchResponse(response, arrayClz);
    }

    @Override
    public <T extends Object> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters) {
        SearchResponse response = rawSearch(arrayClz, filters);
        return RequestHelper.handleSearchResponse(response, arrayClz);
    }

    @Override
    public <T> T[] search(String index, String type, Class<T[]> arrayClz, List<ElasticFilter> filters) {
        SearchResponse response = rawSearch(index, type, arrayClz, filters);
        return RequestHelper.handleSearchResponse(response, arrayClz);
    }

    @Override
    public <T> T[] search(String index, String type, Class<T[]> arrayClz, ElasticFilter[] filters) {
        SearchResponse response = rawSearch(index, type, arrayClz, filters);
        return RequestHelper.handleSearchResponse(response, arrayClz);
    }

    @Override
    public SearchResponse rawSearch(Class arrayClz, ElasticFilter[] filters) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return rawSearch(schema.index, schema.type, arrayClz, filters);
    }

    @Override
    public SearchResponse rawSearch(Class arrayClz, List<ElasticFilter> filters) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return rawSearch(schema.index, schema.type, arrayClz, filters);
    }

    @Override
    public SearchResponse rawSearch(String index, String type, Class arrayClz, ElasticFilter[] filters) {
        Class clz = arrayClz.getComponentType();

        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (ElasticFilter filter : filters) {
            sourceBuilder.query(filter.builder());
        }

        request.source(sourceBuilder);

        return this.executor.client().search(request).actionGet();
    }

    @Override
    public SearchResponse rawSearch(String index, String type, Class arrayClz, List<ElasticFilter> filters) {
        Class clz = arrayClz.getComponentType();

        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (ElasticFilter filter : filters) {
            sourceBuilder.query(filter.builder());
        }

        request.source(sourceBuilder);

        return this.executor.client().search(request).actionGet();
    }


    public <T extends Object> Map<String, Object> aggregate(Class<T[]> arrayClz,
                                                            List<ElasticFilter> filters,
                                                            List<ElasticAggregate> aggregations) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return aggregate(schema.index, schema.type, filters, aggregations);
    }

    @Override
    public <T> Map<String, Object> aggregate(String index, String type,
                                             List<ElasticFilter> filters, List<ElasticAggregate> aggregations) {

        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (ElasticFilter filter : filters) {
            sourceBuilder.query(filter.builder());
        }

        Map<String, ElasticAggregate> aggregationMap = new HashMap<>();

        for (ElasticAggregate aggregation : aggregations) {
            sourceBuilder.aggregation(aggregation.builder());
            aggregationMap.put(aggregation.name(), aggregation);
        }

        request.source(sourceBuilder);

        Aggregations aggResults = this.executor.client().search(request).actionGet().getAggregations();

        Map<String, Object> results = new HashMap<>();

        for (Map.Entry<String, Aggregation> entry : aggResults.asMap().entrySet()) {
            ElasticAggregate xtempl = aggregationMap.get(entry.getValue().getName());

            // @TODO could use response.getAggregations().get("text").getProperty("value") to replace
            // results.put(entry.getKey(), entry.getValue().getProperty("value"));
            results.put(entry.getKey(), xtempl.value(entry.getValue()));
        }

        return results;
    }
}
