package io.hybridtheory.mutalisk.mapper.template.dao;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.executor.ElasticExecutor;
import io.hybridtheory.mutalisk.mapper.ElasticSearchMapperTemplate;
import io.hybridtheory.mutalisk.executor.highlevel.ElasticHighLevelExecutor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ElasticInsertDAOTemplate<T> implements ElasticSearchMapperTemplate {
    public String index;
    public String mapping;
    public String primary;

    // target clz
    public Class clz;

    public ElasticInsertDAOTemplate(String index, String mapping, String primary, Class clz) {
        this.index = index;
        this.mapping = mapping;
        this.primary = primary;
        this.clz = clz;
    }

    @Override
    public Object apply(ElasticExecutor executor,
                        Object[] args) throws IOException, ExecutionException, InterruptedException {
        JsonObject jobj = (JsonObject) StorageUtil.gson.toJsonTree(args[0]);
        return executor.impl().insertById(index, mapping, primary, jobj.toString());
    }
}
