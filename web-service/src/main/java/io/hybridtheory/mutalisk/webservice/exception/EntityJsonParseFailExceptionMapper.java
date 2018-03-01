package io.hybridtheory.mutalisk.webservice.exception;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class EntityJsonParseFailExceptionMapper implements ExceptionMapper<EntityJsonParseFailException> {
    @Override
    public Response toResponse(EntityJsonParseFailException exception) {
        return Response.status(400)
            .entity(exception.getMessage())
            .type(MediaType.TEXT_PLAIN).
                build();
    }
}
