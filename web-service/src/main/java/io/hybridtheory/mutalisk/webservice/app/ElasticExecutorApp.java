package io.hybridtheory.mutalisk.webservice.app;


import com.google.gson.Gson;
import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.webservice.exception.EntityJsonParseFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutionException;

@Path("/elastic")
public class ElasticExecutorApp {
    private static final Logger log = LoggerFactory.getLogger(ElasticExecutorApp.class);

    @Inject
    private ElasticExecutor executor;
    private Gson gson = new Gson();

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEntityWithId(@QueryParam("clz") String clzName,
                                  @QueryParam("id") String id) throws ClassNotFoundException {
        Class clz = Class.forName(clzName);
        return gson.toJson(executor.get(clz, id));
    }

    @PUT
    @Path("/create_index")
    @Produces(MediaType.APPLICATION_JSON)
    public String createIndex(@QueryParam("clz") String clzName) throws ClassNotFoundException {
        Class clz = Class.forName(clzName);
        boolean result = executor.createIndex(clz);

        return "{\"result\": " + result + "}";
    }

    @POST
    @Path("/insert")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String insert(@QueryParam("clz") String clzName, String body)
        throws ClassNotFoundException, ExecutionException,
        InterruptedException, EntityJsonParseFailException {
        Class clz = Class.forName(clzName);
        // check if parse successfull
        Object object = gson.fromJson(body, clz);

        if (object == null) {
            log.error("Unable to parse body to {}", clzName);
            throw new EntityJsonParseFailException(clzName);
        }

        boolean result = executor.insert(object, clz);

        return "{\"result\": " + result + "}";
    }

    @PUT
    @Path("/raw")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String raw(String body) {
        return "{\"result\": " + true + "}";
    }


    @GET
    @Path("/info")
    @Produces("text/plain")
    public String getBasicInfo() {
        return executor == null ? "null" : executor.toString();
    }
}
