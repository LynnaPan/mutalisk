package io.hybridtheory.mutalisk.webservice.app;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/ping")
public class TestApp {
    @GET
    @Produces("text/plain")
    public String ping() {
        return "java/hibernate";
    }
}
