package io.hybridtheory.mutalisk.executor.highlevel;


import io.hybridtheory.mutalisk.aggregate.ElasticAggregate;
import io.hybridtheory.mutalisk.executor.ElasticExecutorImpl;
import io.hybridtheory.mutalisk.executor.exception.BulkDeleteException;
import io.hybridtheory.mutalisk.filter.ElasticFilter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ElasticHighLevelExecutorImpl implements ElasticExecutorImpl {
    private ElasticHighLevelExecutor executor;

    public ElasticHighLevelExecutorImpl(ElasticHighLevelExecutor executor) {
        this.executor = executor;
    }

    @Override
    public boolean createIndex(Class clz) {
        return false;
    }

    @Override
    public boolean createIndex(Class clz, TimeValue timeout) {
        return false;
    }

    @Override
    public boolean createIndex(String index, String type, String mappingSource) {
        return false;
    }

    @Override
    public boolean createIndex(String index, String type, String mappingSource, TimeValue timeout) {
        return false;
    }

    @Override
    public boolean deleteIndex(Class clz) {
        return false;
    }

    @Override
    public boolean deleteIndex(String index) {
        return false;
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
    public boolean insertById(Object object, String id, TimeValue timeout) {
        return false;
    }

    @Override
    public boolean insertById(String index, String type, String id, String source) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean insert(String index, String type, String id, String source, TimeValue timeout) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean insertByNoId(Object object) {
        return false;
    }

    @Override
    public boolean insertByNoId(Object object, TimeValue timeout) {
        return false;
    }

    @Override
    public boolean insert(Object object) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean insert(Object object, TimeValue timeout) throws ExecutionException, InterruptedException {
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
    public <T> boolean bulkInsert(T[] objects) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public <T> boolean bulkInsert(List<T> objects) throws ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public boolean bulkInsert(String index, String type, List<String> ids, List<String> objects) throws ExecutionException, InterruptedException {
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
    public <T> T[] search(String index, String type, Class<T[]> arrayClz, List<ElasticFilter> filters) {
        return null;
    }

    @Override
    public <T> T[] search(String index, String type, Class<T[]> arrayClz, ElasticFilter[] filters) {
        return null;
    }

    @Override
    public SearchResponse rawSearch(Class arrayClz, ElasticFilter[] filters) {
        return null;
    }

    @Override
    public SearchResponse rawSearch(Class arrayClz, List<ElasticFilter> filters) {
        return null;
    }

    @Override
    public SearchResponse rawSearch(String index, String type, Class arrayClz, ElasticFilter[] filters) {
        return null;
    }

    @Override
    public SearchResponse rawSearch(String index, String type, Class arrayClz, List<ElasticFilter> filters) {
        return null;
    }

    @Override
    public <T> Map<String, Object> aggregate(Class<T[]> arrayClz, List<ElasticFilter> filters, List<ElasticAggregate> aggregations) {
        return null;
    }

    @Override
    public <T> Map<String, Object> aggregate(String index, String type, List<ElasticFilter> filters, List<ElasticAggregate> aggregations) {
        return null;
    }


}
