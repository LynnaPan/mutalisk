package io.hybridtheory.mutalisk.common.api;

import io.hybridtheory.mutalisk.common.api.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.common.api.exception.BulkDeleteException;
import io.hybridtheory.mutalisk.common.api.filter.ElasticFilter;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface ElasticExecutor extends Closeable{

    // create action
    // https://www.elastic.co/guide/en/elasticsearch/client/java-api/5.2/java-admin-indices.html
    public boolean createIndex(Class clz);

    public boolean createIndex(Class clz, long timeout);

    public boolean createIndex(String index, String type, String settingSource, String mappingSource);

    public boolean createIndex(String index, String type, String settingSource, String mappingSource, long timeout);

    // remove index
    public boolean deleteIndex(Class clz);

    public boolean deleteIndex(String index);

    boolean openIndex(String index);

    boolean openIndex(Class clz);

    boolean closeIndex(String index);

    boolean closeIndex(Class clz);

    public boolean existedIndex(Class clz);

    public boolean existedIndex(String index);

    public long countIndex(Class clz);

    public long countIndex(String index, String type);

    public boolean clearIndexType(Class clz) throws BulkDeleteException;

    public boolean clearIndexType(String index, String type) throws BulkDeleteException;

    // insert with id
    public boolean insertById(Object object, String id);

    public boolean insertById(Object object, String id, long timeout);

    public boolean insertById(Object object, Class clz, String id);

    public boolean insertById(Object object, Class clz, String id, long timeout);

    public boolean insertByNoId(Object object);

    public boolean insertByNoId(Object object, long timeout);

    public boolean insertByNoId(Object object, Class clz);

    public boolean insertByNoId(Object object, Class clz, long timeout);

    // normal insert, id is judged by @ElasticSearchMeta
    public boolean insert(Object object) throws ExecutionException, InterruptedException;

    public boolean insert(Object object, long timeout) throws ExecutionException, InterruptedException;

    public boolean insert(Object object, Class clz) throws ExecutionException, InterruptedException;

    public boolean insert(Object object, Class clz, long timeout) throws ExecutionException, InterruptedException;

    //delete by id
    public boolean delete(Class clz, String id);

    // get one specifed object with id
    public <T extends Object> T get(Class<T> clz, String id);

    // bulkInsert multi values, values may not in same type
    public <T> boolean bulkInsert(T[] objects, Class<T> clz) throws ExecutionException, InterruptedException;

    public <T> boolean bulkInsert(List<T> objects, Class<T> clz) throws ExecutionException, InterruptedException;

    public <T> boolean bulkInsert(T[] objects) throws ExecutionException, InterruptedException;

    public <T> boolean bulkInsert(List<T> objects) throws ExecutionException, InterruptedException;

    public <T extends Object> T[] search(Class<T[]> arrayClz, List<ElasticFilter> filters);

    public <T extends Object> T[] search(Class<T[]> arrayClz, ElasticFilter[] filters);

    public <T extends Object> Map<String, Object> aggregate(Class<T[]> arrayClz,
                                                            List<ElasticFilter> filters,
                                                            List<ElasticAggregate> aggregates);

}
