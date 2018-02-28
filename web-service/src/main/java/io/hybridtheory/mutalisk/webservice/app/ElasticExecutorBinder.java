package io.hybridtheory.mutalisk.webservice.app;


import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.conf.ElasticClientConf;
import org.apache.http.HttpHost;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.Bindings;

import java.lang.reflect.Constructor;

public class ElasticExecutorBinder extends AbstractBinder {
    public static final String ElasticExecutorClzName =
        "io.hybridtheory.mutalisk.transport.executor.ElasticTransportExecutor";

    @Override
    protected void configure() {
        try {
            Class<?> clz = Class.forName(ElasticExecutorClzName);
            Constructor constructor = clz.getConstructor(ElasticClientConf.class);
            ElasticClientConf conf = new ElasticClientConf();
            conf.hostPorts = new HttpHost[] {
                new HttpHost("localhost", 9300)
            };
            conf.cluster = "mrtang";

            ElasticExecutor executor = (ElasticExecutor) constructor.newInstance(conf);

            bind(Bindings.service(executor)).to(ElasticExecutor.class);
        } catch (Throwable t) {
            t.printStackTrace();
        }


    }
}
