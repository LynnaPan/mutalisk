package io.hybridtheory.mutalisk.executor.transport.mapper.template.dao;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.executor.ElasticTransportExecutor;
import io.hybridtheory.mutalisk.executor.transport.mapper.ElasticSearchMapperTemplate;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;

public class NElasticProxyBulkInsertTemplate implements ElasticSearchMapperTemplate {
    public String index;
    public String mapping;
    public String primary;

    public Class arrayClz;
    public Class clz;

    public NElasticProxyBulkInsertTemplate(String index, String mapping, String primary, Class arrayClz) {
        this.index = index;
        this.mapping = mapping;
        this.primary = primary;
        this.arrayClz = arrayClz;

        this.clz = arrayClz.getComponentType();
    }

    @Override
    public Object apply(ElasticTransportExecutor executor, Object[] args) {
        Object[] values = (Object[]) args[0];

        BulkRequest bulkRequest = new BulkRequest();

        for (Object value : values) {
            JsonObject jobj = (JsonObject) StorageUtil.gson.toJsonTree(value);

            IndexRequest request = null;

            if (primary != null) {
                JsonElement element = jobj.get(primary);
                request = new IndexRequest(index, mapping, element.getAsString());
            } else {
                request = new IndexRequest(index, mapping);
            }
            request.source(jobj.toString());

            bulkRequest.add(request);
        }

        return !executor.client().bulk(bulkRequest).actionGet().hasFailures();
    }
}
