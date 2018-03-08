package io.hybridtheory.mutalisk.highlevel.executor;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.common.api.exception.BulkDeleteException;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.api.sort.ElasticSort;
import io.hybridtheory.mutalisk.common.conf.ElasticClientConf;
import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.highlevel.executor.aggregation.ElasticAggregateParser;
import io.hybridtheory.mutalisk.highlevel.executor.sort.ElasticSortParser;
import io.hybridtheory.mutalisk.transport.executor.filter.ElasticSearchParser;
import io.hybridtheory.mutalisk.transport.executor.util.RequestHelper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by lynna on 2018/2/28.
 */
public class HighLevelExecutor implements ElasticExecutor {
    private static final Logger log = LoggerFactory.getLogger(HighLevelExecutor.class);

    private RestHighLevelClient client;

    public HighLevelExecutor(ElasticClientConf conf) {
        try {
            this.client = new RestHighLevelClient(RestClient.builder(conf.hostPorts));
        } catch (Throwable t) {
            t.printStackTrace();
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

        return createIndex(schema.index, schema.type, schema.toIndexSetting(), schema.toTypeMapping(), timeout);
    }

    @Override
    public boolean createIndex(String index, String type, String settingSource,  String mappingSource) {
        return createIndex(index, type, settingSource, mappingSource, 0);
    }

    @Override
    public boolean createIndex(String index, String type, String settingSource,  String mappingSource, long timeout) {
        log.info("Creating Index {}:{}", index, type);
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);

            request.mapping(type, mappingSource, XContentType.JSON);
            if(timeout != 0) {
                request.timeout(TimeValue.timeValueMillis(timeout));
            }
            CreateIndexResponse createIndexResponse = client.indices().create(request);

            return createIndexResponse.isShardsAcknowledged();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);
        log.info("Delete Index {} for class {}", schema.index, clz.getName());

