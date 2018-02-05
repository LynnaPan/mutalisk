package io.hybridtheory.mutalisk.mapper.template.dao;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.executor.ElasticExecutor;
import io.hybridtheory.mutalisk.mapper.ElasticSearchMapperTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ElasticBulkInsertDAOTemplate implements ElasticSearchMapperTemplate {
    public String index;
    public String mapping;
    public String primary;

    public Class arrayClz;
    public Class clz;

    public ElasticBulkInsertDAOTemplate(String index, String mapping, String primary, Class arrayClz) {
        this.index = index;
        this.mapping = mapping;
        this.primary = primary;
        this.arrayClz = arrayClz;

        this.clz = arrayClz.getComponentType();
    }

    @Override
    public Object apply(ElasticExecutor executor,
                        Object[] args) throws IOException, ExecutionException, InterruptedException {

        Object[] values = (Object[]) args[0];
        List<String> xids = new ArrayList<>();
        List<String> xvalues = new ArrayList<>();

        for (Object value : values) {
            JsonObject jobj = (JsonObject) StorageUtil.gson.toJsonTree(value);

            if (primary != null) {
                JsonElement element = jobj.get(primary);
                xids.add(element.getAsString());
            }

            xvalues.add(jobj.toString());
        }



        return executor.impl().bulkInsert(index, mapping, xids, xvalues);
    }

}
