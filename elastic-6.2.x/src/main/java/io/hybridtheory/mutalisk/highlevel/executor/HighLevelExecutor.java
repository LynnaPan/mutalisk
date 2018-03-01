package io.hybridtheory.mutalisk.highlevel.executor;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.common.api.exception.BulkDeleteException;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;
import io.hybridtheory.mutalisk.common.conf.ElasticClientConf;
import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

        return createIndex(schema.index, schema.type, schema.toTypeMapping(), timeout);
    }

    @Override
    public boolean createIndex(String index, String type, String mappingSource) {
        return createIndex(index, type, mappingSource, 0);
    }

    @Override
    public boolean createIndex(String index, String type, String mappingSource, long timeout) {
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
    public boolean existedIndex(Class clz) {
        return false;
    }

    @Override
    public boolean existedIndex(String index) {
        return false;
    }

    @Override
    public long countIndex(Class clz) {
        return 0;
    }

    @Override
    public long countIndex(String index, String type) {
        return 0;
    }

    @Override
    public boolean clearIndexType(Class clz) throws BulkDeleteException {
        return false;
    }

    @Override
    public boolean clearIndexType(String index, String type) throws BulkDeleteException {
        return false;
    }

    @Override
    public boolean insertById(Object object, String id) {
        return false;
    }

    @Override
    public boolean insertById(Object object, String id, long timeout) {
        return false;
    }

    @Override
    public boolean insertById(Object object, Class clz, String id) {
        return false;
    }

    @Override
    public boolean insertById(Object object, Class clz, String id, long timeout) {
        return false;
    }

    @Override
    public boolean insertByNoId(Object object) {
        return false;
    }

    @Override
    public boolean insertByNoId(Object object, long timeout) {
        return false;
    }

    @Override
    public boolean insertByNoId(Object object, Class clz) {
        return false;
    }

    @Override
    public boolean insertByNoId(Object object, Class clz, long timeout) {
        return false;
    }

    @Override
    public boolean insert(Object object) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean insert(Object object, long timeout) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean insert(Object object, Class clz) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean insert(Object object, Class clz, long timeout) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean delete(Class clz, String id) {
        return false;
    }

    @Override
    public <T> T get(Class<T> clz, String id) {
        return null;
    }

    @Override
    public <T> boolean bulkInsert(T[] objects, Class<T> clz) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public <T> boolean bulkInsert(List<T> objects, Class<T> clz) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public <T> boolean bulkInsert(T[] objects) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public <T> boolean bulkInsert(List<T> objects) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters) {
        return null;
    }

    @Override
    public <T> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters) {
        return null;
    }

    @Override
    public <T> Map<String, Object> aggregate(Class<T[]> arrayClz, List<ElasticFilter> filters, List<ElasticAggregate> aggregates) {
        return null;
    }

    @Override
    public void close() throws IOException {
        this.client.close();
    }
}