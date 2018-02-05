package io.hybridtheory.mutalisk.executor;

import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.executor.exception.BulkDeleteException;
import io.hybridtheory.mutalisk.filter.ElasticFilter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface ElasticExecutorImpl {
    public boolean createIndex(Class clz);

    public boolean createIndex(Class clz, TimeValue timeout);

    public boolean createIndex(String index, String type, String mappingSource);

    public boolean createIndex(String index, String type, String mappingSource, TimeValue timeout);

    // remove index
    public boolean deleteIndex(Class clz);

    public boolean deleteIndex(String index);

    public boolean existedIndex(Class clz);

    public boolean existedIndex(String index);

    public long countIndex(Class clz);

    public long countIndex(String index, String type);

    public boolean clearIndexType(Class clz) throws BulkDeleteException;

    public boolean clearIndexType(String index, String type) throws BulkDeleteException;

    // insertById with id
    public boolean insertById(Object object, String id);
    public boolean insertById(Object object, String id, TimeValue timeout);

    // insertById with id, with json as source
    public boolean insertById(String index, String type, String id,
                              String source) throws ExecutionException, InterruptedException;
    public boolean insert(String index, String type, String id,
                          String source, TimeValue timeout) throws ExecutionException, InterruptedException;

    public boolean insertByNoId(Object object);
    public boolean insertByNoId(Object object, TimeValue timeout);

    // normal insertById, id is judged by @ElasticSearchMeta
    public boolean insert(Object object) throws ExecutionException, InterruptedException;
    public boolean insert(Object object, TimeValue timeout) throws ExecutionException, InterruptedException;



    //delete by id
    public boolean delete(Class clz, String id);

    // get one specifed object with id
    public <T extends Object> T get(Class<T> clz, String id);

    // bulkInsert multi values, values may not in same type
    public <T> boolean bulkInsert(T[] objects) throws ExecutionException, InterruptedException;
    public <T> boolean bulkInsert(List<T> objects) throws ExecutionException, InterruptedException;

    boolean bulkInsert(String index, String type,
                       List<String> ids, List<String> objects) throws ExecutionException, InterruptedException;

    public <T> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters);
    public <T extends Object> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters);
    public <T extends Object> T[] search(String index, String type, Class<T[]> arrayClz, List<ElasticFilter> filters);
    public <T extends Object> T[] search(String index, String type, Class<T[]> arrayClz, ElasticFilter[] filters);

    SearchResponse rawSearch(Class arrayClz, ElasticFilter[] filters);
    SearchResponse rawSearch(Class arrayClz, List<ElasticFilter> filters);
    SearchResponse rawSearch(String index, String type, Class arrayClz, ElasticFilter[] filters);
    SearchResponse rawSearch(String index, String type, Class arrayClz, List<ElasticFilter> filters);

    public <T extends Object> Map<String, Object> aggregate(Class<T[]> arrayClz,
                                                            List<ElasticFilter> filters,
                                                            List<ElasticAggregate> aggregations);
    public <T extends Object> Map<String, Object> aggregate(String index, String type,
                                                            List<ElasticFilter> filters,
                                                            List<ElasticAggregate> aggregations);
}
