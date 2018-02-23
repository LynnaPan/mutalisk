package io.hybridtheory.mutalisk.common.mapper.template;

import io.hybridtheory.mutalisk.common.api.ElasticExecutor;

public interface ElasticTemplate {
    Object apply(ElasticExecutor executor, Object[] args);

    // @TODO should be append Objec.. parameters in the apply function to avert createIndex action every function call
    // Object apply(NElasticTransportClient executor, Object object ...);
}
