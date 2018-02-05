package io.hybridtheory.mutalisk.executor.transport.mapper.template.dao;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.executor.ElasticTransportExecutor;
import io.hybridtheory.mutalisk.executor.transport.mapper.ElasticSearchMapperTemplate;
import io.hybridtheory.mutalisk.executor.transport.mapper.template.filter.NElasticProxyFilterTemplate;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Arrays;
import java.util.List;

public class NElasticProxySearchTemplate implements ElasticSearchMapperTemplate {
    public String index;
    public String mapping;

    public List<NElasticProxyFilterTemplate> filters;
    public Class arrayClz;
    public Class clz;

    private JsonParser parser = new JsonParser();

    public NElasticProxySearchTemplate(String index, String mapping,
                                       List<NElasticProxyFilterTemplate> filters, Class arrayClz) {
        this.index = index;
        this.mapping = mapping;
        this.filters = filters;
        this.arrayClz = arrayClz;
        this.clz = this.arrayClz.getComponentType();
    }

    @Override
    public Object apply(ElasticTransportExecutor executor, Object[] args) {
        SearchRequest request = new SearchRequest(index);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        for (NElasticProxyFilterTemplate filter : filters) {
            sourceBuilder.query(filter.apply(args));
        }

        request.source(sourceBuilder);

        SearchHits hits = executor.client().search(request).actionGet().getHits();

        Object[] results = new Object[hits.hits().length];

        for (int i = 0; i < results.length; i++) {
            JsonObject jobj = parser.parse(hits.getAt(i).sourceAsString()).getAsJsonObject();
            results[i] = StorageUtil.gson.fromJson(jobj, clz);
        }

        return Arrays.copyOf(results, results.length, arrayClz);
    }
}
