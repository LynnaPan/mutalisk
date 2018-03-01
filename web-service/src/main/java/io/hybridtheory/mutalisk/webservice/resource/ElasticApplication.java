package io.hybridtheory.mutalisk.webservice.resource;


import io.hybridtheory.mutalisk.webservice.app.ElasticExecutorApp;
import io.hybridtheory.mutalisk.webservice.app.ElasticExecutorBinder;
import io.hybridtheory.mutalisk.webservice.exception.EntityJsonParseFailExceptionMapper;
import io.hybridtheory.mutalisk.webservice.exception.ThrowableMapper;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("mrtang")
public class ElasticApplication extends ResourceConfig {
    public ElasticApplication() {
        this.register(ThrowableMapper.class);
        this.register(EntityJsonParseFailExceptionMapper.class);
        this.register(ElasticExecutorApp.class);
        this.register(new ElasticExecutorBinder());

        this.setApplicationName("mrtang");
    }

}
