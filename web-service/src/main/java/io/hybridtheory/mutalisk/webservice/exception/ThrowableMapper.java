package io.hybridtheory.mutalisk.webservice.exception;


import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable throwable) {
        return Response.status(500).
            entity(throwable.getMessage()).
            type("text/plain").
            build();
    }
}
