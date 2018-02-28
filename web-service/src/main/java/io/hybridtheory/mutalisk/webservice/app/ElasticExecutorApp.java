package io.hybridtheory.mutalisk.webservice.app;


import com.google.gson.Gson;
import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

@Path("/elastic")
public class ElasticExecutorApp {
    private static final Logger log = LoggerFactory.getLogger(ElasticExecutorApp.class);

    @Inject
    private ElasticExecutor executor;
    private Gson gson = new Gson();

    public ElasticExecutorApp(ElasticExecutor executor) {
        this.executor = executor;
    }

    @GET
    @Path("/get")
    @Produces("application/json")
    public String getEntityWithId(String clzName, String id) {
        Class clz = null;
        try {
            clz = Class.forName(clzName);
        } catch (ClassNotFoundException e) {

        }

        return gson.toJson(executor.get(clz, id));
    }

    @GET
    @Path("/info")
    @Produces("text/plain")
    public String getBasicInfo() {
        return this.executor.toString();
    }
}
