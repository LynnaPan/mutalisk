package io.hybridtheory.mutalisk.mapper;

import io.hybridtheory.mutalisk.executor.ElasticExecutor;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ElasticSearchMapperTemplate {
    Object apply(ElasticExecutor executor, Object[] args) throws IOException, ExecutionException, InterruptedException;

    // @TODO should be append Objec.. parameters in the apply function to avert createIndex action every function call
    // Object apply(NElasticTransportClient executor, Object object ...);
}
