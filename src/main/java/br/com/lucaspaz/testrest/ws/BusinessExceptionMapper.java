package br.com.lucaspaz.testrest.ws;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 *
 * @author lfpaz
 */
@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException exception) {

        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("error", exception.getMessage()).build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(jsonResponse).build();
    }

}
