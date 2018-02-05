package io.hybridtheory.mutalisk.mapper;

import io.hybridtheory.mutalisk.executor.ElasticTransportExecutor;

public interface ElasticSearchMapperTemplate {
    Object apply(ElasticTransportExecutor executor, Object[] args);

    // @TODO should be append Objec.. parameters in the apply function to avert createIndex action every function call
    // Object apply(NElasticTransportClient executor, Object object ...);
}