        return deleteIndex(schema.index);
    }

    @Override
    public boolean deleteIndex(String index) {
        log.info("Delete index {}", index);
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(index);
            DeleteIndexResponse deleteIndexResponse = client.indices().delete(request);
            return deleteIndexResponse.isAcknowledged();
        } catch (ElasticsearchException exception){
            if(exception.status() == RestStatus.NOT_FOUND) {
                log.info("Index does not exist!");
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean openIndex(String index) {
        log.info("Open Index {}", index);
        try {
            OpenIndexRequest request = new OpenIndexRequest(index);
            OpenIndexResponse openIndexResponse = client.indices().open(request);
            return openIndexResponse.isShardsAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean openIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);
        log.info("Open Index {} for class {}", schema.index, clz.getName());

        return openIndex(schema.index);
    }

    @Override
    public boolean closeIndex(String index) {
        log.info("Close Index {}", index);
        try {
            CloseIndexRequest request = new CloseIndexRequest(index);
            CloseIndexResponse closeIndexResponse = client.indices().close(request);
            return closeIndexResponse.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean closeIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);
        log.info("Close Index {} for class {}", schema.index, clz.getName());

        return closeIndex(schema.index);
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
            log.info("Test Existed Index {}", index);
            Response response = client.getLowLevelClient().performRequest("HEAD", index);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200) {
                return true;
            }
            else if(statusCode == 404) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public long countIndex(Class clz) {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);
        return countIndex(schema.index, schema.type);
    }

    @Override
    public long countIndex(String index, String type) {
        try {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        log.info("Search {} items in index {}.", searchResponse.getHits().getTotalHits(), index);

        return searchResponse.getHits().getTotalHits();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean clearIndexType(Class clz) throws BulkDeleteException {
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        return clearIndexType(schema.index, schema.type);
    }

    @Override
    public boolean clearIndexType(String index, String type) throws BulkDeleteException {
        try {
            BulkByScrollResponse response =
                    DeleteByQueryAction.INSTANCE.newRequestBuilder((ElasticsearchClient) client)
                            .source(index)
                            .get();


        long deleted = response.getDeleted();
        log.info("Delete {} items in index.", deleted, index);
        if (response.getBulkFailures().size() > 0) {
            throw new BulkDeleteException(response.getBulkFailures());
        }

        return true;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    private boolean insertByRequest(IndexRequest request, long timeout) {
        try {
            if (timeout > 0) {
                request.timeout(TimeValue.timeValueMillis(timeout));
            }
            IndexResponse indexResponse = client.index(request);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                log.info("Document was created!");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                log.info("Document was updated!");
            }
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                log.info("Successful shards is less than total shards!");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                    log.info("insert by id is failure: {} ", reason);
                }
                return false;
            }
            return RequestHelper.assertCreatedOrUpdated(indexResponse.status());

        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    @Override
    public boolean insertById(Object object, String id) {
        return insertById(object, id, 0);
    }

    @Override
    public boolean insertById(Object object, String id, long timeout) {
            IndexRequest request = RequestHelper.buildIdIndexRequest(object, id);
            log.info("insert id {} ", id);

            return insertByRequest(request, timeout);
    }

    @Override
    public boolean insertById(Object object, Class clz, String id) {

        return insertById(object, clz, id, 0);
    }

    @Override
    public boolean insertById(Object object, Class clz, String id, long timeout) {
            IndexRequest request = RequestHelper.buildIdIndexRequest(clz, object, id);

            return insertByRequest(request, timeout);
    }

    @Override
    public boolean insertByNoId(Object object) {
        return insertByNoId(object, 0);
    }

    @Override
    public boolean insertByNoId(Object object, long timeout) {
            IndexRequest request = RequestHelper.buildNoIdIndexRequest(object);
            return insertByRequest(request, timeout);
    }

    @Override
    public boolean insertByNoId(Object object, Class clz) {
        return insertByNoId(object, clz, 0);
    }

    @Override
    public boolean insertByNoId(Object object, Class clz, long timeout) {
            IndexRequest request = RequestHelper.buildNoIdIndexRequest(clz, object);
            return insertByRequest(request, timeout);
    }

    @Override
    public boolean insert(Object object) throws ExecutionException, InterruptedException {

        return insert(object, 0);
    }

    @Override
    public boolean insert(Object object, long timeout) throws ExecutionException, InterruptedException {
            IndexRequest request = RequestHelper.buildIndexRequest(object);
            return insertByRequest(request, timeout);
    }

    @Override
    public boolean insert(Object object, Class clz) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean insert(Object object, Class clz, long timeout) throws ExecutionException, InterruptedException {
            IndexRequest request = RequestHelper.buildIndexRequest(clz, object);
            return insertByRequest(request, timeout);
    }

    @Override
    public boolean delete(Class clz, String id) {
        try {
            ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);
            DeleteRequest request = new DeleteRequest(schema.index, schema.type, id);
            DeleteResponse deleteResponse = client.delete(request);
            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                log.info("Delete target does not exist.");
            }
            return deleteResponse.getResult() == DocWriteResponse.Result.DELETED;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    @Override
    public <T> T get(Class<T> clz, String id) {
        try{
            ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);
            GetRequest request = new GetRequest(schema.index, schema.type, id);
            GetResponse getResponse = client.get(request);
            if (getResponse.isExists()) {
                String sourceAsString = getResponse.getSourceAsString();
                return StorageUtil.gson.fromJson(sourceAsString, clz);
            } else {
                log.info("Response does not exist.");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> boolean bulkInsert(T[] objects, Class<T> clz) throws ExecutionException, InterruptedException {
        return bulkInsert(Arrays.asList(objects),clz);
    }

    @Override
    public <T> boolean bulkInsert(List<T> objects, Class<T> clz) throws ExecutionException, InterruptedException {
        BulkRequest bulkRequest = new BulkRequest();

        for(Object object:objects) {
            bulkRequest.add(RequestHelper.buildIndexRequest(clz,object));
        }
        try {
            BulkResponse responses = client.bulk(bulkRequest);
            if(responses.hasFailures()){
                throw new ExecutionException(new Exception(responses.buildFailureMessage()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public <T> boolean bulkInsert(T[] objects) throws ExecutionException, InterruptedException {
        return bulkInsert(Arrays.asList(objects));
    }

    @Override
    public <T> boolean bulkInsert(List<T> objects) throws ExecutionException, InterruptedException {
        BulkRequest bulkRequest = new BulkRequest();

        for(Object object:objects) {
            bulkRequest.add(RequestHelper.buildIndexRequest(object));
        }
        try {
            BulkResponse responses = client.bulk(bulkRequest);
            if(responses.hasFailures()){
                throw new ExecutionException(new Exception(responses.buildFailureMessage()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters) {
        return search(arrayClz, filters, 0);
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters) {
        return search(arrayClz, Arrays.asList(filters));
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters, int size) {
        return search(arrayClz, filters,size,null);
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters, int size) {
        return search(arrayClz, Arrays.asList(filters),size, null);
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters, int size, List<ElasticSort> sorts) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        SearchRequest request = new SearchRequest(schema.index);
        request.types(schema.type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if(sorts != null && sorts.size() > 0){
        for(SortBuilder builder: ElasticSortParser.parse(sorts)){
            searchSourceBuilder.sort(builder);
        }}


        if(size > 0){
            searchSourceBuilder.size(size);
        }

        for (QueryBuilder filter: ElasticSearchParser.parse(filters)){
            searchSourceBuilder.query(filter);
        }

        request.source(searchSourceBuilder);

        try {
            SearchHits hits = client.search(request).getHits();
            int totalHits = (int)hits.getTotalHits();
            Object[] results = new Object[totalHits];
            for(int i = 0; i <totalHits; i++) {
                results[i] = StorageUtil.gson.fromJson(hits.getAt(i).getSourceAsString(), clz);
            }
            return Arrays.copyOf(results, totalHits, arrayClz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters, int size, List<ElasticSort> sorts) {
        return search(arrayClz, Arrays.asList(filters),size,null);
    }

    @Override
    public <T> Map<String, Object> aggregate(Class<T[]> arrayClz, List<ElasticFilter> filters, List<ElasticAggregate> aggregates) {
        Class clz = arrayClz.getComponentType();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(clz);

        SearchRequest request = new SearchRequest(schema.index);
        request.types(schema.type);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (QueryBuilder filter : ElasticSearchParser.parse(filters)) {
            sourceBuilder.query(filter);
        }

        for (AggregationBuilder builder : ElasticAggregateParser.parse(aggregates)) {
            sourceBuilder.aggregation(builder);
        }

        request.source(sourceBuilder);

        Aggregations aggResults = null;
        try {
            aggResults = client.search(request).getAggregations();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Object> results = new HashMap<>();

        assert aggResults != null;
        for (Map.Entry<String, Aggregation> entry : aggResults.asMap().entrySet()) {
            Aggregation agg = entry.getValue();

            if (NumericMetricsAggregation.SingleValue.class.isAssignableFrom(agg.getClass())) {
                // @TODO could use response.getAggregations().get("text").getProperty("value") to replace
                // results.put(entry.getKey(), entry.getValue().getProperty("value"));
                results.put(entry.getKey(), ((NumericMetricsAggregation.SingleValue) agg).value());
            }
        }

        return results;
    }

    @Override
    public void close() throws IOException {
        this.client.close();
    }
}
