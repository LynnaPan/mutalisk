package io.hybridtheory.mutalisk.webservice.exception;


import org.glassfish.jersey.internal.util.ExceptionUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable throwable) {
        return Response.status(500).
            entity(ExceptionUtils.exceptionStackTraceAsString(throwable)).
            type("text/plain").
            build();
    }
}
