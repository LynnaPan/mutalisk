package io.hybridtheory.mutalisk.mapper;

import io.hybridtheory.mutalisk.executor.ElasticTransportExecutor;

import java.lang.reflect.Proxy;

public class ElasticSearchMapperInterfaceFactory {
    public <T> T getMapper(Class<T> clz, ElasticTransportExecutor executor) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz},
            new ElasticSearchMapperInterfaceHandler<T>(clz, executor));
    }
}
