package io.hybridtheory.mutalisk.executor.transport.mapper.template.dao;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.executor.transport.ElasticTransportExecutor;
import io.hybridtheory.mutalisk.executor.transport.mapper.ElasticSearchMapperTemplate;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.rest.RestStatus;

public class NElasticProxyInsertTemplate<T> implements ElasticSearchMapperTemplate {
    public String index;
    public String mapping;
    public String primary;

    // target clz
    public Class clz;

    public NElasticProxyInsertTemplate(String index, String mapping, String primary, Class clz) {
        this.index = index;
        this.mapping = mapping;
        this.primary = primary;
        this.clz = clz;
    }

    @Override
    public Object apply(ElasticTransportExecutor executor, Object[] args) {

        JsonObject jobj = (JsonObject) StorageUtil.gson.toJsonTree(args[0]);

        IndexRequest request = null;

        if (primary != null) {
            JsonElement element = jobj.get(primary);
            request = new IndexRequest(index, mapping, element.getAsString());
        } else {
            request = new IndexRequest(index, mapping);
        }
        request.source(jobj.toString());

        return executor.client().index(request).actionGet().status() == RestStatus.CREATED;
    }
}
