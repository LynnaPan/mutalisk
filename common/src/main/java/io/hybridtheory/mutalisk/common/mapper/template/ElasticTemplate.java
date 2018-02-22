package io.hybridtheory.mutalisk.common.mapper.template;

import io.hybridtheory.mutalisk.common.api.ElasticAPIExecutor;

public interface ElasticTemplate {
    Object apply(ElasticAPIExecutor executor, Object[] args);

    // @TODO should be append Objec.. parameters in the apply function to avert createIndex action every function call
    // Object apply(NElasticTransportClient executor, Object object ...);
}
