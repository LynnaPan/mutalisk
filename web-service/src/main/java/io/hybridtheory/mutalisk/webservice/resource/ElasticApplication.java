package io.hybridtheory.mutalisk.webservice.resource;


import io.hybridtheory.mutalisk.webservice.app.ElasticExecutorApp;
import io.hybridtheory.mutalisk.webservice.app.ElasticExecutorBinder;
import io.hybridtheory.mutalisk.webservice.exception.ThrowableMapper;
import org.glassfish.jersey.server.ResourceConfig;

public class ElasticApplication extends ResourceConfig {
    public ElasticApplication() {
        this.register(new ElasticExecutorBinder());
        this.register(ElasticExecutorApp.class);
        this.register(ThrowableMapper.class);
    }

}
