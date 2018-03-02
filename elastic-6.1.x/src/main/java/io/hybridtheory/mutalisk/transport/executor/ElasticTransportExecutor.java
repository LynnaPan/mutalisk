package io.hybridtheory.mutalisk.transport.executor;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.common.api.exception.BulkDeleteException;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.conf.ElasticClientConf;
import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.transport.executor.aggregation.ElasticAggregateParser;
import io.hybridtheory.mutalisk.transport.executor.filter.ElasticSearchParser;
import io.hybridtheory.mutalisk.transport.executor.util.RequestHelper;
import org.apache.http.HttpHost;
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
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ElasticTransportExecutor implements ElasticExecutor {
    private static final Logger log = LoggerFactory.getLogger(ElasticTransportExecutor.class);

    private TransportClient client;

    public ElasticTransportExecutor(ElasticClientConf conf) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");

         this.client = new PreBuiltTransportClient(Settings.EMPTY);

        for (HttpHost httpHost : conf.hostPorts) {
            try {
                client.addTransportAddress(
                    new TransportAddress(InetAddress.getByName(httpHost.getHostName()), httpHost.getPort()));
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    @Override
    public boolean createIndex(Class clz) {
        return createIndex(clz, 0);
    }

    @Override
    public boolean createIndex(Class clz, long timeout) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        log.info("Create Index {}:{} for Class {}", schema.index, schema.type, clz.getName());

        return createIndex(schema.index, schema.type, schema.toTypeMapping(), timeout);
    }

    @Override
    public boolean createIndex(String index, String type, String mappingSource) {
        return createIndex(index, type, mappingSource, 0);
    }

    @Override
    public boolean createIndex(String index, String type, String mappingSource, long timeout) {
        log.info("Create Index {}:{}", index, type);

        try {
            CreateIndexRequestBuilder builder = this.client.admin().indices()
                .prepareCreate(index).addMapping(type, mappingSource);

            CreateIndexResponse response;
            if (timeout != 0) {
                response = builder.get(TimeValue.timeValueMillis(timeout));
            } else {
                response = builder.get();
            }

            return response.isShardsAcked();
        } catch (Throwable t) {
            log.error("Unable to createIndex", t);
            return false;
        }
    }

    @Override
    public boolean deleteIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        log.info("Delete Index {} for Class {}", schema.index, clz.getName());

        return deleteIndex(schema.index);
    }

    @Override
    public boolean deleteIndex(String index) {
        log.info("Delete Index {} ", index);
        try {
            DeleteIndexResponse response = this.client.admin().indices().prepareDelete(index).get();

            return response.isAcknowledged();
        } catch (Throwable t) {
            log.error("Failed to delete Index", t);
            return false;
        }
    }

    @Override
    public boolean openIndex(String index) {
        return false;
    }

    @Override
    public boolean openIndex(Class clz) {
        return false;
    }

    @Override
    public boolean closeIndex(String index) {
        return false;
    }

    @Override
    public boolean closeIndex(Class clz) {
        return false;
    }

    @Override
    public boolean existedIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        log.info("Test Existed Index {} for Class {}", schema.index, clz.getName());

        return existedIndex(schema.index);
    }

    @Override
    public boolean existedIndex(String index) {
        try {
            IndicesExistsResponse response = this.client.admin().indices().prepareExists(index).get();

            return response.isExists();
        } catch (Throwable t) {
            log.error("Delete to createIndex", t);
            return false;
        }
    }

    @Override
    public long countIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return countIndex(schema.index, schema.type);
    }

    @Override
    public long countIndex(String index, String type) {
        SearchResponse response = this.client.prepareSearch(index).setTypes(type)
            .setSize(0).setRequestCache(false).get();

        return response.getHits().getTotalHits();
    }

    @Override
    public boolean clearIndexType(Class clz) throws BulkDeleteException {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return clearIndexType(schema.index, schema.type);
    }

    @Override
    public boolean clearIndexType(String index, String type) throws BulkDeleteException {
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(this.client)
            .source(index).get();

        if (response.getBulkFailures().size() > 0) {
            throw new BulkDeleteException(response.getBulkFailures());
        }

        return true;
    }

    @Override
    public boolean insertById(Object object, String id) {
        return insertById(object, id, 0);
    }

    @Override
    public boolean insertById(Object object, String id, long timeout) {
        IndexRequest indexRequest = RequestHelper.buildIdIndexRequest(object, id);

        IndexResponse response;
        if (timeout != 0) {
            response = this.client.index(indexRequest).actionGet(TimeValue.timeValueMillis(timeout));
        } else {
            response = this.client.index(indexRequest).actionGet();
        }

        return RequestHelper.assertCreatedOrUpdated(response.status());
    }

    @Override
    public boolean insertById(Object object, Class clz, String id) {
        return insertById(object, clz, id, 0);
    }

    @Override
    public boolean insertById(Object object, Class clz, String id, long timeout) {
        IndexRequest indexRequest = RequestHelper.buildIdIndexRequest(clz, object, id);

        IndexResponse response;
        if (timeout != 0) {
            response = this.client.index(indexRequest).actionGet(timeout);
        } else {
            response = this.client.index(indexRequest).actionGet();
        }

        return RequestHelper.assertCreatedOrUpdated(response.status());
    }

    @Override
    public boolean insertByNoId(Object object) {
        return insertByNoId(object, 0);
    }

    @Override
    public boolean insertByNoId(Object object, long timeout) {
        IndexRequest indexRequest = RequestHelper.buildNoIdIndexRequest(object);

        IndexResponse response;
        if (timeout != 0){
            response = this.client.index(indexRequest).actionGet(timeout);
        } else {
            response = this.client.index(indexRequest).actionGet();
        }

        return RequestHelper.assertCreatedOrUpdated(response.status());
    }

    @Override
    public boolean insertByNoId(Object object, Class clz) {
        return insertByNoId(object, clz, 0);
    }

    @Override
    public boolean insertByNoId(Object object, Class clz, long timeout) {
        IndexRequest indexRequest = RequestHelper.buildNoIdIndexRequest(clz, object);

        IndexResponse response;
        if (timeout != 0){
            response = this.client.index(indexRequest).actionGet(timeout);
        } else {
            response = this.client.index(indexRequest).actionGet();
        }

        return RequestHelper.assertCreatedOrUpdated(response.status());
    }

    @Override
    public boolean insert(Object object) throws ExecutionException, InterruptedException {
        return insert(object, 0);
    }

    @Override
    public boolean insert(Object object, long timeout) throws ExecutionException, InterruptedException {
        IndexRequest indexRequest = RequestHelper.buildIndexRequest(object);

        IndexResponse response;
        if (timeout != 0) {
            response = this.client.index(indexRequest).actionGet(timeout);
        } else {
            response = this.client.index(indexRequest).actionGet();
        }

        return RequestHelper.assertCreatedOrUpdated(response.status());
    }

    @Override
    public boolean insert(Object object, Class clz) throws ExecutionException, InterruptedException {
        return insert(object, clz, 0);
    }

    @Override
    public boolean insert(Object object, Class clz, long timeout) throws ExecutionException, InterruptedException {
        IndexRequest indexRequest = RequestHelper.buildIndexRequest(clz, object);

        IndexResponse response;
        if (timeout != 0) {
            response = this.client.index(indexRequest).actionGet(timeout);
        } else {
            response = this.client.index(indexRequest).actionGet();
        }

        return RequestHelper.assertCreatedOrUpdated(response.status());
    }

    @Override
    public boolean delete(Class clz, String id) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        DeleteResponse response = this.client.prepareDelete()
            .setIndex(schema.index).setType(schema.type).setId(id)
            .get();

        return response.status().equals(RestStatus.ACCEPTED);
    }

    @Override
    public <T> T get(Class<T> clz, String id) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        GetResponse response = this.client.prepareGet()
            .setIndex(schema.index).setType(schema.type).setId(id)
            .get();

        if (response.isExists()) {
            return StorageUtil.gson.fromJson(response.getSourceAsString(), clz);
        } else {
            return null;
        }
    }

    @Override
    public <T> boolean bulkInsert(T[] objects, Class<T> clz) throws ExecutionException, InterruptedException {
        return bulkInsert(Arrays.asList(objects), clz);
    }

    @Override
    public <T> boolean bulkInsert(List<T> objects, Class<T> clz) throws ExecutionException, InterruptedException {
        BulkRequest bulkRequest = new BulkRequest();

        for (Object object : objects) {
            bulkRequest.add(RequestHelper.buildIndexRequest(clz, object));
        }

        log.info("bulkInsert insert into elastic search with size = {}", objects.size());
        BulkResponse responses = this.client.bulk(bulkRequest).get();

        if (responses.hasFailures()) {
            throw new ExecutionException(new Exception(responses.buildFailureMessage()));
        }

        return true;
    }

    @Override
    public <T> boolean bulkInsert(T[] objects) throws ExecutionException, InterruptedException {
        return bulkInsert(Arrays.asList(objects));
    }

    @Override
    public <T> boolean bulkInsert(List<T> objects) throws ExecutionException, InterruptedException {
        BulkRequest bulkRequest = new BulkRequest();

        for (Object object : objects) {
            bulkRequest.add(RequestHelper.buildIndexRequest(object));
        }

        log.info("bulkInsert insert into elastic search with size = {}", objects.size());
        BulkResponse responses = this.client.bulk(bulkRequest).get();

        if (responses.hasFailures()) {
            throw new ExecutionException(new Exception(responses.buildFailureMessage()));
        }

        return true;
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        SearchRequest request = new SearchRequest(schema.index);
        request.types(schema.type);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (QueryBuilder filter : ElasticSearchParser.parse(filters)) {
            sourceBuilder.query(filter);
        }


        request.source(sourceBuilder);

        SearchHits hits = this.client.search(request).actionGet().getHits();

        Object[] results = new Object[hits.getHits().length];

        for (int i = 0; i < results.length; i++) {
            results[i] = StorageUtil.gson.fromJson(hits.getAt(i).getSourceAsString(), clz);
        }

        return Arrays.copyOf(results, results.length, arrayClz);
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters) {
        return search(arrayClz, Arrays.asList(filters));
    }

    @Override
    public <T> Map<String, Object> aggregate(Class<T[]> arrayClz,
                                             List<ElasticFilter> filters,
                                             List<ElasticAggregate> aggregates) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        SearchRequest request = new SearchRequest(schema.index);
        request.types(schema.type);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (QueryBuilder filter : ElasticSearchParser.parse(filters)) {
            sourceBuilder.query(filter);
        }

        for (AggregationBuilder builder: ElasticAggregateParser.parse(aggregates)) {
            sourceBuilder.aggregation(builder);
        }

        request.source(sourceBuilder);

        Aggregations aggResults = this.client.search(request).actionGet().getAggregations();

        Map<String, Object> results = new HashMap<>();

        for (Map.Entry<String, Aggregation> entry : aggResults.asMap().entrySet()) {
            Aggregation agg = entry.getValue();

            if (NumericMetricsAggregation.SingleValue.class.isAssignableFrom(agg.getClass())) {
                // @TODO could use response.getAggregations().get("text").getProperty("value") to replace
                // results.put(entry.getKey(), entry.getValue().getProperty("value"));
                results.put(entry.getKey(), ((NumericMetricsAggregation.SingleValue)agg).value());
            }
        }

        return results;
    }

    @Override
    public void close() throws IOException {
        this.client.close();
    }
}
